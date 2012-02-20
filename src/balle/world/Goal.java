package balle.world;

public class Goal implements StaticFieldObject {

    private final double minX, maxX, minY, maxY;

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

    public Goal(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public Coord getPosition() {
        return new Coord((minX + maxX) / 2, (minY + maxY / 2));
    }

    @Override
    public boolean containsCoord(Coord point) {
        if (point.getX() > maxX)
            return false;
        if (point.getX() < minX)
            return false;
        if (point.getY() > maxY)
            return false;
        if (point.getY() < minY)
            return false;
        return true;
    }

    /**
     * Gets the coordinate of the left goal post.
     * 
     * @return the left goal post coord
     */
    public Coord getLeftPostCoord() {
        // TODO: Implement
        return null;
    }

    /**
     * Gets the coordinate of the right goal post.
     * 
     * @return the right goal post coord
     */
    public Coord getRightPostCoord() {
        // TODO: Implement
        return null;
    }

}
