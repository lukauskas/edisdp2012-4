import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;

/* Need to implement kick encoding.
 * Also need to implement speech control.
 * 	Alex Shearn
 */

public class BotCommunication 
{	
	String connected = "Connected";
	String waiting = "Waiting...";
	Pilot pilot;
	Controller control;
	DataInputStream dis;
	DataOutputStream dos;
	int command;
	Boolean success;
	Sensors sensor;
//	Defend defense;
	
	public static final int MESSAGE_FORWARD = 1;
	public static final int MESSAGE_BACKWARD = 2;
	public static final int MESSAGE_STOP = 3;
	public static final int MESSAGE_KICK = 5;
	public static final int MESSAGE_FLOAT_WHEELS = 6;
	public static final int MESSAGE_DEFEND = 7;
		
	
	/**
	 * Constructor method. Takes robot dimensions as arguments, sets the pilot object.
	 */
	public BotCommunication(){
		this.control = new BrickController();
	}
	
	/**
	 * The wait method. Takes two streams as input. Reads integer from the server,
	 * processes it, and then writes out to the server with a 0 or 1 for. It then
	 * loops again. Escapable by sending a -1 to the robot from the server.
	 * @throws IOException Thrown if there's a failure either on rserver.send(2);eading the input
	 * stream or on writing to output stream.
	 */
	boolean listen() throws IOException{
		LCD.clear();
		LCD.drawString("Getting command", 0, 0);
		LCD.refresh();
		command = dis.readInt();
		success = process(command);
		if (success){
			dos.writeInt(0);
			dos.flush();
			System.gc();
			return true;
		} else {
			dos.writeInt(1);
			dos.flush();
			return false;
		}
	}
	/**
	 * Processes the integer command from wait().
	 * @param n The integer given.
	 * @return Whether the command passed or failed. False if invalid.
	 */
	Boolean process(int n){
		/* 1 = go
		 * 2 = stop
		 * 3 = accelerate on
		 * 4 = accelerate off
		 * 5 = kick
		 * 6 = float motors.
		 * 7 = defend mode.
		 * anything between -360 and 360: rotate
		 * anything over 100,000 - travel (n-110,000)
		 * anything else - speed, multiplied by 100 (up to 900, ie 90,000)
		 */
		if(!sensor.listening){
			// rotating due to sensor input, so ignore this command.
			return true;
		}
		if (n == MESSAGE_FORWARD){
			control.go();
			return true;
		} 
		else if (n == MESSAGE_BACKWARD) 
	    {
			control.reverse();
	    }
	    else if (n == MESSAGE_STOP){
			control.stop();
			return true;
//		} else if (n == 3){
//			control.ballCarrier = true;
//			// pilot.setTurnSpeed(20);
//			return true;
//		} else if (n == 4){
//			control.ballCarrier = false;
//			// pilot.setTurnSpeed(45);
//			return true;
		} else if (n == MESSAGE_KICK){
			control.kick();
			return true;
		} else if (n == MESSAGE_FLOAT_WHEELS){
			control.floatWheels();
			return true;
		} else if (n == MESSAGE_DEFEND){
			control.setSpeed(15);
			control.travel(10);
			control.travel(-10);
			control.stop();
			//control.setSpeed(control.maxSpeed);
			return true;
		} else if (n >= -360 && n <= 360){
			control.rotate(n);
			return true;
		} else if (n >= 100000){
			int dist = n-110000;
			// say 10, goes 13.5
			int a = (int) (dist*10.0/13.5);
			control.travel(a);
			return true;
		} else {
			int speed = n/100;
			control.setSpeed(speed);
			return true;
		}
	}
	
	/**
	 * The BlueTooth connection method. Connects, and returns the connection as an object.
	 */
	static BTConnection connect(){
		LCD.drawString("Waiting...", 0, 0);
		BTConnection btc = Bluetooth.waitForConnection();
		LCD.refresh();
		LCD.clear();
		LCD.drawString("Connected...", 0, 0);
		LCD.refresh();
		return btc;
	}
	
	/**
	 * The main method. Initializes objects, runs the wait loop.
	 * @param args (Command line) arguments.
	 * @throws Exception IOException from the bluetooth communications.
	 */
	public static void main(String [] args)  throws Exception {
		// Voicebox voice = new Voicebox();
		BTConnection btc = connect();
		BotCommunication bot = new BotCommunication();
		
		bot.sensor = new Sensors(bot.control);
		bot.sensor.start();
		// bot.defense = new Defend(bot.control);

		/* create streams */
		bot.dis = btc.openDataInputStream();
		bot.dos = btc.openDataOutputStream();
		
		/* The waiting loop */
		boolean listening = true;
		while(listening){
			try {
				LCD.clear();
				LCD.drawString("Listening...", 0, 0);
				LCD.refresh();
				listening = bot.listen();
			} catch (IOException e){
				bot.dos.close();
				bot.dis.close();
				btc.close();
			}
		}
		bot.dos.close();
		bot.dis.close();
		btc.close();
		try {
			bot.sensor.interrupt();
		} catch (Exception e){
			
		}
		
		try {
			Thread.sleep(1000);
		} catch (Exception e){
		}
	}
}
