package balle.brick;

import lejos.nxt.Motor;
import lejos.robotics.navigation.TachoPilot;
import balle.controller.Controller;

/**
 * The Control class. Handles the actual driving and movement of the bot, once
 * BotCommunication has processed the commands.
 * 
 * That is -- defines the behaviour of the bot when it receives the command.
 * 
 * Adapted from SDP2011 groups 10 code -- original author shearn89
 * 
 * @author sauliusl
 */
public class BrickController implements Controller {
    TachoPilot         pilot;
    public int         maxPilotSpeed       = 600;    // 20 for friendlies

    public final Motor LEFT_WHEEL          = Motor.B;
    public final Motor RIGHT_WHEEL         = Motor.C;
    public final Motor KICKER              = Motor.A;

    public final float WHEEL_DIAMETER      = 81.6f;  // milimetres
                                                      // milimetres
    public final float TRACK_WIDTH         = 130f;

    public final int   MAXIMUM_MOTOR_SPEED = 720;

    public BrickController() {

        pilot = new TachoPilot(WHEEL_DIAMETER, TRACK_WIDTH, LEFT_WHEEL,
                RIGHT_WHEEL);
        pilot.setMoveSpeed(maxPilotSpeed);
        pilot.setTurnSpeed(45); // 45 has been working fine.
        pilot.regulateSpeed(true);
        LEFT_WHEEL.regulateSpeed(true);
        RIGHT_WHEEL.regulateSpeed(true);
        LEFT_WHEEL.smoothAcceleration(true);
        RIGHT_WHEEL.smoothAcceleration(true);
        KICKER.smoothAcceleration(false);
        KICKER.regulateSpeed(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#floatWheels()
     */
    @Override
    public void floatWheels() {
        LEFT_WHEEL.flt();
        RIGHT_WHEEL.flt();
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#stop()
     */
    @Override
    public void stop() {
        pilot.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see balle.brick.Controller#kick()
     */
    @Override
    public void kick() {
        KICKER.setSpeed(900);
        KICKER.resetTachoCount();
        KICKER.rotateTo(60);
        KICKER.rotateTo(0);
    }

    public float getTravelDistance() {
        return pilot.getTravelDistance();
    }

    public void reset() {
        pilot.reset();
    }

    private void setMotorSpeed(Motor motor, int speed) {
        boolean forward = true;
        if (speed < 0) {
            forward = false;
            speed = -1 * speed;
        }

        motor.setSpeed(speed);
        if (forward)
            motor.forward();
        else
            motor.backward();
    }

    @Override
    public void setWheelSpeeds(int leftWheelSpeed, int rightWheelSpeed) {
        if (leftWheelSpeed > MAXIMUM_MOTOR_SPEED)
            leftWheelSpeed = MAXIMUM_MOTOR_SPEED;
        if (rightWheelSpeed > MAXIMUM_MOTOR_SPEED)
            rightWheelSpeed = MAXIMUM_MOTOR_SPEED;

        setMotorSpeed(LEFT_WHEEL, leftWheelSpeed);
        setMotorSpeed(RIGHT_WHEEL, rightWheelSpeed);
    }

    @Override
    public int getMaximumWheelSpeed() {
        return MAXIMUM_MOTOR_SPEED;
    }

    @Override
    public void backward(int speed) {
        pilot.setMoveSpeed(speed);
        pilot.backward();
    }

    @Override
    public void forward(int speed) {
        pilot.setMoveSpeed(speed);
        pilot.forward();

    }

    @Override
    public void rotate(int deg, int speed) {
        pilot.setTurnSpeed(speed);
        pilot.rotate(deg);
    }

    @Override
    public void penaltyKick() {
        int turnAmount = 28;
        if (Math.random() <= 0.5)
            turnAmount *= -1;
        rotate(turnAmount, 180);
        kick();

    }

    @Override
    public boolean isReady() {
        return true;
    }

}
