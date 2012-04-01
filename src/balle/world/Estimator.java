package balle.world;

import org.apache.log4j.Logger;


public class Estimator {

	private static final Logger LOG = Logger.getLogger(Estimator.class);

	private final DESP velocityPredictor;
	private final DESP positionPredictor;
	
	private Velocity velocity;

	private int estimatedFrames = 0;

	public Estimator(double posAlpha, double velAlpha) {
		velocityPredictor = new DESP(velAlpha);
		positionPredictor = new DESP(posAlpha);

		velocity = new Velocity(0, 0, 1000);
	}

	public static Estimator getBallEstimator() {
		return new Estimator(0.7, 0.4);
	}

	public static Estimator getRobotEstimator() {
		return new Estimator(0.4, 0.5);
	}
	
	/**
	 * Get the estimated current position
	 * 
	 * @return The ball's current position
	 */
	public Coord getPosition() {
		return estimatePosition(0);
	}

	/**
	 * Get the ball's estimated future position
	 * 
	 * @param frames
	 *            How many frames to predict
	 * @return The estimated position
	 */
	public Coord estimatePosition(int frames) {
		return new Coord(positionPredictor.predict(frames), estimatedFrames
				+ frames);
	}

	/**
	 * Get the ball's estimated current velocity
	 * 
	 * @return The current velocity of the ball
	 */
	public Velocity getVelocity() {
		return velocity;
	}

	public void update(Coord pos, double dt) {

		boolean estimated = pos == null;
		if (estimated) {
			pos = getPosition();
			estimatedFrames++;
		} else {
			estimatedFrames = 0;
		}

		velocityPredictor.update(pos);
		positionPredictor.update(pos);

		velocity = new Velocity(velocityPredictor.predict((int) (1000 / dt))
				.sub(pos), 1000);
	}

	public void reset() {
		velocityPredictor.reset();
		positionPredictor.reset();
	}
}
