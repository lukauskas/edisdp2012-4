package balle.simulator;

import balle.controller.Controller;

public class SoftBot implements Controller {
	
	private boolean kick = false;
	
	private float leftWheelSpeed = 0;
	private float rightWheelSpeed = 0;
	
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
		leftWheelSpeed = -speed;
		rightWheelSpeed = -speed;
	}

	@Override
	public void forward(int speed) {
		leftWheelSpeed = speed;
		rightWheelSpeed = speed;
	}

	@Override
	public void floatWheels() {
		// TODO Changes behaviour of the robot.
		// Not exactly sure how will behave?
	}

	@Override
	public void stop() {
		leftWheelSpeed = 0;
		rightWheelSpeed = 0;
	}

	@Override
	public void rotate(int deg, int speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void travel(int dist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
		this.leftWheelSpeed = leftWheelSpeed;
		this.rightWheelSpeed = rightWheelSpeed;
		// System.out.println("setWheelSpeeds(): "+leftWheelSpeed+" "+rightWheelSpeed);
		
	}

	@Override
	public int getMaximumWheelSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void kick() {
		kick = true;
	}

    @Override
    public void penaltyKick() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return true;
    }

}
