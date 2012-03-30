package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class Dribble extends AbstractPlanner {

    private static final int INITIAL_TURN_SPEED = 100;

    private static final int INITIAL_CURRENT_SPEED = 150;

    private static Logger LOG = Logger.getLogger(Dribble.class);

	private int currentSpeed = INITIAL_CURRENT_SPEED;
	private int turnSpeed = INITIAL_TURN_SPEED;
    private long lastDribbled = 0;
    private long firstDribbled = 0;
	private double MAX_DRIBBLE_PAUSE = 700; // ms
    private double MAX_DRIBBLE_LENGTH = 500; // ms

    private boolean triggerHappy;

    public boolean isTriggerHappy() {
        return triggerHappy;
    }

    public void setTriggerHappy(boolean triggerHappy) {
        this.triggerHappy = triggerHappy;
    }

    public Dribble() {
        this(false);
	}
	
    public Dribble(boolean triggerHappy) {
        super();
        setTriggerHappy(triggerHappy);
    }

    @FactoryMethod(designator = "Dribble", parameterNames = {})
	public static Dribble factoryMethod()
	{
		return new Dribble();
	}

    public boolean isDribbling() {
        double deltaPause = (System.currentTimeMillis() - lastDribbled);
        double deltaStart = (System.currentTimeMillis() - firstDribbled);
        return deltaPause < MAX_DRIBBLE_PAUSE
                && deltaStart < MAX_DRIBBLE_LENGTH;
    }
	
	@Override
	public void onStep(Controller controller, Snapshot snapshot) {

        // Make sure to reset the speeds if we haven't been dribbling for a
        // while
        long currentTime = System.currentTimeMillis();
        if (!isDribbling()) {
            currentSpeed = INITIAL_CURRENT_SPEED;
            turnSpeed = INITIAL_TURN_SPEED;
            firstDribbled = currentTime;
        }

        lastDribbled = currentTime;

        boolean facingGoal = snapshot.getBalle().getFacingLine()
				.intersects(snapshot.getOpponentsGoal().getGoalLine());

        if (snapshot.getBall().getPosition() != null)
            facingGoal = facingGoal
                    || snapshot
                            .getBalle()
                            .getBallKickLine(snapshot.getBall())
                            .intersects(
                                    snapshot.getOpponentsGoal().getGoalLine());


		if (currentSpeed <= 560) {
			currentSpeed += 20;
		}

		if (turnSpeed <= 150) {
			turnSpeed += 5;
		}

        int turnSpeedToUse = turnSpeed;

		boolean isLeftGoal = snapshot.getOpponentsGoal().isLeftGoal();

		double angle = snapshot.getBalle().getOrientation().radians();

		double threshold = Math.toRadians(5);
		
		boolean facingOwnGoalSide = snapshot.getBalle().isFacingGoalHalf(snapshot.getOwnGoal());
        boolean nearWall = snapshot.getBall().isNearWall(snapshot.getPitch());

        // Turn twice as fast near walls
        if (nearWall)
            turnSpeedToUse *= 2;

		if (isLeftGoal) {
			if (facingGoal) {
                controller.setWheelSpeeds(Globals.MAXIMUM_MOTOR_SPEED,
                        Globals.MAXIMUM_MOTOR_SPEED);
            } else if ((!facingGoal) && (angle < Math.PI - threshold)) {
				controller.setWheelSpeeds(currentSpeed, currentSpeed
                        + turnSpeedToUse);
			} else if ((!facingGoal) && (angle > Math.PI + threshold)) {
                controller.setWheelSpeeds(currentSpeed + turnSpeedToUse,
						currentSpeed);
			} else {
                controller.setWheelSpeeds(currentSpeed, currentSpeed);
			}
        } else {
			if (facingGoal) {
                controller.setWheelSpeeds(Globals.MAXIMUM_MOTOR_SPEED,
                        Globals.MAXIMUM_MOTOR_SPEED);
            } else if ((!facingGoal) && (angle > threshold)
					&& (angle < Math.PI)) {
                controller.setWheelSpeeds(currentSpeed + turnSpeedToUse,
						currentSpeed);
			} else if ((!facingGoal) && (angle < (2 * Math.PI) - threshold)
					&& (angle > Math.PI)) {
				controller.setWheelSpeeds(currentSpeed, currentSpeed
                        + turnSpeedToUse);
			} else {
                controller.setWheelSpeeds(currentSpeed, currentSpeed);
			}
		}

        if (facingGoal || (isTriggerHappy() && nearWall && !facingOwnGoalSide)) {
            controller.kick();
        }

	}

}