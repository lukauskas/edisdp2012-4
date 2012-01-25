package balle.brick;
import lejos.nxt.Motor;
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
public class BrickController implements Controller{
	TachoPilot pilot;
	public int maxSpeed=30; // 20 for friendlies
	
	public final Motor LEFT_WHEEL  = Motor.B;
	public final Motor RIGHT_WHEEL = Motor.C;
	public final Motor KICKER      = Motor.A;
	
	public final float LEFT_WHEEL_DIAMETER = 3.0f; // TODO: Measure these properly
	public final float RIGHT_WHEEL_DIAMETER = 3.0f;
	/**
	 * Constructor method: takes the Pilot object from the main program.
	 * @param pilot
	 */
	public BrickController () {
		TachoPilot pilot = new TachoPilot(8, 16, LEFT_WHEEL, RIGHT_WHEEL);
		pilot.setMoveSpeed(maxSpeed);
		pilot.setTurnSpeed(35); // 45 has been working fine.
		pilot.regulateSpeed(true);
		LEFT_WHEEL.smoothAcceleration(true);
		RIGHT_WHEEL.smoothAcceleration(true);
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#reverse()
	 */
	@Override
	public void reverse(){
		pilot.backward();
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#go()
	 */
	@Override
	public void go(){
		pilot.forward();
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#floatWheels()
	 */
	@Override
	public void floatWheels(){
		LEFT_WHEEL.flt();
		RIGHT_WHEEL.flt();
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#stop()
	 */
	@Override
	public void stop(){
		pilot.stop();
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#rotate(int)
	 */
	@Override
	public void rotate(int deg){
		pilot.rotate(-deg);
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#travel(int)
	 */
	@Override
	public void travel(int dist){
		pilot.travel(dist);
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#setSpeed(int)
	 */
	@Override
	public void setSpeed(int speed){
		pilot.setMoveSpeed(speed);
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#kick()
	 */
	@Override
	public void kick(){
		KICKER.setSpeed(900);
		KICKER.resetTachoCount();
		KICKER.rotateTo(90);
		KICKER.rotateTo(0);
	}
}
