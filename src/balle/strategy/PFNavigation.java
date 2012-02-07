package balle.strategy;

import balle.controller.Controller;
import balle.strategy.pFStrategy.*;
import balle.world.AbstractWorld;
import balle.world.Snapshot;

public class PFNavigation extends AbstractStrategy {

	public PFNavigation(Controller controller, AbstractWorld world) {
		super(controller, world);
		// TODO Auto-generated constructor stub
	}
	
	VelocityVec res;

	@Override
	protected void aiStep() {
		// TODO Auto-generated method stub
		double default_power = 300;
		double b = 15.2;
		double r = 8.27;
		RobotConf conf = new RobotConf(b, r);
		
		PFPlanning plann = new PFPlanning(conf, 10000000, Double.MAX_VALUE, 0.05, 500.0);
		Snapshot snap = getSnapshot();
		
		Pos opponent = new Pos(new Point(snap.getOpponent().getPosition().getX(),snap.getOpponent().getPosition().getY()),0);
		Pos initPos = new Pos(new Point(snap.getBalle().getPosition().getX(),snap.getBalle().getPosition().getY()),0);
		Point ball = new Point(snap.getBall().getPosition().getX(),snap.getBall().getPosition().getY());
		res = plann.update(initPos, opponent, ball,false);
		System.out.println(res);
	}

	@Override
	protected void aiMove(Controller controller) {
		// TODO Auto-generated method stub
		controller.setWheelSpeeds((int)res.getLeft(), (int)res.getRight());
	}
	
	

}
