package balle.strategy.executor.turning;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.strategy.executor.Executor;
import balle.world.Orientation;
import balle.world.Snapshot;

public class FaceAngle implements Executor, RotateToOrientationExecutor {
    private static final Logger LOG = Logger.getLogger(FaceAngle.class);
    protected final static double DEFAULT_ACCURACY        = Math.PI / 16;
    protected final double        ACCURACY;

    Orientation                   targetOrientation       = null;

    private int                   timeToTurn              = 0;
    private long                  startedTurning          = 0;
    private static final int      ADDITIONAL_TIME_TO_TURN = 700;         // ms
                                                                          // to
                                                                          // always
                                                                          // add
                                                                          // to
                                                                          // timeToTurn
    private static final int      TURN_SPEED              = 180;         // Degreees
                                                                          // per
                                                                          // second

    private boolean               needStop                = false;

    /**
     * Initialise FaceAngle executor with specified accuracy.
     * 
     * @param accuracy
     *            the allowed error to use (radians)
     */
    public FaceAngle(double accuracy) {
        ACCURACY = accuracy;
    }

    public FaceAngle() {
        this(DEFAULT_ACCURACY);
    }

    /**
     * Set the target orientation to turn to. Will stop the robot first thing
     * (on the next step) if currently turning.
     * 
     * @param targetOrientation
     */
    @Override
    public void setTargetOrientation(Orientation targetOrientation) {
        this.targetOrientation = targetOrientation;
        this.needStop = true;
    }

    /**
     * Returns true if the robot is already facing the correct angle.
     */
    @Override
    public boolean isFinished(Snapshot snapshot) {
		if (snapshot == null)
            return false;
        else if (targetOrientation == null)
            return true;
        else {
			double current = snapshot.getBalle().getOrientation().radians();
            double target = targetOrientation.radians();
            if (Math.abs(current - target) <= ACCURACY)
                return true;
            else
                return false;
        }
    }

    @Override
    public boolean isPossible(Snapshot snapshot) {
		return ((snapshot != null) && (snapshot.getBalle() != null));
    }

    private int calculateTimeToTurn(double radiansToTurn) {
        return (int) Math.round(Math.abs(radiansToTurn) / ((Math.toRadians(TURN_SPEED / 1000.0))))
                + ADDITIONAL_TIME_TO_TURN;
    }

    /**
     * Returns true if robot is turning at the moment
     * 
     * @return
     */
    @Override
    public boolean isTurning() {
        LOG.trace(System.currentTimeMillis() + " "
                + (startedTurning + timeToTurn));
        return System.currentTimeMillis() <= startedTurning + timeToTurn;
    }

    private void initiateTurn(Controller controller, double radians) {
        timeToTurn = calculateTimeToTurn(radians);
        startedTurning = System.currentTimeMillis();

        // System.out.printf("Rotate %s (%s ms)\n", Math.toDegrees(radians),
        // timeToTurn);
        controller.rotate((int) Math.toDegrees(radians), TURN_SPEED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * balle.strategy.executor.turning.RotateToOrientationExecutor#getAngleToTurn
     * ()
     */
    @Override
	public double getAngleToTurn(Snapshot snapshot) {
        if (!isPossible(snapshot))
            return 0;

		return snapshot.getBalle().getAngleToTurn(targetOrientation);
    }

    @Override
	public void step(Controller controller, Snapshot snapshot) {
        if (!isPossible(snapshot))
            return;

        if (isFinished(snapshot)) {
            stop(controller);
        }
        if (needStop) {
            stop(controller);
            return;
        } else if (isTurning())
            return; // If we're still turning, continue.
        else {
            // Else start turning
            initiateTurn(controller, getAngleToTurn(snapshot));
        }

    }

    @Override
    public void stop(Controller controller) {
        if (isTurning()) {
            controller.stop();
        }
        startedTurning = 0;
        timeToTurn = 0;
        needStop = false;
    }

    @Override
    public ArrayList<Drawable> getDrawables() {
        return new ArrayList<Drawable>();
    }

}
