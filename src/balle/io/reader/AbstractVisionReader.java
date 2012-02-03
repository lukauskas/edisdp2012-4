package balle.io.reader;

import balle.io.listener.Listener;

/** Interface for any class that updates world positions.
 *  
 *  NOTE: Reader implements this well,
 *  		and use that unless you want to extend something else.
 * 
 */
public abstract interface AbstractVisionReader {

    public abstract void addListener(Listener listener);
    
}
