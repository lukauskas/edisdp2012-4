import cv
from SimpleCV import Image
from operator import sub

class Preprocessor:
    
    #_cropRect = (0, 60, 640, 350)
    
    def __init__(self):
        self.hasPitchSize = False
        self._cropRect = []

    @property
    def pitch_size(self):
        if not self.hasPitchSize:
            return None

        return (self._cropRect[2], self._cropRect[3])

    def setNextPitchCorner(self, where):

        assert len(where) == 2, "setNextPitchCorner takes a tuple (x, y)"

        length = len(self._cropRect)
        if length == 0:
            next = where
        elif length == 2:
            print where
            next = map(sub, where, self._cropRect)
            self.hasPitchSize = True
        else:
            return

        self._cropRect.extend(next)
        print "Cropped rectangle {0}".format(self._cropRect)
        
    def preprocess(self, frame):
        
        if self.hasPitchSize:
            frame = frame.crop(*self._cropRect)

        return frame
