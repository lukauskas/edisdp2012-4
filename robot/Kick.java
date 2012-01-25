import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

public class Kick {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pilot pilot = new TachoPilot(8, 16, Motor.A, Motor.C,true);
		Controller control = new Controller(pilot);
		
		control.kick();
		try {
			Thread.sleep(1000);
		} catch (Exception e){
			// 
		}
	}

}
