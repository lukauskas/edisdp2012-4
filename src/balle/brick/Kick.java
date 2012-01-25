package balle.brick;

import lejos.nxt.Motor;

public class Kick {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Controller control = new Controller();
		
		control.kick();
		try {
			Thread.sleep(1000);
		} catch (Exception e){
			// 
		}
	}

}
