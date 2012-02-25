package balle.bluetooth.messages;

import balle.brick.BrickController;

public class MessageMove extends AbstractTwoArgMessage {

    public static final int    OPCODE = 2;
    // offset to add to speeds so we never see negative values
    protected final static int OFFSET = BrickController.MAXIMUM_MOTOR_SPEED;
    public static final String NAME   = "MOVE";

    /**
     * Message for the robot to move.
     * 
     * @param leftWheelSpeed
     *            the left wheel speed [-900;900]
     * @param rightWheelSpeed
     *            the right wheel speed [-900;900]
     * @throws InvalidArgumentException
     *             the invalid argument exception
     */

    public MessageMove(int leftWheelSpeed, int rightWheelSpeed)
            throws InvalidArgumentException {
        super(leftWheelSpeed + OFFSET, rightWheelSpeed + OFFSET);
    }

    public int getLeftWheelSpeed() {
        try {
            return getArgument(0) - OFFSET;
        } catch (InvalidArgumentException e) {
            // Should never happen!!!
            return 0; // Robot will just stop
        }
    }

    public int getRightWheelSpeed() {
        try {
            return getArgument(1) - OFFSET;
        } catch (InvalidArgumentException e) {
            // Should never happen!!!
            return 0; // Robot will just stop
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getOpcode() {
        return OPCODE;
    }

}
