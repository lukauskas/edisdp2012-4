package balle.strategy.pFStrategy;

//a Velocity vector that includes left and right wheels.
public class VelocityVec extends Vector {

    public static final int MAX_SPEED = 900; // Degrees/s
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
        // Check if we really need to scale
        if ((Math.abs(getLeft()) <= Math.toRadians(MAX_SPEED))
                && (Math.abs(getRight()) <= Math.toRadians(MAX_SPEED)))
            return this;

        double theta = normalAngle();
        double newNorm;
        // Calculate the length of the new vector so it fits into the wheel
        // speeds
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

        // Calculate the new speeds
        double newX = Math.cos(theta) * newNorm;
        double newY = Math.sin(theta) * newNorm;

        // Flip the angle if the direction is incorrect.
        if (((newX > 0) && (getX() < 0)) || ((newX < 0) && (getX() > 0)))
            newX *= -1;
        if (((newY > 0) && (getY() < 0)) || ((newY < 0) && (getY() > 0)))
            newY *= -1;

        // Finally, return everything
        return new VelocityVec(newX, newY);

    }
}
