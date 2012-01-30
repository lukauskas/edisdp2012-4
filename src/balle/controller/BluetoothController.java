package balle.controller;

import balle.bluetooth.Communicator;
import balle.brick.Controller;
import balle.brick.Roboto;

public class BluetoothController implements Controller {
	Communicator connection;
   
	public BluetoothController(Communicator communicator)
	{
		connection = communicator;
	}
	@Override
	public void floatWheels() {
		System.out.println("IMPLEMENT");
		//connection.send(Roboto.MESSAGE_FLOAT_WHEELS);
	}

	@Override
	public void stop() {
		connection.send(Roboto.MESSAGE_STOP);
	}

	@Override
	public void travel(int dist) {
		System.out.println("IMPLEMENT");

	}

	@Override
	public void kick() {
		connection.send(Roboto.MESSAGE_KICK);
	}
	@Override
	public void backward(int speed) {
		connection.send(Roboto.MESSAGE_MOVE);
		connection.send(-speed);
		connection.send(-speed);
		
	}
	@Override
	public void forward(int speed) {
		connection.send(Roboto.MESSAGE_MOVE);
		connection.send(speed);
		connection.send(speed);
	}
	@Override
	public void rotate(int degrees, int speed) {
		connection.send(Roboto.MESSAGE_ROTATE);
		connection.send(degrees);
		connection.send(speed);
		
	}
	@Override
	public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
		connection.send(Roboto.MESSAGE_MOVE);
		connection.send(leftWheelSpeed);
		connection.send(rightWheelSpeed);		
	}
	@Override
	public int getMaximumWheelSpeed() {
		return 720;
	}
	@Override
	public void penaltyKick() {
		connection.send(Roboto.MESSAGE_PENALTY);		
	}

}
