package balle.bluetooth.messages;

public class MessageKick extends AbstractSingleArgMessage {
    public static final int OPCODE = 0;

    public MessageKick(int isPenalty) throws InvalidArgumentException {
        super(isPenalty);
        if ((isPenalty != 0) && (isPenalty != 1))
            throw new InvalidArgumentException("isPenalty should either be 0 or 1");

    }

    public boolean isPenalty() {
        return (getArgument() == 1);
    }

}
