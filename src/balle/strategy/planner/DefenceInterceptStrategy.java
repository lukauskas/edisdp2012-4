package balle.strategy.planner;

import java.awt.Color;

import balle.main.drawable.DrawableLine;
import balle.misc.Globals;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.world.Coord;
import balle.world.Estimator;
import balle.world.Line;
import balle.world.Snapshot;
import balle.world.objects.Ball;
import balle.world.objects.FieldObject;
import balle.world.objects.Goal;
import balle.world.objects.Point;
import balle.world.objects.Robot;

public class DefenceInterceptStrategy extends GoToBall {

	public DefenceInterceptStrategy() {
		super(new GoToObjectPFN(0), false);
	}

	@Override
	public boolean shouldStealStep(Snapshot snapshot) {
		Ball ball = snapshot.getBall();
		Goal ownGoal = snapshot.getOwnGoal();
		Goal opponentsGoal = snapshot.getOpponentsGoal();
		Robot ourRobot = snapshot.getBalle();

		double adjustment = 0.3; // 30cm
		double velocityThreshold = 0.008 / 1000; // 8 cm/s
		double ballXVel = ball.getVelocity().getX();

		if (Math.abs(ballXVel) > velocityThreshold) {
			Coord pos = ball.getPosition();
			FieldObject target = null;

			if (ownGoal.isRightGoal() && ballXVel > 0) {
				target = new Point(new Coord(pos.getX() - adjustment,
						pos.getY()));
			} else if (!ownGoal.isRightGoal() && ballXVel < 0) {
				target = new Point(new Coord(pos.getX() + adjustment,
						pos.getY()));
			}

			if (target != null) {
				Line line = new Line(ball.getPosition(), ownGoal.getPosition());
				if (ourRobot.isApproachingTargetFromCorrectSide(target,
						opponentsGoal) && !ourRobot.intersects(line)) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	protected FieldObject getTarget(Snapshot snapshot) {
		if ((snapshot.getBall().getPosition() == null)
				|| (snapshot.getBalle().getPosition() == null))
			return null;

		Line approachLine = getApproachLine(snapshot);
		addDrawable(new DrawableLine(approachLine, Color.RED));

		return new Point(approachLine.getB());
	}

	private Line getApproachLine(Snapshot snapshot) {
		Estimator ballEstimator = snapshot.getBallEstimator();
		Line approachLine = new Line(snapshot.getBalle().getPosition(),
				ballEstimator.estimatePosition(20));

		return approachLine.extend(Globals.PITCH_WIDTH);
	}

}
