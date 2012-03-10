package balle.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import balle.main.drawable.Drawable;
import balle.main.drawable.DrawableLine;
import balle.misc.Globals;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.Scaler;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Robot;
import balle.world.processing.AbstractWorldProcessor;

public class SimpleWorldGUI extends AbstractWorldProcessor {

    private final JPanel        panel;
    private final Screen        screen;
	private final JPanel fpsMainPanel;
	private final JPanel fpsVisionPanel;
	private final JLabel fpsVisionText;
    private final JLabel        fpsWarning;
	private final JLabel fpsVision;
	private final JLabel fpsStrategyText;
	private final JLabel fpsStrategy;
	private final JPanel fpsStrategyPanel;

    private static final Logger LOG = Logger.getLogger(SimpleWorldGUI.class);

    public SimpleWorldGUI(AbstractWorld world) {
        super(world);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			System.err.println("UI Manager exception: " + e.getMessage());
		}
		System.out.println("This should happen");
		// Initialising Vision FPS
		fpsVisionPanel = new JPanel();
		fpsVisionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fpsVisionText = new JLabel("Input FPS:");
        fpsWarning = new JLabel("Vision down?");
        fpsWarning.setForeground(Color.RED);

		fpsVision = new JLabel();
		fpsVisionPanel.add(fpsVisionText);
		fpsVisionPanel.add(fpsVision);
		fpsVisionPanel.add(fpsWarning);

		// Initialising Strategy FPS
		fpsStrategyPanel = new JPanel();
		fpsStrategyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fpsStrategyText = new JLabel("Strategy FPS:");

		fpsStrategy = new JLabel();
		fpsStrategyPanel.add(fpsStrategyText);
		fpsStrategyPanel.add(fpsStrategy);

		// Put both FPS into one JPanel
		fpsMainPanel = new JPanel();
		fpsMainPanel.add(BorderLayout.WEST, fpsVisionPanel);
		fpsMainPanel.add(BorderLayout.EAST, fpsStrategyPanel);

