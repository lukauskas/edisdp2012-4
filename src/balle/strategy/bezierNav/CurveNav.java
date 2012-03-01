package balle.strategy.bezierNav;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.curve.Bezier4;
import balle.strategy.curve.Curve;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

public class CurveNav implements OrientedMovementExecutor {

	private final double TARGET_PERIMETER = Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2)) / 2;

	private StaticFieldObject target;
	private Snapshot state;

	private float stepDist = 0.5f;

	private Curve curve;
	private Orientation orient;

	private MovementExecutor movementExecutor;

	public CurveNav(MovementExecutor me) {
		this.movementExecutor = me;
	}

	@Override
	public void stop(Controller controller) {
		controller.stop();
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		ArrayList<Drawable> l = new ArrayList<Drawable>();

		// Null check.
		if (curve != null)
			l.add(curve);

		return l;
	}

	@Override
	public void updateTarget(StaticFieldObject target, Orientation o) {
		this.target = target;
		this.orient = o;
	}

	@Override
	public boolean isFinished() {
		return false;
		// TODO
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
		if (isFinished()) {
			stop(controller);
			return;
		}

		// Update curve.
		curve = getCurve();
		if (curve == null)
			return;

		// Update Movement Executor.
		movementExecutor.updateState(state);
		movementExecutor.updateTarget(curve.pos(stepDist).getPoint());
		movementExecutor.step(controller);
	}

	@Override
	public void setStopDistance(double stopDistance) {
		movementExecutor.setStopDistance(stopDistance);
	}

	public Curve getCurve() {
		// calculate bezier points 0 to 3
		Coord rP = state.getBalle().getPosition(), tP = target.getPosition()
				.add(new Coord(-TARGET_PERIMETER, 0).rotate(orient));
		double distS = rP.dist(tP) / 2;

		// Null test
		if (rP == null || tP == null)
			return null;

		// Compile the test.
		Coord[] controls = new Coord[] {
				rP,
				rP.add(state.getBalle().getOrientation().getUnitCoord()
						.mult(distS / 4)),
				tP.add(orient.getOpposite().getUnitCoord().mult(distS)), tP,
		};

		return new Bezier4(controls);
	}
}