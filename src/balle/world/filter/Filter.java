package balle.world.filter;

import balle.world.Snapshot;

public interface Filter {

	public abstract Snapshot filter(Snapshot s);
}
