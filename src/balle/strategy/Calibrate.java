package balle.strategy;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.world.AngularVelocity;
import balle.world.Snapshot;

public class Calibrate implements Strategy {

	private final long ACCEL_TIME = 500; // time needed to accelerate to the
											// terminal speed
	private final int POWER_STEP = 10;
	private final int SAMPLES = 10; // how many angular velocity samples to take
	private long lastPowerChangeTime = Long.MAX_VALUE;
	private int power = 0;
	private AngularVelocity[] samples = new AngularVelocity[SAMPLES];
	private int sampleIndex = 0;
	
	private boolean done = false;

    /**
     * Tell the strategy to do a step (e.g. move forward).
     * @param snapshot TODO
     */
	public void step(Controller controller, Snapshot snapshot) {
		if(!done) {
			// if accelerating, just let it accelerate
			if (System.currentTimeMillis() - lastPowerChangeTime < ACCEL_TIME)
				return;
			// else collect the sample
			samples[sampleIndex] = snapshot.getBalle().getAngularVelocity();
			sampleIndex++;
			
			// if this is the last sample for the current power
			if(sampleIndex == SAMPLES) {
				// record the results
				record();
				// move to the next power (or stop if done)
				sampleIndex = 0;
				power += POWER_STEP;
				if(power <= Globals.MAXIMUM_MOTOR_SPEED) {
					controller.setWheelSpeeds(power, -power);
					lastPowerChangeTime = System.currentTimeMillis();
				} else {
					done = true;
				}
			}
		}
	}
	
	/**
	 * uses the current samples and power to record a (power, velocity) pair 
	 */
	private void record() {
		// calculate the wheel velocity
		double avgAngAccel = 0;
		for(int i = 0; i < sampleIndex; i++) {
			avgAngAccel += samples[i].radians();
		}
		avgAngAccel /= sampleIndex;
		double wheelVelocity = angularVelToWheelSpeed(avgAngAccel);
		
		// record (just print out for now)
		System.out.println(power+"\t"+wheelVelocity);
	}

	/**
	 * assuming the robot is spinning (has no linear velocity) calculate the
	 * wheel speed (absolute velocity) based on angular velocity
	 * 
	 * @return
	 */
	private double angularVelToWheelSpeed(double angVel) {
		return angVel * Globals.ROBOT_TRACK_WIDTH / 2;
	}

    /**
     * Tell the strategy to stop doing whatever it was doing.
     * 
     * @param controller
     */
	public void stop(Controller controller) {
		/* DO NOTHING */
	}

    /**
     * Retrieves all Drawable objects from the strategy
     * 
     */
	public ArrayList<Drawable> getDrawables() {
		return new ArrayList<Drawable>();
	}

}
