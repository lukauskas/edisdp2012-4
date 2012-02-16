package balle.simulator;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jbox2d.collision.Collision;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.FrictionJoint;
import org.jbox2d.dynamics.joints.FrictionJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import sun.security.action.GetLongAction;

import balle.io.listener.Listener;
import balle.io.reader.AbstractVisionReader;
import balle.io.reader.Reader;
import balle.misc.Globals;

public class Simulator extends TestbedTest implements AbstractVisionReader {

	// Increases sizes, but keeps real-world scale; jbox2d acts unrealistically
	// at a small scale
	protected final float scale = 10;

	private Body ground;
	protected Body ball;

	protected Robot blue;
	protected Robot yellow;

	private SoftBot blueSoft = new SoftBot();
	private SoftBot yellowSoft = new SoftBot();

	private long startTime;
	private long lastStepTime;

	private CircleShape ballShape;

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

	private void addEdge(float x1, float y1, float x2, float y2) {
		World w = getWorld();
		PolygonShape r = new PolygonShape();
		r.setAsEdge(new Vec2(x1, y1), new Vec2(x2, y2));
		FixtureDef f = new FixtureDef();
		f.shape = r;
		// TODO find real restitution
		f.restitution = 0.4f;
		BodyDef b = new BodyDef();
		b.type = BodyType.STATIC;
		// b.position.set(10f,10f);
		w.createBody(b).createFixture(f);

	}

	@Override
	public void initTest(boolean arg0) {

		lastStepTime = startTime = System.currentTimeMillis();

		// centre the camera
		setCamera(new Vec2(1.22f * scale, 1.22f * scale),
				getCachedCameraScale());
		// No Gravity
		World w = getWorld();
		w.setGravity(new Vec2(0, 0));

		// create ground
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(2.64f * scale, 1.42f * scale);
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.STATIC;
		groundBodyDef.position.set(1.22f * scale, 0.61f * scale);
		ground = w.createBody(groundBodyDef);
		FixtureDef groundF = new FixtureDef();
		groundF.shape = groundShape;
		groundF.density = (1f / 0.36f) / scale;
		groundF.filter.maskBits = 0;
		ground.createFixture(groundF);

		// Create edge of field
		// Main Pitch
		addEdge(0f * scale, 0f * scale, 2.44f * scale, 0f * scale);
		addEdge(0f * scale, 1.22f * scale, 2.44f * scale, 1.22f * scale);
		addEdge(0f * scale, 0f * scale, 0f * scale, 0.31f * scale);
		addEdge(2.44f * scale, 0f * scale, 2.44f * scale, 0.31f * scale);
		addEdge(0f * scale, 1.22f * scale, 0f * scale, 0.91f * scale);
		addEdge(2.44f * scale, 1.22f * scale, 2.44f * scale, 0.91f * scale);
		// Left-hand goal area
		addEdge(0f * scale, 0.31f * scale, -0.1f * scale, 0.31f * scale);
		addEdge(-0.1f * scale, 0.31f * scale, -0.1f * scale, 0.91f * scale);
		addEdge(2.44f * scale, 0.31f * scale, 2.54f * scale, 0.31f * scale);
		// Right-hand goal area
		addEdge(2.44f * scale, 0.91f * scale, 2.54f * scale, 0.91f * scale);
		addEdge(0f * scale, 0.91f * scale, -0.1f * scale, 0.91f * scale);
		addEdge(2.54f * scale, 0.31f * scale, 2.54f * scale, 0.91f * scale);

		// Create ball
		ballShape = new CircleShape();
		ballShape.m_radius = Globals.BALL_RADIUS * scale;
		FixtureDef f = new FixtureDef();
		f.shape = ballShape;
		f.density = (1f / 0.36f) / scale;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(1.22f * scale, 0.61f * scale);
		bd.bullet = true;
		ball = w.createBody(bd);
		ball.createFixture(f);
		ball.setLinearDamping(0);

		// TODO correct friction
		FrictionJointDef ballFriction = new FrictionJointDef();
		ballFriction.initialize(ball, ground, ball.getWorldCenter());
		ballFriction.maxForce = 0.1f;
		ballFriction.maxTorque = 0.001f;
		w.createJoint(ballFriction);

		// create robots at either end of pitch
		blue = new Robot(new Vec2((0.1f * scale), (float) (0.61 * scale)), 0f);
		blueSoft.setBody(blue.getBody());
		yellow = new Robot(new Vec2((float) (2.34 * scale),
				(float) (0.61 * scale)), 0f);
		yellowSoft.setBody(yellow.getBody());

		// Send the size of the pitch to the world
		this.reader.propagatePitchSize();

	}

