package balle.strategy;

public class ConfusedException extends Exception {
	private static final long serialVersionUID = 2640531908864256322L;

	public ConfusedException() {
        super();
    }

    public ConfusedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfusedException(String message) {
        super(message);
    }

    public ConfusedException(Throwable cause) {
        super(cause);
    }

}
