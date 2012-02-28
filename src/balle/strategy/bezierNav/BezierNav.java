package balle.strategy.bezierNav;

import java.awt.Color;
import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Circle;
import balle.main.drawable.Dot;
import balle.main.drawable.Drawable;
import balle.misc.Globals;
import balle.strategy.executor.movement.MovementExecutor;
import balle.world.Coord;
import balle.world.Snapshot;
import balle.world.objects.StaticFieldObject;

public class BezierNav implements MovementExecutor {

	private StaticFieldObject target;
	private Snapshot state;
	private double stopDistance = 0.02;

	private Coord p0, p1, p2, p3;

	@Override
	public void stop(Controller controller) {
		controller.stop();
	}

	@Override
	public ArrayList<Drawable> getDrawables() {
		ArrayList<Drawable> l = new ArrayList<Drawable>();
		if (p0 == null) {
			return l;
		}
		for (double t = -0.1; t < 1.1; t += 0.05) {
			Color c = Color.ORANGE;
			if (t < 0 || t > 1)
				c = Color.GRAY;
			l.add(new Dot(pos(t), c));
		}
		Coord center = getCenterOfRotation(0);
		l.add(new Dot(center, Color.BLACK));
		l.add(new Circle(center, center.dist(state.getBalle().getPosition()),
				Color.yellow));
		return l;
	}

	@Override
	public void updateTarget(StaticFieldObject target) {
		this.target = target;
	}

	@Override
	public boolean isFinished() {
		return state.getBalle().getPosition()
				.dist(target.getPosition().add(new Coord(0, 0.16))) <= stopDistance;
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
		// calculate bezier points 0 to 3
		Coord rP = state.getBalle().getPosition(), tP = target.getPosition()
				.add(new Coord(0, 0.15));
		double distS = rP.dist(tP) / 2;

		p0 = rP;
		p1 = rP.add(state.getBalle().getOrientation().getUnitCoord()
				.mult(distS / 4));
		p2 = tP.add(new Coord(0, 1).mult(distS));
		p3 = tP;

		System.out.println("----");
		System.out.println("----");
		System.out.println(pos(0));
		System.out.println(vel(0));
		System.out.println(accel(0));
		System.out.println("----");
		// calculate turning radius
		Coord a = accel(0);
		Coord turnCenter = getCenterOfRotation(0);
		// System.out.println(a);
		// if (k == 0) {
		// controller.forward(Globals.MAXIMUM_MOTOR_SPEED);
		// return;
		// }
		boolean isLeft = new Coord(0, 0).angleBetween(
				state.getBalle().getOrientation().getUnitCoord(), a)
				.atan2styleradians() > 0;
		double r = turnCenter.dist(pos(0));
		System.out.println("r: " + r);
		System.out.println((isLeft ? "left" : "right"));
		System.out.println("center\t\t" + turnCenter);
		// calcualte wheel speeds/powers
		double max = Globals.powerToVelocity(Globals.MAXIMUM_MOTOR_SPEED);
		int v1, v2;
		v1 = (int) Globals
				.velocityToPower((float) (max * getMinVelocityRato(r)));
		v2 = (int) Globals.velocityToPower((float) max);

		System.out.println("v1,v2\t\t" + v1 + "\t" + v2);
		// apply wheel speeds
		if (true) {
			if (isLeft)
				controller.setWheelSpeeds(v1, v2);
			else
				controller.setWheelSpeeds(v2, v1);
		}

	}

	@Override
	public void setStopDistance(double stopDistance) {
		this.stopDistance = stopDistance;
	}

	private Coord pos(double t) {
		return p0.mult(Math.pow((1 - t), 3))
				.add(p1.mult(3 * Math.pow(1 - t, 2) * t))
				.add(p2.mult(3 * (1 - t) * t * t)).add(p3.mult(Math.pow(t, 3)));
	}

	private Coord vel(double t) {
		return p0.mult(-3 + (6 * t) - (3 * t * t))
				.add(p1.mult(3 * (1 - (4 * t) + (3 * t * t))))
				.add(p2.mult(3 * ((2 * t) - (3 * t * t))))
				.add(p3.mult(3 * t * t));
	}

	private Coord accel(double t) {
		return p0.mult(6 - (6 * t)).add(p1.mult(3 * (-4 + (6 * t))))
				.add(p2.mult(3 * (2 - (6 * t)))).add(p3.mult(6 * t));
	}

	private Coord getCenterOfRotation(double t) {
		double f, f1, f11, g, g1, g11;
		f = pos(t).getX();
		f1 = vel(t).getX();
		f11 = accel(t).getX();
		g = pos(t).getY();
		g1 = vel(t).getY();
		g11 = accel(t).getY();
		return new Coord(
				f
						- ((((f1 * f1) + (g1 * g1)) * g1) / ((f1 * g11) - (f11 * g1))),
				g
						+ ((((f1 * f1) + (g1 * g1)) * f1) / ((f1 * g11) - (f11 * g1))));
	}

	private double getMinVelocityRato(double radius) {
		double rtw = Globals.ROBOT_TRACK_WIDTH / 2;
		return (radius - rtw) / (radius + rtw);
	}

}
