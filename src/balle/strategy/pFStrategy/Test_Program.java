package balle.strategy.pFStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Test_Program {
    public static void main(String[] args) {
        double default_power = 300;
        double b = 15.2;
        double r = 8.27;
        RobotConf conf = new RobotConf(b, r);
        // 10000, 500.0
        PFPlanning plann = new PFPlanning(conf, 10000000, Double.MAX_VALUE,
                0.05, 500.0);
        Pos initPos = new Pos(new Point(0, 380), 0);
        Pos opponent = new Pos(new Point(150, 435), 0);
        // Pos opponent = new Pos(new Point(300, 550), 0);
        Point ball = new Point(600, 650);

        double wall_Thickness = 0;
        Point top_left = new Point(0, 0);
        Point bottom_right = new Point(50, 50);
        RectObject left_wall = new RectObject(new Point(top_left.getX()
                - wall_Thickness, top_left.getY()), new Point(top_left.getX()
                + wall_Thickness, bottom_right.getY()), default_power,
                Double.MAX_VALUE);
        System.out.println("Left wall:" + left_wall.toString());
        RectObject right_wall = new RectObject(new Point(bottom_right.getX()
                - wall_Thickness, top_left.getY()), new Point(
                bottom_right.getX() + wall_Thickness, bottom_right.getY()),
                default_power, Double.MAX_VALUE);
        System.out.println("Right wall:" + right_wall.toString());
        RectObject top_wall = new RectObject(new Point(top_left.getX(),
                top_left.getY() - wall_Thickness), new Point(
                bottom_right.getX(), top_left.getY() + wall_Thickness),
                default_power, Double.MAX_VALUE);
        System.out.println("Top wall:" + top_wall.toString());
        RectObject bottom_wall = new RectObject(new Point(top_left.getX(),
                bottom_right.getY() - wall_Thickness), new Point(
                bottom_right.getX(), bottom_right.getY() + wall_Thickness),
                default_power, Double.MAX_VALUE);
        System.out.println("Bottom wall:" + bottom_wall.toString());
        // plann.AddObjects(left_wall);
        // plann.AddObjects(right_wall);
        // plann.AddObjects(top_wall);
        // plann.AddObjects(bottom_wall);

        // Point robot=new Point(.5*10,0.5*50);

        // opponent=null;
        // ball=null;
        ouput_allVectors(top_left, bottom_right, ball, opponent, plann, 0.5,
                false);
        // 100,380,180,400,380,700,380

        // output_path(initPos, 1, ball, opponent, plann, false, conf);

    }

    public static Vector CvtVelocity(double Vlin, double VAng, double r) {
        double left = Vlin - r * Math.sin(VAng);
        double right = Vlin + r * Math.sin(VAng);
        Vector vector = new Vector(left, right);
        return vector;
    }

    public static void output_path(Pos init, double time_step, Point ball,
            Pos opponent, PFPlanning planner, boolean srr, RobotConf config) {
        File path = new File("pathOutput.txt");
        FileWriter writer;

        File path2 = new File("pathOutput2.txt");
        FileWriter writer2;
        try {
            writer = new FileWriter(path);

            writer2 = new FileWriter(path2);

            writer.write("[");
            writer2.write("[");

            Simulate sim = new Simulate(init, config.getb());
            Pos current = init;
            current = new Pos(current.getLocation(), Math.toRadians(current
                    .getAngle()));
            double distance = Math.sqrt((current.getLocation().getX() - ball
                    .getX())
                    * (current.getLocation().getX() - ball.getX())
                    + (current.getLocation().getY() - ball.getY())
                    * (current.getLocation().getY() - ball.getY()));

            int i = 0;

            while (i < 150) {
                VelocityVec res = planner.update(current, opponent, ball);
                if (res.getLeft() == 0 && res.getRight() == 0)
                    break;
                System.out.println("Sending :"
                        + (int) Math.toDegrees(res.getLeft()) + " "
                        + (int) Math.toDegrees(res.getRight()));
                current = sim.move(res.getLeft(), res.getRight(), time_step);
                System.out.println("Current position: "
                        + current.getLocation().getX() + " "
                        + current.getLocation().getY());
                distance = Math.sqrt((current.getLocation().getX() - ball
                        .getX())
                        * (current.getLocation().getX() - ball.getX())
                        + (current.getLocation().getY() - ball.getY())
                        * (current.getLocation().getY() - ball.getY()));
                writer.write(current.getLocation().getX() + ","
                        + current.getLocation().getY() + "," + res.getX() + ","
                        + res.getY() + "\n");
                // writer.write(current.getLocation().getX() + "," + "\n");
                // writer2.write(current.getLocation().getY() + "," + "\n");
                i++;
            }

            writer.write(current.getLocation().getX() + "\n");
            writer2.write(current.getLocation().getY() + "\n");

            writer.write("]");
            writer2.write("]");

            writer.close();
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void ouput_allVectors(Point top_left, Point bottom_right,
            Point ball, Pos opponent, PFPlanning planner, double resolution,
            boolean srr) {
        File vel = new File("vel-Output.txt");
        File vel2 = new File("vel2-Output.txt");
        FileWriter writer;
        FileWriter writer2;
        try {
            writer = new FileWriter(vel);
            writer2 = new FileWriter(vel2);

            writer.write("[");
            writer2.write("[");
            for (double x = top_left.getX(); x <= bottom_right.getX(); x += resolution) {
                for (double y = top_left.getY(); y <= bottom_right.getY(); y += resolution) {
                    Pos robot = new Pos(new Point(x, y), 0);
                    Vector vec;
                    Point b;

                    if (ball != null)
                        b = ball;
                    else
                        b = robot.getLocation();
                    if (opponent != null)
                        vec = planner.update(robot, opponent, b);
                    else
                        vec = planner.update(robot,
                                new Pos(new Point(0, 0), 0), b);
                    System.out.println(String.valueOf(x) + ","
                            + String.valueOf(y) + ","
                            + String.valueOf(vec.getX()) + ","
                            + String.valueOf(vec.getY()) + "\n");
                    writer.write(String.valueOf(x) + "," + "\n");
                    writer2.write(String.valueOf(y) + "," + "\n");
                }
            }
            writer.write("]");
            writer2.write("]");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
