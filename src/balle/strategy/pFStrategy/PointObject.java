package balle.strategy.pFStrategy;

import org.apache.log4j.Logger;

//basic Point class includes x,y
public class PointObject extends Vector implements PFObject {

    private static final Logger LOG = Logger.getLogger(PointObject.class);

    private final double        power;
    private final double        infl_distance;
    private final double        alpha;

    public PointObject(double x, double y, double power, double infl_distance) {
        super(x, y);
        this.power = power;
        this.infl_distance = infl_distance;
        this.alpha = 1;
    }

    public PointObject(double x, double y, double power, double infl_distance, double alpha) {
        super(x, y);
        this.power = power;
        this.infl_distance = infl_distance;
        this.alpha = alpha;
    }

    public PointObject(Point point, double power, double infl_distance) {
        this(point.getX(), point.getY(), power, infl_distance);
    }

    public PointObject(Point point, double power, double infl_distance, double alpha) {
        this(point.getX(), point.getY(), power, infl_distance, alpha);
    }

    /** Get vector for normal Potential Field algorithm */
    @Override
    public Vector getVector(Point point, boolean repulsive) {
        if (repulsive) {

            double distance = Math.sqrt((this.getX() - point.getX()) * (this.getX() - point.getX())
                    + (this.getY() - point.getY()) * (this.getY() - point.getY()));
            if (distance < infl_distance) {
                LOG.trace("Within influence distance");

                double p = power * (1 / distance - 1 / infl_distance) * 1 / (distance * distance)
                        * 1 / distance;

                Vector out_point = new Vector(point);
                return out_point.subtract(this).mult(p);
            } else
                return new Vector(new Point(0, 0));
        } else {

            Vector out_point = new Vector(point);
            return out_point.subtract(this).mult(power * -1);

        }

    }

    @Override
    public String toString() {
        return super.toString();
    }

    /** Get vector for extended Potential Field algorithm */
    @Override
    public Vector getVector(Pos point, boolean repulsive) {
        if (repulsive) {
            double distance = Math.sqrt((this.getX() - point.getLocation().getX())
                    * (this.getX() - point.getLocation().getX())
                    + (this.getY() - point.getLocation().getY())
                    * (this.getY() - point.getLocation().getY()));


            if ((distance < infl_distance) && (power != 0)) {
                LOG.trace("Influenced by obstacle");
                try {
                    Vector out_point = new Vector(point.getLocation());
                    out_point = this.subtract(out_point);
                    double angle = Math.atan2(out_point.getY(), out_point.getX());
                    double diffAngle = Math.abs(angle - point.getAngle());
                    double norm = Util.map2Pi(diffAngle);
                    LOG.trace("Angle: " + norm);
                    double p = power * (1 / distance - 1 / infl_distance)
                            * (1 / (distance * distance)) * (1 / distance);
                    double orientationComponent = ((Math.PI - norm) / (Math.PI)) * alpha;

                    LOG.trace("p: " + p + " orientationComponent: " + orientationComponent);
                    p *= orientationComponent;
                    LOG.trace("Final: " + p);

                    return out_point.mult(-1 * p);
                } catch (Exception ex) {
                    LOG.error("Exception thrown");
                    return new Vector(new Point(0, 0));
                }
            } else
                return new Vector(new Point(0, 0));
        } else {

            Vector out_point = new Vector(point.getLocation());
            Vector res = out_point.subtract(this);

            Vector final_res = res.mult(power * -1);


            return final_res;
        }
    }
}
