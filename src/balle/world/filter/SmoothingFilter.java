package balle.world.filter;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import balle.misc.Globals;
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

    private static final Logger LOG = Logger.getLogger(SmoothingFilter.class);
	// Instance

	protected final int historyLength;

	protected Queue<Snapshot> queue = new LinkedList<Snapshot>();

	// Constructors

	public SmoothingFilter(int historyLength) {
		this.historyLength = historyLength;
	}

	public SmoothingFilter() {
        this(0);
	}

	// Interface

	@Override
	public Snapshot filter(Snapshot s) {
        if (s.getBall().getPosition() == null)
            return s;

        queue.add(s);
        MutableSnapshot ms = s.unpack();

        // Smooth ball velocities.

        Velocity avgBallVelocity = new Velocity(0, 0, 1);

        for (Snapshot qS : queue) {
            Velocity prevVelocity = qS.getBall().getVelocity();
			if (prevVelocity.abs() < Globals.VELOCITY_NOISE_THRESHOLD) {
				continue; // Just treat it as a (0,0,1) velocity.
			}
            avgBallVelocity = avgBallVelocity.add(prevVelocity);
        }
        avgBallVelocity = avgBallVelocity.div(queue.size());

        ms.setBall(new Ball(s.getBall().getPosition(), avgBallVelocity));

        // Crop history.
        if (queue.size() >= historyLength)
            queue.poll();

        // Pack-up and return.
        return ms.pack();
	}

}
