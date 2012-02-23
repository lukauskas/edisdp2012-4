import cv
import os
import util
from SimpleCV import Image, ColorSpace

class Threshold:
    
    # File for storing temporary threshold defaults
    filepath = os.path.join('data', 'threshdefaults_{0}')

    def __init__(self, pitch):
        
        self._pitch = pitch
        self.__getDefaults()
        
    def __getDefaults(self):
        self._values = None
        
        path = self.filepath.format(self._pitch)
        self._values = util.loadFromFile(path)

        if self._values is None:
            self._values = defaults[self._pitch]
            
    def __saveDefaults(self):
        util.dumpToFile(self._values, self.filepath.format(self._pitch))

    def yellowT(self, frame):
        return self.threshold(frame, self._values['yellow'][0], self._values['yellow'][1])

    def blueT(self, frame):
        return self.threshold(frame, self._values['blue'][0], self._values['blue'][1])

    def ball(self, frame):
        return self.threshold(frame, self._values['ball'][0], self._values['ball'][1])
    
    def threshold(self, frame, threshmin, threshmax):
        """
        Performs thresholding on a frame.
        The image must be in the HSV colorspace!
        """

        assert frame.getColorSpace() == ColorSpace.HSV, "Image must be HSV!"

        iplframe = frame.getBitmap()

        crossover = False
        if threshmin[0] > threshmax[0]:
            # Handle hue threshold crossing over
            # angle boundry e.g. when thresholding on red

            hMax = threshmin[0]
            hMin = threshmax[0]

            crossover = True
            threshmax2 = [hMin, threshmax[1], threshmax[2]]
            threshmin = [hMax, threshmin[1], threshmin[2]] 
            threshmax = [255, threshmax[1], threshmax[2]]
            threshmin2 = [0, threshmin[1], threshmin[2]]

        iplresult = cv.CreateImage(cv.GetSize(iplframe), frame.depth, 1)
        cv.InRangeS(iplframe, threshmin, threshmax, iplresult)

        result = Image(iplresult)

        if crossover:
            iplresult2 = cv.CreateImage(cv.GetSize(iplframe), frame.depth, 1)
            cv.InRangeS(iplframe, threshmin2, threshmax2, iplresult2)
            
            result = result + Image(iplresult2)

        return result

    def updateValues(self, entity, newValues):
        self._values[entity] = newValues
        
        self.__saveDefaults()

"""
defaults[0] for the main pitch, and defaults[1] for the other table
"""
defaults =[
        {
        'yellow' : [[23, 28, 155], [42, 255, 255]],
        'blue' : [[83,  54,  74], [115, 255, 255]],
        'ball' : [[0, 160, 100], [10, 255, 255]]
        },
        {
        'yellow' : [[17, 147, 183], [44, 255, 255]],
        'blue' : [[57,  29,  43], [92, 255, 255]],
        'ball' : [[0, 160, 100], [7, 255, 255]]
        }]
