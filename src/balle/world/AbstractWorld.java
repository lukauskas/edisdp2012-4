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
    private final boolean goalIsLeft;

    public AbstractWorld(boolean isBalleBlue, boolean goalIsLeft) {
        this.balleIsBlue = isBalleBlue;
        this.goalIsLeft = goalIsLeft;
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
    public Coord estimatedPosition(MovingPoint object, double timestep) {
        if ((object.getPosition() == null) || (object.getVelocity() == null))
            return null;
        else if (timestep == 0) {
            return object.getPosition();
        } else
            // TODO: Make sure the robot does not go through the wall
            // make sure the ball bounces from the wall, etc.

            return new Coord(object.getPosition().add(object.getVelocity().adjustLength(timestep)),
                    true);
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
    abstract protected void updateScaled(Coord ourPos, Orientation ourOrientation, Coord theirsPos,
            Orientation theirsOrientation, Coord ballPos, long timestamp);

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
    public void update(double yPosX, double yPosY, double yDeg, double bPosX, double bPosY,
            double bDeg, double ballPosX, double ballPosY, long timestamp) {

        if ((pitchWidth < 0) || (pitchHeight < 0)) {
            System.err
                    .println("Cannot update locations as pitch size is not set properly. Restart vision");
            return;
        }

        Coord yPos = null;
        Orientation yOrientation = null;
        Coord bPos = null;
        Orientation bOrientation = null;
        Coord ballPos = null;

        if ((yPosX != UNKNOWN_VALUE) && (yPosY != UNKNOWN_VALUE)) {
            yPos = new Coord(scaleXToMeters(yPosX), scaleYToMeters(yPosY));
            yOrientation = new Orientation(yDeg, false);
        }

        if ((bPosX != UNKNOWN_VALUE) && (bPosY != UNKNOWN_VALUE)) {
            bPos = new Coord(scaleXToMeters(bPosX), scaleYToMeters(bPosY));
            bOrientation = new Orientation(bDeg, false);
        }

        if ((ballPosX != UNKNOWN_VALUE) && (ballPosY != UNKNOWN_VALUE)) {
            ballPos = new Coord(scaleXToMeters(ballPosX), scaleYToMeters(ballPosY));
        }

        if (isBlue())
            updateScaled(bPos, bOrientation, yPos, yOrientation, ballPos, timestamp);
        else
            updateScaled(yPos, yOrientation, bPos, bOrientation, ballPos, timestamp);
    }

    @Override
    public void updatePitchSize(double width, double height) {

        pitchWidth = width;
        pitchHeight = height;
    }

    @Override
    public void updateGoals(double xMin, double xMax, double yMin, double yMax) {
        left = new Goal(-100000, xMin, yMin, yMax);
        right = new Goal(xMax, 100000, yMin, yMax);
    }

    protected Goal left  = new Goal(-0.2, 0, 0.3, 0.9);
    protected Goal right = new Goal(2.45, 2.65, 0.3, 0.9);

    public Goal getOwnGoal() {
        if (goalIsLeft)
            return right;
        else
            return left;
    }

    public Goal getOpponentsGoal() {
        if (goalIsLeft)
            return left;
        else
            return right;
    }

}
