package balle.bluetooth.messages;

public abstract class AbstractMessage {

    public final static int BITS_FOR_OPCODE = 3;
    public final static int BITS_PER_INT    = 32;

    public abstract int getOpcode();

    public abstract int hash();

    public static final int extactOpcodeFromEncodedMessage(int message) {
        return message >>> (BITS_PER_INT - BITS_FOR_OPCODE);
    }
}
