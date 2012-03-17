package balle.controller;

import balle.strategy.bezierNav.ControllerHistoryElement;

public interface ControllerListener {

	public abstract void commandSent(ControllerHistoryElement che);

}
