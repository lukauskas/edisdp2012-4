import cv
from SimpleCV import Image

class Preprocessor:
    
    _cropRect = (0, 60, 640, 350)
    
    def __init__(self):
        self._initMatrices()
        
    def preprocess(self, frame):
        frame = self.undistort(frame)
        
        return frame.crop(*self._cropRect)
    
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
