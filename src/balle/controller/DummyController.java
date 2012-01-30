/**
 * 
 */
package balle.controller;

import balle.brick.Controller;

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
     * @see balle.brick.Controller#travel(int)
     */
    @Override
    public void travel(int dist) {
        System.out.println("Travel (dist: " + dist + ")");

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

}