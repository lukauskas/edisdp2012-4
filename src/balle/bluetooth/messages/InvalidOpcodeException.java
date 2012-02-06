package balle.bluetooth.messages;

/**
 * Exception that is raised when a message with an incorrect opcode is hashed
 * 
 * @author s0909773
 * 
 */
public class InvalidOpcodeException extends Exception {

    private static final long serialVersionUID = 2048667342561658918L;

    public InvalidOpcodeException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public InvalidOpcodeException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}
