package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.SimpleWorldGUI;
import balle.world.AbstractWorld;
import balle.world.Snapshot;
import balle.world.processing.AbstractWorldProcessor;

public class StrategyRunner extends AbstractWorldProcessor {

	private final static Logger LOG = Logger.getLogger(StrategyRunner.class);

	private Controller controllerA;
	private Controller controllerB;
	private Strategy currentStrategyA;
	private Strategy currentStrategyB;
	private final SimpleWorldGUI gui;

	/**
	 * Initialises strategy runner
	 * 
	 * @param controllerA
	 *            controller that will be used to move green robot
	 * @param controllerB
	 *            controller that will be used to move red robot
	 * @param worldA
	 *            world that will be used for green robot
	 * @param worldB
	 *            world that will be used for red robot
	 * @param gui
	 *            GUI that Drawables will be drawn on.
	 */
	public StrategyRunner(Controller controllerA, Controller controllerB,
			AbstractWorld worldA, AbstractWorld worldB, SimpleWorldGUI gui) {
		super(worldA);
		this.controllerA = controllerA;
		this.controllerB = controllerB;
		this.currentStrategyA = null;
		this.currentStrategyB = null;
		this.gui = gui;
	}

	@Override
	protected void actionOnStep() {

	}

	@Override
	protected void actionOnChange() {
		if (currentStrategyA != null && currentStrategyB != null) {
			Snapshot snapshot = getSnapshot();
			// Snapshot centered on opponent robot (Balle from snapshot
			// becomes opponent in snapshot2 etc
			Snapshot snapshot2 = new Snapshot(snapshot.getBalle(),
					snapshot.getOpponent(), snapshot.getBall(),
					snapshot.getOwnGoal(), snapshot.getOpponentsGoal(),
					snapshot.getPitch(), snapshot.getTimestamp());
			try {
				currentStrategyA.step(controllerA, snapshot);
				if (controllerB != null) {
					currentStrategyB.step(controllerB, snapshot2);
				}
			} catch (Exception e) {
				LOG.error("Strategy raised exception" + e.toString());

				for (StackTraceElement se : e.getStackTrace())
					LOG.debug(se.toString());

				controllerA.stop();
				if (controllerB != null) {
					controllerB.stop();
				}
			}
			gui.setDrawables(currentStrategyA.getDrawables());
			gui.setDrawables(currentStrategyB.getDrawables());
		}

	}

	/**
	 * Stops the current running strategy
	 */
	public void stopStrategy() {
		if (currentStrategyA != null && currentStrategyB != null) {
			LOG.info("Stopping " + currentStrategyA.getClass().getName());
			currentStrategyA.stop(controllerA);
			currentStrategyA = null;
			if (controllerB != null) {
				currentStrategyB.stop(controllerB);
			}
			currentStrategyB = null;
		}
	}

	/**
	 * Starts the strategy
	 * 
	 * @param strategy
	 *            the strategy
	 */

	public void startStrategy(Strategy strategyA, Strategy strategyB) {
		// Stop the old strategy
		if (currentStrategyA != null && currentStrategyB != null)
			stopStrategy();
		// Start the new strategy
		currentStrategyA = strategyA;
		currentStrategyB = strategyB;
		LOG.info("Started " + currentStrategyA.getClass().getName());
	}

	/**
	 * Used by the simulator to switch robots
	 * 
	 * @param controller
	 */
	public void setController(Controller controllerA, Controller controllerB) {
		this.controllerA.stop();
		this.controllerA = controllerA;
		this.controllerB.stop();
		this.controllerB = controllerB;
	}

	@Override
	public void cancel() {
		super.cancel();
		stopStrategy();
		LOG.info("StrategyRunner cancelled");
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public void start() {
		super.start();
		LOG.info("StrategyRunner initialised");
	}
}
