package balle.bluetooth;

import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import balle.controller.BluetoothController;

public class TestSpin extends Frame implements KeyListener {

	/**
	 * 
	 */
	BluetoothController controller;
	int pl = 0;
	int pr = 100;
	int state = 0;
	long timer;
	double fiveTurnsDistance = 5 * 0.5105;

	public TestSpin() {
		super();
		addKeyListener(this);
		setSize(200, 100);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		controller = new BluetoothController(new Communicator());
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == ' ')
		{
			switch(state) {
			default:
				state = 0;
			case 0:
				System.out.println("Start Spinning.");
				controller.setWheelSpeeds(pl, pr);
				break;
			case 1:
				System.out.println("Start timer.");
				timer = System.currentTimeMillis();
				break;
			case 2:
				System.out.println("Stop! (5 turns).");
				long dt = System.currentTimeMillis() - timer; 
				controller.stop();
				System.out.println(pr + ", " + pl + ", " + dt + ", " + (1000*fiveTurnsDistance/dt));
				break;
			}
			state++;
		}
		else if (e.getKeyChar() == 'q')
		{
			pl += 10;
			if (pl > controller.getMaximumWheelSpeed()) pl = controller.getMaximumWheelSpeed();
			System.out.println("Power Left: " +  pl);
		}
		else if (e.getKeyChar() == 'a')
		{
			pl -= 10;
			if (pl < 0) pl = 0;
			System.out.println("Power Left: " +  pl);
		}
		else if (e.getKeyChar() == 'w')
		{
			pr += 10;
			if (pr > controller.getMaximumWheelSpeed()) pr = controller.getMaximumWheelSpeed();
			System.out.println("Power Left: " +  pr);
		}
		else if (e.getKeyChar() == 's')
		{
			pr -= 10;
			if (pr < 0) pr = 0;
			System.out.println("Power Left: " +  pr);
		}
	}

	public void keyReleased(KeyEvent e) {
		return;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestSpin();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
}
