package balle.strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import balle.controller.Controller;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class UserInputStrategy extends AbstractPlanner {

    private Screen screen;
    private int    mouseXPos = 10, mouseYPos = 4;

	protected float leftWheelPower = 0, rightWheelPower = 0;

	// Shouldn't keep references to the controller.
	// private Controller controller;

	protected boolean kick;

    @FactoryMethod(designator = "UserInput", parameterNames = {})
	public static UserInputStrategy gameFactory() {
		return new UserInputStrategy();
	}

    public UserInputStrategy() {
		MouseListener ml = new MouseListener();
		KeyListener kl = new KeyboardListener();

        JFrame frame = new JFrame("Controller");
        frame.setSize(500, 500);
		// V Annoying: frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        screen = new Screen();
        frame.getContentPane().add(BorderLayout.CENTER, screen);
		screen.addMouseMotionListener(ml);
		screen.addMouseListener(ml);
		screen.addKeyListener(kl);
    }

    // TODO: Saulius: I have removed aiStep() from strategy for now...
    // if you really need the functionality below please add it again
    // @Override
    // protected void aiStep() {
    // try {
    // Thread.sleep(100);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    @Override
    // TODO Change to rely on and react to JPanel thing
	public void onStep(Controller controller, Snapshot snapshot)
			throws ConfusedException {
		controller.setWheelSpeeds(Math.round(leftWheelPower) * 900,
				Math.round(rightWheelPower) * 900);

		if (kick)
			controller.kick();
        kick = false;
    }

    // Makes coordinates relative to robot
    // int dX = (mouseXPos - screen.getWidth()/2)
    // int dY = (screen.getHeight()-mouseYPos)-(screen.getHeight()/2) )

    // Used to get coordinates of where the user clicked
	class MouseListener implements MouseInputListener {

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            mouseXPos = screen.getWidth() / 2;
            mouseYPos = screen.getHeight() / 2;
            screen.repaint();
            leftWheelPower = 0;
            rightWheelPower = 0;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseXPos = e.getX();
            mouseYPos = e.getY();
            screen.repaint();
            int dX = (mouseXPos - screen.getWidth() / 2);
            int dY = (screen.getHeight() - mouseYPos)
                    - (screen.getHeight() / 2);
			leftWheelPower = leftWheelTurn(dX, dY);
			rightWheelPower = rightWheelTurn(dX, dY);

        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON3) {
				kick = true;
			}

        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        // Calculates the amount
        public float leftWheelTurn(int dX, int dY) {
			float abs = (float) Math.max(
					Math.sqrt((dX * dX) + (dY * dY)) / 400, 1.0);

            if (dX > 0 && dY > 0) {
				return abs * 1f;
            } else if (dX > 0 && dY < 0) {
				return abs * -1f;
            } else {
                float angleToClick = turningAngle(dX, dY);
                if (dY < 0) {
					return -angleToClick * abs;
                } else {
					return angleToClick * abs;
                }
            }
        }

        public float rightWheelTurn(int dX, int dY) {
			float abs = (float) Math.max(
					Math.sqrt((dX * dX) + (dY * dY)) / 400, 1.0);

            if (dX < 0 && dY > 0) {
				return abs * 1f;
            } else if (dX < 0 && dY < 0) {
				return abs * -1f;
            } else {
                float angleToClick = turningAngle(dX, dY);
                if (dY < 0) {
					return -angleToClick * abs;
                } else {
					return angleToClick * abs;
                }
            }
        }

        public float turningAngle(int dX, int dY) {
            double angle = Math.atan2(Math.abs(dY), Math.abs(dX));
            return (float) (((angle / (Math.PI / 2)) - 0.5) * 2);
        }

    }

	class KeyboardListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			int pressed = arg0.getKeyCode();
			if (pressed == KeyEvent.VK_SPACE) {
				System.out.println("pretend ive kicked");
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

    // Where the user clicks to control robot. Robot drawn in blue.
    // Mouse click shows in red
    @SuppressWarnings("serial")
    protected class Screen extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
            g.setColor(Color.WHITE);
            g.drawLine(0, screen.getHeight() / 2, screen.getWidth(),
                    screen.getHeight() / 2);
            g.drawLine(screen.getWidth() / 2, 0, screen.getWidth() / 2,
                    screen.getHeight());
            g.setColor(Color.BLUE);
            g.fillRect((screen.getWidth() / 2) - 45,
                    (screen.getHeight() / 2) - 50, 90, 100);
            g.setColor(Color.RED);
            g.fillOval(mouseXPos - 5, mouseYPos - 5, 10, 10);
        }

        public Screen() {

        }
    }
}
