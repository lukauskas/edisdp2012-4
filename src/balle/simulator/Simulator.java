package balle.simulator;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;

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

	private static boolean noisy = true;

	private final SoftBot blueSoft = new SoftBot();
	private final SoftBot yellowSoft = new SoftBot();

	private long lastStepTime;
	private long lastFrameTime;

	private SimulatorWorld worldWrapper = new SimulatorWorld();;

	public SoftBot getBlueSoft() {
		return blueSoft;
	}

	public SoftBot getYellowSoft() {
		return yellowSoft;
	}

	@Override
	public String getTestName() {
		return "Super Cool Simulator";
	}

	public static boolean setIsNoisy(boolean noisy) {
		return Simulator.noisy = noisy;
	}

	public static boolean isNoisy() {
		return noisy;
	}

	@Override
	public void initTest(boolean arg0) {
		lastStepTime = System.currentTimeMillis();
		worldWrapper.setStartTime(lastStepTime);
		worldWrapper.setWorld(getWorld());
		worldWrapper.initWorld();
		addBufferedListeners();

		// centre the camera
		setCamera(new Vec2(1.22f * SimulatorWorld.SCALE,
				1.22f * SimulatorWorld.SCALE),
				getCachedCameraScale());
		// // No Gravity
		// World w = getWorld();
		// w.setGravity(new Vec2(0, 0));
		//
		// // create ground
		// PolygonShape groundShape = new PolygonShape();
		// groundShape.setAsBox(2.64f * SCALE, 1.42f * SCALE);
		// BodyDef groundBodyDef = new BodyDef();
		// groundBodyDef.type = BodyType.STATIC;
		// groundBodyDef.position.set(1.22f * SCALE, 0.61f * SCALE);
		// ground = w.createBody(groundBodyDef);
		// FixtureDef groundF = new FixtureDef();
		// groundF.shape = groundShape;
		// groundF.density = (1f / 0.36f) / SCALE;
		// groundF.filter.maskBits = 0;
		// ground.createFixture(groundF);
		//
		// // Create edge of field
		// // Main Pitch
		// addEdge(0f * SCALE, 0f * SCALE, 2.44f * SCALE, 0f * SCALE);
		// addEdge(0f * SCALE, 1.22f * SCALE, 2.44f * SCALE, 1.22f * SCALE);
		// addEdge(0f * SCALE, 0f * SCALE, 0f * SCALE, 0.31f * SCALE);
		// addEdge(2.44f * SCALE, 0f * SCALE, 2.44f * SCALE, 0.31f * SCALE);
		// addEdge(0f * SCALE, 1.22f * SCALE, 0f * SCALE, 0.91f * SCALE);
		// addEdge(2.44f * SCALE, 1.22f * SCALE, 2.44f * SCALE, 0.91f * SCALE);
		// // Left-hand goal area
		// addEdge(0f * SCALE, 0.31f * SCALE, -0.1f * SCALE, 0.31f * SCALE);
		// addEdge(-0.1f * SCALE, 0.31f * SCALE, -0.1f * SCALE, 0.91f * SCALE);
		// addEdge(2.44f * SCALE, 0.31f * SCALE, 2.54f * SCALE, 0.31f * SCALE);
		// // Right-hand goal area
		// addEdge(2.44f * SCALE, 0.91f * SCALE, 2.54f * SCALE, 0.91f * SCALE);
		// addEdge(0f * SCALE, 0.91f * SCALE, -0.1f * SCALE, 0.91f * SCALE);
		// addEdge(2.54f * SCALE, 0.31f * SCALE, 2.54f * SCALE, 0.91f * SCALE);
		//
		// // Create ball
		// resetBallPosition();
		//
		// // create robots at either end of pitch
		// resetRobotPositions();

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
		Simulator.noisy = noisy;
	}

	public static Simulator createSimulator() {
		return createSimulator(noisy);
	}

	/**
	 * Equivalent to a constructor.
	 * 
	 * @return A new simulator object.
	 */
	public static Simulator createSimulator(boolean noisy) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.out
					.println("Could not set the look and feel to nimbus.  "
							+ "Hopefully you're on a mac so the window isn't ugly as crap.");
		}
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
		worldWrapper.resetBallPosition();
	}

	public void resetRobotPositions() {
		worldWrapper.resetRobotPositions();
	}

}