	@Override
	public void keyPressed(char c, int keyCode) {
	}

	@Override
	public void update() {
		long dt = System.currentTimeMillis() - lastStepTime;
		blue.updateRobot(blueSoft, dt);
		yellow.updateRobot(yellowSoft, dt);
		super.update();

		// Update world with new information.
		this.reader.update();
		lastStepTime = System.currentTimeMillis();
	}

	/**
	 * Empty constructor, to make private. For constructor use createSimulator()
	 */
	protected Simulator() { /* James: Do not use. Use initTest() instead. */
	}

	/**
	 * Equivalent to a constructor.
	 * 
	 * @return A new simulator object.
	 */
	public static Simulator createSimulator() {
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
		Simulator pgui = new Simulator();
		model.addTest(pgui);
		JFrame testbed = new TestbedFrame(model, panel);
		testbed.setTitle("Simulator");
		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return pgui;
	}

	private static int robotNo = 0;

	protected class Robot {

		private Body kicker;

		private Body robot;

		private final Vec2 kickPos = new Vec2(0.12f * scale, 0);

		private final float robotWidth = Globals.ROBOT_WIDTH * scale / 2;
		private final float robotLength = Globals.ROBOT_LENGTH * scale / 2;

		private final float kickerWidth = Globals.ROBOT_WIDTH * scale / 2;
		private final float kickerLength = 0.04f * scale / 2;
		private PolygonShape kickerShape;
		private static final float kickForce = 20f;

		// wheel power (-1 for lock and 0 for float and (0,100] for power )

		public Robot(Vec2 startingPos, float angle) {
			World w = getWorld();
			int robotIndex = robotNo++;

			// Create Robot body, set position from Constructor
			PolygonShape robotShape = new PolygonShape();
			robotShape.setAsBox(robotLength, robotWidth);
			BodyDef robotBodyDef = new BodyDef();
			robotBodyDef.type = BodyType.DYNAMIC;
			robotBodyDef.position.set(startingPos);
			robotBodyDef.angularDamping = 0;
			robot = w.createBody(robotBodyDef);
			FixtureDef robotF = new FixtureDef();
			robotF.shape = robotShape;
			robotF.density = (1f / 0.36f) / scale;
			// Stops wheels and body colliding
			robotF.filter.groupIndex = -robotIndex - 20;
			robot.createFixture(robotF);

			kickerShape = new PolygonShape();
			kickerShape.setAsBox(kickerLength, kickerWidth);
			BodyDef kickerBodyDef = new BodyDef();
			kickerBodyDef.type = BodyType.DYNAMIC;
			FixtureDef kickF = new FixtureDef();
			kickF.filter.maskBits = 0x0;
			kickF.filter.groupIndex = -robotIndex - 20;
			kickF.density = (1f / 0.36f) / scale;
			kickF.shape = kickerShape;

			kickerBodyDef.position.set(robot.getWorldCenter().clone()
					.add(kickPos));
			kicker = w.createBody(kickerBodyDef);
			kicker.createFixture(kickF);

			WeldJointDef wjd = new WeldJointDef();
			wjd.initialize(robot, kicker, kicker.getWorldCenter());
			w.createJoint(wjd);
		}

		public boolean ballInRange() {
			Collision c = getWorld().getPool().getCollision();
			return c.testOverlap(ballShape, kickerShape, ball.getTransform(),
					kicker.getTransform());
		}

		public void updateRobot(SoftBot bot, long dt) {

			double blueAng = robot.getAngle();

			// if we are turning
			if (bot.isRotating()) {
				boolean turningLeft = bot.isTurningLeft();
				// get current angle - desired angle
				float dDA = bot.deltaDesiredAngle();
				if (!turningLeft && (0 <= dDA && dDA <= (Math.PI / 4))
						|| turningLeft && (0 >= dDA && dDA >= -(Math.PI / 4))) {
					// stop
					bot.stop();
					System.out.println("done turning");
				}
			}

			float vl = linearVelocity(bot, dt) * scale; // linear velocity
			float va = angularVelocityRadians(bot, dt); // angular velocity
			robot.setLinearVelocity(new Vec2((float) (vl * Math.cos(blueAng)),
					(float) (vl * Math.sin(blueAng))));
			;
			robot.setAngularVelocity(va);
			// System.out.println(va);

			if (bot.isKicking() && ballInRange()) {
				// Kick
				float xF, yF;
				xF = (float) (kickForce * Math.cos(blueAng));
				yF = (float) (kickForce * Math.sin(blueAng));
				ball.applyForce(new Vec2(xF, yF), ball.getWorldCenter());
				bot.stopKicking();
			}
		}

