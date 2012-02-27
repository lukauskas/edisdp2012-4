/**
 * 
 */
package balle.strategy.planner;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.main.drawable.Dot;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.turning.FaceAngle;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.objects.Pitch;
import balle.world.objects.Point;
import balle.world.objects.Robot;
import balle.world.objects.StaticFieldObject;

/**
 * @author s0909773
 * 
 */
public class GoToBall extends AbstractPlanner {

    private static final Logger LOG                 = Logger.getLogger(GoToBall.class);

    MovementExecutor            executorStrategy;

    private static final double AVOIDANCE_GAP       = 0.5;
    private static final double DIST_DIFF_THRESHOLD = 0.4;

    /**
     * @param controller
     * @param world
     */
    public GoToBall(MovementExecutor movementExecutor) {
        executorStrategy = movementExecutor;

    }

    protected StaticFieldObject getTarget() {
        return getSnapshot().getBall();
    }

    protected Color getTargetColor() {
        return Color.CYAN;
    }

    protected Coord calculateAvoidanceCoord(double gap, boolean belowPoint) {
        int side = 1;
        if (belowPoint) {
            side = -1;
        }

        Robot robot = getSnapshot().getBalle();
        Coord point = getSnapshot().getOpponent().getPosition();

        // Gets the angle and distance between the robot and the ball
        double robotObstacleAngle = point.sub(robot.getPosition()).orientation()
                .atan2styleradians();
        double robotObstacleDistance = point.dist(robot.getPosition());
        // Calculate the distance between the robot and the destination point
        double hyp = Math.sqrt((robotObstacleDistance * robotObstacleDistance) + (gap * gap));

        // Calculate the angle between the robot and the destination point
        double robotPointAngle = Math.asin(gap / hyp);
        // Calculate the angle between the robot and the destination point.
        // Side is -1 if robot is below the ball, so will get the angle needed
        // for a point
        // below the ball, whereas side = 1 will give a point above the ball
        double angle = robotObstacleAngle + (side * robotPointAngle);

        // Offsets are in relation to the robot
        double xOffset = hyp * Math.cos(angle);
        double yOffset = hyp * Math.sin(angle);

        return new Coord(robot.getPosition().getX() + xOffset, robot.getPosition().getY() + yOffset);
    }

    protected Point getAvoidanceTarget() {
        Coord pointAbove = calculateAvoidanceCoord(AVOIDANCE_GAP, true);
        Coord pointBelow = calculateAvoidanceCoord(AVOIDANCE_GAP, false);
        Pitch pitch = getSnapshot().getPitch();

        Coord currentPosition = getSnapshot().getBalle().getPosition();
        if (pitch.containsCoord(pointAbove) && pitch.containsCoord(pointBelow)) {
            // If both points happen to be in the pitch, return the closest one
            double distToPointAbove = currentPosition.dist(pointAbove);
            double distToPointBelow = currentPosition.dist(pointBelow);
            double distDiff = Math.abs(distToPointAbove - distToPointBelow);

            // If distances differ by much:
            if (distDiff > DIST_DIFF_THRESHOLD) {
                // Pick the shorter one
                if (distToPointAbove < distToPointBelow)
                    return new Point(pointAbove);
                else
                    return new Point(pointBelow);
            } else {
                Orientation targetOrientation = getTarget().getPosition().sub(currentPosition)
                        .orientation();
                // Else pick by orientation
                double angleToTurnPointAbove = FaceAngle.getAngleToTurn(getSnapshot().getBalle()
                        .getOrientation(), targetOrientation);
                double angleToTurnPointBelow = FaceAngle.getAngleToTurn(getSnapshot().getBalle()
                        .getOrientation(), targetOrientation);

                if (Math.abs(angleToTurnPointAbove) < Math.abs(angleToTurnPointBelow)) {
                    return new Point(pointAbove);
                } else
                    return new Point(pointBelow);

            }

        } else if (pitch.containsCoord(pointAbove)) {
            // Else if pitch contains only pointAbove, return it
            return new Point(pointAbove);
        } else
            // if it doesn't contain pointAbove, it should contain pointBelow
            return new Point(pointBelow);
        // TODO: what happens if it does not contain both points?
        // (it will return pointBelow now, but some other behaviour might be
        // desired)

    }

    @Override
    public void step(Controller controller) {
        StaticFieldObject target = getTarget();

        if ((getSnapshot() == null) || (getSnapshot().getBalle().getPosition() == null)
                || (target == null))
            return;

        // Update the current state of executor strategy
        executorStrategy.updateState(getSnapshot());

        // If we see the opponent
        if (getSnapshot().getOpponent() != null) {
            Line pathToTarget = new Line(getSnapshot().getBalle().getPosition(),
                    target.getPosition());
            // Check if it is blocking our path
            if (getSnapshot().getOpponent().intersects(pathToTarget)) {
                // pick a new target then
                LOG.info("Opponent is blocking the target, avoiding it");
                target = getAvoidanceTarget();
            }
        }

        // Update the target's location in executorStrategy (e.g. if target
        // moved)
        executorStrategy.updateTarget(target);
        // Draw the target
        if (target.getPosition() != null)
            addDrawable(new Dot(target.getPosition(), getTargetColor()));

        // If it says it is not finished, tell it to do something for a step.
        if (!executorStrategy.isFinished()) {
            executorStrategy.step(controller);
        } else {
            // Tell the strategy to stop doing whatever it was doing
            executorStrategy.stop(controller);
        }
    }

    @Override
    public void stop(Controller controller) {
        if (!executorStrategy.isFinished())
            executorStrategy.stop(controller);

    }
}
