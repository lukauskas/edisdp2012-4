package balle.brick.milestone1;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import balle.brick.BrickController;

public class RollThroughField {

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
        int speed = 400;
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
                controller.rotate(180, 200);
                break;
            }
            if (!movingForward) {
                controller.forward(speed);
                drawMessage("Roll");
                movingForward = true;
            } else {
                drawMessage("Keep rollin, rollin..");
            }
        }
    }
}
