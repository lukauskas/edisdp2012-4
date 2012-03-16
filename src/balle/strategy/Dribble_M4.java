package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.world.Coord;
import balle.world.Snapshot;

public class Dribble_M4 extends Game {

	private static Logger LOG = Logger.getLogger(GameFromPenaltyDefence.class);

	private int current_speed = 150;
	private int turn_speed = 100;
	private Coord startingCoordinate = null;
	private Coord currentCoordinate = null;
	private static final double DISTANCE_TO_TRAVEL = 0.2;

	public Dribble_M4() {
		super();
	}
	
	@FactoryMethod(designator = "Game (Dribble)")
	public static Dribble_M4 Dribble_M4()
	{
		return new Dribble_M4();
	}

	public boolean Dribbling(Snapshot snapshot) {
		
		return true;
	}
	
	@Override
	public void onStep(Controller controller, Snapshot snapshot) {

		if (startingCoordinate == null) {
			startingCoordinate = snapshot.getBalle().getPosition();
		}

		currentCoordinate = snapshot.getBalle().getPosition();

		boolean facingGoal = snapshot.getBalle().getFacingLine()
				.intersects(snapshot.getOpponentsGoal().getGoalLine());

		if (current_speed <= 560) {
			current_speed += 20;
		}

		if (turn_speed <= 150) {
			turn_speed += 5;
		}

		boolean isLeftGoal = snapshot.getOpponentsGoal().isLeftGoal();

		double angle = snapshot.getBalle().getOrientation().radians();

		double threshold = Math.toRadians(5);

		if (isLeftGoal) {
			if (facingGoal) {
				controller.setWheelSpeeds(current_speed, current_speed);
				if (currentCoordinate.dist(startingCoordinate) > DISTANCE_TO_TRAVEL) {
					controller.kick();
				}
			} else if ((!facingGoal) && (angle < Math.PI - threshold)) {
				controller.setWheelSpeeds(current_speed, current_speed
						+ turn_speed);
			} else if ((!facingGoal) && (angle > Math.PI + threshold)) {
				controller.setWheelSpeeds(current_speed + turn_speed,
						current_speed);
			} else {
				controller.setWheelSpeeds(current_speed, current_speed);
			}
		}

		if (!isLeftGoal) {
			if (facingGoal) {
				controller.setWheelSpeeds(current_speed, current_speed);
				if (currentCoordinate.dist(startingCoordinate) > DISTANCE_TO_TRAVEL) {
					controller.kick();
				}
			} else if ((!facingGoal) && (angle > threshold)
					&& (angle < Math.PI)) {
				controller.setWheelSpeeds(current_speed + turn_speed,
						current_speed);
			} else if ((!facingGoal) && (angle < (2 * Math.PI) - threshold)
					&& (angle > Math.PI)) {
				controller.setWheelSpeeds(current_speed, current_speed
						+ turn_speed);
			} else {
				controller.setWheelSpeeds(current_speed, current_speed);
			}
		}

	}

}