package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.FieldObject;
import balle.world.Robot;

public class Strategy_M2 extends AbstractStrategy {

    private static final double DISTANCE_TO_TRAVEL    = 1;
    private double              EPSILON               = 0.000001;
    private double              DISTANCE_THRESHOLD    = 0.13;
    private int                 INIT_SPEED            = 220;
    private int                 speed                 = INIT_SPEED;
    boolean                     reachedBall           = false;
    float                       distance              = 0;
    private Coord               startCoord            = null;
    private boolean             assumeDistanceReached = false;

    public Strategy_M2(Controller controller, AbstractWorld world) {
        super(controller, world);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void aiStep() {
        // TODO Auto-generated method stub

    }

    protected boolean reachedDistance() {
        if (assumeDistanceReached)
            return true;
        if (startCoord == null)
            return false;

        Coord currentCoord = getSnapshot().getBalle().getPosition();
        if (currentCoord == null)
            return true; // TODO: is this right?

        double distanceTravelled = currentCoord.sub(startCoord).abs();
        System.out.println("Distance travelled: " + distanceTravelled + " m");
        boolean result = distanceTravelled >= DISTANCE_TO_TRAVEL;
        assumeDistanceReached = result;
        return result;
    }

    @Override
    protected void aiMove(Controller controller) {
        if (getSnapshot() == null) {
            System.out.println("No Snapshot");
            return;
        }

        FieldObject ball = getSnapshot().getBall();
        Coord target = (ball != null) ? ball.getPosition() : null;

        Robot robot = getSnapshot().getBalle();
        Coord currentPosition = robot != null ? robot.getPosition() : null;

        if ((target == null) || (currentPosition == null)) {
            System.out.println("Vision Error, Cannot see Ball or Self");
            return;
        }

        if (reachedDistance()) {
            System.out.println("Reached distance, stopping!");
            controller.stop();
            return;
        }

        // Ball in front of robot
        if (currentPosition.dist(target) - DISTANCE_THRESHOLD <= EPSILON) {
            System.out.println("Reached the Ball");
            if (reachedBall == false) {
                System.out.println("Setting start coordinate");
                startCoord = getSnapshot().getBalle().getPosition();
            }
            speed = INIT_SPEED;
            controller.forward(speed);
            reachedBall = true;
        } else {
            // Ball away from robot
            double angleToTarget = target.sub(currentPosition).orientation();

            double currentOrientation = robot.getOrientation()
                    .atan2styleradians();

            if ((speed <= 720) && (Math.abs(angleToTarget) <= 5)) {
                speed = speed + 100;
                controller.forward(speed);
                System.out.println("Speeding Up");
            } else {

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

                if (turnLeftAngle <= turnRightAngle) {
                    // controller.setWheelSpeeds(speed - 120, speed);
                    // System.out.println("Turning Left");
                    controller.setWheelSpeeds(speed, speed - 120);
                    System.out.println("Blocking Right");
                }
                if (turnLeftAngle > turnRightAngle) {
                    // controller.setWheelSpeeds(speed, speed - 120);
                    // System.out.println("Turning Right");
                    controller.setWheelSpeeds(speed - 120, speed);
                    System.out.println("Blocking Left");
                }

            }
        }
    }
}
