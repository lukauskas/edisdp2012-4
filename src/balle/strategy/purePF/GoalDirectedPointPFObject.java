package balle.strategy.purePF;

import balle.world.Coord;
import balle.world.objects.Goal;

public class GoalDirectedPointPFObject extends PFObject {

	/**
	 * automatically sets the orientation to face the goal
	 */
	public void update(Coord pos, Goal goal) {
		setPosition(pos);
		Coord relCoord = pos.sub(goal.getGoalLine().getCenter());
		setOrientation(relCoord.getOrientation());
	}

	@Override
	protected Coord relativePosToForce(Coord pos) {
		// currently this just goes directly to the ball
		return pos.opposite();
	}

}
