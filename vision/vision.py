from __future__ import print_function
import sys
import time
import cv
import socket
from SimpleCV import Image, Camera
from features import Features
from display import Gui

# Socket
HOST = 'localhost' 
PORT = 28546 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect( (HOST, PORT) )

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

def output(ents):
    for name in ['yellow', 'blue', 'ball']:
        x = y = angle = -1
        entity = ents[name]
        if entity is not None:
            x, y = entity.coordinates()
            angle = entity.angle()

        if name == 'ball':
            s.send('{0} {1}'.format(x, y) + ' ')
        else:
            s.send('{0} {1} {2}'.format(x, y, angle) + ' ')

    s.send(str(int(time.time() * 1000)) + " \n")
    


#try:
if len(sys.argv) > 1:
    pitchnum = int(sys.argv[1])
else:
    # Default to the main pitch
    pitchnum = 0

cap = Camera() #Capture()

features = Features(pitchnum)
gui = Gui.getGui()

while True:
    frame = cap.getImage()
    #frame = Image('global05.jpg')
    #frame = Image(frame)

    gui.updateBase(frame)

    ents = features.extractFeatures(frame)
    
    gui.loop()

    output(ents)

    c = cv.WaitKey(16)
    k = chr(c % 0x100)

    if k == 'q' or k == 27: # ESC
        break

s.close()
#finally:
#    cap.stop()
