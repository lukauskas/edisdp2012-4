package balle.strategy;

import java.io.IOException;
import java.util.ArrayList;

import balle.controller.Controller;
import balle.memory.PowersConfigFile;
import balle.misc.Globals;
import balle.misc.Powers;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;

public class Calibrate extends AbstractPlanner {

	protected ArrayList<Powers> record = new ArrayList<Powers>();

	private final long ACCEL_TIME = 1000; // time needed to accelerate to the
											// terminal speed
	private final int POWER_STEP = 1; // 25;
	private final long SAMPLE_TIME = 4000; // how many angular velocity samples
											// to
										// take (Must be greater then 1)

	private final boolean PIVOT = false; // true if to just pivot on one wheel

	private long lastPowerChangeTime = System.currentTimeMillis();
	private long sampleStartTime;
	private long sampleEndTime;
	private int power = 900;
	private ArrayList<Orientation> samples = new ArrayList<Orientation>();
	
	private boolean done = false;

	@FactoryMethod(designator = "Calibrate", parameterNames = {})
	public static Calibrate calibrateFactory() {
		return new Calibrate();
    }

	private Coord startPos;
	private long startT;
	
    /**
     * Tell the strategy to do a step (e.g. move forward).
     * @param snapshot TODO
     */
	public void onStep(Controller controller, Snapshot snapshot) {

		// if (true) {
		// controller.setWheelSpeeds(900, 900);
		// return;
		// }

		// if (true) {
		// Coord pos = snapshot.getBalle().getPosition();
		// if (startPos == null) {
		// startPos = pos;
		// startT = snapshot.getTimestamp();
		// }
		// System.out
		// .println(((startPos.dist(pos)) / (snapshot.getTimestamp() -
		// startT)));
		// controller.setWheelSpeeds(900, 900);
		// return;
		// }
		if(!done) {
			controller.setWheelSpeeds(power, (PIVOT ? 0 : -power));
			// if accelerating, just let it accelerate
			if (System.currentTimeMillis() - lastPowerChangeTime < ACCEL_TIME)
				return;
			// else collect the sample if possible
			boolean isLastSample = false;
			if (snapshot.getBalle() != null
					&& snapshot.getBalle().getOrientation() != null) {
				if (samples.size() == 0) {
					sampleStartTime = snapshot.getTimestamp();
				}
				samples.add(snapshot.getBalle().getOrientation());
				if (snapshot.getTimestamp() - sampleStartTime > SAMPLE_TIME) {
					sampleEndTime = snapshot.getTimestamp();
					isLastSample = true;
				}
			}

			// if this is the last sample for the current power
			if (isLastSample) {
				// record the results
				record();
				// move to the next power (or stop if done)
				samples.clear();
				power -= POWER_STEP;
				if (power > 0) {
					controller.setWheelSpeeds(power, -power);
					lastPowerChangeTime = System.currentTimeMillis();
				} else {
					done = true;
				}

			}
		} else {
			controller.stop();
		}
	}
	
	@Override
	public void stop(Controller controller) {
		super.stop(controller);

		try {
			PowersConfigFile pcf = new PowersConfigFile(Globals.resFolder,
					"powersConfig.txt");
			pcf.writeArray(record);
			record = new ArrayList<Powers>();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * uses the current samples and power to record a (power, velocity) pair 
	 */
	private void record() {
		// calculate the wheel velocity
		double deltaA = 0;
		for (int i = 1; i < samples.size(); i++) {
			deltaA += samples.get(i - 1).angleToatan2Radians(samples.get(i));
		}
		double angVel = Math.abs(deltaA / (sampleEndTime - sampleStartTime));
		double wheelVelocity = angularVelToWheelSpeed(angVel);
		
		// print to file and console
		Powers powers = new Powers(power, (float) wheelVelocity);
		record.add(powers);
		System.out.println(powers.save());
	}

	/**
	 * assuming the robot is spinning (has no linear velocity) calculate the
	 * wheel speed (absolute velocity) based on angular velocity
	 * 
	 * @return
	 */
	private double angularVelToWheelSpeed(double angVel) {
		return (angVel * Globals.ROBOT_TRACK_WIDTH) / (PIVOT ? 1 : 2);
	}

}
