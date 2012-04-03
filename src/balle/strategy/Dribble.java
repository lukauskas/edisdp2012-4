package balle.strategy;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Label;
import balle.misc.Globals;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Coord;
import balle.world.Orientation;
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
    private static final double ABOUT_TO_LOSE_BALL_THRESHOLD = Globals.ROBOT_WIDTH
            / 2 + Globals.BALL_RADIUS - 0.02;

    private static final double FACING_WALL_THRESHOLD = Math.toRadians(25);
    private static final double SPINNING_DISTANCE = Globals.DISTANCE_TO_WALL;// Globals.ROBOT_LENGTH
                                                                             // /
                                                                             // 2
                                                                             // +
                                                                             // 0.02;

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

    public boolean shouldStopDribblingDueToDribbleLength() {
        double deltaStart = (System.currentTimeMillis() - firstDribbled);
        return deltaStart > MAX_DRIBBLE_LENGTH;
    }

    public boolean isInactiveForAWhile() {
        double deltaPause = (System.currentTimeMillis() - lastDribbled);

        return deltaPause > MAX_DRIBBLE_PAUSE;
    }
    public boolean isDribbling() {
        return !isInactiveForAWhile()
                && (!shouldStopDribblingDueToDribbleLength());
    }

    public void spinLeft(Snapshot snapshot, Controller controller, int speed) {
        controller.setWheelSpeeds(-speed, speed);
        addDrawable(new Label("<---", snapshot.getBalle().getPosition(),
                Color.CYAN));
    }
	
    public void spinRight(Snapshot snapshot, Controller controller, int speed) {
        controller.setWheelSpeeds(speed, -speed);
        addDrawable(new Label("--->", snapshot.getBalle().getPosition(),
                Color.CYAN));
    }

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {

        if (snapshot.getBalle().getPosition() == null)
            return;

        // Make sure to reset the speeds if we haven't been dribbling for a
        // while
        long currentTime = System.currentTimeMillis();
        boolean facingOwnGoalSide = snapshot.getBalle().isFacingGoalHalf(snapshot.getOwnGoal());
      
        if (!isDribbling()) {
            // Kick the ball if we're triggerhappy and should stop dribbling
            if (isTriggerHappy() && !isInactiveForAWhile()
                    && shouldStopDribblingDueToDribbleLength()
                    && !facingOwnGoalSide)
                controller.kick();
            currentSpeed = INITIAL_CURRENT_SPEED;
            turnSpeed = INITIAL_TURN_SPEED;
            firstDribbled = currentTime;
        }

        lastDribbled = currentTime;

        boolean facingGoal = snapshot.getBalle().getFacingLine().intersects(snapshot.getOpponentsGoal().getGoalLine());

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

        double distanceToBall = snapshot.getBalle().getFrontSide().midpoint()
                .dist(snapshot.getBall().getPosition());


        if (snapshot.getBall().getPosition().isEstimated())
            distanceToBall = 0;

        boolean aboutToLoseBall = distanceToBall >= ABOUT_TO_LOSE_BALL_THRESHOLD;
        Color c = Color.BLACK;
        if (aboutToLoseBall)
            c = Color.PINK;

        Coord ourPos = snapshot.getBalle().getPosition();
        addDrawable(new Label(String.format("%.5f", distanceToBall), new Coord(
                ourPos.getX(), ourPos.getY()), c));
                
        int turnSpeedToUse = turnSpeed;

		boolean isLeftGoal = snapshot.getOpponentsGoal().isLeftGoal();

		double angle = snapshot.getBalle().getOrientation().radians();

		double threshold = Math.toRadians(5);
		
        boolean nearWall = snapshot.getBall().isNearWall(snapshot.getPitch());
        boolean wereNearWall = snapshot.getBalle().isNearWall(
                snapshot.getPitch(), SPINNING_DISTANCE);

        boolean closeToGoal = snapshot.getOpponentsGoal().getGoalLine()
                .dist(snapshot.getBalle().getPosition()) < SPINNING_DISTANCE;

        // Actually it might be helpful to turn when we're in this situation
        // close to our own goal
        closeToGoal = closeToGoal
                || snapshot.getOwnGoal().getGoalLine()
                        .dist(snapshot.getBalle().getPosition()) < SPINNING_DISTANCE;
        // Turn twice as fast near walls
        if (nearWall)
            turnSpeedToUse *= 2;
        
		if (isTriggerHappy() && (!closeToGoal) && (nearWall) && wereNearWall)
        {
            Coord goalVector = snapshot.getOwnGoal().getGoalLine()
                    .midpoint().sub(ourPos);
            Orientation angleTowardsGoal = goalVector.orientation();

            // Always turn opposite from own goal
            boolean shouldTurnRight = !angleTowardsGoal.isFacingRight(0);

            // If we're facing wall
            if ((Math.abs(angle) <= FACING_WALL_THRESHOLD)
                || (Math.abs(angle - Math.PI / 2) <= FACING_WALL_THRESHOLD)
                || (Math.abs(angle - Math.PI) <= FACING_WALL_THRESHOLD)
                    || (Math.abs(angle - 3 * Math.PI / 2) <= FACING_WALL_THRESHOLD)) {
                LOG.info("Spinning!!!");

                // If We are facing the bottom wall we should flip the spinning
                // directions
                if (Math.abs(angle - 3 * Math.PI / 2) <= FACING_WALL_THRESHOLD)
                    shouldTurnRight = !shouldTurnRight;
                else if ((Math.abs(angle) <= FACING_WALL_THRESHOLD)
                        || (Math.abs(angle - Math.PI) <= FACING_WALL_THRESHOLD))
                {
                    // If we're facing one of the walls with goals
                    boolean facingLeftWall = (Math.abs(angle) <= FACING_WALL_THRESHOLD);
                    if (facingLeftWall) {
                        shouldTurnRight = snapshot.getOpponentsGoal()
                                .getGoalLine()
                                .midpoint().getY() > ourPos.getY();
                         
                        // Turn away from own goal
                        if (snapshot.getOwnGoal().isLeftGoal())
                            shouldTurnRight = !shouldTurnRight;
                    } else {
                        shouldTurnRight = snapshot.getOpponentsGoal()
                                .getGoalLine().midpoint().getY() < ourPos
                                .getY();

                        // Turn away from own goal
                        if (snapshot.getOwnGoal().isRightGoal())
                            shouldTurnRight = !shouldTurnRight;
                    }
                }
                if (shouldTurnRight)
                    spinRight(snapshot, controller, Globals.MAXIMUM_MOTOR_SPEED);
                else
                    spinLeft(snapshot, controller, Globals.MAXIMUM_MOTOR_SPEED);

                return;
            }
  
                
        }

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

        if (facingGoal || (isTriggerHappy() && nearWall && !facingOwnGoalSide)
                || (isTriggerHappy() && aboutToLoseBall && !facingOwnGoalSide)) {
            controller.kick();
        }

	}

}