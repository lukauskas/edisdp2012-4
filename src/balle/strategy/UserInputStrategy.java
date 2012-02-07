package balle.strategy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import balle.controller.Controller;
import balle.world.AbstractWorld;

public class UserInputStrategy extends AbstractStrategy {

	private Screen screen;
	private int mouseXPos = 10, mouseYPos = 4;
	private float leftWheelPower = 0, rightWheelPower = 0;

	public UserInputStrategy(Controller controller, AbstractWorld world) {
		super(controller, world);
		Listener l = new Listener();

		JFrame frame = new JFrame("Controller");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		screen = new Screen();
		frame.getContentPane().add(BorderLayout.CENTER, screen);
		screen.addMouseMotionListener(l);
		screen.addMouseListener(l);
	}

	@Override
	protected void aiStep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	// TODO Change to rely on and react to JPanel thing
	protected void aiMove(Controller controller) {
		System.out.println(leftWheelPower + " " + rightWheelPower);
		controller.setWheelSpeeds((int)Math.round(leftWheelPower),
				(int)Math.round(rightWheelPower));
		


	}

	// Makes coordinates relative to robot
	// int dX = (mouseXPos - screen.getWidth()/2)
	// int dY = (screen.getHeight()-mouseYPos)-(screen.getHeight()/2) )

	// Used to get coordinates of where the user clicked
	class Listener implements MouseInputListener {

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
			int dX = (mouseXPos - screen.getWidth()/2);
			int dY = (screen.getHeight()-mouseYPos)-(screen.getHeight()/2);
			leftWheelPower = leftWheelTurn(dX, dY) * 100;
			rightWheelPower = rightWheelTurn(dX, dY) * 100;
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		// Calculates the amount 
		public float leftWheelTurn(int dX, int dY) {
			if (dX > 0 && dY > 0) {
				return 1;
			} else if (dX > 0 && dY < 0) {
				return -1;
			} else {
				float angleToClick = turningAngle(dX,dY);
				if (dY < 0) {
					return -angleToClick;
				} else {
					return angleToClick;
				}
			}
		}
		
		public float rightWheelTurn(int dX, int dY) {
			if (dX < 0 && dY > 0) {
				return 1;
			} else if (dX < 0 && dY < 0) {
				return -1;
			} else {
				float angleToClick = turningAngle(dX,dY);
				if (dY < 0) {
					return -angleToClick;
				} else {
					return angleToClick;
				}
			}
		}
		
		public float turningAngle(int dX, int dY) {
			double angle = Math.atan2((double)Math.abs(dY),(double)Math.abs(dX));
			return (float) (((angle/(Math.PI/2)) -0.5)*2);
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
			g.drawLine(0, screen.getHeight()/ 2, screen.getWidth(), screen.getHeight()/2);
			g.drawLine(screen.getWidth()/2, 0, screen.getWidth()/2, screen.getHeight());
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
