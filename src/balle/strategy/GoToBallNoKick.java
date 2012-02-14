package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.FieldObject;
import balle.world.Orientation;
import balle.world.Robot;

public class GoToBallNoKick extends AbstractStrategy {

    private static final int    TURN_SPEED_DEGREES = 180;
    private static final double TURN_SPEED_RADIANS = (TURN_SPEED_DEGREES * Math.PI) / 180;
    private boolean             isMoving           = false;
    private long                startedTurning     = 0;
    private long                timeToTurn         = 0;
    private final static double DISTANCE_THRESHOLD = 0.23;
    private final static double SLOWDOWN_DISTANCE  = 0.4;
    private final static double EPSILON            = 0.00001;
    private final static double DISTANCE_DIFF      = 0.3;
    private final static int    SPEED_CONSTANT     = 700;
    private final static int    MOVEMENT_SPEED     = 500;
    private int                 currentSpeed       = 0;

    public GoToBallNoKick(Controller controller, AbstractWorld world) {
        super(controller, world);
    }

    @Override
    protected void aiStep() {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "Go to ball";
    }

    @Override
    protected void aiMove(Controller controller) {
        // Assume ball is static
        if (getSnapshot() == null)
            return;

        FieldObject ball = getSnapshot().getBall();
        Coord target = (ball != null) ? ball.getPosition() : null;
        Robot robot = getSnapshot().getBalle();

        Coord currentPosition = robot != null ? robot.getPosition() : null;
        if ((target == null) || (currentPosition == null)) {
            System.out.println("Cannot see ball or self");
            return;
        }
        // System.out.println("Ball " + target.getX() + " " + target.getY());
        double distanceToTarget = currentPosition.dist(target);
        if ((distanceToTarget - DISTANCE_THRESHOLD) < EPSILON) {
            if (isMoving) {
                System.out.println("Target reached");
                controller.stop();
                isMoving = false;
            }
            return;
        } else {

            double angleToTarget = target.sub(currentPosition).orientation();

            Orientation currentOr = robot.getOrientation();
            if (currentOr == null) {
                System.out.println("getOrientation is null");
                if (isMoving) {
                    controller.stop();
                    isMoving = false;
                }
                return;
            }
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

            double dist = target.dist(robot.getPosition());
            double distDiffFromTarget = Math.abs(Math.sin(turnAngle) * dist);

            if ((Math.abs(turnAngle) > Math.PI / 2) // sin(180) = sin(0) thus
                                                    // the check
                    || (Math.abs(distDiffFromTarget) > DISTANCE_DIFF * dist)) {
                if (isMoving) {
                    controller.stop();
                    isMoving = false;
                }
                if (System.currentTimeMillis() - startedTurning > timeToTurn) {
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
                    if (distanceToTarget > SLOWDOWN_DISTANCE) {
                        currentSpeed = MOVEMENT_SPEED;
                    } else {
                        currentSpeed = MOVEMENT_SPEED - 100;
                    }

                    controller.forward(currentSpeed);
                    isMoving = true;
                } else {
                    if ((distanceToTarget > SLOWDOWN_DISTANCE)
                            && (currentSpeed < MOVEMENT_SPEED)) {
                        currentSpeed = MOVEMENT_SPEED;
                        controller.forward(currentSpeed);
                    } else if ((distanceToTarget <= SLOWDOWN_DISTANCE)
                            && (currentSpeed == MOVEMENT_SPEED)) {
                        currentSpeed = MOVEMENT_SPEED - 100;
                        controller.forward(currentSpeed);
                    }
                }

            }

            // System.out.println("Angle to target unadjusted: "
            // + (((angleToTarget * 180) / Math.PI)));
            // System.out.println("Current orientation: "
            // + (robot.getOrientation().atan2styledegrees()));
            // System.out.println("Turn angle " + ((turnAngle * 180)) /
            // Math.PI);

        }

    }
}
