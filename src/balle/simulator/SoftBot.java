package balle.simulator;

import java.util.ArrayList;

import org.jbox2d.dynamics.Body;

import balle.controller.Controller;
import balle.controller.ControllerListener;
import balle.misc.Globals;
import balle.strategy.bezierNav.ControllerHistoryElement;

public class SoftBot implements Controller {

	private boolean kick = false;

	private float leftWheelSpeed = 0;
	private float rightWheelSpeed = 0;

	private Body body;

	private float desiredAngle;

	private boolean turningLeft;

	private boolean rotating;

	protected ArrayList<ControllerListener> listeners = new ArrayList<ControllerListener>();

	public float getLeftWheelSpeed() {
		return leftWheelSpeed;
	}

	public float getRightWheelSpeed() {
		return rightWheelSpeed;
	}

	@Override
	public void backward(int speed) {
		rotating = false;
		setWheelSpeeds(-speed, -speed);
		propogate(-speed, -speed);
	}

	@Override
	public void forward(int speed) {
		rotating = false;
		setWheelSpeeds(speed, speed);
		propogate(speed, speed);
	}

	@Override
	public void floatWheels() {
		rotating = false;
		// TODO Changes behaviour of the robot.
		// Not exactly sure how will behave?
	}

	@Override
	public void stop() {
		rotating = false;
		setWheelSpeeds(0,0);
		propogate(0, 0);
	}

	/**
	 * @param deg
	 *            signed angle relative to the robots current angle, that the
	 *            robot will rotate to (in place).
	 * @param speed
	 *            POSITIVE angular velocity in degrees/seconds. If a negative
	 *            value is given, the robot will rotate in the opposite
	 *            direction but end up in the same position as if speed was
	 *            positive.
	 */
	@Override
	public void rotate(int deg, int speed) {
        if (body == null)
            return;
		float wheelVelocity = Globals.velocityToPower((speed
				* Globals.ROBOT_TRACK_WIDTH * (float) Math.PI) / 360f);
		// turning right
		if (deg < 0) {
			setWheelSpeeds((int)wheelVelocity, (int)-wheelVelocity);
			// Wheel speeds not propagated as real bluetooth controller wont
			// either.
		}
		// turning left
		else {
			setWheelSpeeds((int)-wheelVelocity, (int)wheelVelocity);
			// Wheel speeds not propagated as real bluetooth controller wont
			// either.
		}

		desiredAngle = (body.getAngle() + ((float) Math.PI * deg / 180))
				% (2 * (float) Math.PI);
		turningLeft = deg < 0;

		rotating = true;
	}

	@Override
	public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
		rotating = false;
		int max = getMaximumWheelSpeed();
		if(leftWheelSpeed > max) leftWheelSpeed = max;
		else if(leftWheelSpeed < -max) leftWheelSpeed = -max;
		if(rightWheelSpeed > max) rightWheelSpeed = max;
		else if(rightWheelSpeed < -max) rightWheelSpeed = -max;
		
		this.leftWheelSpeed = leftWheelSpeed;
		this.rightWheelSpeed = rightWheelSpeed;

		propogate(leftWheelSpeed, rightWheelSpeed);
		// System.out.println("setWheelSpeeds(): "+leftWheelSpeed+" "+rightWheelSpeed);

	}

	@Override
	public int getMaximumWheelSpeed() {
		return Globals.MAXIMUM_MOTOR_SPEED;
	}

	@Override
    public synchronized void kick() {
		kick = true;
	}

	@Override
	public void penaltyKick() {
		rotating = false;
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public Body getBody() {
		return body;
	}

	public float getDesiredAngle() {
		return desiredAngle;
	}

	public boolean isTurningLeft() {
		return turningLeft;
	}

	/**
	 * return the angles to the desired angle. between -PI and PI
	 * 
	 * @return
	 */
    public float deltaDesiredAngle() {
        if (body == null) {
            return 0;
        }
		float d = (float) ((body.getAngle()% (2 * (float)Math.PI)) - getDesiredAngle());
		// -2PI <= d <= 2PI
		// if |d| > PI then change direction of the angle
		if(Math.abs(d) > Math.PI) {
			if(d < 0) d += 2*Math.PI;
			else      d -= 2*Math.PI;
		}
		if (d == Math.PI) {
			return d;
		}
		return d;
	}

	public boolean isRotating() {
		return rotating;
	}

    public synchronized boolean isKicking() {
		return kick;
	}
	
    public synchronized void stopKicking() {
		kick = false;
	}

	@Override
	public void addListener(ControllerListener cl) {
		listeners.add(cl);
	}

	protected void propogate(int left, int right) {
		ControllerHistoryElement che = new ControllerHistoryElement(left,
				right, System.currentTimeMillis());
		for (ControllerListener cl : listeners)
			cl.commandSent(che);
	}

}
