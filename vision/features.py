import math

import cv
from SimpleCV import Image, Features, DrawingLayer
from threshold import Threshold

class Features:
    # Sizes of various features
    # Format : ( min_w, max_w, min_l, max_l)
    # width is defined as the shorter dimension
    Sizes = { 'ball'     : (4, 20, 4,  20),
          'T'         : (15, 40, 20, 55),
          'dirmarker' : (3,  12, 3,  12),
        }

    def __init__(self, display, threshold):
        self.threshold = threshold
        self._display = display

    def extractFeatures(self, frame):
        
        ents = {'yellow': None, 'blue': None, 'ball': None}
        yellow = self.threshold.yellowT(frame)
        blue = self.threshold.blueT(frame)
        ball = self.threshold.ball(frame)

        self._display.updateLayer('threshY', yellow)
        self._display.updateLayer('threshB', blue)
        self._display.updateLayer('threshR', ball)

        ents['yellow'] = self.findEntity(yellow, 'T')
        ents['blue'] = self.findEntity(blue, 'T')
        ents['ball'] = self.findEntity(ball, 'ball')

        self._display.updateLayer('yellow', ents['yellow'])
        self._display.updateLayer('blue', ents['blue'])
        self._display.updateLayer('ball', ents['ball'])

        return ents

    def findEntity(self, image, which):

        # Work around OpenCV crash on some nearly black images
        nonZero = cv.CountNonZero(image.getGrayscaleMatrix())
        if nonZero < 10:
            return Entity()

        blobs = image.findBlobs()

        if blobs is None:
            return Entity()

        entityblob = None
        for blob in blobs:
            if self.sizeMatch(blob, which):
                entityblob = blob
                break
        
        if entityblob is None:
            return Entity()
        
        entity = Entity.fromFeature(entityblob, which != 'ball')

        return entity

    def sizeMatch(self, feature, which):
        width = feature.width()
        length = feature.length()

        expected = self.Sizes[which]

        return expected[0] < width < expected[1] \
            and expected[2] < length < expected[3]

class Entity:

    @classmethod
    def fromFeature(cls, feature, hasAngle):
        entity = Entity(hasAngle)
        entity._coordinates = feature.coordinates()
        entity._feature = feature

        return entity

    def __init__(self, hasAngle=True):
        """
        hasAngle = True if it makes sense for this entity to have an angle
        i.e. it isn't a ball
        """

        self._coordinates = (-1, -1)
        self._hasAngle = hasAngle
        self._angle = None
        self._feature = None
    
    def coordinates(self):
        return self._coordinates

    def angle(self):
        """
        Calculates the orientation of the entity
        """

        feature = self._feature

        if self._feature is None:
            return -1

        if self._angle is None:
            # This method is stolen from sdp2011 group 4
            # It first calculates the center of mass of the T shape (TODO: might be faster to)
            # take feature.centroid() and scale it to the T region), then blacks out a circle
            # over the center. This covers most of the top of the T shape, but leaves lots of
            # the bottom, so when we calculate the center of mass again, it is nearer the bottom
            # (i.e. the opposite end to the original center of mass) and we can calculate the angle
            # of the line between them.

            mask = feature.crop().getGrayscaleMatrix()

            moments = cv.Moments(mask, 1)
            m00 = cv.GetSpatialMoment(moments, 0, 0)
            m10 = cv.GetSpatialMoment(moments, 1, 0)
            m01 = cv.GetSpatialMoment(moments, 0, 1)

            if m00 == 0:
                m00 = 0.01
            
            centroid1 = (round(m10/m00), round(m01/m00))

            cv.Circle(mask, centroid1, 14, cv.RGB(0, 0, 0), -1)

            moments = cv.Moments(mask, 1)
            m00 = cv.GetSpatialMoment(moments, 0, 0)
            m10 = cv.GetSpatialMoment(moments, 1, 0)
            m01 = cv.GetSpatialMoment(moments, 0, 1)

            if m00 == 0:
                m00 = 0.01
            
            centroid2 = (round(m10/m00), round(m01/m00))

            self._angle = math.atan2(centroid1[1] - centroid2[1], centroid1[0] - centroid2[0])

        return self._angle

    def draw(self, layer, angle=True):
        """
        Draw this entity to the specified layer
        If angle is true then orientation will also be drawn
        """
        feature = self._feature

        if feature is not None:
            feature.draw(layer=layer)

            if angle and self._hasAngle:
                center = (feature.minRectX(), feature.minRectY())
                angle = self.angle()
                endx = center[0] + 30 * math.cos(angle)
                endy = center[1] + 30 * math.sin(angle)

                layer.line(center, (endx, endy), antialias=False);

