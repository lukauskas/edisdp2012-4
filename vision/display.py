import pygame
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
        self._base.save(self._display)
        
        #self._display.checkEvents()
        
        for event in pygame.event.get():
            if event.type == pygame.KEYDOWN:
                self._keyListener.processKey(chr(event.key))
            
        # Todo: process openCV events here too
        
    def getKeyHandler(self):
        return self._keyListener   

    def updateBase(self, image):
        if self._base is not None:
            self._base.clearLayers()

        self._base = image

        size = image.size()
        for key in self._layers.keys():
            layer = DrawingLayer(size)
            image.addDrawingLayer(layer)

            self._layers[key] = layer

        #self.draw()

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
                
