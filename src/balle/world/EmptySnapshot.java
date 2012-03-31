package balle.world;

import balle.world.objects.Ball;
import balle.world.objects.Robot;

/**
 * An abstraction for Snapshot that creates it without any robots, etc. -- Just
 * to reduce the number of null checks.
 */
public class EmptySnapshot extends Snapshot {

	public EmptySnapshot(BasicWorld world) {
		super(world, new Robot(null, null, null, null), new Robot(null, null,
				null, null), new Ball(null, null), System.currentTimeMillis(),
				null);
	}

	public EmptySnapshot(BasicWorld world, long timeStamp) {
		super(world, new Robot(null, null, null, null), new Robot(null, null,
				null, null), new Ball(null, null), timeStamp, null);
	}

}
