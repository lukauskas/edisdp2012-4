package balle.strategy.bezierNav;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

public class BezierNav implements MovementExecutor {

	private StaticFieldObject target;
	private Snapshot state;
	private double stopDistance = 0;

	private Coord p0, p1, p2, p3;

	@Override
	public void stop(Controller controller) {
		controller.stop();
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		return new ArrayList<Drawable>();
	}

	@Override
	public void updateTarget(StaticFieldObject target) {
		this.target = target;
	}

	@Override
	public boolean isFinished() {
		return state.getBalle().getPosition().dist(target.getPosition()) <= stopDistance;
	}

	@Override
	public boolean isPossible() {
		return true;
	}

	@Override
	public void updateState(Snapshot snapshot) {
		state = snapshot;
	}

	@Override
	public void step(Controller controller) {
		// calculate bezier points 0 to 3
		Coord rP = state.getBalle().getPosition(), tP = target.getPosition();
		double dist = rP.dist(tP);

		Coord p0, p1, p2, p3;
		p0 = rP;
		p1 = rP.add(rP.getUnitCoord().mult(dist / 2));
		p2 = tP.add(new Coord(0, 1).mult(dist / 2));
		p3 = tP;

		// calculate turning radius
		Coord a = accel(0);
		// rP.getOrientation()a.getOrientation()
		double k = a.abs();
		if (k == 0) {
			controller.forward(Globals.MAXIMUM_MOTOR_SPEED);
			return;
		}
		double r = 1 / k;

		// apply wheel speeds
	}

	@Override
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;
	}

	private Coord accel(double t) {
		return p0.mult(6 * (1 - t)).add(p1.mult(6)).add(p2.mult(6))
				.add(p3.mult(6 * t));
	}

}
