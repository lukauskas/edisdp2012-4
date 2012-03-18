package balle.simulator;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import balle.strategy.bezierNav.ControllerHistoryElement;
import balle.world.AngularVelocity;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.Velocity;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;

public class SnapshotPredictor extends WorldSimulator {

    // Recreate object if you want to update the history
    private final ArrayList<ControllerHistoryElement> controllerHistory;
    private long                                      simulatorTimestamp;

    private final Goal                                opponentsGoal;
    private final Goal                                ownGoal;

    private final Pitch                               pitch;

    private final Logger LOG = Logger.getLogger(SnapshotPredictor.class);

    /**
     * Create new snapshot predictor
     * 
     * @param controllerHistory
     */

    public SnapshotPredictor(Snapshot initialSnapshot,
            ArrayList<ControllerHistoryElement> controllerHistory) {
        this(initialSnapshot.getOpponent(), initialSnapshot.getBalle(), initialSnapshot.getBall(),
                initialSnapshot.getOpponentsGoal(), initialSnapshot.getOwnGoal(), initialSnapshot
                        .getPitch(), initialSnapshot.getTimestamp(), controllerHistory);
    }

    /**
     * Create new snapshot predictor
     * 
     * @param controllerHistory
     */

    public SnapshotPredictor(balle.world.objects.Robot opponent, balle.world.objects.Robot balle,
            Ball ball, Goal opponentsGoal, Goal ownGoal, Pitch pitch, long timestamp,
            ArrayList<ControllerHistoryElement> controllerHistory) {
        super(false);
        setWorld(new World(new Vec2(), true));
        initWorld();
        setVisionDelay(0);
        this.simulatorTimestamp = timestamp;
        this.controllerHistory = controllerHistory;
        this.opponentsGoal = opponentsGoal;
        this.ownGoal = ownGoal;
        this.pitch = pitch;
        updatePositions(opponent, balle, ball);
    }

    /**
     * Roll the world simulation forward for some delta.
     * 
     * @param delta
     *            how much time to move forwards
     */
    protected synchronized void simulate(long delta) {
        long startTime = simulatorTimestamp;
        long endTime = simulatorTimestamp + delta;

        // TODO: add the wheelspeeds again
        // clean up the history (ensure there is at least one element left in
        // history)
        while (controllerHistory.size() > 1 && controllerHistory.get(0).getTimestamp() < startTime) {
            controllerHistory.remove(0);
        }
        //
        // // setup a simulator using the current snapshot (assume we are blue)
        float lastLPower = 0, lastRPower = 0;
        if (controllerHistory.size() > 0) {
            ControllerHistoryElement lastState = controllerHistory.get(0);
            lastLPower = lastState.getPowerLeft();
            lastRPower = lastState.getPowerRight();
        }

        // Assume we are always blue
        SoftBot virtual = getBlueSoft();
        virtual.setWheelSpeeds((int) lastLPower, (int) lastRPower);

        // use the controllerHistory to simulate the wheelspeeds
        // run a simulation while adjusting wheel speeds

        long maxTD = 50; // low values may make this less accurate
        for (int i = 0; i < controllerHistory.size(); i++) {
            ControllerHistoryElement curr = controllerHistory.get(i);
            long nextTime = endTime;

            if (i < controllerHistory.size() - 1)
                nextTime = controllerHistory.get(i + 1).getTimestamp();
            for (long tD = maxTD; startTime < nextTime; tD = Math.min(startTime + tD, nextTime)
                    - startTime) {
                // simulator.getBlueSoft().setWheelSpeeds(900, 900);
                virtual.setWheelSpeeds(curr.getPowerLeft(), curr.getPowerRight());
                update(tD);
                getWorld().step(tD / 1000f, 8, 3);
                // System.out.println(tD);
                // System.out
                // .println(world.getSnapshot().getBalle().getPosition());
                startTime += tD;
            }
        }



        // Finish simulation off if we run out of controller commands
        if (startTime < endTime) {
            getWorld().step((endTime - startTime) / 1000f, 8, 3);
            // TODO: this does not seem to simulate the ball movement if it is
            // not
            // moved by the other robots.
            // WTF?
        }
        // Update the timestamp
        simulatorTimestamp = endTime;
    }

