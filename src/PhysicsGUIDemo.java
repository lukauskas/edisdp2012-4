import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.FrictionJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;


public class PhysicsGUIDemo extends TestbedTest {

	private final float scale = 10;
	
	// Robot Variables
	private final Vec2 leftWheelPos = new Vec2(0.01f*scale, 0.05f*scale);
	private final Vec2 rightWheelPos = new Vec2(0.01f*scale, -0.05f*scale);
	private final float robotWidth = 0.15f*scale;
	private final float robotLength = 0.2f*scale;
	private final float wheelWidth = 0.02f*scale;
	private final float wheelLength = 0.05f*scale;

	private Body ground;
	private Body robot;
	private Body wheelL;
	private Body wheelR;
	private Body ball;
	
	// wheel power (-1 for lock and 0 for float and (0,100] for power )
	private float wheelLPower;
	private float wheelRPower;
	
	@Override
	public String getTestName() {
		return "Super Cool Simulator";
	}

	private void addEdge(float x1, float y1, float x2, float y2) {
		World w = getWorld();
		PolygonShape r = new PolygonShape();
		r.setAsEdge(new Vec2(x1,y1), new Vec2(x2,y2));
		FixtureDef f = new FixtureDef();
		f.shape = r;
		// TODO find real restitution
		f.restitution = 0.4f;
		BodyDef b = new BodyDef();
		b.type = BodyType.STATIC;
		//b.position.set(10f,10f);
		w.createBody(b).createFixture(f);
	}
	
	@Override
	public void initTest(boolean arg0) {
		
		// center the camera
		setCamera(new Vec2(1.22f*scale,1.22f*scale), getCachedCameraScale());
		// No Gravity
		World w = getWorld();
		w.setGravity(new Vec2(0,0));
		
		// Ground
		PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(2.64f*scale,1.42f*scale);
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.STATIC;
		groundBodyDef.position.set(1.22f*scale,0.61f*scale);
		ground = w.createBody(groundBodyDef);
		FixtureDef groundF = new FixtureDef();
		groundF.shape = groundShape;
		groundF.density = (1f/0.36f)/scale;
		groundF.filter.maskBits = 0;
		ground.createFixture(groundF);
		
		// Edges of field
		// Main Pitch
		addEdge(0f*scale,0f*scale,2.44f*scale,0f*scale);
		addEdge(0f*scale,1.22f*scale,2.44f*scale,1.22f*scale);
		addEdge(0f*scale,0f*scale,0f*scale,0.31f*scale);
		addEdge(2.44f*scale,0f*scale,2.44f*scale,0.31f*scale);
		addEdge(0f*scale,1.22f*scale,0f*scale,0.91f*scale);
		addEdge(2.44f*scale,1.22f*scale,2.44f*scale,0.91f*scale);
		// Lef*scalet-hand goal area
		addEdge(0f*scale,0.31f*scale,-0.1f*scale,0.31f*scale);
		addEdge(-0.1f*scale,0.31f*scale,-0.1f*scale,0.91f*scale);
		addEdge(2.44f*scale,0.31f*scale,2.54f*scale,0.31f*scale);		
		// Right-hand goal area
		addEdge(2.44f*scale,0.91f*scale,2.54f*scale,0.91f*scale);
		addEdge(0f*scale,0.91f*scale,-0.1f*scale,0.91f*scale);
		addEdge(2.54f*scale,0.31f*scale,2.54f*scale,0.91f*scale);
		
		CircleShape ballShape = new CircleShape();
		ballShape.m_radius = 0.02135f*scale;
		FixtureDef f = new FixtureDef();
		f.shape = ballShape;
		f.density = (1f/0.36f)/scale;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(1.22f*scale,0.61f*scale);
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
		
		PolygonShape robotShape = new PolygonShape();
		robotShape.setAsBox(robotLength,robotWidth);
		BodyDef robotBodyDef = new BodyDef();
		robotBodyDef.type = BodyType.DYNAMIC;
		robotBodyDef.position.set(0.22f*scale,0.61f*scale);
		robot = w.createBody(robotBodyDef);
		FixtureDef robotF = new FixtureDef();
		robotF.shape = robotShape;
		robotF.density = (1f/0.36f)/scale;
		robotF.filter.groupIndex = -1;
		robot.createFixture(robotF);

		PolygonShape wheelShape = new PolygonShape();
		wheelShape.setAsBox(wheelLength, wheelWidth);
		BodyDef wheelBodyDef = new BodyDef();
		wheelBodyDef.type = BodyType.DYNAMIC;
		FixtureDef wheelF = new FixtureDef();
		wheelF.filter.groupIndex = -1;
		wheelF.density = (1f/0.36f)/scale;
		wheelF.shape = wheelShape;
		
		wheelBodyDef.position.set(robot.getWorldCenter().clone().add(leftWheelPos));
		wheelL = w.createBody(wheelBodyDef);
		wheelL.createFixture(wheelF);

		wheelBodyDef.position.set(robot.getWorldCenter().clone().add(rightWheelPos));
		wheelR = w.createBody(wheelBodyDef);
		wheelR.createFixture(wheelF);
		
		WeldJointDef wjd = new WeldJointDef();
		wjd.initialize(robot, wheelL, wheelL.getWorldCenter());
		w.createJoint(wjd);
		wjd.initialize(robot, wheelR, wheelR.getWorldCenter());
		w.createJoint(wjd);
	}
	
	@Override
	public void keyPressed(char c, int keyCode) {
	}
	
	@Override
	public void update() {
		//System.out.println(robot.getAngle()%(2*Math.PI));
		killAllOrtogonal(robot);
		//killAllOrtogonal(wheelL);
		//killAllOrtogonal(wheelR);
		super.update();
		float mag = 5f;
		double ang = robot.getAngle();
		wheelL.applyForce(new Vec2(-(float)(mag*Math.cos(ang)), -(float)(mag*Math.sin(ang))), wheelL.getWorldCenter());
		wheelR.applyForce(new Vec2((float)(mag*Math.cos(ang)), (float)(mag*Math.sin(ang))), wheelR.getWorldCenter());
	}

	private void killAllOrtogonal(Body b) {
		Vec2 v = b.getLinearVelocity();
		double ang = b.getAngle();
		Vec2 unitDir = new Vec2((float)(Math.cos(ang)), (float)(Math.sin(ang)));
		float newMag = Vec2.dot(v, unitDir);
		b.setLinearVelocity(new Vec2(newMag*unitDir.x, newMag*unitDir.y));
		//b.setLinearVelocity(new Vec2(0, 0));
	}
	
	public static void main(String[] args) {
	    try {
	      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    } catch (Exception e) {
	      System.out.println("Could not set the look and feel to nimbus.  "
	          + "Hopefully you're on a mac so the window isn't ugly as crap.");
	    }
	    TestbedModel model = new TestbedModel();
	    TestbedPanel panel = new TestPanelJ2D(model);
	    model.addCategory("Buggy");
	    model.addTest(new PhysicsGUIDemo());
	    JFrame testbed = new TestbedFrame(model, panel);
	    testbed.setVisible(true);
	    testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
}
