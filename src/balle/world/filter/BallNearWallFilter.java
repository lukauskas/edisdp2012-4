package balle.world.filter;

import balle.misc.Globals;
import balle.world.Coord;
import balle.world.MutableSnapshot;
import balle.world.Snapshot;
import balle.world.objects.Ball;

/**
 * BallNearWallFilter.
 * 
 * Makes sure the ball is never too close to a wall
 */
public class BallNearWallFilter implements Filter {

    private static final double MIN_DIST_TO_WALL = Globals.ROBOT_WIDTH / 2;

    @Override
    public Snapshot filter(Snapshot s) {
        Ball ball = s.getBall();
        Coord currentPosition = ball.getPosition();
        if (currentPosition == null)
            return s;
        
        if (ball.isNearWall(s.getPitch(), MIN_DIST_TO_WALL)) {
            double minX = s.getPitch().getMinX() + MIN_DIST_TO_WALL;
            double maxX = s.getPitch().getMaxX() - MIN_DIST_TO_WALL;
            double minY = s.getPitch().getMinY() + MIN_DIST_TO_WALL;
            double maxY = s.getPitch().getMaxY() - MIN_DIST_TO_WALL;

            double newX = Math.max(currentPosition.getX(), minX);
            newX = Math.min(newX, maxX);

            double newY = Math.max(currentPosition.getY(), minY);
            newY = Math.min(newY, maxY);

            // Update ball position
            MutableSnapshot ms = s.unpack();
            ms.setBall(new Ball(new Coord(newX, newY), ball.getVelocity()));
            return ms.pack();
        } else
            return s;
    }

}
