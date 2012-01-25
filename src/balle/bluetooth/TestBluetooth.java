package balle.bluetooth;

import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import balle.brick.Controller;
import balle.controller.BluetoothController;

public class TestBluetooth extends Frame implements KeyListener {

	/**
	 * 
	 */
	TextField t1;
	Controller controller;

	public TestBluetooth() {
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
		if (e.getKeyChar() == 'w')
		{
			System.out.println("Forward!");
			controller.forward();
		}
		else if (e.getKeyChar() == 's')
		{
			System.out.println("Backward!");
			controller.backward();
		}		
		else if (e.getKeyChar() == 'd')
		{
			System.out.println("Right");
			controller.rotate(45);
		}
		else if (e.getKeyChar() == 'a')
		{
			System.out.println("Left");
			controller.rotate(-45);
		}
		else if (e.getKeyChar() == 'x')
		{
			System.out.println("Kick!");
			controller.kick();
		}
		else if (e.getKeyChar() == 'q')
		{
			System.out.println("Stop!");
			controller.stop();
		}
		else
		{
			System.out.println("pressed: " + e.getKeyChar());
		}
	}

	public void keyReleased(KeyEvent e) {
		return;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestBluetooth();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
}
