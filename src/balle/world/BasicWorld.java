package balle.world;

public class BasicWorld extends AbstractWorld {

    private Snapshot prev;

    public BasicWorld(DataReader visionInput, boolean balleIsBlue) {
        super(visionInput, balleIsBlue);
    }

    @Override
    public Snapshot getSnapshot() {
        return prev;
    }

    /**
     * NOTE: DO ROBOTS ALWAYS MOVE FORWARD !? NO, treat angle of velocity
     * different from angle the robot is facing.
     * 
     */
    @Override
    void interpret(double yPosX, double yPosY, double yRad, double bPosX,
            double bPosY, double bRad, double ballPosX, double ballPosY,
            long timestamp) {

        Robot ours, them;
        FieldObject ball;

        // Our Robot
        double oPosX, oPosY, oRad;

        // Their Robot
        double tPosX, tPosY, tRad;

        // Adjust based on our color.
        if (isBlue()) {
            oPosX = bPosX;
            oPosY = bPosY;
            oRad = bRad;
            tPosX = yPosX;
            tPosY = yPosY;
            tRad = yRad;
        } else {
            oPosX = yPosX;
            oPosY = yPosY;
            oRad = yRad;
            tPosX = bPosX;
            tPosY = bPosY;
            tRad = bRad;
        }

        // First case when there is no past snapshot (assume velocities are 0)
        if (prev == null) {
            them = new Robot(new Coord(tPosX, tPosY), 0, 0, tRad);
            ours = new Robot(new Coord(oPosX, oPosY), 0, 0, oRad);
            ball = new FieldObject(new Coord(ballPosX, ballPosY), 0, 0);
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
            oursDPos = (new Coord(oPosX, oPosY)).sub(prev.getBalle()
                    .getPosition());
            themDPos = (new Coord(tPosX, tPosY)).sub(prev.getOpponent()
                    .getPosition());
            ballDPos = (new Coord(ballPosX, ballPosY)).sub(prev.getBall()
                    .getPosition());

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
            them = new Robot(new Coord(tPosX, tPosY), themAngle, themVel, tRad);
            ours = new Robot(new Coord(oPosX, oPosY), oursAngle, oursVel, oRad);
            ball = new FieldObject(new Coord(ballPosX, ballPosY), ballAngle,
                    ballVel);
        }

        // pack into a snapshot
        prev = new Snapshot(them, ours, ball, timestamp);
    }

}
