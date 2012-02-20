package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.pFStrategy.PFPlanning;
import balle.strategy.pFStrategy.Point;
import balle.strategy.pFStrategy.Pos;
import balle.strategy.pFStrategy.RobotConf;
import balle.strategy.pFStrategy.Vector;
import balle.strategy.pFStrategy.VelocityVec;
import balle.world.AbstractWorld;
import balle.world.Snapshot;

public class PFNavigation extends AbstractStrategy {

    private static final Logger LOG   = Logger.getLogger(PFNavigation.class);

    double                      b     = 13.0f;                               // wheel
                                                                              // width
    double                      r     = 8.16f / 2;                           // wheel
                                                                              // diameter
    RobotConf                   conf  = new RobotConf(b, r);

    // PFPlanning plann = new PFPlanning(conf, 100000,
    // Double.MAX_VALUE, 0.05, 500.0);

    // TargetPower=16 works quite well.
    PFPlanning                  plann = new PFPlanning(conf, 1, 1, 16, 0.5);

    public PFNavigation(Controller controller, AbstractWorld world) {
        super(controller, world);
    }

    @Override
    protected void aiStep() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void aiMove(Controller controller) {
        // TODO Auto-generated method stub
        Snapshot snap = getSnapshot();

        if (snap != null) {

            Pos opponent = new Pos(new Point(snap.getOpponent().getPosition()
                    .getX(), snap.getOpponent().getPosition().getY()), snap
                    .getOpponent().getOrientation().radians());
            // Pos opponent = null;
            Pos initPos = new Pos(new Point(snap.getBalle().getPosition()
                    .getX(), snap.getBalle().getPosition().getY()), snap
                    .getBalle().getOrientation().radians());
            Point ball = new Point(snap.getBall().getPosition().getX(), snap
                    .getBall().getPosition().getY());
            VelocityVec res = plann.update(initPos, opponent, ball);
            LOG.trace("Left speed: " + Math.toDegrees(res.getLeft())
                    + "right speed: " + Math.toDegrees(res.getRight()));
            double resNorm = res.norm();

            double left, right;
            // If the speeds given are more than the maximum speeds allowed
            // Scale them
            if (resNorm > VelocityVec.MAXIMUM_NORM) {
                Vector newRes = res.mult(1 / res.norm()).mult(
                        VelocityVec.MAXIMUM_NORM);
                res = new VelocityVec(newRes.getX(), newRes.getY());
            }

            left = Math.toDegrees(res.getLeft());
            right = Math.toDegrees(res.getRight());
            LOG.trace("Left speed: " + left + "right speed: " + right);

            controller.setWheelSpeeds((int) left, (int) right);
        }
    }
}