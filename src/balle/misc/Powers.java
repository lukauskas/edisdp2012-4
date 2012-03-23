package balle.misc;

public class Powers {
	private final int power;
	private final float velocity;

	public int getPower() {
		return power;
	}

	public float getVelocity() {
		return velocity;
	}

	public Powers(int power, float velocity) {
		this.power = power;
		this.velocity = velocity;
	}

	/**
	 * Used in PwrVeloFile.java if you change it, update it their too. Will
	 * damage all saved files.
	 */
	public String toString() {
		return power + ";" + velocity;
	}
}
