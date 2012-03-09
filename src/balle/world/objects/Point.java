package balle.world.objects;

import balle.world.Coord;
import balle.world.Line;

public class Point extends StaticFieldObject {

    private Coord position;

    public Point(Coord position) {
        super();
        this.position = position;
    }

    @Override
    public Coord getPosition() {
        return position;
    }

    public void setPosition(Coord position) {
        this.position = position;
    }

    @Override
    public boolean containsCoord(Coord point) {
        return point.equals(getPosition());
    }

    @Override
    public boolean intersects(Line line) {
        return line.contains(getPosition());
    }

}
