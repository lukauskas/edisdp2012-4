package balle.strategy.executor.dribbling;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.strategy.executor.Executor;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Robot;

public class DribbleStraight implements Executor {

    boolean                  isMoving     = false;

    private static final int INIT_SPEED   = 320;
    private int              speed        = INIT_SPEED;

    @Override
	public boolean isFinished(Snapshot snapshot) {
        // Dribbling never finishes
        // we assume it to dribble straight until told to cancel everything
        return false;
    }

    /**
     * Returns whether it is possible to dribble at this point. It basically
     * answers the question "is the ball nearby?"
     */
    @Override
	public boolean isPossible(Snapshot snapshot) {
		if (snapshot == null)
            return false;

		FieldObject ball = snapshot.getBall();
        Coord target = ball.getPosition();

		Robot robot = snapshot.getBalle();
        Coord currentPosition = robot.getPosition();

        if ((target == null) || (currentPosition == null)) {
            return false;
        }

		return ballIsCloseToRobot(snapshot);
    }

	public boolean ballIsCloseToRobot(Snapshot snapshot) {

        double DISTANCE_THRESHOLD = 0.15;
        double EPSILON = 0.000001;
        return snapshot.getBalle().getPosition().dist(snapshot.getBall().getPosition())
                - DISTANCE_THRESHOLD <= EPSILON;
    }

    @Override
	public void step(Controller controller, Snapshot snapshot) {

		if (snapshot == null) {
            return;
        }

        if ((!isPossible(snapshot)) && (!isMoving)) // If we're not currently moving and
                                            // it is not possible
            return; // to drible, do not do it!

		if (ballIsCloseToRobot(snapshot)) {
            if (!isMoving) {
                controller.forward(INIT_SPEED);
                isMoving = true;
            }
			blockBall(controller, snapshot.getBall().getPosition(),
					snapshot.getBalle());
        } else {
            // Ball away from robot
			Coord target = snapshot.getBall().getPosition();
			Robot robot = snapshot.getBalle();
            Coord currentPosition = robot.getPosition();
            double angleToTarget = target.sub(currentPosition).orientation().atan2styleradians();

            double currentOrientation = robot.getOrientation().atan2styleradians();

            if ((speed <= 720) && (Math.abs(angleToTarget) <= 5)) {
                speed = speed + 100;
                controller.forward(speed);
            } else {

                double turnLeftAngle, turnRightAngle;

                if (angleToTarget > currentOrientation) {
                    turnLeftAngle = angleToTarget - currentOrientation;
                    turnRightAngle = currentOrientation + (2 * Math.PI - angleToTarget);
                } else {
                    turnLeftAngle = (2 * Math.PI) - currentOrientation + angleToTarget;
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
        double angleToTarget = target.sub(robot.getPosition()).orientation().atan2styleradians();
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

    @Override
    public ArrayList<Drawable> getDrawables() {
        return new ArrayList<Drawable>();
    }

}
