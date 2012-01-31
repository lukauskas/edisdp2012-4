package balle.world;

import balle.io.listener.Listener;
import balle.io.reader.DataReader;

/***
 * 
 * This class will take raw data from the vision system and model the world
 * accordingly. This includes e.g. velocities of the robots and ball and angle
 * of the robots in radians.
 * 
 * All values returned by this interface should be the best guess of the system.
 * All tweaking of the raw values recieved from the vision system is done here.
 * 
 */
public abstract class AbstractWorld implements Listener {

    // JEV: Scanner is final and can't be extended, makes it difficult for the
    // simulator.
    private final boolean balleIsBlue;

    public AbstractWorld(boolean isBalleBlue) {
        this.balleIsBlue = isBalleBlue;
    }

    /**
     * Returns whether our robot is blue or not
     * 
     * @return true if our robot is the blue one, false otherwise
     */
    protected boolean isBlue() {
        return balleIsBlue;
    }

    /***
     * Gets the best guess of the coordinates of the robot (our team's robot).
     * 
     * @return coordinates of the robot.
     */
    public abstract Snapshot getSnapshot();

}
