package balle.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import balle.main.StrategyLogPane;

public class StrategyLogAppender extends AppenderSkeleton {

	private final StrategyLogPane strategyLogPane;
	LoggingEvent previousEvent = null;

	public StrategyLogAppender(StrategyLogPane strategyLogPane) {
		super();
		this.strategyLogPane = strategyLogPane;
	}

	@Override
	protected void append(LoggingEvent event) {
		// We care only about messages with priority higher than Info
		if (!event.getLevel().isGreaterOrEqual(Level.INFO))
			return;
		// Do not forward the same thing twice
		if ((previousEvent != null)
				&& (event.getMessage().equals(previousEvent.getMessage())))
			return;

		strategyLogPane.append(event);
		previousEvent = event;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

}
