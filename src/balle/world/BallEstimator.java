package balle.world;

import org.apache.log4j.Logger;


public class BallEstimator {

	private static final Logger LOG = Logger.getLogger(BallEstimator.class);

	private DESP velocityPredictor;
	private DESP positionPredictor;
	
	private Velocity velocity;

	public BallEstimator() {
		velocityPredictor = new DESP(0.7);
		positionPredictor = new DESP(0.4);
		
		velocity = new Velocity(0, 0, 1000);
	}
	
	public Coord getPosition() {
		return estimatePosition(0);
	}

	public Coord estimatePosition(int frames) {
		return positionPredictor.predict(frames);
	}

	public Velocity getVelocity() {
		return velocity;
	}

	public void update(Coord pos, double dt) {

		// dt = dt / 1000;
		if (pos == null) {
			pos = getPosition();
		}

		velocityPredictor.update(pos);
		positionPredictor.update(pos);

		LOG.debug(positionPredictor.predict(0));

		velocity = new Velocity(velocityPredictor.predict((int) (dt * 1000))
				.sub(pos),
				1000);
	}
}
