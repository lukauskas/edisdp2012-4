package balle.strategy;

import java.util.ArrayList;

import balle.controller.Controller;
import balle.main.drawable.Drawable;
import balle.world.Snapshot;

public interface Strategy {

    /**
     * Tell the strategy to do a step (e.g. move forward).
     * @param snapshot TODO
     */
	public void step(Controller controller, Snapshot snapshot);

    /**
     * Tell the strategy to stop doing whatever it was doing.
     * 
     * @param controller
     */
    public void stop(Controller controller);

    /**
     * Retrieves all Drawable objects from the strategy
     * 
     */
    public ArrayList<Drawable> getDrawables();

}
