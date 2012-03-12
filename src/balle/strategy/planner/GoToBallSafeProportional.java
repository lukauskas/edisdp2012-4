package balle.strategy.planner;

import java.awt.Color;

import balle.controller.Controller;
import balle.main.drawable.DrawableLine;
import balle.misc.Globals;
import balle.strategy.FactoryMethod;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.world.Line;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.FieldObject;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Point;
import balle.world.objects.Robot;

public class GoToBallSafeProportional extends GoToBall {

    private static final double BALL_SAFE_GAP = 0.4;

    public GoToBallSafeProportional() {
        super(new GoToObjectPFN(0));
    }

    protected FieldObject getOriginalTarget(Snapshot snapshot) {
        return snapshot.getBall();
    }

    protected boolean ballSafeGapCanBeIncreased(Snapshot snapshot, Line newTargetLine) {
        Pitch pitch = snapshot.getPitch();
        Robot ourRobot = snapshot.getBalle();
        Ball ball = snapshot.getBall();

        // We cannot extend the line, if we cannot reach the endpoint
        if (!pitch.containsCoord(newTargetLine.extend(
                Globals.ROBOT_LENGTH).getB())) return false;

        // We must extend the line if we are not approaching the ball from
        // correct side
        Line targetGoalLine = new Line(snapshot.getBalle().getPosition(),
                ball.getPosition());
        targetGoalLine = targetGoalLine.extend(Globals.PITCH_WIDTH);

        if (!targetGoalLine.intersects(snapshot.getOpponentsGoal()
                .getGoalLine().extendBothDirections(0.7)))
           return true;
        else {
            // If we are approaching the ball from correct side
            // and we are far away from point, keep extending the line
            if (newTargetLine.getB().dist(ourRobot.getPosition()) > Globals.ROBOT_LENGTH
                    / 2 + BALL_SAFE_GAP / 3) {
                return true;
            } else
                // Otherwise do not extend it anymore
                return false;
        }
    }
    @Override
    protected FieldObject getTarget(Snapshot snapshot) {
        FieldObject ball = getOriginalTarget(snapshot);
        Robot ourRobot = snapshot.getBalle();

        if (ball.getPosition() == null) {
            LOG.warn("Cannot see the ball");
            return null;
        }
        if (ourRobot.getPosition() == null) {
            LOG.warn("Cannot see self");
            return null;
        }
        Goal targetGoal = snapshot.getOpponentsGoal();

        Line targetLine = new Line(targetGoal.getPosition(), ball.getPosition());

        Pitch pitch = snapshot.getPitch();
        double ballSafeGap = 0.005;
        Line newTargetLine = targetLine;

        Line targetGoalLine = new Line(snapshot.getBalle().getPosition(),
                ball.getPosition());
        targetGoalLine = targetGoalLine.extend(Globals.PITCH_WIDTH);

        while (ballSafeGap < BALL_SAFE_GAP
                && ballSafeGapCanBeIncreased(snapshot, newTargetLine)) {
            ballSafeGap *= 1.05;
            newTargetLine = targetLine.extend(ballSafeGap);
        }
        targetLine = newTargetLine;

        addDrawable(new DrawableLine(targetLine, Color.ORANGE));
        return new Point(targetLine.getB());
    }

    @FactoryMethod(designator = "GoToBallSafeProportional")
    public static GoToBallSafeProportional factoryMethod() {
        return new GoToBallSafeProportional();
    }

    @Override
    protected void onStep(Controller controller, Snapshot snapshot) {
        FieldObject ball = getOriginalTarget(snapshot);
        Robot ourRobot = snapshot.getBalle();

        if (ourRobot.isApproachingTargetFromCorrectSide(ball,
                snapshot.getOpponentsGoal())) {
            setApproachTargetFromCorrectSide(false);
        } else {
            setApproachTargetFromCorrectSide(true);
        }

        super.onStep(controller, snapshot);
    }
}
