package balle.strategy.planner;

import java.util.Date;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Point;
import balle.world.objects.Robot;


public class BackingOffStrategy extends GoToBall {

	private static final Logger LOG = Logger
			.getLogger(BackingOffStrategy.class);

	/**
	 * Distance to back off (in m)
	 */
	private static final double BACK_OFF_GAP = 1;

	/**
	 * Max time we can spend backing off
	 */
	private static final long BACK_OFF_TIME = (long) (0.5 * 1000);

	/**
	 * Velocity threshold below which we could be stuck
	 */
	private static final double VELOCITY_THRESH = 3E-5; // NOTE: noise renders
														// the velocity really
														// unreliable atm

	/**
	 * Distance between our front and the other robot below which we could be
	 * stuck
	 */
	private static final double DISTANCE_THRESH = Globals.ROBOT_LENGTH * 3 / 4;

	/**
	 * Time in milliseconds that must pass before we decide that we're stuck
	 */
	private static final long TIME_THRESH = 2 * 1000;

	private long timeWhenMaybeStuck = -1;
	private long timeStartedBackingOff = -1;

	private Point target;

	public BackingOffStrategy() {
		super(new GoToObjectPFN(0), false);
		setShouldAvoidOpponent(false);
	}

	@Override
	public boolean couldRun(Snapshot snapshot) {
		Robot us = snapshot.getBalle();
		Line ourFront = us.getFrontSide();
		Robot opponent = snapshot.getOpponent();
		
		if (timeStartedBackingOff > 0) {
			return true;
		}
		
		if (ourFront.dist(opponent.getPosition()) < DISTANCE_THRESH
				&& us.getVelocity().abs() < VELOCITY_THRESH) {

			long timeNow = new Date().getTime();
			if (timeWhenMaybeStuck == -1) {
				timeWhenMaybeStuck = timeNow;
				return false;
			}

			if (timeNow - timeWhenMaybeStuck > TIME_THRESH) {
				timeStartedBackingOff = timeNow;
				setTarget(snapshot);

				LOG.info("We're stuck! Back off");
				return true;
			} else {
				return false;
			}
		}

		timeWhenMaybeStuck = -1;
		return false;
	}

	private void setTarget(Snapshot snapshot) {
		Robot ourRobot = snapshot.getBalle();
		Coord target = ourRobot.getFacingLine().flip().extend(BACK_OFF_GAP)
				.getB();

		this.target = new Point(target);
	}

	@Override
	protected FieldObject getTarget(Snapshot snapshot) {
		return target;
	}

	@Override
	protected void onStep(Controller controller, Snapshot snapshot) {

		LOG.trace("Backing off!");

		long timeNow = new Date().getTime();

		if (timeNow - timeStartedBackingOff > BACK_OFF_TIME) {
			timeStartedBackingOff = -1;
			timeWhenMaybeStuck = -1;

			LOG.info("Finished backing off");
		}

		super.onStep(controller, snapshot);
	}

}
