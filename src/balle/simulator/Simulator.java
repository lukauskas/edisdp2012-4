package balle.simulator;

import balle.io.reader.DataReader;

/** A tool for testing the robot strategy without the need for a robot.
 * 
 * @author James Vaughan
 */
public class Simulator implements DataReader {
	
	// -------------------\\
	// Utility and Tools \\
	// -------------------\\

	private SoftBot yellow, blue;
	
	public SoftBot getYellow() {
		return yellow;
	}
	
	public SoftBot getBlue() {
		return blue;
	}
	
	/** 
	 * Updates world object.
	 */
	@Override
	public String nextLine() {
		return "#";
	}
	
}
