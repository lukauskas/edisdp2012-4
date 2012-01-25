import cv
from SimpleCV import Image, Camera
from capture import Capture
from threshold import Threshold
from features import Features
from display import Display

def undistort(image):
    intrinsics = cv.CreateMat(3, 3, cv.CV_64FC1)
    cv.Zero(intrinsics)
    #camera data
    intrinsics[0, 0] = 1100.850708957251072
    intrinsics[1, 1] = 778.955239997982062
    intrinsics[2, 2] = 1.0
    intrinsics[0, 2] = 348.898495232253822
    intrinsics[1, 2] = 320.213734835526282
    dist_coeffs = cv.CreateMat(1, 4, cv.CV_64FC1)
    cv.Zero(dist_coeffs)
    dist_coeffs[0, 0] = -0.326795877008420
    dist_coeffs[0, 1] = 0.139445565548056
    dist_coeffs[0, 2] = 0.001245710462327
    dist_coeffs[0, 3] = -0.001396618726445
    
    size = cv.GetSize(image)
    image2 = cv.CreateImage(size, image.depth, image.nChannels)
    cv.Undistort2(image, image2, intrinsics, dist_coeffs)
    
    return image2

#try:

cap = Camera() #Capture()
cv.NamedWindow('camera')

threshold = Threshold()
features = Features()
display = Display.getDisplay()

while True:
    frame = cap.getImage()
    #frame = Image('global05.jpg')
    #frame = Image(frame)

    display.putBase(frame)

    ents = features.extractFeatures(frame)
    
    display.draw()

    print ents
    if ents['blue'] is not None:
        print ents['blue'].angle()

    if ents['yellow'] is not None:
        print ents['yellow'].angle()

    cv.ShowImage('camera', frame.getBitmap())
    key = cv.WaitKey(16)
    if key != -1:
        break

#finally:
#    cap.stop()
