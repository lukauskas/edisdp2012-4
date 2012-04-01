package balle.world;

import java.util.ArrayList;
import java.util.List;

import balle.simulator.SnapshotPredictor;
import balle.strategy.bezierNav.ControllerHistoryElement;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Robot;


/**
 * Immutable
 */
public class Snapshot {
	
	private final List<ControllerHistoryElement> controllerHistory;

	public List<ControllerHistoryElement> getControllerHistory() {
        // Clone so we do not break the immutability assumption
		return new ArrayList<ControllerHistoryElement>(controllerHistory);
    }

    private final Robot opponent;
	private final Robot bot;
	private final Ball ball;

	private final BasicWorld world;

	private final long timestamp;

	public Snapshot(BasicWorld world, Robot opponent, Robot balle,
			Ball ball, long timestamp,
			List<ControllerHistoryElement> controllerHistory) {

		super();
		this.opponent = opponent;
		this.bot = balle;
		this.ball = ball;
		this.timestamp = timestamp;
		this.world = world;
        this.controllerHistory = controllerHistory;
	}

	public Snapshot(BasicWorld world, Robot opponent, Robot balle, Ball ball,
			long timestamp) {

        // Create a new snapshot predictor based on the information provided
		this(world, opponent, balle, ball, timestamp,
				new ArrayList<ControllerHistoryElement>());
                
    }

	/**
	 * Copy constructor
	 * 
	 * @param s
	 *            the snapshot to copy from
	 */
	public Snapshot(Snapshot s) {
		this(s.world, s.opponent, s.bot, s.ball, s.timestamp,
				s.controllerHistory);
	}

	public Estimator getBallEstimator() {
		return world.getBallEstimator();
	}
	
	public SnapshotPredictor getSnapshotPredictor() {
        // Always create new in order not to break the immutability
		return new SnapshotPredictor(this, getControllerHistory());

    }

	public BasicWorld getWorld() {
		return world;
	}

    public Pitch getPitch() {
		return world.getPitch();
	}

	public Goal getOpponentsGoal() {
		return world.getOpponentsGoal();
	}

	public Goal getOwnGoal() {
		return world.getOwnGoal();
	}

	public Robot getOpponent() {
		return opponent;
	}

	public Robot getBalle() {
		return bot;
	}

	public Ball getBall() {
		return ball;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Snapshot duplicate() {
		return new Snapshot(world, opponent, bot, ball, timestamp,
				controllerHistory);
	}

	public MutableSnapshot unpack() {
		return new MutableSnapshot(world, opponent, bot, ball, timestamp,
				controllerHistory);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Snapshot))
			return false;
		Snapshot otherSnapshot = (Snapshot) other;

		if (this.getTimestamp() == otherSnapshot.getTimestamp())
			return true;
		else
			return false;
	}
}
