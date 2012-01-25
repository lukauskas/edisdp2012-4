package balle.brick.milestone1;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import balle.brick.BrickController;

public class RollAndKick {

	private static void drawMessage(String message) {
		LCD.clear();
		LCD.drawString(message, 0, 0);
		LCD.refresh();
	}
	
	
	public static void main(String[] args) {
		
		BrickController controller = new BrickController();
		TouchSensor sensorLeft = new TouchSensor(SensorPort.S1);
		TouchSensor sensorRight = new TouchSensor(SensorPort.S2);
		
		
		boolean movingForward = false;
		while (true)
		{
			if (sensorLeft.isPressed() || sensorRight.isPressed())
			{
				drawMessage("Whoops, wall!");
				controller.stop();
				controller.backward();
				try {
					Thread.sleep(200);
					break;
				}
				catch (Exception e)
				{
					drawMessage(";/");
					break;
				}
			}
			
			
			
			if (!movingForward)
			{
				drawMessage("Roll");
				movingForward = true;
				controller.reset();
				controller.forward();
			}			
			else
			{
				float distance = controller.getTravelDistance();
				if (distance > 100)
				{
					drawMessage("Stop!");
					controller.floatWheels();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					drawMessage("Kick!");
					controller.kick();
					break;
				}
				else
				{
				    drawMessage(Float.toString(distance));
				}
			}
		}
	}

}
