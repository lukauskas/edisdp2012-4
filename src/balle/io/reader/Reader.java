package balle.io.reader;

import java.util.ArrayList;
import java.util.List;

import balle.io.listener.Listener;


/** For easy implementation of AbstractVisionReader interface.
 * Just sends out 
 * 
 * @author james
 */
public class Reader implements AbstractVisionReader {
	
	private final List<Listener> listeners = new ArrayList<Listener>();

	
	/**
	 * @param listener
	 */
    public final void addListener(Listener listener) {
        listeners.add(listener);
    }

    protected final void propagate(double yPosX, double yPosY, double yRad,
                             double bPosX, double bPosY, double bRad, double ballPosX,
                             double ballPosY, long timestamp) {

        for (Listener listener : listeners) {
            listener.update(yPosX, yPosY, yRad, bPosX, bPosY, bRad, ballPosX, ballPosY, timestamp);
        }
    }
    
}
