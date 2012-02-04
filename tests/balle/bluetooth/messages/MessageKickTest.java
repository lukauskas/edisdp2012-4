package balle.bluetooth.messages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MessageKickTest {

    /**
     * Given a valid argument (0 or 1) testMessageKick should be initialised
     * appropriately, return correct argument and should not throw any
     * exceptions
     * 
     * @throws InvalidArgumentException
     */
    @Test
    public void testMessageKickInitialisation() throws InvalidArgumentException {
        MessageKick message1 = new MessageKick(1);
        assertTrue(message1.isPenalty());

        MessageKick message2 = new MessageKick(0);
        assertFalse(message2.isPenalty());
    }

    /**
     * Given an invalid argument (say 2) testMessageKick should raise an
     * InvalidArgumentException
     * 
     * @throws InvalidArgumentException
     */
    @Test(expected = InvalidArgumentException.class)
    public void testMessageKickInitialisationInvalidArgument() throws InvalidArgumentException {
        MessageKick message1 = new MessageKick(2);
    }
}
