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
	public void backward() {
		connection.send(Roboto.MESSAGE_BACKWARD);
	}

	@Override
	public void forward() {
		connection.send(Roboto.MESSAGE_FORWARD);

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
	public void rotate(int degrees) {
		connection.send(Roboto.MESSAGE_ROTATE);
		connection.send(degrees);
	}

	@Override
	public void travel(int dist) {
		System.out.println("IMPLEMENT");

	}

	@Override
	public void setSpeed(int speed) {
		 System.out.println("IMPLEMENT");

	}

	@Override
	public void kick() {
		connection.send(Roboto.MESSAGE_KICK);
	}

}
