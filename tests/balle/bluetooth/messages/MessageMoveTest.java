package balle.bluetooth.messages;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageMoveTest {

    /**
     * Given correct speeds, messageMove should instantiate itself without
     * problems, and the wheel speeds should be returned correctly.
     * 
     * @throws InvalidArgumentException
     */
    @Test
    public void testMessageMove() throws InvalidArgumentException {
        MessageMove message1 = new MessageMove(-720, 720);
        assertEquals(-720, message1.getLeftWheelSpeed());
        assertEquals(720, message1.getRightWheelSpeed());

        MessageMove message2 = new MessageMove(720, -720);
        assertEquals(720, message2.getLeftWheelSpeed());
        assertEquals(-720, message2.getRightWheelSpeed());

        MessageMove message3 = new MessageMove(0, 300);
        assertEquals(0, message3.getLeftWheelSpeed());
        assertEquals(300, message3.getRightWheelSpeed());
    }

}
