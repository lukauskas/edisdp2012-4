package balle.brick;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class SensorsTest {

	private static void drawMessage(String message) {
		LCD.clear();
		LCD.drawString(message, 0, 0);
		LCD.refresh();
	}
	
	public static void main (String [] args)
	{
		TouchSensor sensor1 = new TouchSensor(SensorPort.S1);
		TouchSensor sensor2 = new TouchSensor(SensorPort.S2);
		TouchSensor sensor3 = new TouchSensor(SensorPort.S3);
		TouchSensor sensor4 = new TouchSensor(SensorPort.S4);
		
		while (true)
		{
			if (sensor1.isPressed()) drawMessage("S1");
			else if (sensor2.isPressed()) drawMessage("S2");
			else if (sensor3.isPressed()) drawMessage("S3");
			else if (sensor4.isPressed()) drawMessage("S4");
			else drawMessage("No sensors");
		}
	}
	
}
