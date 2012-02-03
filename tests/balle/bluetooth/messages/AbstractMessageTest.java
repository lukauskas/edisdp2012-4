package balle.bluetooth.messages;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AbstractMessageTest {

    /**
     * Given a hashed message, the static method extactOpcodeFromEncodedMessage
     * should return the message's opcode.
     */
    @Test
    public void testExtactOpcodeFromEncodedMessage() {

        // 111 00000000000000000000000000000
        assertEquals(7,
                AbstractMessage.extactOpcodeFromEncodedMessage(0xE0000000));

        // 011 00000001100100100010101010000
        assertEquals(3,
                AbstractMessage.extactOpcodeFromEncodedMessage(0x60324550));

        // 000 11111111111111111111111111111
        assertEquals(0,
                AbstractMessage.extactOpcodeFromEncodedMessage(0x1FFFFFFF));

    }

}
