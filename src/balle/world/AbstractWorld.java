package balle.world;

import balle.io.listener.Listener;
import balle.misc.Globals;

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

    private double        pitchWidth    = -1;
    private double        pitchHeight   = -1;

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
     * // TODO James: this sounds like more of a job for within strategy. NOPE!
     * 1) We do not want to reimplement this for all strategies 2) We DO want
     * the world class to use this to estimate coordinates in case vision fails!
     * 
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
        if ((object == null) || (object.getPosition() == null)
                || (object.getVelocity() == null))
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

    /**
     * Update the current state of the world using scaled coordinates
     * 
     * @param yPosX
     * @param yPosY
     * @param yRad
     * @param bPosX
     * @param bPosY
     * @param bRad
     * @param ballPosX
     * @param ballPosY
     * @param timestamp
     */
    abstract protected void updateScaled(double yPosX, double yPosY,
            double yRad, double bPosX, double bPosY, double bRad,
            double ballPosX, double ballPosY, long timestamp);

    protected double scaleXToMeters(double x) {
        if (x < 0)
            return x;

        return (x / pitchWidth) * Globals.PITCH_WIDTH;
    }

    protected double scaleYToMeters(double y) {
        if (y < 0)
            return y;

        return (y / pitchHeight) * Globals.PITCH_HEIGHT;
    }

    @Override
    public void update(double yPosX, double yPosY, double yRad, double bPosX,
            double bPosY, double bRad, double ballPosX, double ballPosY,
            long timestamp) {

        if ((pitchWidth < 0) || (pitchHeight < 0)) {
            System.err
                    .println("Cannot update locations as pitch size is not set properly. Restart vision");
            return;
        }
        // Scale the coordinates from vision to meters:
        yPosX = scaleXToMeters(yPosX);
        yPosY = scaleYToMeters(yPosY);

        bPosX = scaleXToMeters(bPosX);
        bPosY = scaleYToMeters(bPosY);

        ballPosX = scaleXToMeters(ballPosX);
        ballPosY = scaleYToMeters(ballPosY);

        updateScaled(yPosX, yPosY, yRad, bPosX, bPosY, bRad, ballPosX,
                ballPosY, timestamp);
    }

    @Override
    public void updatePitchSize(double width, double height) {

        pitchWidth = width;
        pitchHeight = height;
    }

}
