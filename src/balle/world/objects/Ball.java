package balle.world.objects;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.Velocity;

public class Ball extends CircularObject implements FieldObject {

	// private final BallPredictor predictor;

	public Ball(Coord position, Velocity velocity) {
		super(position, velocity, Globals.BALL_RADIUS);

		// this.predictor = predictor;
	}

	// public Ball(Coord position, Velocity velocity, BallPredictor predictor) {
	// super(position, velocity, Globals.BALL_RADIUS);
	//
	// this.predictor = predictor;
	// }

	// public Coord estimatePosition(double time) {
	// return predictor.getPosition(time);
	// }

}
