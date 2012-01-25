import lejos.nxt.Motor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

/** 
 * The Control class. Handles the actual driving and movement of the bot, once
 * BotCommunication has processed the commands.
 * 
 * That is -- defines the behaviour of the bot when it receives the command.
 * 
 * Adapted from SDP2011 group 10 code -- original author shearn89
 * @author sauliusl
 */
public class Controller{
	TachoPilot pilot;
	public int maxSpeed=30; // 20 for friendlies
	public boolean ballCarrier = false;
	
	public final Motor LEFT_WHEEL  = Motor.B;
	public final Motor RIGHT_WHEEL = Motor.C;
	public final Motor KICKER      = Motor.A;
	
	public final float LEFT_WHEEL_DIAMETER = 3.0f; // TODO: Measure these properly
	public final float RIGHT_WHEEL_DIAMETER = 3.0f;
	/**
	 * Constructor method: takes the Pilot object from the main program.
	 * @param pilot
	 */
	public Controller () {
		TachoPilot pilot = new TachoPilot(8, 16, LEFT_WHEEL, RIGHT_WHEEL);
		pilot = (TachoPilot) p;
		pilot.setMoveSpeed(maxSpeed);
		pilot.setTurnSpeed(35); // 45 has been working fine.
		pilot.regulateSpeed(true);
		LEFT_WHEEL.smoothAcceleration(true);
		RIGHT_WHEEL.smoothAcceleration(true);
	}
	/**
	 * Makes the robot reverse until it receives a stop() command.
	 */
	public void reverse(){
		pilot.backward();
	}
	/**
	 * Makes the robot go until it receives a stop() command.
	 */
	public void go(){
		pilot.forward();
	}
	/**
	 * Floats the motors, allowing the robot to drift.
	 */
	public void floatWheels(){
		LEFT_WHEEL.flt();
		RIGHT_WHEEL.flt();
	}
	/**
	 * Makes the robot stop.
	 */
	public void stop(){
		pilot.stop();
	}
	/**
	 * The robot will rotate deg degrees.
	 * @param deg The degree to rotate. +ve is right, -ve left.
	 */
	public void rotate(int deg){
		pilot.rotate(-deg);
	}
	/**
	 * Moves the robot dist distance. Only used by the Sensors class
	 * @param dist the distance to move.
	 */
	public void travel(int dist){
		pilot.travel(dist);
	}
	/**
	 * Sets the motor speed.
	 * @param speed
	 */
	public void setSpeed(int speed){
		pilot.setMoveSpeed(speed);
	}
	/**
	 * Kicks the ball.
	 */
	public void kick(){
		KICKER.setSpeed(900);
		KICKER.resetTachoCount();
		KICKER.rotateTo(90);
		KICKER.rotateTo(0);
	}
}
