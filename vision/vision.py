from __future__ import print_function
import sys
import time
import math
import socket
import cv

from optparse import OptionParser

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
    
    def __init__(self, pitchnum, stdout, sourcefile):
               
        self.running = True
        
        self.stdout = stdout 

        if sourcefile is None:  
            self.cap = Camera()
        else:
            filetype = 'video'
            if sourcefile.endswith(('jpg', 'png')):
                filetype = 'image'

            self.cap = VirtualCamera(sourcefile, filetype)

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
            if name == 'ball':
                self.send('{0} {1} '.format(x, y))
            else:
                angle = 360 - (((entity.angle() * (180/math.pi)) - 360) % 360)
                self.send('{0} {1} {2} '.format(x, y, angle))

        self.send(str(int(time.time() * 1000)) + " \n")
        
    def send(self, string):
        if self.stdout:
            sys.stdout.write(string)
        else:
            self.socket.send(string)

if __name__ == "__main__":

    parser = OptionParser()
    parser.add_option('-p', '--pitch', dest='pitch', type='int', metavar='PITCH',
                      help='PITCH should be 0 for main pitch, 1 for the other pitch')

    parser.add_option('-f', '--file', dest='file', metavar='FILE',
                      help='Use FILE as input instead of capturing from Camera')

    parser.add_option('-s', '--stdout', action='store_true', dest='stdout', default=False,
                      help='Send output to stdout instead of using a socket')

    (options, args) = parser.parse_args()

    if options.pitch not in [0,1]:
        parser.error('Pitch must be 0 or 1')

    Vision(options.pitch, options.stdout, options.file)



