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

    /**
     * NOTE: DO ROBOTS ALWAYS MOVE FORWARD !? NO, treat angle of velocity
     * different from angle the robot is facing.
     * 
     */
    @Override
    public void update(double yPosX, double yPosY, double yRad, double bPosX,
            double bPosY, double bRad, double ballPosX, double ballPosY,
            long timestamp) {
        Robot ours, them;
        FieldObject ball;

        // Coordinates
        Coord ourPosition, theirsPosition;
        // Orientations
        double ourOrientation, theirsOrientation;

        // Adjust based on our color.
        if (isBlue()) {
            ourPosition = new Coord(bPosX, bPosY);
            theirsPosition = new Coord(yPosX, yPosY);
            ourOrientation = bRad;
            theirsOrientation = yRad;
        } else {
            ourPosition = new Coord(yPosX, yPosY);
            theirsPosition = new Coord(bPosX, bPosY);
            ourOrientation = yRad;
            theirsOrientation = bRad;
        }

        // Ball position
        Coord ballPos = new Coord(ballPosX, ballPosY);

        Snapshot prev = getSnapshot();

        // First case when there is no past snapshot (assume velocities are 0)
        if (prev == null) {

            them = new Robot(theirsPosition, 0, 0, theirsOrientation);
            ours = new Robot(ourPosition, 0, 0, ourOrientation);
            ball = new FieldObject(ballPos, 0, 0);
        } else {
            // change in time
            long deltaT = timestamp - prev.getTimestamp();

            // Special case when we get two inputs with the same timestamp:
            if (deltaT == 0) {
                // This will just keep the prev world in the memory, not doing
                // anything
                return;
            }

            // Change in position
            Coord oursDPos, themDPos, ballDPos;
            oursDPos = (ourPosition).sub(prev.getBalle().getPosition());
            themDPos = (theirsPosition).sub(prev.getOpponent().getPosition());
            ballDPos = (ballPos).sub(prev.getBall().getPosition());

            // velocities
            double oursVel, themVel, ballVel;
            oursVel = oursDPos.abs() / deltaT;
            themVel = themDPos.abs() / deltaT;
            ballVel = ballDPos.abs() / deltaT;

            // angles (of velocity)
            double oursAngle, themAngle, ballAngle;
            // TODO: does this fit the angle conventions?
            oursAngle = Math.atan2(oursDPos.getY(), oursDPos.getX());
            themAngle = Math.atan2(oursDPos.getY(), oursDPos.getX());
            ballAngle = Math.atan2(oursDPos.getY(), oursDPos.getX());

            // put it all together (almost)
            them = new Robot(theirsPosition, themAngle, themVel, theirsOrientation);
            ours = new Robot(ourPosition, oursAngle, oursVel, ourOrientation);
            ball = new FieldObject(ballPos, ballAngle, ballVel);
        }

        synchronized (this) {
            // pack into a snapshot
            this.prev = new Snapshot(them, ours, ball, timestamp);
        }
    }
}
