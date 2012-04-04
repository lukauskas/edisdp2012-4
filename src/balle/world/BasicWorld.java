package balle.world;

import org.apache.log4j.Logger;

import balle.world.objects.Ball;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class BasicWorld extends AbstractWorld {

    private static final Logger LOG = Logger.getLogger(BasicWorld.class);

	protected Snapshot prev, prevRaw;

	private final Estimator ballEstimator;
	private final Estimator ourRobotEstimator;
	private final Estimator theirRobotEstimator;

	public BasicWorld(boolean balleIsBlue, boolean goalIsLeft, Pitch pitch) {
		super(balleIsBlue, goalIsLeft, pitch);

		ballEstimator = Estimator.getBallEstimator();
		ourRobotEstimator = Estimator.getRobotEstimator();
		theirRobotEstimator = Estimator.getRobotEstimator();

		prev = new EmptySnapshot(this);
		prevRaw = new EmptySnapshot(this);
	}

	@Override
	public synchronized Snapshot getSnapshot() {
		return prev;
	}

	public synchronized Snapshot getSnapshotRaw() {
		return prevRaw;
	}

	public Estimator getBallEstimator() {
		return ballEstimator;
	}

	/**
	 * Returns true if this position seems to be reasonable enough. Returns true
	 * if previousPosition was null and false if currentPosition is null
	 * 
	 * TODO: Make this function account for velocity and check whether it is
	 * close to the expected position at this velocity!!
	 * 
	 * @param previousPosition
	 * @param currentPosition
	 * @return
	 */
	protected boolean positionIsCloseToExpected(Coord previousPosition,
			Coord currentPosition) {

		final double EPSILON = 1; // 1 Metre

		if (previousPosition == null)
			return true;
		else if (currentPosition == null)
			return false;
		// Check whether current position is 1m further than previous position
		// and previous position wasn't estimated
		else if (previousPosition.dist(currentPosition) > EPSILON
				&& !previousPosition.isEstimated())
			return false;
		else
			return true;

	}

	/**
	 * NOTE: DO ROBOTS ALWAYS MOVE FORWARD !? NO, treat angle of velocity
	 * different from angle the robot is facing.
	 * 
	 */
	@Override
	public void updateScaled(Coord ourPosition, Orientation ourOrientation,
			Coord theirsPosition, Orientation theirsOrientation,
			Coord ballPosition, long timestamp) {

		Robot ours = null;
		Robot them = null;
		Ball ball = null;

		Snapshot prev = getSnapshot();

		// change in time
		long deltaT = timestamp - prev.getTimestamp(); // Hopefully that does
														// not cause issues
														// with EmptySnapshot
														// and
														// currentTimeMilis()

		// Special case when we get two inputs with the same timestamp:
		if (deltaT == 0) {
			// This will just keep the prev world in the memory, not doing
			// anything
			return;
		}

		ourRobotEstimator.update(ourPosition, deltaT);
		ourPosition = ourRobotEstimator.getPosition();

		theirRobotEstimator.update(ourPosition, deltaT);
		ourPosition = theirRobotEstimator.getPosition();

		ballEstimator.update(ballPosition, deltaT);
		ballPosition = ballEstimator.getPosition();

		Velocity oursVel = ourRobotEstimator.getVelocity();
		Velocity themVel = theirRobotEstimator.getVelocity();
		Velocity ballVel = ballEstimator.getVelocity();

		// LOG.debug("BallVel = " + ballVel.abs() * 1000 + " "
		// + ballPosition.isEstimated());

		AngularVelocity oursAngVel = null, themAngVel = null;
		if (ourOrientation == null) {
			ourOrientation = prev.getBalle().getOrientation();
		} else if (prev.getBalle().getOrientation() != null) {
			oursAngVel = new AngularVelocity(
					ourOrientation.angleToatan2Radians(prev.getBalle()
							.getOrientation()), deltaT);
		}

		if (theirsOrientation == null) {
			theirsOrientation = prev.getOpponent().getOrientation();
		} else if (prev.getOpponent().getOrientation() != null) {
			themAngVel = new AngularVelocity(
					theirsOrientation.angleToatan2Radians(prev.getOpponent()
							.getOrientation()), deltaT);
		}

		themAngVel = (them != null) ? new AngularVelocity(
				theirsOrientation.angleToatan2Radians(prev.getOpponent()
						.getOrientation()), deltaT) : null;

		// put it all together (almost)
		them = new Robot(theirsPosition, themVel, themAngVel, theirsOrientation);
		ours = new Robot(ourPosition, oursVel, oursAngVel, ourOrientation);
		ball = new Ball(ballPosition, ballVel);

		// Pack into a snapshot
		Snapshot nextSnapshot = new Snapshot(this, them, ours, ball, timestamp);
		prevRaw = nextSnapshot;

		nextSnapshot = filter(nextSnapshot);
        updateSnapshot(nextSnapshot);
    }

    protected synchronized void updateSnapshot(Snapshot nextSnapshot) {
		prev = nextSnapshot;
	}

	@Override
	public void updatePitchSize(double width, double height) {
		super.updatePitchSize(width, height);
		synchronized (this) {
			ballEstimator.reset();
			ourRobotEstimator.reset();
			theirRobotEstimator.reset();

			prev = new EmptySnapshot(this);
			prevRaw = new EmptySnapshot(this);
		}
	}

}