    private void updatePositions(balle.world.objects.Robot opponent,
            balle.world.objects.Robot ourRobot, balle.world.objects.Ball ball) {

        balle.world.objects.Robot yRobot, bRobot;
        // Assume we are blues
        yRobot = opponent;
        bRobot = ourRobot;

        // Can see blue robot.
        if (bRobot.getPosition() != null) {
            blue.setPosition(bRobot.getPosition().mult(SCALE), bRobot.getOrientation());

            blue.getBody().setLinearVelocity(bRobot.getVelocity().vec2(SCALE));
        } else {
            // Place blue robot miles off the pitch.
            // TODO: I DON'T LIKE THIS -- find a way to fix it - Saulius
            blue.setPosition(new Coord(0, -100), new Orientation(0));
            blue.getBody().setLinearVelocity(new Vec2(0, 0));
        }

        // Can see yellow robot.
        if (yRobot.getPosition() != null) {
            yellow.setPosition(yRobot.getPosition().mult(SCALE), yRobot.getOrientation());
            yellow.getBody()
                    .setLinearVelocity(yRobot.getVelocity().vec2(SCALE));
        } else {
            // Place yellow robot miles off the pitch.
            // TODO: I DON'T LIKE THIS -- find a way to fix it - Saulius
            yellow.setPosition(new Coord(0, -100), new Orientation(0));
            yellow.getBody().setLinearVelocity(new Vec2(0, 0));
        }
        setBallPosition(ball.getPosition().mult(SCALE), ball.getVelocity());
    }

    public Snapshot getSnapshotAfterTime(long deltaTime) {
        if (deltaTime > 0) {
            simulate(deltaTime);
        }
        return getSnapshot();
    }

    private Coord v2C(Vec2 v) {
        return new Coord(v.x, v.y);
    }

    private balle.world.objects.Robot getRobotFromBody(Body robot)
    {
        return new balle.world.objects.Robot(v2C(robot.getPosition()).div(SCALE), new Velocity(v2C(
                robot.getLinearVelocity()).div(SCALE), 1000), new AngularVelocity(
                robot.getAngularVelocity(), 1000), new Orientation(robot.getAngle()));
    }

    /**
     * Returns snapshot of the current state of the simulator
     * 
     * @return the snapshot
     */
    private Snapshot getSnapshot() {
        Body ballBody = ball;
        
        // Assume we are blue and opponent is yellow again
        balle.world.objects.Robot ourRobot = getRobotFromBody(getBlueSoft().getBody());
        balle.world.objects.Robot opponent = getRobotFromBody(getYellowSoft().getBody());
      
        balle.world.objects.Ball ball = new Ball(v2C(ballBody.getPosition())
                .div(SCALE), Velocity.fromVec2(ballBody.getLinearVelocity(),
                SCALE));
        
        return new Snapshot(opponent, ourRobot, ball, getOpponentsGoal(), getOwnGoal(), getPitch(),
                getSimulatorTimestamp(), getControllerHistory());
    }

    protected ArrayList<ControllerHistoryElement> getControllerHistory() {
        return controllerHistory;
    }

    public long getSimulatorTimestamp() {
        return simulatorTimestamp;
    }

    public void setSimulatorTimestamp(long simulatorTimestamp) {
        this.simulatorTimestamp = simulatorTimestamp;
    }

    protected Goal getOpponentsGoal() {
        return opponentsGoal;
    }

    protected Goal getOwnGoal() {
        return ownGoal;
    }

    protected Pitch getPitch() {
        return pitch;
    }

}
