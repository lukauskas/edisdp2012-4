package balle.bluetooth.messages;

/**
 * Represents an abstract type of message that takes only one argument
 * 
 * @author s0909773
 */
public abstract class ZeroArgsMessage extends AbstractMessage {

    @Override
    public abstract int getOpcode();

    @Override
    public int hash() {
        return getOpcode() << 32;
    }
}
