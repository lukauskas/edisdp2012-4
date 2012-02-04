package balle.brick;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import balle.bluetooth.messages.AbstractMessage;
import balle.bluetooth.messages.InvalidArgumentException;
import balle.bluetooth.messages.MessageKick;
import balle.bluetooth.messages.MessageMove;
import balle.bluetooth.messages.MessageRotate;
import balle.bluetooth.messages.MessageStop;
import balle.controller.Controller;

public class RobotoTest {
    Controller controller;

    @Before
    public void setUp() {
        controller = mock(Controller.class);

    }

    /**
     * Given a valid message kick, processMessage should be able to recognise
     * that this is a MessageKick and should call the controller to kick.
     */
    @Test
    public void testProcessMessageCanRecogniseMessageKick() {
        AbstractMessage message = null;
        try {
            message = new MessageKick(0);
        } catch (InvalidArgumentException e) {
            // Shouldn't happen
        }

        assertTrue(Roboto.processMessage(message, controller));
        verify(controller).kick();
    }

    /**
     * Given a valid message kick, processMessage should be able to recognise
     * that this is a MessageKick with isPenalty=1 and should call the
     * controller to penalty kick.
     */
    @Test
    public void testProcessMessageCanRecogniseMessagePenaltyKick() {
        AbstractMessage message = null;
        try {
            message = new MessageKick(1);
        } catch (InvalidArgumentException e) {
            // Shouldn't happen
        }

        assertTrue(Roboto.processMessage(message, controller));
        verify(controller).penaltyKick();
    }

    /**
     * Given a valid message stop, processMessage should be able to recognise
     * that this is a MessageStop and should call the controller to stop.
     */
    @Test
    public void testProcessMessageCanRecogniseMessageStop() {
        AbstractMessage message = null;
        try {
            message = new MessageStop(0);
        } catch (InvalidArgumentException e) {
            // Shouldn't happen
        }

        assertTrue(Roboto.processMessage(message, controller));
        verify(controller).stop();
    }

    /**
     * Given a valid message stop, processMessage should be able to recognise
     * that this is a MessageStop with floatWheels=1 and should call the
     * controller to float wheels
     */
    @Test
    public void testProcessMessageCanRecogniseMessageStopFloatWheels() {
        AbstractMessage message = null;
        try {
            message = new MessageStop(1);
        } catch (InvalidArgumentException e) {
            // Shouldn't happen
        }

        assertTrue(Roboto.processMessage(message, controller));
        verify(controller).floatWheels();
    }

    /**
     * Given a valid message rotate, processMessage should be able to recognise
     * that this is a MessageRotate and should call the controller to rotate
     * with these exact parameters
     */
    @Test
    public void testProcessMessageCanRecogniseMessageRotate() {
        AbstractMessage message = null;
        try {
            message = new MessageRotate(180, 70);
        } catch (InvalidArgumentException e) {
            // Shouldn't happen
        }

        assertTrue(Roboto.processMessage(message, controller));
        verify(controller).rotate(180, 70);
    }

    /**
     * Given a valid message move, processMessage should be able to recognise
     * that this is a MessageMove and should call the controller to set the
     * correct wheel speeds
     */
    @Test
    public void testProcessMessageCanRecogniseMessageMove() {
        AbstractMessage message = null;
        try {
            message = new MessageMove(500, 600);
        } catch (InvalidArgumentException e) {
            // Shouldn't happen
        }

        assertTrue(Roboto.processMessage(message, controller));
        verify(controller).setWheelSpeeds(500, 600);
    }
}
