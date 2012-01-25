import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class Sensors extends Thread{
	final TouchSensor left = new TouchSensor(SensorPort.S1);
	final TouchSensor right = new TouchSensor(SensorPort.S2);
	Controller control;
	Boolean wait = false;
	public Boolean listening = true;
	
	public Sensors(Controller c){
		control = c;
	}
	void listen(){
			while (!this.isInterrupted()){
				while (!wait){
					wait = left.isPressed() || right.isPressed();
				}
		    	if (left.isPressed()){
		    		if (right.isPressed()){
		    			control.stop();
		    			wait = false;
		    		} else {
			    		LCD.clear();
			    		listening = false;
			    		control.travel(-5);
			    		control.rotate(20);
			    		listening = true;
			    		wait = false;
		    		}
		    	} else if (right.isPressed()){
		    		if (left.isPressed()){
		    			control.stop();
		    			wait = false;
		    		} else {
			    		LCD.clear();
			    		listening = false;
			    		control.travel(-5);
			    		control.rotate(-20);
			    		listening = true;
			    		wait = false;
		    		}
		    	}
			}
	}
	
	public void run(){
		listen();
	}
}