        screen = new Screen();
        screen.addMouseMotionListener(screen);
		panel.add(BorderLayout.NORTH, fpsMainPanel);
        panel.add(BorderLayout.CENTER, screen);
    }

    public void setDrawables(ArrayList<Drawable> drawables) {
        screen.setDrawables(drawables);
    }

    public JPanel getPanel() {
        return panel;
    }

    @SuppressWarnings("serial")
    private class Screen extends JPanel implements MouseMotionListener {

        private final float         XSHIFTM     = 0.4f;
        private final float         YSHIFTM     = 0.39f;
        private final float         VIEWHEIGHTM = 2;
        private ArrayList<Drawable> drawables   = new ArrayList<Drawable>();
        private final Scaler        scaler;

        public Screen() {
            super();
            scaler = new Scaler(XSHIFTM, YSHIFTM);
        }

        public void setDrawables(ArrayList<Drawable> drawables) {
            this.drawables = new ArrayList<Drawable>(drawables);
        }

        @Override
        public void paintComponent(Graphics g) {
            float scale = getHeight() / VIEWHEIGHTM;
            scaler.setScale(scale);
            g.setColor(new Color(72, 104, 22));
            g.fillRect(0, 0, getWidth(), getHeight());
            drawField(g);
            drawFieldObjects(g);

            drawMousePos(g);

            for (Drawable d : drawables) {
                d.draw(g, scaler);
            }
            drawables.clear();
        }

        private void drawField(Graphics g) {
            g.setColor(Color.BLACK);
            drawLineTransformMeters(g, 0f, 0f, Globals.PITCH_WIDTH, 0f);
            drawLineTransformMeters(g, 0f, Globals.PITCH_HEIGHT, Globals.PITCH_WIDTH,
                    Globals.PITCH_HEIGHT);
            drawLineTransformMeters(g, 0f, 0f, 0f, Globals.GOAL_POSITION);
            drawLineTransformMeters(g, Globals.PITCH_WIDTH, 0f, Globals.PITCH_WIDTH,
                    Globals.GOAL_POSITION);
            drawLineTransformMeters(g, 0f, Globals.PITCH_HEIGHT, 0f, Globals.PITCH_HEIGHT
                    - Globals.GOAL_POSITION);
            drawLineTransformMeters(g, Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT,
                    Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT - Globals.GOAL_POSITION);
            // Left-hand goal area
            drawLineTransformMeters(g, 0f, Globals.GOAL_POSITION, -0.1f, Globals.GOAL_POSITION);
            drawLineTransformMeters(g, -0.1f, Globals.GOAL_POSITION, -0.1f, Globals.PITCH_HEIGHT
                    - Globals.GOAL_POSITION);
            drawLineTransformMeters(g, Globals.PITCH_WIDTH, Globals.GOAL_POSITION,
                    Globals.PITCH_WIDTH + 0.1f, Globals.GOAL_POSITION);
            // Right-hand goal area
            drawLineTransformMeters(g, Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT
                    - Globals.GOAL_POSITION, Globals.PITCH_WIDTH + 0.1f, Globals.PITCH_HEIGHT
                    - Globals.GOAL_POSITION);
            drawLineTransformMeters(g, 0f, Globals.PITCH_HEIGHT - Globals.GOAL_POSITION, -0.1f,
                    Globals.PITCH_HEIGHT - Globals.GOAL_POSITION);
            drawLineTransformMeters(g, Globals.PITCH_WIDTH + 0.1f, Globals.GOAL_POSITION,
                    Globals.PITCH_WIDTH + 0.1f, Globals.PITCH_HEIGHT - Globals.GOAL_POSITION);
        }

        private void drawFieldObjects(Graphics g) {
            Snapshot s = getSnapshot();
            if (s != null) {
                drawRobot(g, Color.GREEN, s.getBalle());
                drawRobot(g, Color.RED, s.getOpponent());
                drawBall(g, Color.RED, s.getBall());
                drawGoals(g);
            }
        }

        private void drawBall(Graphics g, Color c, Ball ball) {
            // TODO: Use ball.getRadius() instead of constants here

            // Daniel: There is no method getRadius() for ball because its a
            // MovingPoint object?
            // For the mean time I've changed the object to Ball

            // TODO: draw the velocity vector.

            if ((ball == null) || (ball.getPosition() == null)) {
                return;
            }
            float radius = (float) ball.getRadius();
            Coord pos = ball.getPosition();

            if (!pos.isEstimated())
                g.setColor(c);
            else
                g.setColor(Color.LIGHT_GRAY);

            float w = radius * 2 * scaler.getScale();
            g.fillOval((int) (scaler.m2PX(pos.getX()) - (w / 2)),
                    (int) (scaler.m2PY(pos.getY()) - (w / 2)), (int) w, (int) w);
        }

        private void drawRobot(Graphics g, Color c, Robot robot) {

            Snapshot s = getSnapshot();

            // Fail early, fail often
            if ((robot.getPosition() == null) || (robot.getOrientation() == null)) {
                return;
            }

            // position of center of the robot
            float x = (float) robot.getPosition().getX();
            float y = (float) robot.getPosition().getY();
            boolean isEstimated = robot.getPosition().isEstimated();

            // half length and width of robot
            float hl = (float) (robot.getHeight() / 2);
            float hw = (float) (robot.getWidth() / 2);

            // list of (x,y) positions of the corners of the robot
            // with the center at (0,0)
            float[][] poly = new float[][] { { hl, hw }, { hl, -hw }, { -hl, -hw }, { -hl, hw } };

            // for each point
            double a = robot.getOrientation().radians();
            for (int i = 0; i < poly.length; i++) {
                float px = poly[i][0];
                float py = poly[i][1];

                // a = (90 * Math.PI) / 180;

                // System.out.println(robot.getOrientation().degrees());
                // rotate by angle of orientation
                poly[i][0] = (float) ((px * Math.cos(a)) + (py * -Math.sin(a)));
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
                xs[i] = scaler.m2PX(poly[i][0]);
                ys[i] = scaler.m2PY(poly[i][1]);
            }

            // draw robot, setting the colour depending on robot properties
            // temporary colours until methods have been implemented
            if (robot.possessesBall(s.getBall())) {
                DrawableLine ballKickLine = new DrawableLine(robot.getBallKickLine(s.getBall()),
                        Color.BLUE);
                ballKickLine.draw(g, scaler);
                if (robot.isInScoringPosition(s.getBall(), s.getOpponentsGoal(), s.getOpponent())) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.ORANGE);
                }

            } else if (!isEstimated) {
                g.setColor(Color.LIGHT_GRAY);
            } else {
                g.setColor(Color.DARK_GRAY);
            }

            g.fillPolygon(xs, ys, n);

            DrawableLine orientationLine = new DrawableLine(robot.getFacingLine(), c);
            orientationLine.draw(g, scaler);

            g.setColor(c);
            g.fillPolygon(new int[] { xs[2], xs[3], scaler.m2PX(x) }, new int[] { ys[2], ys[3],
                    scaler.m2PY(y) }, 3);
        }

        // Convert meters into pixels and draws line
        private void drawLineTransformMeters(Graphics g, float x1, float y1, float x2, float y2) {

            g.drawLine(scaler.m2PX(x1), scaler.m2PY(y1), scaler.m2PX(x2), scaler.m2PY(y2));

        }

        private void drawGoals(Graphics g) {
            drawGoal(g, Color.red, getSnapshot().getOwnGoal());
            drawGoal(g, Color.green, getSnapshot().getOpponentsGoal());
        }

        private void drawGoal(Graphics g, Color c, Goal goal) {
            g.setColor(c);

            // Daniel: Does hardcoding the positions matter?
            int xMin, width, yMax, height;
            xMin = scaler.m2PX(goal.getMinX()) - 2;
            width = scaler.m2PX(goal.getMaxX()) - scaler.m2PX(goal.getMinX());
            yMax = scaler.m2PY(goal.getMaxY()) - 2;
            height = scaler.m2PY(goal.getMinY()) - scaler.m2PY(goal.getMaxY());

            g.drawRect(xMin, yMax, width, height);
        }

        /**
         * Where the mouse was last detected on screen.
         */
        private java.awt.Point mouse;

        private void drawMousePos(Graphics g) {
            if (mouse != null) {
                g.setColor(Color.RED);

                double x = scaler.pX2m(mouse.getX()), y = scaler.pY2m(mouse.getY());
                String s = String.format("Mouse Position (%.3f,%.3f)", x, y);

                g.drawString(s, 10, 10);
            }
        }

        @Override
        public void mouseDragged(MouseEvent arg0) {
            mouse = arg0.getPoint();
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
            mouse = arg0.getPoint();
        }

    }

    private void redrawFPS() {
        long age = getFPSAge();
        double fpsCount = getFPS();

        String s = String.format("%1$5.3f", fpsCount);

        double timePerFrame = 1000.0 / fpsCount;
        if ((fpsCount > 0) && (age < timePerFrame * 1.5)) {
			fpsVision.setForeground(new Color(50, 150, 50));
            fpsWarning.setVisible(false);
        } else if ((fpsCount > 0) && (age < timePerFrame * 3)) {
			fpsVision.setForeground(Color.ORANGE);
            fpsWarning.setVisible(false);
        } else {
			fpsVision.setForeground(Color.RED);
            fpsWarning.setVisible(true);
        }

		fpsVision.setText(s);
    }

    @Override
    protected void actionOnStep() {
        redrawFPS();
    }

    @Override
    protected void actionOnChange() {
        screen.repaint();
    }

}
