package balle.bluetooth.messages;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageRotateTest {

    /**
     * Given valid angle and speed parameters, the message should be able to
     * initialise and store them in a correct way.
     * 
     * @throws InvalidArgumentException
     */
    @Test
    public void testMessageRotate() throws InvalidArgumentException {
        MessageRotate message1 = new MessageRotate(-360, 70);
        assertEquals(-360, message1.getAngle());
        assertEquals(70, message1.getSpeed());

        MessageRotate message2 = new MessageRotate(360, 50);
        assertEquals(360, message2.getAngle());
        assertEquals(50, message2.getSpeed());

        MessageRotate message3 = new MessageRotate(0, 30);
        assertEquals(0, message3.getAngle());
        assertEquals(30, message3.getSpeed());
    }

}
