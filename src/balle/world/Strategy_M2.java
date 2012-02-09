package balle.world;

import balle.controller.Controller;
import balle.strategy.AbstractStrategy;

public class Strategy_M2 extends AbstractStrategy {

	private double EPSILON = 0.000001;
	private double DISTANCE_THRESHOLD = 0.5;
	private int INIT_SPEED = 120;
	private int speed = INIT_SPEED;
	boolean reachedBall = false;
	float distance = 0;

	public Strategy_M2(Controller controller, AbstractWorld world) {
		super(controller, world);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void aiStep() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void aiMove(Controller controller) {
		// TODO Auto-generated method stub
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

		// Ball in front of robot
		if (currentPosition.dist(target) - DISTANCE_THRESHOLD <= EPSILON) {
			System.out.println("Reached the Ball");
			speed = INIT_SPEED;
			controller.forward(speed);
			reachedBall = true;
			//distance++
			if (distance > 1000) {
				controller.stop();
			}

		}

		// Ball away from robot
		
		if ((currentPosition.dist(target) - DISTANCE_THRESHOLD) > EPSILON) {
			
			double angleToTarget = -1
					* target.sub(currentPosition).orientation();
			
			double currentOrientation = robot.getOrientation()
			.atan2styleradians();

			if ((speed <= 320) && (angleToTarget <= currentOrientation)) {
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

				if (turnLeftAngle < turnRightAngle)
					//controller.setWheelSpeeds(INIT_SPEED - 20, INIT_SPEED);
					controller.forward(120);
					System.out.println("Turning Left");
				if (turnLeftAngle > turnRightAngle)
					controller.forward(120);
					//controller.setWheelSpeeds(INIT_SPEED, INIT_SPEED - 20);
					System.out.println("Turning Right");

			}
			
			if (reachedBall == true) {
				//distance ++
			}
			
			if (distance > 1000) {
				controller.stop();
			}

		}

	}
}
