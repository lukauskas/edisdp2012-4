package balle.strategy;

import balle.controller.Controller;
import balle.main.GUITab;
import balle.world.AbstractWorld;
import balle.world.processing.AbstractWorldProcessor;

public abstract class AbstractStrategy extends AbstractWorldProcessor {

    private Controller controller;

    public AbstractStrategy(Controller controller, AbstractWorld world) {
        super(world);
        this.controller = controller;
        
    }

    @Override
    protected void actionOnStep() {
        aiStep();
    }

    @Override
    protected void actionOnChange() {
        aiMove(controller);
    }

    protected abstract void aiStep();

    protected abstract void aiMove(Controller controller);
    
    @Override
    public void start() {
    	super.start();
    	GUITab.addStrategy(this);
    }
    
    @SuppressWarnings("deprecation")
	public void pause(boolean flag) {
    	if (flag) {
    		this.suspend();
    		controller.stop();
    	} else {
    		this.resume();
    	}
    }
    
    @Override
    public String toString() {
    	return "Unnamed Strategy";
    }
}
