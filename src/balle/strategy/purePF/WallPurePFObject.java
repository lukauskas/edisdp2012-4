package balle.strategy.purePF;

import balle.world.Coord;
import balle.world.Orientation;

public class WallPurePFObject extends PFObject {

	private double strength, expo;

	public double getStrength() {
		return strength;
	}

	public WallPurePFObject(Coord pos, Orientation ori, double strength,
			double expo) {
		super(pos, ori);
		this.strength = strength;
		this.expo = expo;
	}

	/**
	 * automatically sets the orientation to face the goal
	 */
	public void update(Coord pos) {
		setPosition(pos);
	}

	@Override
	protected Coord relativePosToForce(Coord position) {
		Coord pos = new Coord(position.getX(), 0);
		return pos.mult(strength).mult(1 / Math.pow(pos.abs(), expo));
	}

}
