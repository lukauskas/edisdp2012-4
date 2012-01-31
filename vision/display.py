import pygame
import cv
from SimpleCV import Display, DrawingLayer

class Gui:

    _instance = None

    @classmethod
    def getGui(cls):
        if cls._instance is None:
            cls._instance = Gui()

        return cls._instance

    def __init__(self):
        self._layers = {'raw': None,
                'yellow': None,
                'blue': None,
                'ball' : None
                }

        self._base = None
        self._display = Display()
        
        self._keyListener = Gui.KeyListener()

    def loop(self):
        """
        Draw the image to the display, and process any events
        """
        
        baseLayer = self._base
        size = baseLayer.size()
        for key in self._layers.keys():
            layer = DrawingLayer(size)
            baseLayer.addDrawingLayer(layer)

            self._layers[key] = layer

        baseLayer.save(self._display)
        

        # Process any keyboard events
        #self._display.checkEvents()
        
        for event in pygame.event.get():
            if event.type == pygame.KEYDOWN:
                self._keyListener.processKey(chr(event.key % 0x100))
            
        # Process OpenCV events (for if the focus is on the thresholding window)
        c = cv.WaitKey(16)
        self._keyListener.processKey(chr(c % 0x100))

    def getKeyHandler(self):
        return self._keyListener   

    def updateBase(self, image):
        self._base = image

    def updateFeature(self, name, feature):
        """
        Update the layer specified by 'name'
        """

        if feature is None:
            #self._layers[name].clear()
            return

        assert name in self._layers.keys(), \
                name + ' is not in the known list of layers'

        feature.draw(layer=self._layers[name]);

        #self.draw()
        
    class KeyListener:
        
        def __init__(self):
            self._listeners = {}
        
        def processKey(self, key):
            if key in self._listeners.keys():
                self._listeners[key]()
            
        def addListener(self, key, callback):
            
            assert callable(callback), '"callback" must be callable'
            
            self._listeners[key] = callback
                
