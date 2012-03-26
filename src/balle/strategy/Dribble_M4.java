package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class Dribble_M4 extends AbstractPlanner {

    private static final int INITIAL_TURN_SPEED = 100;

    private static final int INITIAL_CURRENT_SPEED = 150;

    private static Logger LOG = Logger.getLogger(Dribble_M4.class);

	private int currentSpeed = INITIAL_CURRENT_SPEED;
	private int turnSpeed = INITIAL_TURN_SPEED;
    private long lastDribbled = 0;
	private double MAX_DRIBBLE_PAUSE = 700; // ms

	public Dribble_M4() {
		super();
	}
	
    @FactoryMethod(designator = "Dribble", parameterNames = {})
	public static Dribble_M4 factoryMethod()
	{
		return new Dribble_M4();
	}
	
	@Override
	public void onStep(Controller controller, Snapshot snapshot) {

        // Make sure to reset the speeds if we haven't been dribbling for a
        // while
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastDribbled) > MAX_DRIBBLE_PAUSE) {
            currentSpeed = INITIAL_CURRENT_SPEED;
            turnSpeed = INITIAL_TURN_SPEED;
        }

        lastDribbled = currentTime;

        boolean facingGoal = snapshot.getBalle().getFacingLine()
				.intersects(snapshot.getOpponentsGoal().getGoalLine());

		if (currentSpeed <= 560) {
			currentSpeed += 20;
		}

		if (turnSpeed <= 150) {
			turnSpeed += 5;
		}

		boolean isLeftGoal = snapshot.getOpponentsGoal().isLeftGoal();

		double angle = snapshot.getBalle().getOrientation().radians();

		double threshold = Math.toRadians(5);

		if (isLeftGoal) {
			if (facingGoal) {
				controller.setWheelSpeeds(currentSpeed, currentSpeed);
                controller.kick();
			} else if ((!facingGoal) && (angle < Math.PI - threshold)) {
				controller.setWheelSpeeds(currentSpeed, currentSpeed
						+ turnSpeed);
			} else if ((!facingGoal) && (angle > Math.PI + threshold)) {
				controller.setWheelSpeeds(currentSpeed + turnSpeed,
						currentSpeed);
			} else {
                controller.setWheelSpeeds(currentSpeed, currentSpeed);
			}
		}

		if (!isLeftGoal) {
			if (facingGoal) {
				controller.setWheelSpeeds(currentSpeed, currentSpeed);
                controller.kick();
			} else if ((!facingGoal) && (angle > threshold)
					&& (angle < Math.PI)) {
				controller.setWheelSpeeds(currentSpeed + turnSpeed,
						currentSpeed);
			} else if ((!facingGoal) && (angle < (2 * Math.PI) - threshold)
					&& (angle > Math.PI)) {
				controller.setWheelSpeeds(currentSpeed, currentSpeed
						+ turnSpeed);
			} else {
                controller.setWheelSpeeds(currentSpeed, currentSpeed);
			}
		}

	}

}