package balle.strategy.executor.movement;

import balle.controller.Controller;
import balle.world.Coord;
import balle.world.FieldObject;
import balle.world.Orientation;
import balle.world.Robot;
import balle.world.Snapshot;

public class GoToObject implements MovementExecutor {

    private final static double DISTANCE_THRESHOLD = 0.2;
    private final static double EPSILON            = 0.00001;

    protected FieldObject       target             = null;
    protected Snapshot          currentState       = null;
    private boolean             isMoving           = false;

    private final static double DISTANCE_DIFF      = 0.3;
    private final static int    SPEED_CONSTANT     = 700;

    protected long              startedTurning     = 0;
    private long                timeToTurn         = 0;
    private static final int    TURN_SPEED_DEGREES = 180;
    private static final double TURN_SPEED_RADIANS = (TURN_SPEED_DEGREES * Math.PI) / 180;

    @Override
    public void updateTarget(FieldObject target) {
        this.target = target;
    }

    @Override
    public boolean isFinished() {
        Robot robot = currentState.getBalle();
        Coord currentPosition = robot.getPosition();
        if ((target == null) || (currentPosition == null)) {
            return false;
        }
        return ((currentPosition.dist(target.getPosition()) - DISTANCE_THRESHOLD) < EPSILON);
    }

    @Override
    public boolean isPossible() {
        Robot robot = currentState.getBalle();
        Coord currentPosition = robot.getPosition();
        Orientation currentOrientation = robot.getOrientation();
        Coord targetPosition = (target != null) ? target.getPosition() : null;
        return ((currentOrientation != null) && (currentPosition != null) && (targetPosition != null));
    }

    @Override
    public void updateState(Snapshot snapshot) {
        currentState = snapshot;
    }

    @Override
    public void step(Controller controller) {
        // Fail quickly if state not set
        if (currentState == null)
            return;

        Coord targetCoord = target.getPosition();
        Robot robot = currentState.getBalle();

        Coord currentPosition = robot.getPosition();

        if (isFinished()) {
            if (isMoving) {
                controller.stop();
                isMoving = false;
            }
            return;
        } else {
            // Fail quickly if not possible
            if (!isPossible())
                return;

            double angleToTarget = targetCoord.sub(currentPosition)
                    .orientation();

            Orientation currentOr = currentState.getBalle().getOrientation();
            double currentOrientation = currentOr.atan2styleradians();

            double turnLeftAngle, turnRightAngle;
            if (angleToTarget > currentOrientation) {
                turnLeftAngle = angleToTarget - currentOrientation;
                turnRightAngle = currentOrientation
                        + (2 * Math.PI - angleToTarget);
            } else {
                turnLeftAngle = (2 * Math.PI) - currentOrientation
                        + angleToTarget;
                turnRightAngle = currentOrientation - angleToTarget;
            }

            double turnAngle;

            if (turnLeftAngle < turnRightAngle)
                turnAngle = turnLeftAngle;
            else
                turnAngle = -turnRightAngle;

            double dist = targetCoord.dist(robot.getPosition());
            double distDiffFromTarget = Math.abs(Math.sin(turnAngle) * dist);

            if ((Math.abs(turnAngle) > Math.PI / 2) // sin(180) = sin(0) thus
                                                    // the check
                    || (Math.abs(distDiffFromTarget) > DISTANCE_DIFF * dist)) {
                if (isMoving) {
                    controller.stop();
                    isMoving = false;
                }
                // TODO: This should really be handled by an sub-executor
                // strategy
                // that would turn the robot by amount X if needed.
                if (!isTurning()) {
                    timeToTurn = Math.round(Math.abs(turnAngle)
                            / (TURN_SPEED_RADIANS * 0.001))
                            + SPEED_CONSTANT;

                    System.out.println("Turning " + turnAngle + " should take "
                            + timeToTurn + " ms");
                    controller.rotate((int) (turnAngle * 180 / Math.PI), 180);
                    startedTurning = System.currentTimeMillis();
                }
            } else {
                if (!isMoving) {
                    startedTurning = 0;
                    controller.forward(500);
                    isMoving = true;
                }
            }
        }

    }

    public boolean isTurning() {
        return System.currentTimeMillis() - startedTurning < timeToTurn;
    }

    @Override
    public void stop(Controller controller) {
        // If its doing anything, it will stop
        if (isMoving || isTurning())
            controller.stop();

        // Otherwise it will just make sure to clean up
        startedTurning = 0;
        timeToTurn = 0;
        isMoving = false;

        // Note that we do not want to just call controller.stop()
        // blindly in case there are some other executors using it. (even though
        // there shouldn't be)
    }
}