		public Body getBody() {
			return robot;
		}

		
		private float getSlipableWheelVelocity(SoftBot bot, float nonSlipVelocity, Vec2 wheelPos, long dt) {
        	// current velocity Left wheel
			Vec2 unitForward = new Vec2((float)Math.cos(bot.getBody().getAngle()),(float)Math.sin(bot.getBody().getAngle()));
        	float vi = Vec2.dot(bot.getBody().getLinearVelocityFromLocalPoint(wheelPos), unitForward)/scale;

        	// no-slip velocity
        	float vf = nonSlipVelocity;
        	
        	// find acceleration of wheel
        	// NOTE: must convert dt to seconds (td/1000)
        	float dts = dt/1000f;
        	float a = (vf-vi)/dts;
        	// Account for slip
        	// if above maximum (Globals.MaxWheelAccel)
        	if(Math.abs(a) > Globals.MaxWheelAccel) {
        		// use Globals.WheelSlipAccel to calculate velocities
        		float slipAccel = Globals.SlipWheelAccel;
        		if(a < 0) slipAccel = -slipAccel;
        		vf =  vi + (dts*slipAccel);
        	}
        	
        	return vf;
        }
		
		private float getLeftWheelVelocity(SoftBot bot, long dt) {
			return getSlipableWheelVelocity(bot, 
					Globals.powerToVelocity(bot.getLeftWheelSpeed()),
					Globals.ROBOT_LEFT_WHEEL_POS,
					dt);
		}

		private float getRightWheelVelocity(SoftBot bot, long dt) {
			return getSlipableWheelVelocity(bot, 
					Globals.powerToVelocity(bot.getRightWheelSpeed()),
					Globals.ROBOT_RIGHT_WHEEL_POS,
					dt);
		}

		private float angularVelocityRadians(SoftBot bot, long dt) {
			float vl = getLeftWheelVelocity(bot, dt);
			float vr = getRightWheelVelocity(bot, dt);
			float w = Globals.ROBOT_TRACK_WIDTH;

			return (vr - vl) / w;
		}

		private float linearVelocity(SoftBot bot, long dt) {
			float vl = getLeftWheelVelocity(bot, dt);
			float vr = getRightWheelVelocity(bot, dt);

			return (vl + vr) / 2f;
		}
	}

	SimulatorReader reader = new SimulatorReader();

	/*
	 * Output to World: All code refering to the output from the simulator goes
	 * here.
	 */
	class SimulatorReader extends Reader {

		private long getTimeStamp() {
			return System.currentTimeMillis() - startTime;
		}

		private float convAngle(float a) {
			return (180f / (float) Math.PI) * a;
		}

		private Vec2 convPos(Vec2 a) {
			Vec2 output = new Vec2();
			output.x = a.x / scale;
			output.y = a.y / scale;
			return output;
		}

		public synchronized String getWorldData() {
			Vec2 yellowPos, bluePos;
			yellowPos = convPos(yellow.robot.getPosition());
			bluePos = convPos(blue.robot.getPosition());

			float yellowAng, blueAng;
			yellowAng = convAngle(yellow.robot.getAngle());
			blueAng = convAngle(blue.robot.getAngle());
			return yellowPos.x + " " + yellowPos.y + " " + yellowAng + " "
					+ bluePos.x + " " + bluePos.y + " " + blueAng + " "
					+ getTimeStamp();
		}

		public void update() {
			float yPosX, yPosY, yRad, bPosX, bPosY, bRad, ballPosX, ballPosY;

			Vec2 yPos = convPos(yellow.robot.getPosition());
			yPosX = yPos.x;
			yPosY = yPos.y;
			yRad = convAngle(yellow.robot.getAngle());

			Vec2 bPos = convPos(blue.robot.getPosition());
			bPosX = bPos.x;
			bPosY = bPos.y;
			bRad = convAngle(blue.robot.getAngle());

			Vec2 ballPos = convPos(ball.getPosition());
			ballPosX = ballPos.x;
			ballPosY = ballPos.y;

			long timestamp = getTimeStamp();

			super.propagate(yPosX, yPosY, yRad, bPosX, bPosY, bRad, ballPosX,
					ballPosY, timestamp);
		}

		public void propagatePitchSize() {
			super.propagatePitchSize(Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT);
		}
	}

	/**
	 * Marks a world to be updated of any changes
	 * 
	 * @param listener
	 *            World to be updated.
	 */
	@Override
	public void addListener(Listener listener) {
		reader.addListener(listener);
	}

}
