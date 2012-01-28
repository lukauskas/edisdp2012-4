import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;


public class PhysicsGUIDemo extends TestbedTest {

	@Override
	public String getTestName() {
		return "Super Cool Simulator";
	}

	@Override
	public void initTest(boolean arg0) {
		// TODO Auto-generated method stub

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
	    model.addTest(new PhysicsGUIDemo());
	    JFrame testbed = new TestbedFrame(model, panel);
	    testbed.setVisible(true);
	    testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
}
