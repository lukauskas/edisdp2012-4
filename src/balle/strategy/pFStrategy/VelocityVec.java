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

    public VelocityVec scale() {
        if ((Math.abs(getLeft()) <= Math.toRadians(MAX_SPEED))
                && (Math.abs(getRight()) <= Math.toRadians(MAX_SPEED)))
            return this;

        double theta = normalAngle();
        double newNorm;
        if (Math.abs(getX()) > Math.abs(getY())) {
            if (Math.cos(theta) == 0) // Double check for division by zero,
                                      // should not happen though!
                return new VelocityVec(0, 0);
            newNorm = Math.toRadians(MAX_SPEED) / Math.cos(theta);
        } else {
            if (Math.sin(theta) == 0) // Double check for division by zero,
                                      // should not happen though!
                return new VelocityVec(0, 0);
            newNorm = Math.toRadians(MAX_SPEED) / Math.sin(theta);
        }
        VelocityVec newRes = new VelocityVec(Math.cos(theta) * newNorm,
                Math.sin(theta) * newNorm);

        return newRes;

    }

}
