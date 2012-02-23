package balle.strategy.pFStrategy;

//a Velocity vector that includes left and right wheels.
public class VelocityVec extends Vector {

    public static final int    MAX_SPEED    = 720;       // Degrees/s
    public static final double MAXIMUM_NORM = Math.sqrt(Math
                                                    .toRadians(MAX_SPEED)
                                                    * Math.toRadians(MAX_SPEED)
                                                    * 2);

    public VelocityVec(double left, double right) {
        super(left, right);

    }

    public double getLeft() {
        return this.getX();
    }

    public double getRight() {
        return this.getY();
    }

}
