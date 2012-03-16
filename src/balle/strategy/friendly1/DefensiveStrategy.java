package balle.strategy.friendly1;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.main.drawable.DrawableLine;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.planner.GoToBall;
import balle.world.Coord;
import balle.world.Line;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.Goal;
import balle.world.objects.Point;
import balle.world.objects.Robot;
import balle.world.objects.StaticFieldObject;

/**
 * The Class DefensiveStrategy. Controls the robot so it gets in between
 * opponent and our own goal and stays there.
 */
public class DefensiveStrategy extends GoToBall {

    private final static Logger LOG = Logger.getLogger(DefensiveStrategy.class);

    public DefensiveStrategy(MovementExecutor movementExecutor) {
        super(movementExecutor);
    }

    protected Coord calculateDefenceCoord(Snapshot snapshot) {

        Goal ownGoal = snapshot.getOwnGoal();

        Robot opponent = snapshot.getOpponent();
        Ball ball = snapshot.getBall();
        Robot our = snapshot.getBalle();
        if (our.getPosition() == null)
            return null;
        if (ball.getPosition() == null)
            return null;
        if (opponent.getPosition() == null)
            return null;

        Coord intersectionPoint = opponent.getBallKickLine(ball).getIntersect(
                ownGoal.getGoalLine());

        if (intersectionPoint == null) {
            LOG.debug("No intersection between getBallKickLine and getGoalLine");
            intersectionPoint = opponent.getFacingLine().getIntersect(
                    ownGoal.getGoalLine());
            if (intersectionPoint == null) {
                // TODO: fix this, this case should not be happening
                LOG.error("Opponent is not even facing our goal. Defensive strategy should not be in play");
                return null;
            }

        }

        Line defenceLine = new Line(ball.getPosition(), intersectionPoint);
        addDrawable(new DrawableLine(defenceLine, Color.WHITE));

        // return defenceLine.closestPoint(our.getPosition());
        return defenceLine.midpoint();
    }

    @Override
    protected StaticFieldObject getTarget(Snapshot snapshot) {
        Coord defenceCoord = calculateDefenceCoord(snapshot);
        return new Point(defenceCoord);
    }

    @Override
    protected Color getTargetColor() {
        return Color.PINK;
    }

}