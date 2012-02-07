import cv
from SimpleCV import Image
from operator import sub

class Preprocessor:
    
    #_cropRect = (0, 60, 640, 350)
    
    def __init__(self):
        self._initMatrices()
        self.hasPitchSize = False
        self._cropRect = []

    @property
    def pitch_size(self):
        if not self.hasPitchSize:
            return None

        return ((self._cropRect[2] - self._cropRect[0]),
                (self._cropRect[3] - self._cropRect[1]))

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
        frame = self.undistort(frame)
        
        if self.hasPitchSize:
            frame = frame.crop(*self._cropRect)

        return frame
    
    def undistort(self, frame):
        #logging.debug("Undistorting a frame")
    
        # Drop down to OpenCV
        iplFrame = frame.getBitmap()
        iplUndistorted = cv.CreateImage(cv.GetSize(iplFrame), iplFrame.depth, iplFrame.nChannels)

        cv.Undistort2(iplFrame, iplUndistorted,
                        self.Intrinsic, self.Distortion)
                        
        return Image(iplUndistorted)

    def _initMatrices(self):
        """
        Initialise matrices for camera distortion correction.
        """
        
        #logging.debug("Initialising camera matrices")

        dmatL = [ -3.1740235091903346e-01, -8.6157434640872499e-02,
                   9.2026812110876845e-03, 4.4950266773574115e-03 ]

        imatL = [ 8.6980146658682384e+02, 0., 3.7426130495414304e+02,
                  0., 8.7340754327613899e+02, 2.8428760615670581e+02,
                  0., 0., 1. ]

        imat = cv.CreateMat(3,3, cv.CV_32FC1)
        dmat = cv.CreateMat(1,4, cv.CV_32FC1)

        for i in range(3):
            for j in range(3):
                imat[i,j] = imatL[3*i + j]

        for i in range(4):
            dmat[0,i] = dmatL[i]

        self.Distortion = dmat
        self.Intrinsic = imat
