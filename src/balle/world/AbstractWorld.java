package balle.world;

import balle.io.listener.Listener;

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

    public final int      UNKNOWN_VALUE = -1;

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

    /**
     * Estimated position of the object after timestep (in miliseconds)
     * 
     * @param object
     *            object which position to estimate
     * @param timestep
     *            time in miliseconds after which to estiamte the position of
     *            the object
     * @return new coordinate for the position of the object after timestep
     */
    public Coord estimatedPosition(FieldObject object, double timestep) {
        if ((object == null) || (object.getPosition() == null))
            return null;
        else if (timestep == 0) {
            return object.getPosition();
        } else
            // TODO: Make sure the robot does not go through the wall
            // make sure the ball bounces from the wall, etc.
            return new Coord(object.getPosition().add(
                    object.getVelocity().adjustLength(timestep)), true);
    }

    /***
     * Gets the best guess of the coordinates of the robot (our team's robot).
     * 
     * @return coordinates of the robot.
     */
    public abstract Snapshot getSnapshot();

}
