package balle.bluetooth.messages;

public abstract class AbstractTwoArgMessage extends AbstractMessage {

    private final int        arg1;
    private final int        arg2;

    private static final int BITS_PER_ARGUMENT = (AbstractMessage.BITS_PER_INT - AbstractMessage.BITS_FOR_OPCODE) / 2;

    private void validateArgument(int argument) throws InvalidArgumentException {
        if (argument < 0) {
            throw new InvalidArgumentException("Provided argument " + arg1
                    + " is < 0. Arguments should be unsigned. "
                    + " If you need a negative valued one, consider offsetting it "
                    + " so it is always positive.");

        } else if (argument > (int) Math.pow(2, BITS_PER_ARGUMENT) - 1) {
            throw new InvalidArgumentException("Provided argument " + arg1
                    + " exceeds number of bits per argument");
        }
    }

    public AbstractTwoArgMessage(int arg1, int arg2) throws InvalidArgumentException {
        this.arg1 = arg1;
        this.arg2 = arg2;

        // Check if everything is OK with the arguments
        validateArgument(arg1);
        validateArgument(arg2);

    }

    /**
     * Decode arguments from hash.
     * 
     * @param hash
     *            hashed version of command
     * @return int[] of arguments of structure {firstArgument, secondArgument}
     */
    public static int[] decodeArgumentsFromHash(int hash) {
        int firstArgument = (hash & 0x3FFF8000) >>> BITS_PER_ARGUMENT;
        int secondArgument = hash & 0x0007FFF;

        int[] result = { firstArgument, secondArgument };
        return result;

    }

    /**
     * Hashes arguments.
     * 
     * @return the int
     */
    protected int hashArguments() {
        return (arg1 << BITS_PER_ARGUMENT) | arg2;
    }

    @Override
    public int hash() throws InvalidOpcodeException {
        return hashOpcode() | hashArguments();
    }

    /**
     * Returns the argument specified by index
     * 
     * @param argumentIndex
     *            which argument to get
     * @return appropriate argument
     * @throws InvalidArgumentException
     */
    protected int getArgument(int argumentIndex) throws InvalidArgumentException {
        if (argumentIndex == 0)
            return this.arg1;
        else if (argumentIndex == 1)
            return this.arg2;
        else
            throw new InvalidArgumentException("Invalid argumentIndex: " + argumentIndex);
    }
}
