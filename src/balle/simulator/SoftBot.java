package balle.simulator;

import org.jbox2d.dynamics.Body;

import balle.controller.Controller;

public class SoftBot implements Controller {
	
	private boolean kick = false;
	
	private float leftWheelSpeed = 0;
	private float rightWheelSpeed = 0;

	private Body body;

	private float desiredAngle;

	private boolean turningLeft;

	private boolean rotating;
	
	public float getLeftWheelSpeed() {
		return leftWheelSpeed;
	}
	
	public float getRightWheelSpeed() {
		return rightWheelSpeed;
	}
	
	public boolean getKick() {
		boolean out = kick;
		kick = false;
		return out;
	}

	@Override
	public void backward(int speed) {
		rotating = false;
		leftWheelSpeed = -speed;
		rightWheelSpeed = -speed;
	}

	@Override
	public void forward(int speed) {
		rotating = false;
		leftWheelSpeed = speed;
		rightWheelSpeed = speed;
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
		leftWheelSpeed = 0;
		rightWheelSpeed = 0;
	}

	@Override
	public void rotate(int deg, int speed) {
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! remove this later!!!!!!!!!!!!!!1
		speed = 200;
		// turning right
		if ( deg < 0 ) {
			leftWheelSpeed = speed; 
			rightWheelSpeed = - speed;
		// turning left
		} else {
			leftWheelSpeed = - speed;  
			rightWheelSpeed = speed;
			
		}
			
		desiredAngle = (body.getAngle() + ((float)Math.PI*deg/180))%(2*(float)Math.PI);
		turningLeft = deg < 0;
		
		rotating = true;
	}

	@Override
	public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
		rotating = false;
		this.leftWheelSpeed = leftWheelSpeed;
		this.rightWheelSpeed = rightWheelSpeed;
		// System.out.println("setWheelSpeeds(): "+leftWheelSpeed+" "+rightWheelSpeed);
		
	}

	@Override
	public int getMaximumWheelSpeed() {
		return 720;
	}

	@Override
	public void kick() {
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

	public float getDesiredAngle() {
		return desiredAngle;
	}

	public boolean isTurningLeft() {
		return turningLeft;
	}
	
	/**
	 * return the angles to the desired angle. mod 180
	 * @return
	 */
	public float deltaDesiredAngle() {
		float d = (float)(body.getAngle()%(2*Math.PI)) - getDesiredAngle();
		if(d == Math.PI) {
			return d;
		}
		return  d%(float)Math.PI;
	}

	public boolean isRotating() {
		return rotating;
	}

}
