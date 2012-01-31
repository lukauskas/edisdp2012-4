import cv
from SimpleCV import Image, Features
from threshold import Threshold
from display import Gui

display = Gui.getGui()

class Features:
    # Sizes of various features
    # Format : ( min_w, max_w, min_l, max_l)
    # width is defined as the shorter dimension
    Sizes = { 'ball'     : (4, 20, 4,  20),
          'T'         : (15, 40, 20, 55),
          'dirmarker' : (3,  12, 3,  12),
        }

    def __init__(self, pitch):
        self.threshold = Threshold(pitch);

    def extractFeatures(self, frame):
        
        ents = {'yellow': None, 'blue': None, 'ball': None}
        yellow = self.threshold.yellowT(frame)
        blue = self.threshold.blueT(frame)
        ball = self.threshold.ball(frame)

        display.updateLayer('threshY', yellow)
        display.updateLayer('threshB', blue)
        display.updateLayer('threshR', ball)

        ents['yellow'] = self.findEntity(yellow, 'T')
        ents['blue'] = self.findEntity(blue, 'T')
        ents['ball'] = self.findEntity(ball, 'ball')
        
        display.updateLayer('yellow', ents['yellow'])
        display.updateLayer('blue', ents['blue'])
        display.updateLayer('ball', ents['ball'])

        return ents

    def findEntity(self, image, which):

        # Work around OpenCV crash on some nearly black images
        nonZero = cv.CountNonZero(image.getGrayscaleMatrix())
        if nonZero < 10:
            return None

        blobs = image.findBlobs()

        if blobs is None:
            return None

        robot = None
        for blob in blobs:
            if self.sizeMatch(blob, which):
                robot = blob
                break

        return robot

    def sizeMatch(self, feature, which):
        width = feature.width()
        length = feature.length()

        expected = self.Sizes[which]

        return expected[0] < width < expected[1] \
            and expected[2] < length < expected[3]


class Entity:
    
    @classmethod
    def fromFeature(cls, feature):
        coords = feature.coordinates()
        entity = Entity(coords[0], coords[1])

        return entity

    def __init__(self, x, y, angle):
        self._x = x
        self._y = y
        self._angle = angle
    
    def x(self):
        return self._x

    def y(self):
        return self._y

    def angle(self):
        return self._angle

