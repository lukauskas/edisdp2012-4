package balle.strategy;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Robot;

public class Blocking extends AbstractPlanner {

    private int     INIT_SPEED    = 450;
    private boolean goingForward  = false;
    private boolean goingBackward = false;

    @Override
    public void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {
		if (snapshot == null) {
            System.out.println("No Snapshot");
            return;
        }

		FieldObject ball = snapshot.getBall();
        Coord target = (ball != null) ? ball.getPosition() : null;

		Robot robot = snapshot.getBalle();
        Coord currentPosition = robot != null ? robot.getPosition() : null;

        double angleToTarget = target.sub(robot.getPosition()).orientation()
                .atan2styleradians();
        double currentOrientation = robot.getOrientation().atan2styleradians();

        double turnLeftAngle, turnRightAngle;

        if (angleToTarget > currentOrientation) {
            turnLeftAngle = angleToTarget - currentOrientation;
            turnRightAngle = currentOrientation + (2 * Math.PI - angleToTarget);
        } else {
            turnLeftAngle = (2 * Math.PI) - currentOrientation + angleToTarget;
            turnRightAngle = currentOrientation - angleToTarget;
        }

        double turnAngle;

        if (turnLeftAngle < turnRightAngle)
            turnAngle = turnLeftAngle;
        else
            turnAngle = -turnRightAngle;

        if (turnAngle < 1.5) {
            if (!goingForward) {
                controller.stop();
                controller.forward(INIT_SPEED);
                goingForward = true;
                goingBackward = false;
            }
        } else if (turnAngle == 1.5) {
            controller.stop();
            goingBackward = false;
            goingForward = false;
        } else {
            if (!goingBackward) {
                controller.backward(INIT_SPEED);
                goingForward = false;
                goingBackward = true;
            }
        }

    }
}
