from __future__ import print_function
import sys
import time
import cv
import socket
from SimpleCV import Image, Camera, VirtualCamera
from preprocess import Preprocessor
from features import Features
from threshold import Threshold
from display import Gui, ThresholdGui

HOST = 'localhost' 
PORT = 28546 

PITCH_SIZE = (243.8, 121.9)

# Distinct between field size line or entity line
ENTITY_BIT = 'E';
PITCH_SIZE_BIT  = 'P';

class Vision:
    
    def __init__(self, pitchnum):
               
        self.running = True
        
        self.stdout = True 

        self.cap = Camera()
        #self.cap = VirtualCamera('global05.jpg', 'image')
        self.gui = Gui()
        self.threshold = Threshold(pitchnum)
        self.thresholdGui = ThresholdGui(self.threshold, self.gui)
        self.preprocessor = Preprocessor()
        self.features = Features(self.gui, self.threshold)
        
        eventHandler = self.gui.getEventHandler()
        eventHandler.addListener('q', self.quit)
        eventHandler.setClickListener(self.setNextPitchCorner)
        
        if not self.stdout:
            self.connect()
            
        self.doStuff()
        
    def connect(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect( (HOST, PORT) )

    def quit(self):
        self.running = False
        
    def doStuff(self):
        while self.running:
            frame = self.cap.getImage()
            frame = self.preprocessor.preprocess(frame)
            
            self.gui.updateLayer('raw', frame)

            ents = self.features.extractFeatures(frame)
            self.outputEnts(ents)

            self.gui.loop()
        
        self.socket.close()

    def setNextPitchCorner(self, where):
        self.preprocessor.setNextPitchCorner(where)
        
        if self.preprocessor.hasPitchSize:
            print("Pitch size: {0!r}".format(self.preprocessor.pitch_size))
            self.outputPitchSize()
    
    def outputPitchSize(self):
        self.send('{0} {1} {2} \n'.format(
                PITCH_SIZE_BIT, self.preprocessor.pitch_size[0], self.preprocessor.pitch_size[1]))

    def outputEnts(self, ents):

        # Messyyy
        if not self.preprocessor.hasPitchSize:
            return

        self.send("{0} ".format(ENTITY_BIT))

        for name in ['yellow', 'blue', 'ball']:
            entity = ents[name]
            x, y = entity.coordinates()
            angle = entity.angle()

            if name == 'ball':
                self.send('{0} {1} '.format(x, y))
            else:
                self.send('{0} {1} {2} '.format(x, y, angle))

        self.send(str(int(time.time() * 1000)) + " \n")
        
    def send(self, string):
        if self.stdout:
            sys.stdout.write(string)
        else:
            self.socket.send(string)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        Vision(int(sys.argv[1]))
    else:
        # Default to the main pitch
        Vision(0)

