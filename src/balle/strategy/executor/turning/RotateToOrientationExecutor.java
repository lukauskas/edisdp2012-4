package balle.strategy.executor.turning;

import balle.strategy.executor.Executor;
import balle.world.Orientation;

public interface RotateToOrientationExecutor extends Executor {

    /**
     * Set the target orientation to turn to.
     * 
     * @param targetOrientation
     */
    public abstract void setTargetOrientation(Orientation targetOrientation);

    /**
     * Returns the angle required to turn using atan2 style radians. Positive
     * angle means to turn CCW this much radians, whereas negative means turning
     * CW that amount of radians.
     * 
     * @return angle to turn
     */
    public abstract double getAngleToTurn();

    /**
     * Returns true if robot is turning at the moment
     * 
     * @return
     */
    public abstract boolean isTurning();

}