package balle.misc;

import balle.memory.utility.Saves;


public class Powers implements Saves {
	private int power;
	private float velocity;

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

	public String save() {
		return power + ";" + velocity;
	}

	public static Powers load(String line) {
		String[] tokens = line.split(";");
		int power = Integer.parseInt(tokens[0]);
		float velocity = Float.parseFloat(tokens[1]);
		return new Powers(power, velocity);
	}
}
