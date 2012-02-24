package balle.misc;

public class Globals {

    public static final float BALL_RADIUS   = 0.02135f;
    public static final float ROBOT_WIDTH   = 0.15f;
    public static final float ROBOT_LENGTH  = 0.2f;
    public static final float ROBOT_TRACK_WIDTH = 0.15f; // TODO CHECK THIS
    
    public static final float ROBOT_MAX_KICK_DISTANCE = 1.5f; // Meters TODO CHECK THIS
    
    // For defining near corner, and near wall areas.
    public final static double DISTANCE_TO_WALL = 0.1;
	public final static double DISTANCE_TO_CORNER = 0.2;

    public static final float PITCH_WIDTH   = 2.4384f; // Metres
    public static final float PITCH_HEIGHT  = 1.2192f; // Metres
    public static final float GOAL_POSITION = 0.31f;   // Metres
    
    public static final float METERS_PER_PIXEL = PITCH_WIDTH / 605f;
    
    public static final float VISION_COORD_NOISE_SD = 0.49f * METERS_PER_PIXEL;	// in meters
    public static final float VISION_ANGLE_NOISE_SD = 1.53f;	// in degrees
    public static final float SIMULATED_VISON_FRAMERATE = 15f;

	
	public static float powerToVelocity(float p) {
    	return p * (0.4f/720f);
//		if(p==0) return 0;
//    	boolean isNeg = p < 0;
//    	if(isNeg) p = -p;
//    	float absVelocity =  (float)(2f / Math.exp(-0.0025f * p + 3.1187));
//    	absVelocity /= (1f+Math.exp(-0.1f*(p-50)));
//    	return isNeg?-absVelocity:absVelocity;
    }
	
	public static float velocityToPower(float v) {
    	return v * (720f/0.4f);
//		if(v==0) return 0;
//		return (float) (Math.log(((1f/(v))*2))-3.1187)/-0.0025f;
    }

}
