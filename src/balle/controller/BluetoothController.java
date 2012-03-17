package balle.controller;

import java.util.ArrayList;

import balle.bluetooth.Communicator;
import balle.bluetooth.messages.InvalidArgumentException;
import balle.bluetooth.messages.InvalidOpcodeException;
import balle.bluetooth.messages.MessageKick;
import balle.bluetooth.messages.MessageMove;
import balle.bluetooth.messages.MessageRotate;
import balle.bluetooth.messages.MessageStop;
import balle.brick.BrickController;
import balle.strategy.bezierNav.ControllerHistoryElement;

public class BluetoothController implements Controller {
    Communicator connection;

	protected ArrayList<ControllerListener> listeners = new ArrayList<ControllerListener>();

    public BluetoothController(Communicator communicator) {
        connection = communicator;
    }

    @Override
    public void floatWheels() {
        try {
            connection.send(new MessageStop(1).hash());
        } catch (InvalidOpcodeException e) {
            System.err
                    .println("Failed to send message FLOAT_WHEELS -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err
                    .println("Failed to send message FLOAT_WHEELS -- invalid argument");
        }
    }

    @Override
    public void stop() {
        try {
            connection.send(new MessageStop(0).hash());
			propogate(0, 0);
        } catch (InvalidOpcodeException e) {
            System.err.println("Failed to send message STOP -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err
                    .println("Failed to send message STOP -- invalid argument");
        }
    }

    @Override
    public void kick() {
        try {
            connection.send(new MessageKick(0).hash());
        } catch (InvalidOpcodeException e) {
            System.err.println("Failed to send message KICK -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err
                    .println("Failed to send message KICK -- invalid argument");
        }
    }

    /**
     * Calls controller.setWheelSpeeds(-speed, -speed);
     * 
     * @see balle.controller.Controller#forward(int)
     */
    @Override
    public void backward(int speed) {
        try {
            connection.send(new MessageMove(-speed, -speed).hash());
			propogate(-speed, -speed);
        } catch (InvalidOpcodeException e) {
            System.err
                    .println("Failed to send message BACKWARD -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err.println("Failed to send message BACKWARD(" + speed + ")"
                    + "-- invalid argument");
        }

    }

    /**
     * Calls controller.setWheelSpeeds(speed, speed);
     * 
     * @see balle.controller.Controller#forward(int)
     */
    @Override
    public void forward(int speed) {
        try {
            connection.send(new MessageMove(speed, speed).hash());
			propogate(speed, speed);
        } catch (InvalidOpcodeException e) {
            System.err
                    .println("Failed to send message FORWARD -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err.println("Failed to send message FORWARD(" + speed + ")"
                    + "-- invalid argument");
        }
    }

    @Override
    public void rotate(int degrees, int speed) {
        try {
            connection.send(new MessageRotate(degrees, speed).hash());
        } catch (InvalidOpcodeException e) {
            System.err
                    .println("Failed to send message ROTATE -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err.println("Failed to send message ROTATE(" + degrees
                    + ", " + speed + ")" + "-- invalid argument");
        }

    }

    @Override
    public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
        try {
            connection.send(new MessageMove(leftWheelSpeed, rightWheelSpeed)
                    .hash());
			propogate(leftWheelSpeed, rightWheelSpeed);
        } catch (InvalidOpcodeException e) {
            System.err
                    .println("Failed to send message SETWHEELSPEEDS -- invalid opcode");
        } catch (InvalidArgumentException e) {
            System.err.println("Failed to send message SETWHEELSPEEDS("
                    + leftWheelSpeed + ", " + rightWheelSpeed + ")"
                    + "-- invalid argument");
        }
    }

    @Override
    public int getMaximumWheelSpeed() {
        return BrickController.MAXIMUM_MOTOR_SPEED;
    }

    @Override
    public void penaltyKick() {
        try {
            connection.send(new MessageKick(1).hash());
        } catch (InvalidOpcodeException e) {
            System.err
                    .println("Failed to send message PENALTY_KICK -- invalid argument");
        } catch (InvalidArgumentException e) {
            System.err
                    .println("Failed to send message PENALTY_KICK -- invalid argument");
        }
    }

    @Override
    public boolean isReady() {
        return connection.isConnected();
    }

	@Override
	public void addListener(ControllerListener cl) {
		listeners.add(cl);
	}

	protected void propogate(int left, int right) {
		ControllerHistoryElement che = new ControllerHistoryElement(left,
				right, System.currentTimeMillis());
		for (ControllerListener cl : listeners)
			cl.commandSent(che);
	}

/*	@Override
	public void gentleKick(int speed, int angle) {
		// TODO Auto-generated method stub
		
	}*/

}
