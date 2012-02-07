package balle.strategy;

import balle.controller.Controller;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.Robot;

public class GoToBall extends AbstractStrategy {

    private boolean             isMoving           = false;
    private final static double DISTANCE_THRESHOLD = 0.1;    // 10 cm?
    private final static double EPSILON            = 0.00001;

    public GoToBall(Controller controller, AbstractWorld world) {
        super(controller, world);
    }

    @Override
    protected void aiStep() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void aiMove(Controller controller) {
        // Assume ball is static
        Coord target = getSnapshot().getBall().getPosition();
        Robot robot = getSnapshot().getBalle();

        Coord currentPosition = robot != null ? robot.getPosition() : null;
        if ((target == null) || (currentPosition == null)) {
            System.out.println("Cannot see ball or self");
            return;
        }

        if ((currentPosition.dist(target) - DISTANCE_THRESHOLD) < EPSILON) {
            if (isMoving) {
                System.out.println("Target reached");
                controller.stop();
                isMoving = false;
            }
            return;
        } else {
            double angleToTarget = target.sub(currentPosition).orientation()
                    .radians();
            angleToTarget -= robot.getOrientation().radians();
            System.out.println("Angle to target: " + angleToTarget);
        }

    }
}
