import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.FrictionJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import balle.simulator.SoftBot;
import balle.strategy.AbstractStrategy;
import balle.strategy.UserInputStrategy;
import balle.world.BasicWorld;

public class PhysicsGUIDemo extends TestbedTest {

	// Increases sizes, but keeps real-world scale; jbox2d acts unrealistically at a small scale
	private final float scale = 10;
	private Body ground;
	private Body ball;

	private Robot blue;
	private Robot yellow;
	private SoftBot blueSoft;
	private SoftBot yellowSoft;

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
		CircleShape ballShape = new CircleShape();
		ballShape.m_radius = 0.02135f * scale;
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
		ball.setLinearVelocity(new Vec2(10f, 10f));

		// TODO correct friction
		FrictionJointDef ballFriction = new FrictionJointDef();
		ballFriction.initialize(ball, ground, ball.getWorldCenter());
		ballFriction.maxForce = 0.2f;
		ballFriction.maxTorque = 0.001f;
		w.createJoint(ballFriction);

		// create robots at either end of pitch
		blue = new Robot(new Vec2((float) (0.1f * scale), (float) (0.61 * scale)), 0f);
		yellow = new Robot(new Vec2((float) (2.34 * scale), (float) (0.61 * scale)), 0f);
		blueSoft = new SoftBot();
		yellowSoft = new SoftBot();
		
		AbstractStrategy s = new UserInputStrategy(yellowSoft, new BasicWorld(true));
		s.start();
	}

	@Override
	public void keyPressed(char c, int keyCode) {
	}

	@Override
	public void update() {
		killAllOrtogonal(yellow.wheelL);
		killAllOrtogonal(yellow.wheelR);
		killAllOrtogonal(blue.wheelL);		
		killAllOrtogonal(blue.wheelR);
		
		double blueAng = blue.robot.getAngle();
		double yellowAng = yellow.robot.getAngle();
		
		float blueLeftWheelSpeed = blueSoft.getLeftWheelSpeed();
		float blueRightWheelSpeed = blueSoft.getRightWheelSpeed();
		float yellowLeftWheelSpeed = yellowSoft.getLeftWheelSpeed();
		float yellowRightWheelSpeed = yellowSoft.getRightWheelSpeed();
		
		blue.wheelL.applyForce(new Vec2((float)(blueLeftWheelSpeed*Math.cos(blueAng)),
				(float)(blueLeftWheelSpeed*Math.sin(blueAng))), blue.wheelL.getWorldCenter());
		blue.wheelR.applyForce(new Vec2((float)(blueRightWheelSpeed*Math.cos(blueAng)),
				(float)(blueRightWheelSpeed*Math.sin(blueAng))), blue.wheelR.getWorldCenter());
		
		
		yellow.wheelL.applyForce(new Vec2((float)(yellowLeftWheelSpeed*Math.cos(yellowAng)),
				(float)(yellowRightWheelSpeed*Math.sin(yellowAng))), yellow.wheelL.getWorldCenter());
		yellow.wheelR.applyForce(new Vec2((float)(yellowRightWheelSpeed*Math.cos(yellowAng)),
				(float)(yellowRightWheelSpeed*Math.sin(yellowAng))), yellow.wheelR.getWorldCenter());
		super.update();
	}

	private void killAllOrtogonal(Body b) {
		Vec2 v = b.getLinearVelocity();
		double ang = b.getAngle();
		Vec2 unitDir = new Vec2((float) (Math.cos(ang)),
				(float) (Math.sin(ang)));
		float newMag = Vec2.dot(v, unitDir);
		b.setLinearVelocity(new Vec2(newMag * unitDir.x, newMag * unitDir.y));
	}

	public static void main(String[] args) {
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
		PhysicsGUIDemo pgui = new PhysicsGUIDemo();
		model.addTest(pgui);
		JFrame testbed = new TestbedFrame(model, panel);
		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	private static int robotNo = -20;
	
	class Robot {
		
		private Body robot;
		private Body wheelL;
		private Body wheelR;

		private final Vec2 leftWheelPos;
		private final Vec2 rightWheelPos;
		private final float robotWidth = 0.15f * scale / 2;
		private final float robotLength = 0.2f * scale / 2;
		private final float wheelWidth = 0.02f * scale;
		private final float wheelLength = 0.05f * scale;

		// wheel power (-1 for lock and 0 for float and (0,100] for power )

		public Robot(Vec2 startingPos, float angle) {
			World w = getWorld();
			leftWheelPos = new Vec2(0, (0.05f * scale));
			rightWheelPos = new Vec2(0, -(0.05f * scale));
			
			// Create Robot body, set position from Constructor
			PolygonShape robotShape = new PolygonShape();
			robotShape.setAsBox(robotLength, robotWidth);
			BodyDef robotBodyDef = new BodyDef();
			robotBodyDef.type = BodyType.DYNAMIC;
			robotBodyDef.position.set(startingPos);
			robot = w.createBody(robotBodyDef);
			FixtureDef robotF = new FixtureDef();
			robotF.shape = robotShape;
			robotF.density = (1f / 0.36f) / scale;
			// Stops wheels and body colliding
			robotF.filter.groupIndex = robotNo--;
			robot.createFixture(robotF);

			PolygonShape wheelShape = new PolygonShape();
			wheelShape.setAsBox(wheelLength, wheelWidth);
			BodyDef wheelBodyDef = new BodyDef();
			wheelBodyDef.type = BodyType.DYNAMIC;
			FixtureDef wheelF = new FixtureDef();
			wheelF.filter.groupIndex = -1;
			wheelF.density = (1f / 0.36f) / scale;
			wheelF.shape = wheelShape;

			wheelBodyDef.position.set(robot.getWorldCenter().clone()
					.add(leftWheelPos));
			wheelL = w.createBody(wheelBodyDef);
			wheelL.createFixture(wheelF);

			wheelBodyDef.position.set(robot.getWorldCenter().clone()
					.add(rightWheelPos));
			wheelR = w.createBody(wheelBodyDef);
			wheelR.createFixture(wheelF);

			WeldJointDef wjd = new WeldJointDef();
			wjd.initialize(robot, wheelL, wheelL.getWorldCenter());
			w.createJoint(wjd);
			wjd.initialize(robot, wheelR, wheelR.getWorldCenter());
			w.createJoint(wjd);

		}
	}
}
