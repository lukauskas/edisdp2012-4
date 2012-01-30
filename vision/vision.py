from __future__ import print_function
import sys
import time
import cv
import socket
from SimpleCV import Image, Camera
from preprocess import Preprocessor
from features import Features
from display import Gui

# Socket
HOST = 'localhost' 
PORT = 28546 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect( (HOST, PORT) )

def output(ents):
    for name in ['yellow', 'blue', 'ball']:
        x = y = angle = -1
        entity = ents[name]
        if entity is not None:
            x, y = entity.coordinates()
            angle = entity.angle()

        if name == 'ball':
            s.send('{0} {1} '.format(x, y))
        else:
            s.send('{0} {1} {2} '.format(x, y, angle))

    s.send(str(int(time.time() * 1000)) + " \n")
    
if len(sys.argv) > 1:
    pitchnum = int(sys.argv[1])
else:
    # Default to the main pitch
    pitchnum = 0

cap = Camera()
preprocessor = Preprocessor()
features = Features(pitchnum)
gui = Gui.getGui()

while True:
    frame = cap.getImage()
    #frame = Image('global05.jpg')
    #frame = Image(frame)

    frame = preprocessor.preprocess(frame)
    
    gui.updateBase(frame)

    ents = features.extractFeatures(frame)
    
    gui.loop()

    output(ents)

    c = cv.WaitKey(16)
    k = chr(c % 0x100)

    if k == 'q' or k == 27: # ESC
        break

s.close()
