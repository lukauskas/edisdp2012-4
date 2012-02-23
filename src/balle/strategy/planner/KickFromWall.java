package balle.strategy.planner;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.AbstractWorld;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.Location;

public class KickFromWall extends AbstractPlanner {

	private static final Logger LOG = Logger.getLogger(KickFromWall.class);

	MovementExecutor movementStrategy;
	boolean secondStep = false;

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

		double targetX, targetY;

		if (snap.getOpponentsGoal().getPosition().getX() < snap.getPitch()
				.getMaxX() / 2) {
			if (snap.getBall().getPosition().getY() < snap.getPitch().getMaxY() / 2) {
				targetX = snap.getBall().getPosition().getX() + 0.2;
				targetY = snap.getBall().getPosition().getY() + 0.2;
			} else {
				targetX = snap.getBall().getPosition().getX() + 0.2;
				targetY = snap.getBall().getPosition().getY() - 0.2;
			}
		} else {
			if (snap.getBall().getPosition().getY() < snap.getPitch().getMaxY() / 2) {
				targetX = snap.getBall().getPosition().getX() - 0.2;
				targetY = snap.getBall().getPosition().getY() + 0.2;
			} else {
				targetX = snap.getBall().getPosition().getX() - 0.2;
				targetY = snap.getBall().getPosition().getY() - 0.2;
			}
		}

		Coord coord = new Coord(targetX, targetY);

		movementStrategy.updateState(snap);

		if (!secondStep) {
			Location loc = new Location(coord);
			movementStrategy.updateTarget(loc);
			LOG.trace("Going to location");
		} else {
			movementStrategy.updateTarget(snap.getBall());
			LOG.trace("Going to ball");
		}

		if (!movementStrategy.isFinished())
			movementStrategy.step(controller);
		else {
			movementStrategy.stop(controller);
			if (secondStep) {
				controller.kick();
				LOG.trace("KICKING");
			}
			secondStep = true;
		}
	}
}
