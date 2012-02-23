package balle.strategy.planner;

import balle.controller.Controller;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Location;

public class KickFromWall extends AbstractPlanner {

	MovementExecutor movementStrategy;

	public KickFromWall(Controller controller, AbstractWorld world,
			MovementExecutor movementStrategy) {
		super(controller, world);
		this.movementStrategy = movementStrategy;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void aiStep() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void aiMove(Controller controller) {
		// TODO Auto-generated method stub

		if (getSnapshot() == null)
			return;
		Snapshot snap = getSnapshot();

		double targetX = snap.getBall().getPosition().getX() - 0.04;
		double targetY = snap.getBall().getPosition().getY() - 0.04;
		Coord coord = new Coord(targetX, targetY);

		movementStrategy.updateState(snap);

		Location loc = new Location(coord);

		movementStrategy.updateTarget((FieldObject) loc);

		if (!movementStrategy.isFinished())
			movementStrategy.step(controller);
		else {
			movementStrategy.stop(controller);
			movementStrategy.updateTarget(snap.getBall());
			controller.kick();
		}

	}

}
