package balle.brick;

public interface Controller {

	/**
	 * Makes the robot reverse until it receives a stop() command.
	 */
	public abstract void backward();
	
	/**
	 * Makes the robot go backward with the specified speed
	 * @param speed
	 */
	public abstract void backward(int speed);

	/**
	 * Makes the robot go until it receives a stop() command.
	 */
	public abstract void forward();
	
	/**
	 * Makes robot go forward with the specified speed
	 * @param speed
	 */
	public abstract void forward(int speed);

	/**
	 * Floats the motors, allowing the robot to drift.
	 */
	public abstract void floatWheels();

	/**
	 * Makes the robot stop.
	 */
	public abstract void stop();

	/**
	 * The robot will rotate deg degrees.
	 * @param deg The degree to rotate. +ve is right, -ve left.
	 */
	public abstract void rotate(int deg);
	
	/**
	 * The robot will rotate deg degrees at specified speed.
	 * @param deg The degree to rotate. +ve is right, -ve left.
	 * @param turn speed
	 */
	public abstract void rotate(int deg, int speed);

	/**
	 * Moves the robot dist distance. Only used by the Sensors class
	 * @param dist the distance to move.
	 */
	public abstract void travel(int dist);
  
	/**
	 * Sets the speeds of individual wheels in the robot
	 * 
	 * @param leftWheelSpeed left wheel speed
	 * @param rightWheelSpeed right wheel speed
	 */
	public abstract void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed);
	
	/**
	 * Returns the maximum supported wheel speed that can be used by setWheelSpeeds
	 * @return maximum speed
	 */
	public abstract int getMaximumWheelSpeed();
	
	/**
	 * Kicks the ball.
	 */
	public abstract void kick();

}