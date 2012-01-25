package balle.brick;

import java.io.DataInputStream;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * Create a connection to Roboto from the computer.
 * execute commands send from the computer
 * test out movements of Roboto.
 * 
 * @author s0815695
 */
public class Roboto {

	public static final int MESSAGE_EXIT = -1;
	public static final int MESSAGE_FORWARD = 1;
	public static final int MESSAGE_BACKWARD = 2;
	public static final int MESSAGE_ROTATE = 3;
	public static final int MESSAGE_STOP = 4;
	public static final int MESSAGE_KICK = 5;

	/**
	 * Main program
	 * @param args
	 */
	public static void main(String[] args) {

		TouchSensor touchRight = new TouchSensor(SensorPort.S1);
		TouchSensor touchLeft = new TouchSensor(SensorPort.S2);

		while (true) {
			// Enter button click will halt the program
			if (Button.ENTER.isPressed())
				break;

			drawMessage("Connecting...");

			BTConnection connection = Bluetooth.waitForConnection();

			drawMessage("Connected");

			DataInputStream input = connection.openDataInputStream();
            
			Controller controller = new BrickController();
		
			mainLoop:
			while (true) {
				// Enter button click will halt the program
				if (Button.ENTER.isPressed()){
					controller.stop();
					break;
				}
				try {
					// no input available
					while(input.available() == 0) {
						// Enter button click will halt the program
						if (Button.ENTER.isPressed()){
							controller.stop();
							break mainLoop;
						}
					}

					int command = input.readInt();

					drawMessage(Integer.toString(command));

					if (touchLeft.isPressed() || touchRight.isPressed()){
						controller.backward();
						Thread.sleep(400);
					} 

					switch (command) {
					case MESSAGE_EXIT:
						controller.stop();
						// stop
						break mainLoop;
					case MESSAGE_FORWARD:
						controller.forward();
					    break;
					case MESSAGE_BACKWARD:
						controller.backward();
						break;
					case MESSAGE_KICK:
						controller.kick();
						break;
					case MESSAGE_STOP:
						controller.stop();
						break;
					case MESSAGE_ROTATE:
						int theta = input.readInt();
						controller.rotate(theta);
						break;
					}
//					case MOVE:
//						try {
//							//read further two values for the parameters of MOVE
//							int leftVel = input.readInt();
//							int rightVel = input.readInt();
//							drawMessage(leftVel + " " + rightVel);
//							if (leftVel < 0) {
//								leftVel = leftVel*-1;
//								Motor.C.forward();
//							}
//							else {
//								Motor.C.backward();
//							}
//							Motor.C.setSpeed(leftVel);
//							if (rightVel < 0) {
//								rightVel = rightVel*-1;
//								Motor.A.forward();
//							}
//							else {
//								Motor.A.backward();
//							}
//							Motor.A.setSpeed(rightVel);
//						break;
//						} catch (Exception e) {
//							drawMessage("Error in MOVE: " + e);
//						}

//					case KICK:
//						try {
//							Motor.B.setSpeed(1020);
//							Motor.B.forward();
//							Thread.sleep(120);
//							Motor.B.stop();
//							Motor.B.backward();
//							Thread.sleep(150);
//							Motor.B.stop();
//						} catch (InterruptedException e) {
//							drawMessage("Error in KICK: " + e);
//						}
//						break;
//					case CELEBRATE:
//						Tune.Tune();
//						break;
//					case PENALTY:
//						Motor.A.setSpeed(500);
//						Motor.C.setSpeed(500);
//						int rotateAmount = 100;
//
//						if (Math.random() < 0.5) {
//							try {  
//								//rotate right
//								Motor.A.backward();
//								Motor.C.forward();
//								Thread.sleep(rotateAmount);
//								Motor.A.stop();
//								Motor.C.stop();
//
//								//kick
//								Motor.B.setSpeed(1020);
//								Motor.B.forward();
//								Thread.sleep(120);
//								Motor.B.stop();
//								Motor.B.backward();
//								Thread.sleep(150);
//								Motor.B.stop();
//
//							} catch (InterruptedException e) {
//								drawMessage("Error in PENALTY: " + e);
//							}
//						} else {
//							try {  
//								//rotate left
//								Motor.A.forward();
//								Motor.C.backward();
//								Thread.sleep(rotateAmount);
//								Motor.A.stop();
//								Motor.C.stop();
//
//								//kick
//								Motor.B.setSpeed(1020);
//								Motor.B.forward();
//								Thread.sleep(120);
//								Motor.B.stop();
//								Motor.B.backward();
//								Thread.sleep(150);
//								Motor.B.stop();
//
//							} catch (InterruptedException e) {
//								drawMessage("Error in PENALTY: " + e);
//							}
//
//						}
//					case STOP:
//						Motor.A.stop();
//						Motor.B.stop();
//						Motor.C.stop();
//						break;
//					default:
//						// No command input
//						break;
//					}

				} catch (Exception e1) {
					drawMessage("Error in MainLoop: " + e1.getMessage());
				}
			}

			connection.close();
		}
	}

	private static void drawMessage(String message) {
		LCD.clear();
		LCD.drawString(message, 0, 0);
		LCD.refresh();
	}

}