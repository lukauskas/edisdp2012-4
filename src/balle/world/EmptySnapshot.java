package balle.world;

import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;

/**
 * An abstraction for Snapshot that creates it without any robots, etc. -- Just
 * to reduce the number of null checks.
 */
public class EmptySnapshot extends Snapshot {

	public EmptySnapshot(Goal opponentsGoal, Goal ownGoal, Pitch pitch,
			BallEstimator ballEstimator) {
		super(new Robot(null, null, null, null), new Robot(null, null, null,
				null), new Ball(null, null), opponentsGoal, ownGoal, pitch,
				ballEstimator, System.currentTimeMillis(), null);
	}

	public EmptySnapshot(Goal opponentsGoal, Goal ownGoal, Pitch pitch,
			BallEstimator ballEstimator, long timeStamp) {
		super(new Robot(null, null, null, null), new Robot(null, null, null,
				null), new Ball(null, null), opponentsGoal, ownGoal, pitch,
				ballEstimator, timeStamp, null);
	}

}
