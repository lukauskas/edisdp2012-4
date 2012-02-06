package balle.world;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.AbstractStrategy;

public class SimpleWorldGUI extends AbstractStrategy {

    private JFrame frame;
    private JPanel panel;

    public SimpleWorldGUI(Controller controller, AbstractWorld world) {
        super(controller, world);
        frame = new JFrame("WorldGUI");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new Screen();
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(770, 500);
        frame.setVisible(true);
    }

    @Override
    protected void aiStep() {
        panel.repaint();
    }

    @Override
    protected void aiMove(Controller controller) {
        // TODO Auto-generated method stub

    }

    private class Screen extends JPanel {

        private float       scale;
        private final float XSHIFTM     = 0.4f;
        private final float YSHIFTM     = 0.39f;
        private final float VIEWHEIGHTM = 2;

        @Override
        public void paintComponent(Graphics g) {
            scale = getHeight() / VIEWHEIGHTM;
            g.setColor(Color.YELLOW);
            g.fillRect(0, 0, getWidth(), getHeight());
            drawField(g);
            drawFieldObjects(g);
        }

        // public void

        private void drawField(Graphics g) {
            g.setColor(Color.BLACK);
            drawLineTransformMeters(g, 0f, 0f, 2.44f, 0f);
            drawLineTransformMeters(g, 0f, 1.22f, 2.44f, 1.22f);
            drawLineTransformMeters(g, 0f, 0f, 0f, 0.31f);
            drawLineTransformMeters(g, 2.44f, 0f, 2.44f, 0.31f);
            drawLineTransformMeters(g, 0f, 1.22f, 0f, 0.91f);
            drawLineTransformMeters(g, 2.44f, 1.22f, 2.44f, 0.91f);
            // Left-hand goal area
            drawLineTransformMeters(g, 0f, 0.31f, -0.1f, 0.31f);
            drawLineTransformMeters(g, -0.1f, 0.31f, -0.1f, 0.91f);
            drawLineTransformMeters(g, 2.44f, 0.31f, 2.54f, 0.31f);
            // Right-hand goal area
            drawLineTransformMeters(g, 2.44f, 0.91f, 2.54f, 0.91f);
            drawLineTransformMeters(g, 0f, 0.91f, -0.1f, 0.91f);
            drawLineTransformMeters(g, 2.54f, 0.31f, 2.54f, 0.91f);
        }

        private void drawFieldObjects(Graphics g) {
            Snapshot s = getSnapshot();
            if (s != null) {
                drawRobot(g, Color.GREEN, s.getBalle());
                drawRobot(g, Color.PINK, s.getOpponent());
                drawBall(g, Color.RED, s.getBall());
            }
        }

        private void drawBall(Graphics g, Color c, FieldObject ball) {

            if ((ball == null) || (ball.getPosition() == null)) {
                return;
            }
            float radius = Globals.BALL_RADIUS;
            Coord pos = ball.getPosition();
            g.setColor(c);
            g.fillOval(m2PX(pos.getX() - radius), m2PY(pos.getY() - radius),
                    (int) (radius * 2 * scale), (int) (radius * 2 * scale));
        }

        private void drawRobot(Graphics g, Color c, Robot robot) {

            // Fail early, fail often
            if ((robot == null) || (robot.getPosition() == null)) {
                System.out.println("Cannot draw robot");
                // 451, 157s
                return;
            }

            // position of center of the robot
            float x = (float) robot.getPosition().getX();
            float y = (float) robot.getPosition().getY();
            System.out.println("Drawing robot x:" + x + ", y:" + y);
            // half length and width of robot
            float hl = Globals.ROBOT_LENGTH / 2;
            float hw = Globals.ROBOT_WIDTH / 2;

            // list of (x,y) positions of the corners of the robot
            // with the center at (0,0)
            float[][] poly = new float[][] { { hl, hw }, { hl, -hw },
                    { -hl, -hw }, { -hl, hw } };

            // for each point
            double a = robot.getOrientation();
            for (int i = 0; i < poly.length; i++) {
                float px = poly[i][0];
                float py = poly[i][1];

                // rotate by angle of orientation
                poly[i][0] = (float) ((px * Math.cos(a)) + (-py * Math.sin(a)));
                poly[i][1] = (float) ((px * Math.sin(a)) + (py * Math.cos(a)));

                // transform to robot's position
                poly[i][0] += x;
                poly[i][1] += y;
            }

            // convert to pixel coordinates
            int n = poly.length;
            int[] xs = new int[n];
            int[] ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = m2PX(poly[i][0]);
                ys[i] = m2PY(poly[i][1]);
            }

            // draw
            g.setColor(Color.DARK_GRAY);
            g.fillPolygon(xs, ys, n);
            g.setColor(c);
            g.fillPolygon(new int[] { xs[2], xs[3], m2PX(x) }, new int[] {
                    ys[2], ys[3], m2PX(y) }, 3);
        }

        // Convert meters into pixels and draws line
        private void drawLineTransformMeters(Graphics g, float x1, float y1,
                float x2, float y2) {

            g.drawLine(m2PX(x1), m2PY(y1), m2PX(x2), m2PY(y2));

        }

        private int m2PX(double x) {
            return m2PX((float) x);
        }

        private int m2PX(float x) {
            return (int) ((x + XSHIFTM) * scale);
        }

        private int m2PY(double y) {
            return m2PY((float) y);
        }

        private int m2PY(float y) {
            return (int) ((y + YSHIFTM) * scale);
        }

    }

}
