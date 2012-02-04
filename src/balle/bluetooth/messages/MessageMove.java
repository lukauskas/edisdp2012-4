package balle.bluetooth.messages;

public class MessageMove extends AbstractTwoArgMessage {

    protected static int       OPCODE = 2;
    protected final static int OFFSET = 720; // offset to add to speeds so we
                                             // never see negative values

    /**
     * Message for the robot to move.
     * 
     * @param leftWheelSpeed
     *            the left wheel speed [-720;720]
     * @param rightWheelSpeed
     *            the right wheel speed [-720;720]
     * @throws InvalidArgumentException
     *             the invalid argument exception
     */

    public MessageMove(int leftWheelSpeed, int rightWheelSpeed) throws InvalidArgumentException {
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

}
