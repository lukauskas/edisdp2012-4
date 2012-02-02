package balle.bluetooth.messages;

public abstract class AbstractMessage {

    public final static int BITS_FOR_OPCODE = 3;

    public abstract int getOpcode();

    public abstract int hash();

    public static final int extactOpcodeFromEncodedMessage(int message) {
        return (message & 0xE000000) >> 19;
    }
}
