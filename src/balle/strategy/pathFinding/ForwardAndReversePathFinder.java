package balle.strategy.pathFinding;

import balle.strategy.curve.Interpolator;
import balle.strategy.pathFinding.path.AbstractPath;
import balle.strategy.pathFinding.path.Path;
import balle.strategy.pathFinding.path.ReversePath;
import balle.world.Coord;
import balle.world.Orientation;
import balle.world.Snapshot;
import balle.world.objects.Robot;

public class ForwardAndReversePathFinder extends SimplePathFinder {

	public ForwardAndReversePathFinder(Interpolator i) {
		super(i);
	}

	@Override
	public Path[] getPaths(Snapshot s, Coord end, Orientation endAngle) {
		Path[] paths = new AbstractPath[2];
		Robot robot = s.getBalle();
		// full forwards
		paths[0] = super.getPaths(s, robot.getPosition(),
				robot.getOrientation(), end, endAngle)[0];
		// full backwards
		paths[1] = new ReversePath(super.getPaths(s, robot.getPosition(), robot
				.getOrientation().getOpposite(), end, endAngle)[0]);

		return paths;
	}

}
