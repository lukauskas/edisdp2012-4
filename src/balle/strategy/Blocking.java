package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.FieldObject;
import balle.world.Robot;

public class Blocking extends AbstractStrategy {
	
    private int INIT_SPEED = 520;
	
    public Blocking(Controller controller, AbstractWorld world) {
        super(controller, world);
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void aiStep() {
		// TODO Auto-generated method stub
		
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
        
        double turnAngle;

        if (turnLeftAngle < turnRightAngle)
            turnAngle = turnLeftAngle;
        else
            turnAngle = -turnRightAngle;
        
        if (turnAngle < 1.5) {
        	controller.forward(INIT_SPEED);
        } else if (turnAngle == 1.5) {
        	controller.stop();
        } else {
        	controller.backward(INIT_SPEED);
        }
		
	}
}


