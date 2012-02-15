package balle.brick;

import java.io.DataInputStream;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import balle.bluetooth.messages.AbstractMessage;
import balle.bluetooth.messages.MessageDecoder;

public class TestThroughput {

    public static final int MESSAGES_TO_RECEIVE = 500;

    public static void main(String[] args) {
        while (true) {
            // Enter button click will halt the program
            if (Button.ENTER.isPressed())
                break;

            drawMessage("Connecting...");

            BTConnection connection = Bluetooth.waitForConnection();

            drawMessage("Connected");

            DataInputStream input = connection.openDataInputStream();

            MessageDecoder decoder = new MessageDecoder();

            long firstMessage = 0;
            long lastMessage = 0;
            int receivedMessages = 0;
            mainLoop: while (true) {
                // Enter button click will halt the program
                if (Button.ENTER.isPressed()) {
                    break;
                }
                try {
                    // no input available
                    while (input.available() == 0) {
                        // Enter button click will halt the program
                        if (Button.ENTER.isPressed()) {
                            break mainLoop;
                        }
                    }

                    int hashedMessage = input.readInt();
                    if (receivedMessages == 0)
                        firstMessage = System.currentTimeMillis();

                    AbstractMessage message = decoder
                            .decodeMessage(hashedMessage);
                    if (message == null) {
                        drawMessage("Could not decode: " + hashedMessage);
                        Thread.sleep(5000);
                    }
                    String name = message.getName();

                    receivedMessages++;
                    drawMessage(Integer.toString(receivedMessages));

                    if (receivedMessages == MESSAGES_TO_RECEIVE) {
                        lastMessage = System.currentTimeMillis();
                        drawMessage("Time taken: "
                                + (lastMessage - firstMessage));
                        Thread.sleep(5000);
                        return;
                    }

                } catch (Exception e1) {
                    drawMessage("Error in MainLoop: " + e1.getMessage());
                }
            }

            connection.close();
        }
    }

    private static void drawMessage(String message) {
        LCD.clear();
        LCD.drawString(message, 0, 0);
        LCD.refresh();
    }
}
