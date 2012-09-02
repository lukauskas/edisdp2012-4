package balle.strategy.planner;

import balle.controller.Controller;
import balle.misc.Globals;
import balle.strategy.ConfusedException;
import balle.strategy.bezierNav.BezierNav;
import balle.strategy.curve.CustomCHI;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.strategy.pathFinding.SimplePathFinder;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.FieldObject;
import balle.world.objects.Point;

public class InitialBezierStrategy extends AbstractPlanner {

    private final OrientedMovementExecutor ome;
    private final double OFFSET_ANGLE; // Degrees

    public InitialBezierStrategy(double offsetAngle) {
        ome = new BezierNav(new SimplePathFinder(new CustomCHI()));
        OFFSET_ANGLE = offsetAngle;
    }

    public InitialBezierStrategy() {
        this(70);
    }

    public FieldObject getTarget(Snapshot snapshot) {
        Ball ball = snapshot.getBall();
        FieldObject target;

        if (ball.getPosition() != null)
            target = ball;
        else {
            // If we cannot see the target for some reason, assume it is in the
            // centre
            target = new Point(new Coord(Globals.PITCH_WIDTH / 2,
                    Globals.PITCH_HEIGHT / 2));
        }
        return target;
    }

    public Orientation getDesiredOrientation(FieldObject target,
            Snapshot snapshot) {

        if (snapshot.getBalle().getPosition() == null)
            return new Orientation(0);

        Orientation orientationToTarget = target.getPosition()
                .sub(snapshot.getBalle().getPosition()).orientation();
        // If we are approaching the target from the right use 180+45 degrees
        if (orientationToTarget.isFacingLeft(0))
            return new Orientation(180 + OFFSET_ANGLE, false);
        else
            return new Orientation(OFFSET_ANGLE, false);

    }
    @Override
    protected void onStep(Controller controller, Snapshot snapshot) throws ConfusedException {

        if (snapshot.getBalle().getPosition() == null)
            return;

        addDrawables(ome.getDrawables());
        FieldObject target = getTarget(snapshot);
        Orientation desiredOrientation = getDesiredOrientation(target, snapshot);
        ome.updateTarget(target, desiredOrientation);
        ome.step(controller, snapshot);

    }

}
