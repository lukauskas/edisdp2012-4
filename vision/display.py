import pygame
import cv
from SimpleCV import Display, DrawingLayer, Image, Blob

class Gui:

    """
    Default layer groups available to display
    First element is the 'base' layer, and must be an image,
    the rest must be features and will be drawn on top
    """
    layersets = {
            'default': ['raw', 'yellow', 'blue', 'ball'],
            'yellow': ['threshY', 'yellow'],
            'blue': ['threshB', 'blue'],
            'ball': ['threshR', 'ball']
            }

    _instance = None

    @classmethod
    def getGui(cls):
        if cls._instance is None:
            cls._instance = Gui()

        return cls._instance

    def __init__(self):
        self._layers = {
                # Base layers
                'raw': None,
                'threshY': None,
                'threshB': None,
                'threshR': None,

                # Overlay layers
                'yellow': None,
                'blue': None,
                'ball' : None
                }

        self._currentLayerset = self.layersets['default']

        self._display = Display()
        
        self._keyListener = Gui.KeyListener()

    def __draw(self):

        iterator = iter(self._currentLayerset)
        
        # First element is the base layer
        baseLayer = self._layers[iterator.next()] 
        size = baseLayer.size()

        for key in iterator:
            if self._layers[key] is None:
                continue
            
            layer = DrawingLayer(size)
            baseLayer.addDrawingLayer(layer)

            self._layers[key].draw(layer=layer)

        baseLayer.save(self._display)

    def loop(self):
        """
        Draw the image to the display, and process any events
        """
        
        self.__draw()

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

    def updateLayer(self, name, layer):
        """
        Update the layer specified by 'name'
        """

        assert name in self._layers.keys(), \
                name + ' is not in the known list of layers'
        
        self._layers[name] = layer

    def switchLayerset(self, name):
        assert name in self.layersets.keys(), 'Unknown layerset ' + name + '!'

        self._currentLayerset = self.layersets[name]
        
    class KeyListener:
        
        def __init__(self):
            self._listeners = {}
        
        def processKey(self, key):
            if key in self._listeners.keys():
                self._listeners[key]()
            
        def addListener(self, key, callback):
            
            assert callable(callback), '"callback" must be callable'
            
            self._listeners[key] = callback

class ThresholdGui:

    def __init__(self, thresholdinstance, window=None):

        if window is None:
            self.window = 'thresh_adjust'
            cv.NamedWindow(self.window)
        else:
            self.window = window

        self.threshold = thresholdinstance
        self.currentEntity = 'yellow'

        self._showOnGui = False

        self.__createTrackbars()
        self.__setupKeyEvents()
        
    def __setupKeyEvents(self):
        """
        Adds key listeners to the main gui for switching between entities
        """
        
        def yellow(): self.changeEntity('yellow')
        def blue(): self.changeEntity('blue')
        def ball(): self.changeEntity('ball')
        
        keyHandler = Gui.getGui().getKeyHandler()
        keyHandler.addListener('y', yellow)
        keyHandler.addListener('b', blue)
        keyHandler.addListener('r', ball)

        keyHandler.addListener('t', self.toggleShowOnGui)

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

    def toggleShowOnGui(self):
        self._showOnGui = not self._showOnGui
        
        gui = Gui.getGui()
        if self._showOnGui:
            gui.switchLayerset(self.currentEntity)
        else:
            gui.switchLayerset('default')

    def changeEntity(self, name):
        """
        Change which entity to adjust thresholding
        Can be 'blue', 'yellow' or 'ball'
        """
        
        self.currentEntity = name
        self.setTrackbarValues(self.threshold._values[name])

        if self._showOnGui:
            Gui.getGui().switchLayerset(name)

    def setTrackbarValues(self, values):
        for i, which in enumerate(['min', 'max']):
            for j, channel in enumerate(['H', 'S', 'V']):
                cv.SetTrackbarPos('{0} {1}'.format(channel, which), \
                        self.window, values[i][j])


