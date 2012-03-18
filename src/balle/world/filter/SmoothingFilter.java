package balle.world.filter;

import java.util.LinkedList;
import java.util.Queue;

import balle.world.MutableSnapshot;
import balle.world.Snapshot;
import balle.world.Velocity;
import balle.world.objects.Ball;

/**
 * Performs world smoothing.
 * 
 * @author s0952880
 */
public class SmoothingFilter implements Filter {

	// Instance

	protected final int historyLength;

	protected Queue<Snapshot> queue = new LinkedList<Snapshot>();

	// Constructors

	public SmoothingFilter(int historyLength) {
		this.historyLength = historyLength;
	}

	public SmoothingFilter() {
		this(20);
	}

	// Interface

	@Override
	public Snapshot filter(Snapshot s) {
		queue.add(s);
		MutableSnapshot ms = s.unpack();
		
		// Smooth ball velocities.
		
		Velocity avgBallVelocity = new Velocity(0, 0, 0);

		for (Snapshot each : queue)
			avgBallVelocity.add(each.getBall().getVelocity());

		avgBallVelocity.div(queue.size());

		ms.setBall(new Ball(s.getBall().getPosition(), avgBallVelocity));

		// Crop history.
		if (queue.size() >= historyLength)
			queue.poll();

		// Pack-up and return.
		return ms.pack();
	}

}
