package balle.brick;

public interface Controller {

	/**
	 * Makes the robot reverse until it receives a stop() command.
	 */
	public abstract void reverse();

	/**
	 * Makes the robot go until it receives a stop() command.
	 */
	public abstract void go();

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
	 * Moves the robot dist distance. Only used by the Sensors class
	 * @param dist the distance to move.
	 */
	public abstract void travel(int dist);

	/**
	 * Sets the motor speed.
	 * @param speed
	 */
	public abstract void setSpeed(int speed);

	/**
	 * Kicks the ball.
	 */
	public abstract void kick();

}