package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.FieldObject;
import balle.world.Robot;

public class Strategy_M2 extends AbstractStrategy {

	private double EPSILON = 0.000001;
	private double DISTANCE_THRESHOLD = 0.2;
	private int INIT_SPEED = 220;
	private int speed = INIT_SPEED;
	private double timer_start;
	private double timer_travel;
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
		
		if (distance > 30) {
			controller.stop();
		}

		// Ball in front of robot
		if ((currentPosition.dist(target) - DISTANCE_THRESHOLD <= EPSILON) && (distance <= 30)) {
			System.out.println("Reached the Ball");
			if (reachedBall == false) {
				timer_start = System.currentTimeMillis();
			}
			speed = INIT_SPEED;
			controller.forward(speed);
			reachedBall = true;
			timer_travel = (System.currentTimeMillis() - timer_start) / 1000;
			distance = distance + (float) (timer_travel * speed/360);

		}

		// Ball away from robot
		
		if (((currentPosition.dist(target) - DISTANCE_THRESHOLD) > EPSILON) && (distance <= 30)) {
			
			double angleToTarget = target.sub(currentPosition).orientation();
			
			double currentOrientation = robot.getOrientation()
			.atan2styleradians();

			if ((speed <= 720) && (Math.abs(angleToTarget) <= 5)) {
				speed = speed + 100;
				controller.forward(speed);
				System.out.println("Speeding Up");
				if (reachedBall == true) {
					timer_travel = (System.currentTimeMillis() - timer_start) / 1000;
					distance = distance + (float) (timer_travel * speed/360);
				}
				

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
					controller.setWheelSpeeds(speed - 120, speed);
					System.out.println("Turning Left");
					//controller.setWheelSpeeds(speed, speed - 120);
					//System.out.println("Blocking Right");
					if (reachedBall == true) {
						timer_travel = (System.currentTimeMillis() - timer_start) / 1000;
						distance = distance + (float) (timer_travel * (speed)/360);
					}
				if (turnLeftAngle > turnRightAngle)
					controller.setWheelSpeeds(speed, speed - 120);
					System.out.println("Turning Right");
					//controller.setWheelSpeeds(speed - 120, speed);
					//System.out.println("Blocking Left");
					if (reachedBall == true) {
						timer_travel = (System.currentTimeMillis() - timer_start) / 1000;
						distance = distance + (float) (timer_travel * (speed)/360);
					}

			}
			

		}

	}
}
