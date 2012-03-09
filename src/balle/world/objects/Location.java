package balle.world.objects;

import balle.world.Coord;
import balle.world.Line;

public class Location extends StaticFieldObject {

	private Coord coord;
	
	public Location (Coord coord) {
		this.coord = coord;
	}
	
	@Override
	public Coord getPosition() {
		return coord;
	}

	@Override
	public boolean containsCoord(Coord point) {
		if (coord.getX() == point.getX() && coord.getY() == point.getY())
			return true;
		else
			return false;
	}

	@Override
	public boolean intersects(Line line) {
		// TODO Auto-generated method stub
		return false;
	}

}
