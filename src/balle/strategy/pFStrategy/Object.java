package main.strategy.pFStrategy;

//basic object for any object in the arena.
public interface Object {
	// return the repulsive/attractive vector given an object in a position.
	public Vector getVector(Point point, boolean repulsive);

	public Vector getVector(Pos point, boolean repulsive);

}
