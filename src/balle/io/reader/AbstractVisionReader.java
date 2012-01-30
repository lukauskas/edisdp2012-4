package balle.io.reader;

import java.util.ArrayList;
import java.util.List;

import balle.io.listener.Listener;

public abstract class AbstractVisionReader {
    private final List<Listener> listeners = new ArrayList<Listener>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    protected void propagate(double yPosX, double yPosY, double yRad,
                             double bPosX, double bPosY, double bRad, double ballPosX,
                             double ballPosY, long timestamp) {

        for (Listener listener : listeners) {
            listener.update(yPosX, yPosY, yRad, bPosX, bPosY, bRad, ballPosX, ballPosY, timestamp);
        }
    }
    
    public abstract void start();
}
