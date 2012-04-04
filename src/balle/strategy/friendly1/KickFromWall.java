package balle.strategy.friendly1;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.ConfusedException;
import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.turning.FaceAngle;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.Point;
import balle.world.objects.StaticFieldObject;

public class KickFromWall extends GoToBall {

    private static final Logger LOG = Logger.getLogger(KickFromWall.class);

    boolean secondStep = false;
    boolean additionalStep = false;
    boolean kicked = false;
    boolean goingToBall = false;

    public KickFromWall(MovementExecutor movementStrategy) {
        super(movementStrategy);
        // TODO Auto-generated constructor stub
    }

    public Coord calculateNearWallCoord(Snapshot snapshot) {
        // TODO Auto-generated method stub

        if (snapshot == null)
            return null;
        Snapshot snap = snapshot;

        double targetX, targetY;
        boolean isBottom;

        if (snap.getOpponentsGoal().getPosition().getX() < snap.getPitch()
                .getMaxX() / 2) {
            if (snap.getBall().getPosition().getY() < snap.getPitch().getMaxY() / 2) {
                targetX = snap.getBall().getPosition().getX() + 0.4;
                targetY = snap.getBall().getPosition().getY() + 0.13;
                isBottom = true;
            } else {
                targetX = snap.getBall().getPosition().getX() + 0.4;
                targetY = snap.getBall().getPosition().getY() - 0.13;
                isBottom = false;
            }
        } else {
            if (snap.getBall().getPosition().getY() < snap.getPitch().getMaxY() / 2) {
                targetX = snap.getBall().getPosition().getX() - 0.4;
                targetY = snap.getBall().getPosition().getY() + 0.13;
                isBottom = true;
            } else {
                targetX = snap.getBall().getPosition().getX() - 0.4;
                targetY = snap.getBall().getPosition().getY() - 0.13;
                isBottom = false;
            }
        }

        Coord loc = new Coord(targetX, targetY);
        Coord loc2 = snap.getBall().getPosition();
        Coord loc3;

        if (isBottom) {
            loc3 = new Coord(snap.getBall().getPosition().getX(), snap
                    .getBall().getPosition().getY() + 0.3);
        } else {
            loc3 = new Coord(snap.getBall().getPosition().getX(), snap
                    .getBall().getPosition().getY() - 0.3);
        }

        LOG.trace(snap.getBalle().getPosition().dist(loc3));

        if (snap.getBalle().getPosition().dist(loc3) < 0.15)
            additionalStep = true;

        if (snap.getBalle().getPosition().dist(loc) < 0.15) {
            secondStep = true;
        }

        if (!secondStep) {
            LOG.info("Getting closer to the wall");

            if (Math.abs(snap.getBalle().getPosition().getY()
                    - snap.getBall().getPosition().getY()) < 0.3
                    && !additionalStep) {
                return loc3;
            } else {
                additionalStep = true;
                return loc;
            }
        } else {

            goingToBall = true;

            MovementExecutor strategy;

            if (snap.getBalle().getPosition()
                    .dist(snap.getBall().getPosition()) < 0.5) {
                LOG.info("Approaching the ball gently");
                strategy = new GoToObject(new FaceAngle());
                strategy.setStopDistance(0);
            } else {
                LOG.info("Approaching the ball with PFN");
                strategy = new GoToObjectPFN(0);
            }
            setExecutorStrategy(strategy);
            return loc2;
        }
    }

    @Override
    protected StaticFieldObject getTarget(Snapshot snapshot) {
        Coord nearWallCoord = calculateNearWallCoord(snapshot);
        return new Point(nearWallCoord);
    }

    @Override
    public void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {
        if (snapshot.getBall().isNear(snapshot.getBalle())
                && goingToBall)
            controller.kick();
        super.step(controller, snapshot);
    }

    @Override
    protected Color getTargetColor() {
        return Color.WHITE;
    }

}