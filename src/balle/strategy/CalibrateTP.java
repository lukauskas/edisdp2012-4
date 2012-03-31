package balle.strategy;

import java.io.IOException;
import java.util.ArrayList;

import balle.controller.Controller;
import balle.memory.VelocityFile;
import balle.misc.Globals;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;
import balle.world.Velocity;
import balle.world.objects.Robot;

public class CalibrateTP extends AbstractPlanner {

	protected ArrayList<Velocity> record = new ArrayList<Velocity>();

	private int power = 900;

	@FactoryMethod(designator = "CalibrateTP", parameterNames = {})
	public static CalibrateTP calibrateTPFactory() {
		return new CalibrateTP();
    }
	
    /**
     * Tell the strategy to do a step (e.g. move forward).
     * @param snapshot TODO
     */
	public void onStep(Controller controller, Snapshot snapshot) {

		controller.setWheelSpeeds(power, power);
		record(snapshot.getBalle());
	}
	
	@Override
	public void stop(Controller controller) {
		super.stop(controller);

		try {
			VelocityFile pcf = new VelocityFile(Globals.resFolder,
					"velocity.txt");
			pcf.writeArray(record);
			record = new ArrayList<Velocity>();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * uses the current samples and power to record a (power, velocity) pair 
	 */
	private void record(Robot r) {
		// calculate the wheel velocity
		Velocity vel = r.getVelocity();
		double speed = vel.abs();
		
		// print to file and console
		System.out.println(System.currentTimeMillis() + " " + speed);
		record.add(vel);
	}

}
