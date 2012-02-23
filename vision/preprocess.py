import cv
import os
import util
from SimpleCV import Image
from operator import sub

class Preprocessor:
    
    _filepath = os.path.join('data', 'pitch_size')
    
    def __init__(self, resetPitchSize):
        self._cropRect = None

        if not resetPitchSize:
            self.__loadPitchSize()

        if self._cropRect is None:
            self._cropRect = []
            self.hasPitchSize = False
        else:
            self.hasPitchSize = True

    def __savePitchSize(self):
        util.dumpToFile(self._cropRect, self._filepath)

    def __loadPitchSize(self):
        self._cropRect = util.loadFromFile(self._filepath)
        print self._cropRect

    @property
    def pitch_size(self):
        if not self.hasPitchSize:
            return None

        return (self._cropRect[2], self._cropRect[3])

    def setNextPitchCorner(self, where):

        assert len(where) == 2, "setNextPitchCorner takes a tuple (x, y)"

        length = len(self._cropRect)
        if length == 0:
            self._cropRect.extend(where)
        elif length == 2:
            print where
            next = map(sub, where, self._cropRect)
            self.hasPitchSize = True
            self._cropRect.extend(next)

            self.__savePitchSize()
        else:
            return


        print "Cropped rectangle {0}".format(self._cropRect)
        
    def preprocess(self, frame):
        
        if self.hasPitchSize:
            frame = frame.crop(*self._cropRect)

        return frame
