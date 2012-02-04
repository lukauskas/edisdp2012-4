package balle.bluetooth.messages;

public class MessageKick extends AbstractSingleArgMessage {

    public MessageKick(int isPenalty) throws InvalidArgumentException {
        super(isPenalty);
        if ((isPenalty != 0) && (isPenalty != 1))
            throw new InvalidArgumentException("isPenalty should either be 0 or 1");

    }

    @Override
    public int getOpcode() {
        return 1;
    }

    public boolean isPenalty() {
        return (getArgument() == 1);
    }

}
