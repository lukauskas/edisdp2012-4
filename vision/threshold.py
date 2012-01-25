import cv
from SimpleCV import Image

class Threshold:

    def __init__(self):
        self._yellow = defaults['yellow']
        self._blue = defaults['blue']
        self._ball = defaults['ball']

    def yellowT(self, frame):
        return self.threshold(frame, self._yellow[0], self._yellow[1])

    def blueT(self, frame):
        return self.threshold(frame, self._blue[0], self._blue[1])

    def ball(self, frame):
        return self.threshold(frame, self._ball[0], self._ball[1])
    
    def threshold(self, frame, threshmin, threshmax):
        
        iplhsv = frame.toHSV().getBitmap()

        crossover = False
        if threshmin[0] > threshmax[0]:
            # Handle hue threshold crossing over
            # angle boundry e.g. when thresholding on red
            
            crossover = True
            threshmax2 = [threshmin[0], threshmax[1], threshmax[2]]
            threshmin = [threshmax[0], threshmin[1], threshmin[2]] 
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

defaults = {
        'yellow' : [[14, 54, 235], [45, 255, 255]],
        'blue' : [[84,  108,  108], [132, 255, 255]],
        'ball' : [[230, 100, 100], [20, 255, 255]]
        }

