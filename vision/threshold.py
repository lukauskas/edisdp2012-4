import cv
from SimpleCV import Image



class Threshold:

    def __init__(self):

        self._values = {}

        self._values['yellow'] = defaults['yellow']
        self._values['blue'] = defaults['blue']
        self._values['ball'] = defaults['ball']

        self._gui = Threshold.Gui(self)
        self._gui.changeEntity('ball')

    def getGui(self):
        return self._gui

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

    def updateValues(self, entity, newValues):
        self._values[entity] = newValues

    class Gui:

        def __init__(self, thresholdinstance, window=None):

            if window is None:
                self.window = 'thresh_adjust'
                cv.NamedWindow(self.window)
            else:
                self.window = window

            self.threshold = thresholdinstance
            self.currentEntity = 'yellow'

            self.__createTrackbars()

        def __createTrackbars(self):

            cv.CreateTrackbar('H min', self.window, 0, 255, self.__onTrackbarChanged) 
            cv.CreateTrackbar('S min', self.window, 0, 255, self.__onTrackbarChanged) 
            cv.CreateTrackbar('V min', self.window, 0, 255, self.__onTrackbarChanged) 

            cv.CreateTrackbar('H max', self.window, 0, 255, self.__onTrackbarChanged) 
            cv.CreateTrackbar('S max', self.window, 0, 255, self.__onTrackbarChanged) 
            cv.CreateTrackbar('V max', self.window, 0, 255, self.__onTrackbarChanged) 
    
        def __onTrackbarChanged(self, x):

            allvalues = []
            for which in ['min', 'max']:
                values = []
                for channel in ['H', 'S', 'V']:
                    pos = cv.GetTrackbarPos('{0} {1}'.format(channel, which), \
                            self.window)
                    
                    values.append(pos)
                    
                allvalues.append(values)

            self.threshold.updateValues(self.currentEntity, allvalues)

        def changeEntity(self, name):
            self.currentEntity = name
            self.setTrackbarValues(self.threshold._values[name])

        def setTrackbarValues(self, values):
            for i, which in enumerate(['min', 'max']):
                for j, channel in enumerate(['H', 'S', 'V']):
                    cv.SetTrackbarPos('{0} {1}'.format(channel, which), \
                            self.window, values[i][j])
                    


defaults = {
        'yellow' : [[14, 54, 235], [45, 255, 255]],
        'blue' : [[84,  100,  108], [132, 255, 255]],
        'ball' : [[0, 160, 100], [13, 255, 255]]
        }

