package balle.bluetooth.messages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MessageStopTest {

    /**
     * Given a valid argument (0 or 1) MessageStop should be initialised
     * appropriately, return correct argument and should not throw any
     * exceptions
     * 
     * @throws InvalidArgumentException
     */
    @Test
    public void testMessageStopInitialisation() throws InvalidArgumentException {
        MessageStop message1 = new MessageStop(1);
        assertTrue(message1.floatWheels());

        MessageStop message2 = new MessageStop(0);
        assertFalse(message2.floatWheels());
    }

    /**
     * Given an invalid argument (say 2) MessageStop should raise an
     * InvalidArgumentException
     * 
     * @throws InvalidArgumentException
     */
    @Test(expected = InvalidArgumentException.class)
    public void testMessageStopInitialisationInvalidArgument() throws InvalidArgumentException {
        MessageStop message1 = new MessageStop(2);
    }
}
