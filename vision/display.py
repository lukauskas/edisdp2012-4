import pygame
import time
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
                'ball' : None,

                # fps is always drawn
                'fps': None
                }

        self._currentLayerset = self.layersets['default']
        self._display = Display()
        self._eventHandler = Gui.EventHandler()
        self._lastMouseState = 0
        self._lastFrame = None
        self._lastFrameTime = time.time()

    def __draw(self):

        iterator = iter(self._currentLayerset)
        
        # First element is the base layer
        baseLayer = self._layers[iterator.next()]

        if baseLayer is None:
            return

        size = baseLayer.size()

        for key in iterator:
            toDraw = self._layers[key]
            if toDraw is None:
                continue
            
            elif isinstance(toDraw, DrawingLayer):
                baseLayer.addDrawingLayer(toDraw)

            else:
                layer = DrawingLayer(size)
                baseLayer.addDrawingLayer(layer)

                toDraw.draw(layer)

        # draw fps
        baseLayer.addDrawingLayer(self._layers['fps'])

        baseLayer.save(self._display)

    def __updateFps(self):
        smoothConst = 0.1
        thisFrameTime = time.time()

        thisFrame = thisFrameTime - self._lastFrameTime
        if self._lastFrame is not None:
            # Smooth values
            thisFrame = thisFrame * (1 - smoothConst) + smoothConst * self._lastFrame
        
        fps = 1.0 / thisFrame

        self._lastFrame = thisFrame
        self._lastFrameTime = thisFrameTime

        # Draw the text
        size = self._layers['raw'].size() # This could break

        layer = DrawingLayer(size)
        layer.ezViewText('{0:.2f} fps'.format(fps), (10, 10))
        self._layers['fps'] = layer
 
    def loop(self):
        """
        Draw the image to the display, and process any events
        """

        self.__updateFps()
        
        self.__draw()

        for event in pygame.event.get(pygame.KEYDOWN):
            self._eventHandler.processKey(chr(event.key % 0x100))

        self._display.checkEvents()

        mouseLeft = self._display.mouseLeft
        # Only fire click event once for each click
        if mouseLeft == 1 and self._lastMouseState == 0:
            self._eventHandler.processClick((self._display.mouseX, self._display.mouseY))
        
        self._lastMouseState = mouseLeft
            
        # Process OpenCV events (for if the focus is on the thresholding window)
        c = cv.WaitKey(16)
        self._eventHandler.processKey(chr(c % 0x100))

    def getEventHandler(self):
        return self._eventHandler   

    def getDrawingLayer(self):
        return DrawingLayer(self._layers['raw'].size())

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
        
    class EventHandler:
        
        def __init__(self):
            self._listeners = {}
            self._clickListener = None
        
        def processKey(self, key):
            if key in self._listeners.keys():
                self._listeners[key]()

        def processClick(self, where):
            if self._clickListener is not None:
                self._clickListener(where)
            
        def addListener(self, key, callback):
            """
            Adds a function callback for a key.
            """
            
            assert callable(callback), '"callback" must be callable'
            
            self._listeners[key] = callback

        def setClickListener(self, callback):
            """
            Sets a function to be called on clicking on the image.
            The function will be passed a tuple with the (x,y) of the click.

            Setting a new callback will override the last one (or pass None to clear)
            """
            assert callback is None or callable(callback), '"callback" must be callable'
            
            self._clickListener = callback


class ThresholdGui:

    def __init__(self, thresholdinstance, gui, window=None):

        if window is None:
            self.window = 'thresh_adjust'
            cv.NamedWindow(self.window)
        else:
            self.window = window

        self._gui = gui
        self.threshold = thresholdinstance

        self._showOnGui = False

        self.__createTrackbars()
        self.__setupKeyEvents()

        self.changeEntity('yellow')

        
    def __setupKeyEvents(self):
        """
        Adds key listeners to the main gui for switching between entities
        """
        
        def yellow(): self.changeEntity('yellow')
        def blue(): self.changeEntity('blue')
        def ball(): self.changeEntity('ball')
        
        keyHandler = self._gui.getEventHandler()
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
        
        if self._showOnGui:
            self._gui.switchLayerset(self.currentEntity)
        else:
            self._gui.switchLayerset('default')

    def changeEntity(self, name):
        """
        Change which entity to adjust thresholding
        Can be 'blue', 'yellow' or 'ball'
        """
        
        self.currentEntity = name
        self.setTrackbarValues(self.threshold._values[name])

        if self._showOnGui:
            self._gui.switchLayerset(name)

    def setTrackbarValues(self, values):
        for i, which in enumerate(['min', 'max']):
            for j, channel in enumerate(['H', 'S', 'V']):
                cv.SetTrackbarPos('{0} {1}'.format(channel, which), \
                        self.window, values[i][j])


