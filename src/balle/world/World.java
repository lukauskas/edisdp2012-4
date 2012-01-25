package balle.world;

/***
 *
 * This class will take raw data from the vision system and model the world accordingly.
 * This includes e.g. velocities of the robots and ball and angle of the robots in radians.
 *
 * All values returned by this interface should be the best guess of the system.
 * All tweaking of the raw values recieved from the vision system is done here.
 *
 */
public interface World {

	/***
	 * Gets the best guess of the coordinates of the robot (our team's robot).
	 * @return coordinates of the robot.
	 */
	Snapshot getSnapshot();
	
	
}
