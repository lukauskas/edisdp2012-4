package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.pFStrategy.PFPlanning;
import balle.strategy.pFStrategy.Point;
import balle.strategy.pFStrategy.Pos;
import balle.strategy.pFStrategy.RobotConf;
import balle.strategy.pFStrategy.VelocityVec;
import balle.world.AbstractWorld;
import balle.world.Snapshot;

public class PFNavigation extends AbstractStrategy {

    private static final Logger LOG   = Logger.getLogger(PFNavigation.class);

    double                      b     = 13.0f;                               // wheel
                                                                              // width
    double                      r     = 8.16f;                               // wheel
                                                                              // diameter
    RobotConf                   conf  = new RobotConf(b, r);

    // PFPlanning plann = new PFPlanning(conf, 100000,
    // Double.MAX_VALUE, 0.05, 500.0);

    PFPlanning                  plann = new PFPlanning(conf, 100000, 1000,
                                              0.04, 250000.0);

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
                    .getX() * 100,
                    snap.getOpponent().getPosition().getY() * 100), snap
                    .getOpponent().getOrientation().radians());
            // Pos opponent = null;
            Pos initPos = new Pos(new Point(snap.getBalle().getPosition()
                    .getX() * 100, snap.getBalle().getPosition().getY() * 100),
                    snap.getBalle().getOrientation().radians());
            Point ball = new Point(snap.getBall().getPosition().getX() * 100,
                    snap.getBall().getPosition().getY() * 100);
            VelocityVec res = plann.update(initPos, opponent, ball, false);
            double left = Math.toDegrees(res.getLeft());
            double right = Math.toDegrees(res.getRight());
            if (left > 700)
                left = 700;
            if (left < -700)
                left = -700;
            if (right > 700)
                right = 700;
            if (right < -700)
                right = -700;
            LOG.trace(opponent + " " + initPos + " " + ball + " " + left + " "
                    + right);
            controller.setWheelSpeeds((int) left, (int) right);
        }
    }

}