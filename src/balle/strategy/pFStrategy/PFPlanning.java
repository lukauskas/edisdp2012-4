package balle.strategy.pFStrategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PFPlanning {
    private static final Logger LOG           = Logger.getLogger(PFPlanning.class);

    Pos                         robot;
    PointObject                 opponent;
    PointObject                 ball;
    double                      default_power = 5;
    RobotConf                   config;
    List<PFObject>                objects;
    /** power for opponent. */
    double                      opponentPower;
    /** influence distance for opponent */
    double                      opponentInf;
    /** power for goal location. */
    double                      ballPower;
    /** power orientation, extended potential field. */
    double                      opponentAlphaPower;

    /**
     * Instantiates a new pF planning.
     * 
     * @param conf
     *            Configuration parameters of the robot.
     * @param opponentPower
     *            Repulsive power for opponent
     * @param opponentInf
     *            Influence distance for opponent
     * @param targetPower
     *            Power for goal position
     * @param alpha
     *            Orientation power
     */
    public PFPlanning(RobotConf conf, double opponentPower, double opponentInf,
            double targetPower, double alpha) {
        this.config = conf;
        objects = new ArrayList<PFObject>();
        this.opponentPower = opponentPower;
        this.opponentInf = opponentInf;
        this.ballPower = targetPower;
        this.opponentAlphaPower = alpha;
    }

    private void init(Pos robot, Pos opponent, Point ball) {
        if (opponent != null) {
            PointObject opponentObj = new PointObject(opponent.getLocation(),
                    opponentPower, opponentInf, opponentAlphaPower);
            this.opponent = opponentObj;
        } else
            this.opponent = null;

        PointObject ballObj = new PointObject(ball, ballPower, Double.MAX_VALUE);
        this.robot = robot;
        this.ball = ballObj;

    }
    /**
     * Given Pos of different objects, this function will create PointObjects to
     * be used later.
     * 
     * @param robot
     *            Pos.
     * @param opponent
     *            opponent Pos
     * @param ball
     *            ball Location
     * @return the velocity vec
     */
    public VelocityVec update(Pos robot, Pos opponent, Point ball) {

        init(robot, opponent, ball);

        List<PFObject> complList = new ArrayList<PFObject>(objects);
        if (this.opponent != null)
            complList.add(this.opponent);

        Vector res = GoTo(complList, this.ball, robot);

        return getVelocity(res, robot);

    }

    /** Adds static object in the arena to list of static obstacles. */
    public void addObject(PFObject r) {
        objects.add(r);
    }

    /**
     * Actual function which computes the the next velocity vector to be applied
     * to the robot.
     */
    public Vector GoTo(List<PFObject> obstacles, PointObject dest_obj,
            Point start_point) {

        Vector rep = new Vector(0, 0);
        // iterate through all obstacles and compute sum of all repulsive
        // vectors
        for (int i = 0; i < obstacles.size(); i++) {
            rep = rep.add(obstacles.get(i).getVector(start_point, true));
        }
        // Compute attractive vector.
        Vector att = dest_obj.getVector(start_point, false);

        return att.add(rep);

    }

    /**
     * Extended Potential Field.This function takes into account the orientation
     * of Robot.
     * 
     * @param obstacles
     *            the obstacles
     * @param dest_obj
     *            the dest_obj
     * @param start_point
     *            the start_point
     * @return the vector
     */
    public Vector GoTo(List<PFObject> obstacles, PointObject dest_obj,
            Pos start_point) {

        Vector rep = new Vector(0, 0);
        // iterate through all obstacles and compute sum of all repulsive
        // vectors
        for (int i = 0; i < obstacles.size(); i++) {
            rep = rep.add(obstacles.get(i).getVector(start_point, true));
        }
        // Compute attractive vector.
        Vector att = dest_obj.getVector(start_point, false);

        return att.add(rep);

    }

    /**
     * This function translates linear and angular velocities to left and right
     * velocities.
     * 
     * @param Vlin
     * @param VAng
     * @param r
     * @return
     */
    private VelocityVec CvtVelocity(double Vlin, double VAng, double r) {
        double left = Vlin - r * Math.sin(VAng);
        double right = Vlin + r * Math.sin(VAng);
        VelocityVec vector = new VelocityVec(left, right);
        return vector;
    }

    /**
     * Given current Pos of the robot and current velocity this function
     * computes linear and angular velocities for a differential drive robot
     * using input parameters of the robot.
     */
    private VelocityVec getVelocity(Vector inputVel, Pos current) {

        double size = inputVel.norm();
        if (size == 0)
            return new VelocityVec(0, 0);

        double alpha = inputVel.normalAngle();
        double dist_alpha = alpha - current.getAngle();
        if (dist_alpha > Math.PI)
            dist_alpha = -1 * (2 * Math.PI - dist_alpha);
        else if (dist_alpha < -1 * Math.PI)
            dist_alpha = 2 * Math.PI + dist_alpha;
        double Vlin = Math.cos(dist_alpha) * size;

        double Vang = dist_alpha / Math.PI; // 0.8 works better than 0.4

        return CvtVelocity(Vlin, Vang, config.getr());
    }
}
