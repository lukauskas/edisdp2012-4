package balle.strategy.planner;

import java.awt.Color;

import balle.controller.Controller;
import balle.main.drawable.DrawableLine;
import balle.misc.Globals;
import balle.strategy.FactoryMethod;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.turning.FaceAngle;
import balle.strategy.executor.turning.RotateToOrientationExecutor;
import balle.world.Line;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.FieldObject;
import balle.world.objects.Goal;
import balle.world.objects.Pitch;
import balle.world.objects.Point;
import balle.world.objects.Robot;

public class GoToBallSafeProportional extends GoToBall {

    private static final double BALL_SAFE_GAP = 0.4;

	private final AbstractPlanner turnHack;

    public GoToBallSafeProportional() {
        super(new GoToObjectPFN(0));

		turnHack = new TurnHack();
    }

    public GoToBallSafeProportional(double avoidanceGap, double overshootGap,
            boolean approachfromCorrectSide) {
        super(new GoToObjectPFN(0), avoidanceGap, overshootGap,
                approachfromCorrectSide);
        turnHack = new TurnHack();
    }

    protected FieldObject getOriginalTarget(Snapshot snapshot) {
		return super.getTarget(snapshot);
    }

    protected boolean ballSafeGapCanBeIncreased(Snapshot snapshot, Line newTargetLine) {
        Pitch pitch = snapshot.getPitch();
        Robot ourRobot = snapshot.getBalle();
		FieldObject ball = getOriginalTarget(snapshot);

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

    @FactoryMethod(designator = "GoToBallSafeProportional", parameterNames = {})
    public static GoToBallSafeProportional factoryMethod() {
        return new GoToBallSafeProportional();
    }

    @Override
    protected void onStep(Controller controller, Snapshot snapshot) {
        FieldObject ball = getOriginalTarget(snapshot);
        Robot ourRobot = snapshot.getBalle();

		if ((snapshot == null) || (ourRobot == null) || (ball == null)) {
			return;
		}

		if (turnHack.shouldStealStep(snapshot)) {
			LOG.info("Letting TurnHack handle the step");
			turnHack.step(controller, snapshot);
			return;
		}

        if (ourRobot.isApproachingTargetFromCorrectSide(ball,
                snapshot.getOpponentsGoal())) {
            setApproachTargetFromCorrectSide(false);
        } else {
            setApproachTargetFromCorrectSide(true);
        }

        super.onStep(controller, snapshot);
    }

	/**
	 * PFN's large turning radius mean that we usually overshoot the ball when
	 * we're really close and trying to turn towards it. This strategy just
	 * captures onStep() in that case and makes the robot turn normally
	 * 
	 * @author s0913664
	 * 
	 */
	private class TurnHack extends AbstractPlanner {

		private static final double ANGLE_THRESH = Math.PI / 4;
		private static final double DIST_THRESH = 0.08;

		private RotateToOrientationExecutor turnExecutor;

		private boolean isTurning = false;

		public TurnHack() {
			turnExecutor = new FaceAngle();
		}

		@Override
		public boolean shouldStealStep(Snapshot snapshot) {

			// Steal step when the line from the centre of the goal through the
			// wall intersects the robot, and the robot is close to the ball
			
			return isTurning || needsToTurn(snapshot);

		}

		private boolean needsToTurn(Snapshot snapshot) {
			Robot ourRobot = snapshot.getBalle();
			Ball ball = snapshot.getBall();
			Goal opponentsGoal = snapshot.getOpponentsGoal();
			
			Line line = new Line(opponentsGoal.getPosition(), ball.getPosition()).extend(Globals.PITCH_WIDTH);
			boolean isOnCorrectSide = ourRobot.isApproachingTargetFromCorrectSide(ball, opponentsGoal);
			double absAngleToTurn = Math.abs(ourRobot.getAngleToTurnToTarget(ball
					.getPosition()));

			return isOnCorrectSide && absAngleToTurn > ANGLE_THRESH
					&& line.dist(ourRobot.getPosition()) < DIST_THRESH;
		}

		@Override
		protected void onStep(Controller controller, Snapshot snapshot) {

			Robot ourRobot = snapshot.getBalle();
			Ball ball = snapshot.getBall();

			if (!isTurning) {
				Orientation targetAngle = ball.getPosition()
						.sub(ourRobot.getPosition()).orientation();

				LOG.info("TurnHack: Setting target orientation");
				turnExecutor.setTargetOrientation(targetAngle);

				isTurning = true;
			} else if (turnExecutor.isFinished(snapshot)
					|| !needsToTurn(snapshot)) {
				isTurning = false;
			}

			turnExecutor.step(controller, snapshot);

		}

	}
}
