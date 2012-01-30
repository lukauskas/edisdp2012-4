package balle.brick.milestone1;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import balle.brick.BrickController;

public class RollThroughField {
	
	private static final int ROLL_SPEED = 400;
	private static final int ROLL_DISTANCE = 1800;

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
        int speed = ROLL_SPEED;
        int rollDistance = ROLL_DISTANCE;
        boolean hasStoppedOnce = false;
        while (true) {
            if (sensorLeft.isPressed() || sensorRight.isPressed()) {
                drawMessage("Whoops, wall!");
                controller.stop();
                controller.setWheelSpeeds(-controller.getMaximumWheelSpeed(),
                        -controller.getMaximumWheelSpeed());
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    drawMessage(";/");
                    break;
                }
                controller.rotate(180, 120);
                break;
            }
            if (!movingForward) {
                drawMessage("Roll");
                movingForward = true;
                controller.reset();
                controller.forward(speed);
                
            } else {
            	float distance = controller.getTravelDistance();
                if (distance > rollDistance) {
                	controller.stop();
                	if (!hasStoppedOnce) {
                		controller.rotate(180, 120);
                		controller.reset();
                        controller.forward(speed);
                        hasStoppedOnce = true;
                	}
                	else break;
                } else {
                    drawMessage(Float.toString(distance));
                }
            }
        }
    }
}
