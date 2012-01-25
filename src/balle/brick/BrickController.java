package balle.brick;
import lejos.nxt.Motor;
import lejos.robotics.navigation.TachoPilot;

/** 
 * The Control class. Handles the actual driving and movement of the bot, once
 * BotCommunication has processed the commands.
 * 
 * That is -- defines the behaviour of the bot when it receives the command.
 * 
 * Adapted from SDP2011 groups 10 code -- original author shearn89
 * @author sauliusl
 */
public class BrickController implements Controller{
	TachoPilot pilot;
	public int maxSpeed=30; // 20 for friendlies
	
	public final Motor LEFT_WHEEL  = Motor.B;
	public final Motor RIGHT_WHEEL = Motor.C;
	public final Motor KICKER      = Motor.A;
	
	public final float WHEEL_DIAMETER = 8.0f; // TODO: Measure these properly
	public final float TRACK_WIDTH =  10.3f;

	public BrickController () {
		
		pilot = new TachoPilot(WHEEL_DIAMETER, TRACK_WIDTH, LEFT_WHEEL, RIGHT_WHEEL);
		pilot.setMoveSpeed(maxSpeed);
		pilot.setTurnSpeed(45); // 45 has been working fine.
		pilot.regulateSpeed(true);
		LEFT_WHEEL.smoothAcceleration(true);
		RIGHT_WHEEL.smoothAcceleration(true);
	}
	
	
	/* (non-Javadoc)
	 * @see balle.brick.Controller#backward()
	 */
	@Override
	public void backward(){
		pilot.backward();
	}
	/* (non-Javadoc)
	 * @see balle.brick.Controller#forward()
	 */
	@Override
	public void forward(){
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
