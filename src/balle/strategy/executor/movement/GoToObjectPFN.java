package balle.strategy.executor.movement;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.pFStrategy.PFPlanning;
import balle.strategy.pFStrategy.Point;
import balle.strategy.pFStrategy.Pos;
import balle.strategy.pFStrategy.RectObject;
import balle.strategy.pFStrategy.RobotConf;
import balle.strategy.pFStrategy.VelocityVec;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

public class GoToObjectPFN implements MovementExecutor {
    private Snapshot            snapshot          = null;

    private static final Logger LOG               = Logger.getLogger(MovementExecutor.class);

    // cm
    private static final double ROBOT_TRACK_WIDTH = Globals.ROBOT_TRACK_WIDTH * 100;
    private static final double WHEEL_RADIUS      = Globals.ROBOT_WHEEL_DIAMETER * 100 / 2.0;

    private double              stopDistance;                                                // meters

    public double getStopDistance() {
        return stopDistance;
    }

    @Override
    public void setStopDistance(double stopDistance) {
        this.stopDistance = stopDistance;
    }

    private StaticFieldObject target;

    PFPlanning                plann;

    /**
     * Instantiates a new go to object executor that uses Potential Fields
     * Navigation
     * 
     * @param stopDistance
     *            distance to stop from (in meters)
     */
    public GoToObjectPFN(double stopDistance) {
        this.stopDistance = stopDistance;
        RobotConf conf = new RobotConf(ROBOT_TRACK_WIDTH, WHEEL_RADIUS);
        // plann = new PFPlanning(conf, 0, 1, 12, 0.5); // No opponent avoidance
        // Avoids opponent:
        plann = new PFPlanning(conf, 0.05, 0.4, 20, 32);

        // Add walls
        double wallPower = 0.01;
        RectObject leftWall = new RectObject(new Point(0, Globals.PITCH_HEIGHT), new Point(0, 0),
                wallPower, Double.MAX_VALUE);
        RectObject rightWall = new RectObject(new Point(Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT),
                new Point(Globals.PITCH_WIDTH, 0), wallPower, Double.MAX_VALUE);
        RectObject topWall = new RectObject(new Point(0, Globals.PITCH_HEIGHT), new Point(
                Globals.PITCH_WIDTH, Globals.PITCH_HEIGHT), wallPower, Double.MAX_VALUE);
        RectObject bottomWall = new RectObject(new Point(0, 0), new Point(Globals.PITCH_WIDTH, 0),
                wallPower, Double.MAX_VALUE);

        plann.addObject(leftWall);
        plann.addObject(rightWall);
        plann.addObject(topWall);
        plann.addObject(bottomWall);
    }

    @Override
    public void stop(Controller controller) {
        controller.stop();

    }

    @Override
    public void updateTarget(StaticFieldObject target) {
        this.target = target;

    }

    @Override
    public boolean isFinished() {
        if ((snapshot == null) || (target == null) || (target.getPosition() == null)
                || (snapshot.getBalle().getPosition() == null))
            return false;
        return target.getPosition().dist(snapshot.getBalle().getPosition()) <= getStopDistance();

    }

    @Override
    public boolean isPossible() {
        return ((snapshot != null) && (target != null) && (target.getPosition() != null)
                && (snapshot.getBalle().getPosition() != null) && (snapshot.getBalle()
                .getOrientation() != null));
    }

    @Override
    public void updateState(Snapshot snapshot) {
        this.snapshot = snapshot;

    }

    @Override
    public void step(Controller controller) {
        if (!isPossible())
            return;

        Pos opponent;
        if (snapshot.getOpponent().getOrientation() == null)
            opponent = null;
        else
            opponent = new Pos(new Point(snapshot.getOpponent().getPosition().getX(), snapshot
                    .getOpponent().getPosition().getY()), snapshot.getOpponent().getOrientation()
                    .radians());

        // Our pos
        Pos initPos = new Pos(new Point(snapshot.getBalle().getPosition().getX(), snapshot
                .getBalle().getPosition().getY()), snapshot.getBalle().getOrientation().radians());

        // Target pos
        Point targetLoc = new Point(target.getPosition().getX(), target.getPosition().getY());

        VelocityVec res = plann.update(initPos, opponent, targetLoc);
        LOG.trace("UNSCALED Left speed: " + Math.toDegrees(res.getLeft()) + " right speed: "
                + Math.toDegrees(res.getRight()));
        double left, right;

        res = res.scale();

        left = Math.toDegrees(res.getLeft());
        right = Math.toDegrees(res.getRight());
        LOG.trace("Left speed: " + left + " right speed: " + right);

        controller.setWheelSpeeds((int) left, (int) right);
    }

    @Override
    public ArrayList<Drawable> getDrawables() {
        return new ArrayList<Drawable>();
    }
}