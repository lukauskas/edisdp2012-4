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
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;


public class PhysicsGUIDemo extends TestbedTest {

	private final float scale = 10;
	private Body ball;
	
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
		World w = getWorld();
		
		// No Gravity
		w.setGravity(new Vec2(0,0));

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
		
		CircleShape b = new CircleShape();
		b.m_radius = 0.02135f*scale;
		FixtureDef f = new FixtureDef();
		f.shape = b;
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DYNAMIC;
		bd.position.set(1.22f*scale,0.61f*scale);
		ball = w.createBody(bd);
		ball.createFixture(f);
		// TODO fid correct dampening
		ball.setLinearDamping(0.5f);
		ball.setLinearVelocity(new Vec2(10f, 10f));
		
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
