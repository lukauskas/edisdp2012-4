/**
 * 
 */
package balle.controller;

/**
 * @author s0909773
 * 
 */
public class DummyController implements Controller {

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#backward(int)
     */
    @Override
    public void backward(int speed) {
        System.out.println("Backward " + speed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#forward(int)
     */
    @Override
    public void forward(int speed) {
        System.out.println("Forward " + speed);

    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#floatWheels()
     */
    @Override
    public void floatWheels() {
        System.out.println("FloatWheels");
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#stop()
     */
    @Override
    public void stop() {
        System.out.println("Stop");

    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#rotate(int, int)
     */
    @Override
    public void rotate(int deg, int speed) {
        System.out.println("Rotate (deg: " + deg + ", speed: " + speed + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#setWheelSpeeds(int, int)
     */
    @Override
    public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
        System.out.println("Set Wheel Speeds: " + leftWheelSpeed + ", "
                + rightWheelSpeed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#getMaximumWheelSpeed()
     */
    @Override
    public int getMaximumWheelSpeed() {
        return 720;
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#kick()
     */
    @Override
    public void kick() {
        System.out.println("Kick");
    }

	@Override
	public void penaltyKick() {
		System.out.println("Penalty Kick!");
		
	}

    @Override
    public boolean isReady() {
        return true;
    }

	@Override
	public void addListener(ControllerListener cl) {
		System.out.println("Adding listener.");
	}

/*	@Override
	public void gentleKick(int speed, int angle) {
		// TODO Auto-generated method stub
		
	}*/

}
