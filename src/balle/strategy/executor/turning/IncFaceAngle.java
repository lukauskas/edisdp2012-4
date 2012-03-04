package balle.strategy.executor.turning;

import balle.controller.Controller;
import balle.world.Orientation;
import balle.world.Snapshot;

/**
 * Incremental FaceAngle, Turns in increments.
 */
public class IncFaceAngle extends FaceAngle {

    private boolean turning = false;

    private int     SPEED   = 300;

    public IncFaceAngle() {
        super();
    }

    public IncFaceAngle(double accuracy) {
        super(accuracy);
    }

    public void setSpeed(int speed) {
        this.SPEED = speed;
    }

    public int getSpeed() {
        return SPEED;
    }

    /**
     * Returns true if robot is turning at the moment
     * 
     * @return
     */
    @Override
    public boolean isTurning() {
        return turning;
    }

    @Override
    public void setTargetOrientation(Orientation targetOrientation) {
        this.targetOrientation = targetOrientation;
        this.turning = true;
    }

    @Override
	public void step(Controller controller, Snapshot snapshot) {
        if (!isPossible(snapshot) || !isTurning())
            return;

        double angleToTurn = getAngleToTurn(snapshot);

        if (Math.abs(getAngleToTurn(snapshot)) < ACCURACY) {
            this.stop(controller);
        } else {
            this.turn(controller, angleToTurn);
        }
    }

    @Override
    public void stop(Controller controller) {
        super.stop(controller);
        turning = false;
    }

    protected void turn(Controller controller, double angleToTurn) {
        if (angleToTurn > 0) {
            controller.setWheelSpeeds(-SPEED, SPEED);
        } else if (angleToTurn < 0) {
            controller.setWheelSpeeds(SPEED, -SPEED);
        } else {
            controller.stop();
        }
    }

}
