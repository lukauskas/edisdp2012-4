package balle.strategy.planner;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.turning.FaceAngle;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.Point;
import balle.world.objects.StaticFieldObject;

public class KickFromWall extends GoToBall {

	private static final Logger LOG = Logger.getLogger(KickFromWall.class);

	boolean secondStep = false;
	boolean additionalStep = false;
	boolean kicked = false;

	public KickFromWall(MovementExecutor movementStrategy) {
		super(movementStrategy);
		// TODO Auto-generated constructor stub
	}

	public Coord calculateNearWallCoord() {
		// TODO Auto-generated method stub

		if (getSnapshot() == null)
			return null;
		Snapshot snap = getSnapshot();

		double targetX, targetY;
		boolean isBottom;

		if (snap.getOpponentsGoal().getPosition().getX() < snap.getPitch()
				.getMaxX() / 2) {
			if (snap.getBall().getPosition().getY() < snap.getPitch().getMaxY() / 2) {
				targetX = snap.getBall().getPosition().getX() + 0.4;
				targetY = snap.getBall().getPosition().getY() + 0.13;
				isBottom = true;
			} else {
				targetX = snap.getBall().getPosition().getX() + 0.4;
				targetY = snap.getBall().getPosition().getY() - 0.13;
				isBottom = false;
			}
		} else {
			if (snap.getBall().getPosition().getY() < snap.getPitch().getMaxY() / 2) {
				targetX = snap.getBall().getPosition().getX() - 0.4;
				targetY = snap.getBall().getPosition().getY() + 0.13;
				isBottom = true;
			} else {
				targetX = snap.getBall().getPosition().getX() - 0.4;
				targetY = snap.getBall().getPosition().getY() - 0.13;
				isBottom = false;
			}
		}

		Coord loc = new Coord(targetX, targetY);
		Coord loc2 = snap.getBall().getPosition();
		Coord loc3;

		if (isBottom) {
			loc3 = new Coord(snap.getBall().getPosition().getX(), snap
					.getBall().getPosition().getY() + 0.3);
		} else {
			loc3 = new Coord(snap.getBall().getPosition().getX(), snap
					.getBall().getPosition().getY() - 0.3);
		}

		LOG.trace(snap.getBalle().getPosition().dist(loc3));

		if (snap.getBalle().getPosition().dist(loc3) < 0.15)
			additionalStep = true;

		if (snap.getBalle().getPosition().dist(loc) < 0.15) {
			secondStep = true;
		}

		if (!secondStep) {
			// LOG.info("Going to location");

			if (Math.abs(snap.getBalle().getPosition().getY()
					- snap.getBall().getPosition().getY()) < 0.3
					&& !additionalStep) {
				return loc3;
			} else {
				additionalStep = true;
				return loc;
			}
		} else {
			// LOG.info("Going to ball");
			setExecutorStrategy(new GoToObject(new FaceAngle()));
			return loc2;
		}
	}

	@Override
	protected StaticFieldObject getTarget() {
		Coord nearWallCoord = calculateNearWallCoord();
		return new Point(nearWallCoord);
	}

	@Override
	public void step(Controller controller) {
		if (getSnapshot().getBalle().possessesBall(getSnapshot().getBall())) {
			controller.kick();
		} else {
			super.step(controller);
		}
	}
}
