package balle.bluetooth.messages;

public class MessageRotate extends AbstractTwoArgMessage {
    public static final int    OPCODE       = 3;
    protected static final int ANGLE_OFFSET = 360;
    public static final String NAME         = "ROTATE";

    public MessageRotate(int angle, int speed) throws InvalidArgumentException {
        super(angle + ANGLE_OFFSET, speed);
    }

    public int getAngle() {
        try {
            return getArgument(0) - ANGLE_OFFSET;
        } catch (InvalidArgumentException e) {
            // Should never happen
            return 0;
        }
    }

    public int getSpeed() {
        try {
            return getArgument(1);
        } catch (InvalidArgumentException e) {
            // Should never happen
            return 0;
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
