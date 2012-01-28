from SimpleCV import DrawingLayer

class Display:

    _instance = None

    @classmethod
    def getDisplay(cls):
        if cls._instance is None:
            cls._instance = Display()

        return cls._instance

    def __init__(self):
        self._layers = {'raw': None,
                'yellow': None,
                'blue': None,
                'ball' : None
                }

        self._base = None

    def draw(self):
        self._base.show()
         

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
        make a layer available to show on the display
        """

        if feature is None:
            #self._layers[name].clear()
            return

        assert name in self._layers.keys(), \
                name + ' is not in the known list of layers'

        feature.draw(layer=self._layers[name]);

        #self.draw()
