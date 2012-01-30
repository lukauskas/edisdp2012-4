package balle.io.reader;

import java.util.ArrayList;
import java.util.List;

import balle.io.listener.Listener;

public abstract class AbstractVisionReader {
    private final List<Listener> listeners = new ArrayList<Listener>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    protected void propagateString(String str) {
        System.out.println("Updating: " + str);
        for (Listener listener : listeners) {
            listener.update(str);
        }
    }
    
    public abstract void start();
}
