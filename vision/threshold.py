import cv
import os
import sys
import cPickle
from SimpleCV import Image

class Threshold:
    
    # File for storing temporary threshold defaults
    filepath = "threshdefaults_{0}"

    def __init__(self, pitch):
        
        self._pitch = pitch
        self.__getDefaults()
        
    def __getDefaults(self):
        self._values = {}
        
        path = self.__getFilePath()
        if os.path.exists(path):
            f = open(path, 'r')
            self._values = cPickle.load(f)
        else:
            self._values = defaults[self._pitch]
            
    def __saveDefaults(self):
        f = open(self.__getFilePath(), 'w')
        cPickle.dump(self._values, f)

    def __getFilePath(self):
        return os.path.join(sys.path[0], self.filepath.format(self._pitch))

    def yellowT(self, frame):
        return self.threshold(frame, self._values['yellow'][0], self._values['yellow'][1])

    def blueT(self, frame):
        return self.threshold(frame, self._values['blue'][0], self._values['blue'][1])

    def ball(self, frame):
        return self.threshold(frame, self._values['ball'][0], self._values['ball'][1])
    
    def threshold(self, frame, threshmin, threshmax):
        
        iplhsv = frame.toHSV().getBitmap()

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

        iplresult = cv.CreateImage(cv.GetSize(iplhsv), frame.depth, 1)
        cv.InRangeS(iplhsv, threshmin, threshmax, iplresult)

        result = Image(iplresult)

        if crossover:
            iplresult2 = cv.CreateImage(cv.GetSize(iplhsv), frame.depth, 1)
            cv.InRangeS(iplhsv, threshmin2, threshmax2, iplresult2)
            
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
        'yellow' : [[14, 40, 97], [50, 150, 255]],
        'blue' : [[84,  90,  108], [132, 255, 255]],
        'ball' : [[0, 160, 100], [13, 255, 255]]
        },
        {
        'yellow' : [[17, 147, 183], [44, 255, 255]],
        'blue' : [[57,  29,  43], [92, 255, 255]],
        'ball' : [[0, 160, 100], [7, 255, 255]]
        }]
