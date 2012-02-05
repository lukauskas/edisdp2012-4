package balle.bluetooth.messages;

public class MessageStop extends AbstractSingleArgMessage {

    public static final int    OPCODE = 1;
    public static final String NAME   = "STOP";

    /**
     * Instantiates a new message stop.
     * 
     * @param floatWheels
     *            whether to float wheels or not. 1: floatWheels, 0: stop in
     *            place.
     * @throws InvalidArgumentException
     *             if argument provided is invalid
     */
    public MessageStop(int floatWheels) throws InvalidArgumentException {
        super(floatWheels);
        if ((floatWheels != 0) && (floatWheels != 1))
            throw new InvalidArgumentException("floatWheels should either be 0 or 1");
    }

    /**
     * Returns whether the robot should stop in place or "float the wheels" and
     * roll freely until stop.
     * 
     * @return true if the robot should float wheels
     */
    public boolean floatWheels() {
        return getArgument() == 1;
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
