import math

import cv
from SimpleCV import Image, Features, DrawingLayer, BlobMaker
from threshold import Threshold

class Features:
    # Sizes of various features
    # Format : ( min_w, max_w, min_l, max_l)
    # width is defined as the shorter dimension
    # Sizes = { 'ball'     : (4, 20, 4,  20),
    #       'T'         : (15, 40, 20, 55),
    #       'dirmarker' : (3,  12, 3,  12),
    #     }

    Sizes = { 'ball'     : (20, 300),
          'T'         : (100, 800),
        }

    def __init__(self, display, threshold):
        self.threshold = threshold
        self._display = display

    def extractFeatures(self, frame):

        hsv = frame.toHSV()
        ents = {'yellow': None, 'blue': None, 'ball': None}
        yellow = self.threshold.yellowT(hsv).smooth(grayscale=True)
        blue = self.threshold.blueT(hsv).smooth(grayscale=True)
        ball = self.threshold.ball(hsv).smooth(grayscale=True)

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
        
        size = self.Sizes[which]
        blobmaker = BlobMaker()
        blobs = blobmaker.extractFromBinary(image, image, minsize=size[0], maxsize=size[1])

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

        # return expected[0] < width < expected[1] \
        #     and expected[2] < length < expected[3]

        area = feature.area()
        return expected[0] < area < expected[1]

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
            # Use moments to do magic things.
            # (finds precise line through blob)
            mask = feature.crop().getGrayscaleMatrix()

            m = cv.Moments(mask, 1)

            if m.m00 == 0:
                m.m00 = 0.01
            
            # Intermediate values
            a = m.mu20 / m.m00;
            b = m.mu11 / m.m00;
            c = m.mu02 / m.m00;
            d = a + c;
            e = math.sqrt((4 * b * b) + ((a - c) * (a - c)));

            self._angle = math.atan2(2 * b, a - c + e)
                        
            # Crudely find direction.          
            m00 = cv.GetSpatialMoment(m, 0, 0)
            m10 = cv.GetSpatialMoment(m, 1, 0)
            m01 = cv.GetSpatialMoment(m, 0, 1)
            if m00 == 0.0:
                 m00 = 0.1

            centroid1 = (round(m10/m00), round(m01/m00))
            # cv.Circle is really fussy with what it takes
            centroid1 = tuple(map(int, centroid1))
            cv.Circle(mask, centroid1, 14, cv.RGB(0,0,0), -1)
            
            m = cv.Moments(mask, 1)
            m00 = cv.GetSpatialMoment(m, 0, 0)
            m10 = cv.GetSpatialMoment(m, 1, 0)
            m01 = cv.GetSpatialMoment(m, 0, 1)
            if m00 == 0:
                 m00 = 0.1

            centroid2 = (round(m10/m00), round(m01/m00))
            roughAngle = math.atan2(centroid1[1] - centroid2[1], centroid1[0] - centroid2[0])
            
            pi2 = math.pi*2
            roughAngle = min( (roughAngle-self._angle)%pi2, (self._angle-roughAngle)%pi2 )

            if roughAngle < (math.pi/2):
                self._angle += math.pi

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
                #center = (feature.minRectX(), feature.minRectY())
                center = self._coordinates
                angle = self.angle()
                endx = center[0] + 30 * math.cos(angle)
                endy = center[1] + 30 * math.sin(angle)

                layer.line(center, (endx, endy), antialias=False);

