package balle.strategy;

import balle.controller.Controller;
import balle.strategy.pFStrategy.*;
import balle.world.AbstractWorld;
import balle.world.Snapshot;

public class PFNavigation extends AbstractStrategy {
	
	double b = 13.0f; //wheel width
	double r = 8.16f; //wheel diameter
	RobotConf conf = new RobotConf(b, r);
	
	PFPlanning plann = new PFPlanning(conf, 1, Double.MAX_VALUE, 0.05, 500.0);

public PFNavigation(Controller controller, AbstractWorld world) {
	super(controller, world);
	// TODO Auto-generated constructor stub
}

@Override
protected void aiStep() {
	// TODO Auto-generated method stub

}

@Override
protected void aiMove(Controller controller) {
	// TODO Auto-generated method stub
	Snapshot snap = getSnapshot();
	
	if (snap != null) {
		
		Pos opponent = new Pos(new Point(snap.getOpponent().getPosition().getX() * 100,snap.getOpponent().getPosition().getY()*100),0);
		//Pos opponent = null;
		Pos initPos = new Pos(new Point(snap.getBalle().getPosition().getX()*100,snap.getBalle().getPosition().getY()*100),0);
		Point ball = new Point(snap.getBall().getPosition().getX()*100,snap.getBall().getPosition().getY()*100);
		VelocityVec res = plann.update(initPos, opponent, ball, false);
		double left = Math.toDegrees(res.getLeft());
		double right = Math.toDegrees(res.getRight());
		if (left > 700)
			left = 700;
		if (left < -700)
			left = -700;
		if (right > 700)
			right = 700;
		if (right < -700)
			right = -700;
		System.out.println(opponent + " " + initPos + " " + ball + " " + left + " " + right);
		controller.setWheelSpeeds((int)left, (int)right);
	}
}



}