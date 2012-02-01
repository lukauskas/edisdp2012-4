package balle.world;

public class BasicWorld extends AbstractWorld {

    private Snapshot prev = null;

    public BasicWorld(boolean balleIsBlue) {
        super(balleIsBlue);
    }

    @Override
    public synchronized Snapshot getSnapshot() {
        return prev;
    }

    private Coord subtractOrNull(Coord a, Coord b) {
        if ((a == null) && (b == null))
            return null;
        else
            return a.sub(b);
    }

    private Coord estimatedPosition(Coord prevCoordinate, Velocity prevVelocity) {
        if (prevCoordinate == null)
            return null;
        else
            return new Coord(prevCoordinate.add(prevVelocity), true);
    }

    /**
     * NOTE: DO ROBOTS ALWAYS MOVE FORWARD !? NO, treat angle of velocity
     * different from angle the robot is facing.
     * 
     */
    @Override
    public void update(double yPosX, double yPosY, double yRad, double bPosX,
            double bPosY, double bRad, double ballPosX, double ballPosY,
            long timestamp) {
        Robot ours = null;
        Robot them = null;
        FieldObject ball = null;

        // Coordinates
        Coord ourPosition, theirsPosition;
        // Orientations
        double ourOrientation, theirsOrientation;

        // Adjust based on our color.
        if (isBlue()) {
            if ((bPosX == UNKNOWN_VALUE) || (bPosY == UNKNOWN_VALUE))
                ourPosition = null;
            else
                ourPosition = new Coord(bPosX, bPosY);

            ourOrientation = bRad;

            if ((yPosX == UNKNOWN_VALUE) || (yPosY == UNKNOWN_VALUE))
                theirsPosition = null;
            else
                theirsPosition = new Coord(yPosX, yPosY);

            theirsOrientation = yRad;
        } else {
            if ((yPosX == UNKNOWN_VALUE) || (yPosY == UNKNOWN_VALUE))
                ourPosition = null;
            else
                ourPosition = new Coord(yPosX, yPosY);

            ourOrientation = yRad;

            if ((bPosX == UNKNOWN_VALUE) || (bPosY == UNKNOWN_VALUE))
                theirsPosition = null;
            else
                theirsPosition = new Coord(bPosX, bPosY);

            theirsOrientation = bRad;
        }

        Coord ballPosition;
        // Ball position
        if ((bPosX == UNKNOWN_VALUE) || (bPosY == UNKNOWN_VALUE))
            ballPosition = null;
        else
            ballPosition = new Coord(ballPosX, ballPosY);

        Snapshot prev = getSnapshot();

        // First case when there is no past snapshot (assume velocities are 0)
        if (prev == null) {
            if ((theirsPosition != null)
                    && (theirsOrientation != UNKNOWN_VALUE))
                them = new Robot(theirsPosition, new Velocity(0, 0, 1),
                        theirsOrientation);
            if ((ourPosition != null) && (ourOrientation != UNKNOWN_VALUE))
                ours = new Robot(ourPosition, new Velocity(0, 0, 1),
                        ourOrientation);
            if (ballPosition != null)
                ball = new FieldObject(ballPosition, new Velocity(0, 0, 1));
        } else {
            // change in time
            long deltaT = timestamp - prev.getTimestamp();

            // Special case when we get two inputs with the same timestamp:
            if (deltaT == 0) {
                // This will just keep the prev world in the memory, not doing
                // anything
                return;
            }

            if (ourPosition == null)
                ourPosition = estimatedPosition(prev.getBalle().getPosition(),
                        prev.getBalle().getVelocity());
            if (theirsPosition == null)
                theirsPosition = estimatedPosition(prev.getOpponent()
                        .getPosition(), prev.getOpponent().getVelocity());
            if (ballPosition == null)
                ballPosition = estimatedPosition(prev.getBall().getPosition(),
                        prev.getBall().getVelocity());

            // Change in position
            Coord oursDPos, themDPos, ballDPos;
            oursDPos = subtractOrNull(ourPosition, prev.getBalle()
                    .getPosition());
            themDPos = subtractOrNull(theirsPosition, prev.getOpponent()
                    .getPosition());
            ballDPos = subtractOrNull(ballPosition, prev.getBall()
                    .getPosition());

            // velocities
            Velocity oursVel, themVel, ballVel;
            oursVel = oursDPos != null ? new Velocity(oursDPos, deltaT) : null;
            themVel = themDPos != null ? new Velocity(themDPos, deltaT) : null;
            ballVel = ballDPos != null ? new Velocity(ballDPos, deltaT) : null;

            // put it all together (almost)
            them = new Robot(theirsPosition, themVel, theirsOrientation);
            ours = new Robot(ourPosition, oursVel, ourOrientation);
            ball = new FieldObject(ballPosition, ballVel);
        }

        synchronized (this) {
            // pack into a snapshot
            this.prev = new Snapshot(them, ours, ball, timestamp);
        }
    }
}
