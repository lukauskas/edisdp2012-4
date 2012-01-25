package balle.controller;

import balle.bluetooth.Communicator;
import balle.brick.Controller;
import balle.brick.BotCommunication;

public class BluetoothController implements Controller {
	Communicator connection;
   
	public BluetoothController(Communicator communicator)
	{
		connection = communicator;
	}
	@Override
	public void reverse() {
		connection.send(BotCommunication.MESSAGE_BACKWARD);
	}

	@Override
	public void go() {
		connection.send(BotCommunication.MESSAGE_FORWARD);

	}

	@Override
	public void floatWheels() {
		connection.send(BotCommunication.MESSAGE_FLOAT_WHEELS);

	}

	@Override
	public void stop() {
		connection.send(BotCommunication.MESSAGE_STOP);

	}

	@Override
	public void rotate(int deg) {
		System.out.println("IMPLEMENT");

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
		connection.send(BotCommunication.MESSAGE_KICK);
	}

}
