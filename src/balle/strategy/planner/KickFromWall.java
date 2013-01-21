package balle.strategy.planner;

import java.awt.Color;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.strategy.executor.movement.GoToObject;
import balle.strategy.executor.movement.GoToObjectPFN;
import balle.strategy.executor.movement.MovementExecutor;
import balle.strategy.executor.turning.FaceAngle;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.FieldObject;
import balle.world.objects.Point;

public class KickFromWall extends GoToBall {

	private static final Logger LOG = Logger.getLogger(KickFromWall.class);

	boolean secondStep = false;
	boolean additionalStep = false;
	boolean kicked = false;
	boolean goingToBall = false;

	public KickFromWall(MovementExecutor movementStrategy) {
		super(movementStrategy);
		// TODO Auto-generated constructor stub
	}

	public Coord calculateNearWallCoord(Snapshot snapshot) {
		// TODO Auto-generated method stub

		if (snapshot == null)
			return null;

		double targetX, targetY;
		boolean isBottom;

		if (snapshot.getOpponentsGoal().getPosition().getX() < snapshot
				.getPitch()
				.getMaxX() / 2) {
			if (snapshot.getBall().getPosition().getY() < snapshot.getPitch()
					.getMaxY() / 2) {
				targetX = snapshot.getBall().getPosition().getX() + 0.4;
				targetY = snapshot.getBall().getPosition().getY() + 0.13;
				isBottom = true;
			} else {
				targetX = snapshot.getBall().getPosition().getX() + 0.4;
				targetY = snapshot.getBall().getPosition().getY() - 0.13;
				isBottom = false;
			}
		} else {
			if (snapshot.getBall().getPosition().getY() < snapshot.getPitch()
					.getMaxY() / 2) {
				targetX = snapshot.getBall().getPosition().getX() - 0.4;
				targetY = snapshot.getBall().getPosition().getY() + 0.13;
				isBottom = true;
			} else {
				targetX = snapshot.getBall().getPosition().getX() - 0.4;
				targetY = snapshot.getBall().getPosition().getY() - 0.13;
				isBottom = false;
			}
		}

		Coord loc = new Coord(targetX, targetY);
		Coord loc2 = snapshot.getBall().getPosition();
		Coord loc3;

		if (isBottom) {
			loc3 = new Coord(snapshot.getBall().getPosition().getX(), snapshot
					.getBall().getPosition().getY() + 0.3);
		} else {
			loc3 = new Coord(snapshot.getBall().getPosition().getX(), snapshot
					.getBall().getPosition().getY() - 0.3);
		}

		LOG.trace(snapshot.getBalle().getPosition().dist(loc3));

		if (snapshot.getBalle().getPosition().dist(loc3) < 0.15)
			additionalStep = true;

		if (snapshot.getBalle().getPosition().dist(loc) < 0.15) {
			secondStep = true;
		}

		if (!secondStep) {
            LOG.info("Getting closer to the wall");

			if (Math.abs(snapshot.getBalle().getPosition().getY()
					- snapshot.getBall().getPosition().getY()) < 0.3
					&& !additionalStep) {
				return loc3;
			} else {
				additionalStep = true;
				return loc;
			}
		} else {

			goingToBall = true;

			MovementExecutor strategy;

			if (snapshot.getBalle().getPosition()
					.dist(snapshot.getBall().getPosition()) < 0.5) {
                LOG.info("Approaching the ball gently");
				strategy = new GoToObject(new FaceAngle());
				strategy.setStopDistance(0);
			} else {
                LOG.info("Approaching the ball with PFN");
				strategy = new GoToObjectPFN(0);
			}
			setExecutorStrategy(strategy);
			return loc2;
		}
	}

	@Override
    protected FieldObject getTarget(Snapshot snapshot) {
		Coord nearWallCoord = calculateNearWallCoord(snapshot);
		return new Point(nearWallCoord);
	}

	@Override
	public void onStep(Controller controller, Snapshot snapshot) {
		if (snapshot.getBall().isNear(snapshot.getBalle())
				&& goingToBall)
			controller.kick();
		super.onStep(controller, snapshot);
	}

    @Override
    protected Color getTargetColor() {
        return Color.WHITE;
    }

}
