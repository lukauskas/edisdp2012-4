package balle.strategy;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.pFStrategy.PFPlanning;
import balle.strategy.pFStrategy.Point;
import balle.strategy.pFStrategy.Pos;
import balle.strategy.pFStrategy.RobotConf;
import balle.strategy.pFStrategy.VelocityVec;
import balle.strategy.planner.AbstractPlanner;
import balle.world.Snapshot;

public class PFNavigation extends AbstractPlanner {

    private static final Logger LOG   = Logger.getLogger(PFNavigation.class);

    // Track width
    double                      b     = 0.013f;
    // Wheel radius
    double                      r     = 0.0816f / 2;
    RobotConf                   conf  = new RobotConf(b, r);

    // PFPlanning plann = new PFPlanning(conf, 100000,
    // Double.MAX_VALUE, 0.05, 500.0);

    // TargetPower=16 works quite well.
    PFPlanning                  plann = new PFPlanning(conf, 0.05, 0.4, 52, 80);

    @Override
	public void onStep(Controller controller, Snapshot snapshot) {
        // TODO Auto-generated method stub

        if (snapshot != null) {
            if (snapshot.getBalle().getPosition() == null)
                return;
            Pos opponent = new Pos(new Point(snapshot.getOpponent().getPosition().getX(), snapshot
                    .getOpponent().getPosition().getY()), snapshot.getOpponent().getOrientation()
                    .atan2styleradians());
            // Pos opponent = null;
            Pos initPos = new Pos(new Point(snapshot.getBalle().getPosition().getX(), snapshot.getBalle()
                    .getPosition().getY()), snapshot.getBalle().getOrientation().atan2styleradians());
            Point ball = new Point(snapshot.getBall().getPosition().getX(), snapshot.getBall()
                    .getPosition().getY());
            VelocityVec res = plann.update(initPos, opponent, ball);
            LOG.trace("Left speed: " + Math.toDegrees(res.getLeft()) + ", right speed: "
                    + Math.toDegrees(res.getRight()));

            double left, right;
            // If the speeds given are more than the maximum speeds allowed
            // Scale them
            res = res.scale();

            left = Math.toDegrees(res.getLeft());
            right = Math.toDegrees(res.getRight());
            LOG.trace("Left speed: " + left + " right speed: " + right);

            controller.setWheelSpeeds((int) left, (int) right);
        }
    }
}