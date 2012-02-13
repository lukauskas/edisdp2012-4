package balle.strategy.executor.dribbling;

import balle.controller.Controller;
import balle.strategy.executor.Executor;
import balle.world.Coord;
import balle.world.FieldObject;
import balle.world.Robot;
import balle.world.Snapshot;

public class DribbleStraight implements Executor {

    Snapshot                 currentState = null;
    boolean                  isMoving     = false;

    private static final int INIT_SPEED   = 320;
    private int              speed        = INIT_SPEED;

    @Override
    public boolean isFinished() {
        // Dribbling never finishes
        // we assume it to dribble straight until told to cancel everything
        return false;
    }

    /**
     * Returns whether it is possible to dribble at this point. It basically
     * answers the question "is the ball nearby?"
     */
    @Override
    public boolean isPossible() {
        if (currentState == null)
            return false;

        FieldObject ball = currentState.getBall();
        Coord target = ball.getPosition();

        Robot robot = currentState.getBalle();
        Coord currentPosition = robot.getPosition();

        if ((target == null) || (currentPosition == null)) {
            return false;
        }

        return ballIsCloseToRobot();
    }

    public boolean ballIsCloseToRobot() {

        double DISTANCE_THRESHOLD = 0.2;
        double EPSILON = 0.000001;
        return currentState.getBalle().getPosition()
                .dist(currentState.getBall().getPosition())
                - DISTANCE_THRESHOLD <= EPSILON;
    }

    @Override
    public void updateState(Snapshot snapshot) {
        currentState = snapshot;
    }

    @Override
    public void step(Controller controller) {

        if (currentState == null) {
            return;
        }

        if ((!isPossible()) && (!isMoving)) // If we're not currently moving and
                                            // it is not possible
            return; // to drible, do not do it!

        if (ballIsCloseToRobot()) {
            if (!isMoving) {
                controller.forward(INIT_SPEED);
                isMoving = true;
            }
            blockBall(controller, currentState.getBall().getPosition(),
                    currentState.getBalle());
        } else {
            // Ball away from robot
            Coord target = currentState.getBall().getPosition();
            Robot robot = currentState.getBalle();
            Coord currentPosition = robot.getPosition();
            double angleToTarget = target.sub(currentPosition).orientation();

            double currentOrientation = robot.getOrientation()
                    .atan2styleradians();

            if ((speed <= 720) && (Math.abs(angleToTarget) <= 5)) {
                speed = speed + 100;
                controller.forward(speed);
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
                    controller.setWheelSpeeds(speed - 120, speed);
                }
                if (turnLeftAngle > turnRightAngle) {
                    controller.setWheelSpeeds(speed, speed - 120);
                }
            }
        }

    }

    protected void blockBall(Controller controller, Coord target, Robot robot) {
        double angleToTarget = target.sub(robot.getPosition()).orientation();
        double currentOrientation = robot.getOrientation().atan2styleradians();

        double turnLeftAngle, turnRightAngle;

        if (angleToTarget > currentOrientation) {
            turnLeftAngle = angleToTarget - currentOrientation;
            turnRightAngle = currentOrientation + (2 * Math.PI - angleToTarget);
        } else {
            turnLeftAngle = (2 * Math.PI) - currentOrientation + angleToTarget;
            turnRightAngle = currentOrientation - angleToTarget;
        }

        if (turnLeftAngle <= turnRightAngle) {
            controller.setWheelSpeeds(speed, speed - 20);
        }
        if (turnLeftAngle > turnRightAngle) {
            controller.setWheelSpeeds(speed - 20, speed);
        }
    }

    /**
     * Stops the robot if it was moving.
     */
    @Override
    public void stop(Controller controller) {
        if (isMoving) {
            controller.stop();
            isMoving = false;
        }
    }

}
