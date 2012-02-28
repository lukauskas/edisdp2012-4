package balle.strategy.purePF;

import balle.world.Coord;

public class PullPointPurePFObject extends PFObject {

	private double strength, expo;

	public double getStrength() {
		return strength;
	}

	public PullPointPurePFObject(Coord pos, double strength, double expo) {
		super(pos);
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
	protected Coord relativePosToForce(Coord pos) {
		// currently this just goes directly to the ball
		return pos.opposite().mult(strength).mult(1 / Math.pow(pos.abs(), expo));
	}

}
