package balle.misc;

import org.jbox2d.common.Vec2;

public class Globals {

    public static final float BALL_RADIUS   = 0.02135f;
    public static final float ROBOT_WIDTH   = 0.15f;
    public static final float ROBOT_LENGTH  = 0.2f;
    public static final float ROBOT_TRACK_WIDTH = 0.15f; // TODO CHECK THIS
	public static final Vec2 ROBOT_LEFT_WHEEL_POS = new Vec2(-ROBOT_TRACK_WIDTH/2, 0);
	public static final Vec2 ROBOT_RIGHT_WHEEL_POS = new Vec2(ROBOT_TRACK_WIDTH/2, 0);
    

    public static final float PITCH_WIDTH   = 2.4384f; // Metres
    public static final float PITCH_HEIGHT  = 1.2192f; // Metres
    public static final float GOAL_POSITION = 0.31f;   // Metres
	public static final float MaxWheelAccel = 10f;	   // m/s^2
	public static final float SlipWheelAccel = MaxWheelAccel * 0.6f;
    

	
	public static float powerToVelocity(float p) {
    	return p * (0.4f/720f);
//    	boolean isNeg = p < 0;
//    	if(isNeg) p = -p;
//    	float absVelocity = (float) (1f/(Math.exp(-0.0025 * p + 3.1187))) / ((1f/(p/50f))+1f);
//    	return isNeg?-absVelocity:absVelocity;
    }
	
	public static float velocityToPower(float v) {
    	return v * (720f/0.4f);
    }

}
