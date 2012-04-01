package balle.simulator;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import balle.io.listener.Listener;
import balle.io.reader.AbstractVisionReader;
import balle.misc.Globals;

public class Simulator extends TestbedTest implements AbstractVisionReader {

	private long lastStepTime;
	private long lastFrameTime;

    private WorldSimulator worldWrapper = new WorldSimulator(false);
    private static final Logger LOG = Logger.getLogger(Simulator.class);

	public SoftBot getBlueSoft() {
		return worldWrapper.getBlueSoft();
	}

	public SoftBot getYellowSoft() {
		return worldWrapper.getYellowSoft();
	}

	@Override
	public String getTestName() {
		return "Super Cool Simulator";
	}

	public boolean setIsNoisy(boolean noisy) {
		worldWrapper.setNoisy(noisy);
		return noisy;
	}

	public boolean isNoisy() {
		return worldWrapper.isNoisy();
	}

	@Override
	public void initTest(boolean arg0) {
		lastStepTime = System.currentTimeMillis();
		worldWrapper.setStartTime(lastStepTime);
		worldWrapper.setWorld(getWorld());
		worldWrapper.initWorld();
		addBufferedListeners();

		// centre the camera
		setCamera(new Vec2(1.22f * WorldSimulator.SCALE,
				1.22f * WorldSimulator.SCALE),
				getCachedCameraScale());

		// Send the size of the pitch to the world
		worldWrapper.getReader().propagatePitchSize();

	}

	@Override
	public void keyPressed(char c, int keyCode) {
	}

	@Override
	public void update() {
		long dt = System.currentTimeMillis() - lastStepTime;
		worldWrapper.update(dt);
		super.update();

		// Update world with new information, throttling the frame rate
		worldWrapper.getReader().update();
		if (System.currentTimeMillis() - lastFrameTime > 1000f / Globals.SIMULATED_VISON_FRAMERATE) {
			lastFrameTime = System.currentTimeMillis();
			worldWrapper.getReader().propagate();
		}
	}

	/**
	 * Empty constructor, to make private. For constructor use createSimulator()
	 */
	protected Simulator(boolean noisy) { /*
										 * James: Do not use. Use initTest()
										 * instead.
										 */
		worldWrapper.setNoisy(noisy);
	}

	public static Simulator createSimulator() {
		return createSimulator(true);
	}

	/**
	 * Equivalent to a constructor.
	 * 
	 * @return A new simulator object.
	 */
	public static Simulator createSimulator(boolean noisy) {

		TestbedModel model = new TestbedModel();
		TestbedPanel panel = new TestPanelJ2D(model);
		model.addCategory("Buggy");
		Simulator pgui = new Simulator(noisy);
		model.addTest(pgui);
		JFrame testbed = new TestbedFrame(model, panel);
		testbed.setTitle("Simulator");
		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return pgui;
	}





	private ArrayList<Listener> listenersToAdd = new ArrayList<Listener>();;
	/**
	 * Marks a world to be updated of any changes
	 * 
	 * @param listener
	 *            World to be updated.
	 */
	@Override
	public void addListener(Listener listener) {
		if (worldWrapper != null) {
			worldWrapper.getReader().addListener(listener);
		} else {
			listenersToAdd.add(listener);
		}
	}

	private void addBufferedListeners() {
		if (worldWrapper != null)
			while (!listenersToAdd.isEmpty())
				worldWrapper.getReader().addListener(listenersToAdd.remove(0));
	}


	public void randomiseBallPosition() {
		worldWrapper.randomiseBallPosition();
	}

	public void randomiseRobotPositions() {
		worldWrapper.randomiseRobotPositions();
	}

	public void resetBallPosition() {
		worldWrapper.resetBallPosition(0, 0);
	}

	public void resetRobotPositions() {
		worldWrapper.resetRobotPositions();
	}

}
