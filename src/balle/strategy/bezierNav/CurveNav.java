package balle.strategy.bezierNav;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.ConfusedException;
import balle.strategy.curve.Curve;
import balle.strategy.curve.Interpolator;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.movement.OrientedMovementExecutor;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;

/**
 * Uses the movement executor to follow the given curve towards the target.
 */
public class CurveNav implements OrientedMovementExecutor {

	private final double TARGET_PERIMETER = Math.sqrt(Math.pow(
			Globals.ROBOT_LENGTH, 2) + Math.pow(Globals.ROBOT_WIDTH, 2)) / 2;

    private FieldObject      target;
	private Snapshot state;

	private float stepDist = 0.5f;

	private Curve curve;
	private Orientation orient;

	private Interpolator interpolator;
	private MovementExecutor movementExecutor;

	public CurveNav(Interpolator interpolator, MovementExecutor me) {
		this.movementExecutor = me;
		this.interpolator = interpolator;
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
    public void updateTarget(FieldObject target, Orientation o) {
		this.target = target;
		this.orient = o;
	}

	@Override
	public boolean isFinished(Snapshot snapshot) {
		return false;
		// TODO
	}

	@Override
	public boolean isPossible(Snapshot snapshot) {
		return true;
	}

	@Override
    public void step(Controller controller, Snapshot snapshot)
            throws ConfusedException {
		if (isFinished(snapshot)) {
			stop(controller);
			return;
		}

		// Update curve.
		curve = getCurve();
		if (curve == null)
			return;

		// Update Movement Executor.
		movementExecutor.updateTarget(curve.pos(stepDist).getPoint());
			movementExecutor.step(controller, snapshot);

	}

	@Override
	public void setStopDistance(double stopDistance) {
		movementExecutor.setStopDistance(stopDistance);
	}

	public Curve getCurve() {
		Coord rP = state.getBalle().getPosition(), tP = target.getPosition()
				.add(new Coord(-TARGET_PERIMETER, 0).rotate(orient));
		double distS = rP.dist(tP) / 2;

		// Null test
		if (rP == null || tP == null)
			return null;

		// Compile the test.
		// Coord[] controls = new Coord[] { rP,
		// tP.add(orient.getOpposite().getUnitCoord().mult(distS)), tP, };

		Coord[] controls = new Coord[] { new Coord(0, 0), new Coord(0.5, 0.5),
				new Coord(1, 0.1), new Coord(1.5, 0.5), new Coord(2, 0) };

		return interpolator.getCurve(controls, null, null);
	}
}