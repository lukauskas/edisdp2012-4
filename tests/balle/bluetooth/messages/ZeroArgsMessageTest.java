package balle.bluetooth.messages;

import static org.junit.Assert.fail;

import org.junit.Test;

class ZeroArgsMessageConcrete extends ZeroArgsMessage {

    public ZeroArgsMessage(int opcode) {

    }

    @Override
    public int getOpcode() {
        // TODO Auto-generated method stub
        return 0;
    }

}

public class ZeroArgsMessageTest {

    @Test
    public void testHash() {
        fail("Not yet implemented");
    }

}
