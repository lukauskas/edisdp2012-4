package balle.world;
import balle.brick.Controller;

public abstract class Strategy {

	private Controller controller;
	private World world;
	
	public Strategy(Controller controller, World world) {
		super();
		this.controller = controller;
		this.world = world;
	}

	protected Controller getController() {
		return controller;
	}

	protected World getWorld() {
		return world;
	}
	
}
