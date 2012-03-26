package balle.world;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import balle.world.objects.Ball;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

public class BasicWorld extends AbstractWorld {

	private static final int FRAMES_FOR_VELOCITY = 5;

    private static final Logger LOG = Logger.getLogger(BasicWorld.class);

	protected ArrayList<Snapshot> pastRaw = new ArrayList<Snapshot>();
	protected Snapshot prev, prevRaw;

	public BasicWorld(boolean balleIsBlue, boolean goalIsLeft, Pitch pitch) {
		super(balleIsBlue, goalIsLeft, pitch);
		prev = new EmptySnapshot(getOpponentsGoal(), getOwnGoal(), getPitch());
		prevRaw = new EmptySnapshot(getOpponentsGoal(), getOwnGoal(),
				getPitch());
	}

	@Override
	public synchronized Snapshot getSnapshot() {
		return prev;
	}

	public synchronized Snapshot getSnapshotRaw() {
		return prevRaw;
	}

	private Coord subtractOrNull(Coord a, Coord b) {
		if ((a == null) || (b == null))
			return null;
		else
			return a.sub(b);
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

		Snapshot prev = getSnapshotRaw();
		Snapshot prevVel = pastRaw.size() > 0 ? pastRaw.get(0) : prev;

		// Check if the new positions make sense. For instance, discard
		// the ones that are unreasonably far away from the previous one
		ourPosition = positionIsCloseToExpected(prev.getBalle().getPosition(),
				ourPosition) ? ourPosition : null;
		theirsPosition = positionIsCloseToExpected(prev.getOpponent()
				.getPosition(), theirsPosition) ? theirsPosition : null;
		ballPosition = positionIsCloseToExpected(prev.getBall().getPosition(),
				ballPosition) ? ballPosition : null;

		// change in time
		long deltaT = timestamp - prev.getTimestamp(); // Hopefully that does
														// not cause issues
														// with EmptySnapshot
														// and
														// currentTimeMilis()
		long deltaTVel = timestamp - prevVel.getTimestamp();

		// Special case when we get two inputs with the same timestamp:
		if (deltaT == 0) {
			// This will just keep the prev world in the memory, not doing
			// anything
			return;
		}

		if (ourPosition == null)
			ourPosition = estimatedPosition(prev.getBalle(), deltaT);
		if (theirsPosition == null)
			theirsPosition = estimatedPosition(prev.getOpponent(), deltaT);
		if (ballPosition == null)
			ballPosition = estimatedPosition(prev.getBall(), deltaT, true);

		// Calculate how much each position has changed between frames
		Coord oursDPos, themDPos, ballDPos;
		oursDPos = subtractOrNull(ourPosition, prevVel.getBalle().getPosition());
		themDPos = subtractOrNull(theirsPosition, prevVel.getOpponent()
				.getPosition());
		ballDPos = subtractOrNull(ballPosition, prevVel.getBall().getPosition());

		// Recalculate the velocities from deltapositions above.
		Velocity oursVel, themVel, ballVel;
		oursVel = oursDPos != null ? new Velocity(oursDPos, deltaTVel)
				: new Velocity(0, 0, 1, 1);
		themVel = themDPos != null ? new Velocity(themDPos, deltaTVel)
				: new Velocity(0, 0, 1, 1);
		ballVel = ballDPos != null ? new Velocity(ballDPos, deltaTVel)
				: new Velocity(0, 0, 1, 1);

		AngularVelocity oursAngVel = null, themAngVel = null;
		if (ourOrientation == null) {
			ourOrientation = prevVel.getBalle().getOrientation();
		} else if (prevVel.getBalle().getOrientation() != null) {
			oursAngVel = new AngularVelocity(
					ourOrientation.angleToatan2Radians(prevVel.getBalle()
							.getOrientation()), deltaTVel);
		}

		if (theirsOrientation == null) {
			theirsOrientation = prevVel.getOpponent().getOrientation();
		} else if (prevVel.getOpponent().getOrientation() != null) {
			themAngVel = new AngularVelocity(
					theirsOrientation.angleToatan2Radians(prevVel.getOpponent()
							.getOrientation()), deltaTVel);
		}

		themAngVel = (them != null) ? new AngularVelocity(
				theirsOrientation.angleToatan2Radians(prevVel.getOpponent()
						.getOrientation()), deltaTVel) : null;

		// put it all together (almost)
		them = new Robot(theirsPosition, themVel, themAngVel, theirsOrientation);
		ours = new Robot(ourPosition, oursVel, oursAngVel, ourOrientation);
		ball = new Ball(ballPosition, ballVel);

		// pack into a snapshot, and update prev/prevRaw
		Snapshot nextSnapshot = new Snapshot(them, ours, ball,
				getOpponentsGoal(), getOwnGoal(), getPitch(), timestamp);
		pastRaw.add(nextSnapshot);
		while (pastRaw.size() > FRAMES_FOR_VELOCITY) {
			pastRaw.remove(0);
		}
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
			this.prev = new EmptySnapshot(getOpponentsGoal(), getOwnGoal(),
					getPitch());
			this.prevRaw = new EmptySnapshot(getOpponentsGoal(), getOwnGoal(),
					getPitch());
		}
	}

}
