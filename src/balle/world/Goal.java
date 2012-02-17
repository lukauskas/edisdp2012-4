package balle.world;

public class Goal {
	
	private double minX, maxX, minY, maxY;
	
	public double getMinX() {
		return minX;
	}
	
	public double getMaxX() {
		return maxX;
	}
	
	public double getMinY() {
		return minY;
	}
	
	public double getMaxY() {
		return maxY;
	}
	
	public Goal (double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public boolean inGoal(FieldObject ball) {
		Coord ballCoord = ball.getPosition();
		if (ballCoord.getX() > maxX)	return false;
		if (ballCoord.getX() < minX) 	return false;
		if (ballCoord.getY() > maxY)	return false;
		if (ballCoord.getY() < minY) 	return false;
		return true;
	}

}
