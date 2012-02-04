package balle.bluetooth.messages;

public abstract class AbstractMessage {

    // Can have 4 opcodes only
    public final static int    BITS_FOR_OPCODE  = 2;
    public final static int    BITS_PER_INT     = 32;
    public final static int    MAX_OPCODE_VALUE = (int) Math.pow(2, BITS_FOR_OPCODE) - 1;

    public final static int    OPCODE           = -1;
    public final static String NAME             = "";

    public abstract int getOpcode();

    public abstract String getName();

    public abstract int hash() throws InvalidOpcodeException;

    /**
     * Function to hash the opcode of the message into correct place
     * 
     * @return
     * @throws InvalidOpcodeException
     */
    protected int hashOpcode() throws InvalidOpcodeException {
        int opcode = getOpcode();
        if (opcode < 0)
            throw new InvalidOpcodeException(
                    "Opcode < 0 given. Opcode is supposed to be unsigned int.");

        if (opcode > AbstractMessage.MAX_OPCODE_VALUE)
            throw new InvalidOpcodeException("Opcode " + opcode + " cannot be fit into "
                    + AbstractMessage.BITS_PER_INT + " bits");

        return opcode << (AbstractMessage.BITS_PER_INT - AbstractMessage.BITS_FOR_OPCODE);
    }

    public static final int extactOpcodeFromEncodedMessage(int message) {
        return message >>> (BITS_PER_INT - BITS_FOR_OPCODE);
    }

}
