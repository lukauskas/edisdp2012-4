package balle.world;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import balle.simulator.Simulator;

public class WorldGUI extends Simulator {

    private AbstractWorld world;

    @Override
    public String getTestName() {
        return "Super Cool World GUI";
    }

    protected WorldGUI() {
        super();
    }

    public static WorldGUI createWorldGUI(AbstractWorld w) {
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
        WorldGUI pgui = new WorldGUI();
        model.addTest(pgui);
        JFrame testbed = new TestbedFrame(model, panel);
        testbed.setVisible(true);
        testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pgui.world = w;
        return pgui;
    }

    @Override
    public void initTest(boolean arg0) {
        super.initTest(arg0);
    }

    @Override
    public void update() {
        super.update();
        if (world != null) {
            Snapshot s = world.getSnapshot();
            if (s != null) {
                if (ball != null) {
                    ball.setLinearVelocity(new Vec2(0, 0));
                    ball.getPosition().set(
                            (float) s.getBall().getPosition().getX() * scale,
                            (float) (1.22 - s.getBall().getPosition().getY())
                                    * scale);
                }

                if (yellow != null) {
                    balle.world.Robot robot = world.isBlue() ? s.getOpponent()
                            : s.getBalle();
                    Coord pos = robot.getPosition();
                    float angle = (float) robot.getOrientation().radians();
                    Body b = yellow.getBody();
                    b.setLinearVelocity(new Vec2(0, 0));
                    b.getPosition().set((float) pos.getX() * scale,
                            (float) (1.22 - pos.getY()) * scale);
                    b.setTransform(b.getPosition(), -angle);
                }

                if (blue != null) {
                    balle.world.Robot robot = (!world.isBlue()) ? s
                            .getOpponent() : s.getBalle();
                    Coord pos = robot.getPosition();
                    float angle = (float) robot.getOrientation().radians();
                    Body b = blue.getBody();
                    b.setLinearVelocity(new Vec2(0, 0));
                    b.getPosition().set((float) pos.getX() * scale,
                            (float) (1.22 - pos.getY()) * scale);
                    b.setTransform(b.getPosition(), -angle);
                }
            }
        }
    }

}
