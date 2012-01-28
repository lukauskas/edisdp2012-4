package balle.world;


/***
 * 
 * This class will take raw data from the vision system and model the world
 * accordingly. This includes e.g. velocities of the robots and ball and angle
 * of the robots in radians.
 * 
 * All values returned by this interface should be the best guess of the system.
 * All tweaking of the raw values recieved from the vision system is done here.
 * 
 */
public abstract class AbstractWorld extends Thread {

    // JEV: Scanner is final and can't be extended, makes it difficult for the
    // simulator.
    DataReader visionInput;

    public AbstractWorld(DataReader visionInput) {
        this.visionInput = visionInput;
        this.start();
    }

    /***
     * Gets the best guess of the coordinates of the robot (our team's robot).
     * 
     * @return coordinates of the robot.
     */
    public abstract Snapshot getSnapshot();

    @Override
    public void run() {
        while (true) {
            String line = visionInput.nextLine();

            // Ignore Comments
            if (line.charAt(0) != '#') {
                String[] tokens = line.split(" ");

                interpret(Double.parseDouble(tokens[0]),
                        Double.parseDouble(tokens[1]),
                        Double.parseDouble(tokens[2]),

                        Double.parseDouble(tokens[3]),
                        Double.parseDouble(tokens[4]),
                        Double.parseDouble(tokens[5]),

                        Double.parseDouble(tokens[6]),
                        Double.parseDouble(tokens[7]),

                        Long.parseLong(tokens[8]));
            }

            /*
             * try { Thread.sleep(10); } catch (InterruptedException e) {
             * e.printStackTrace(); }
             */
        }
    }

    abstract void interpret(double yPosX, double yPosY, double yRad,
            double bPosX, double bPosY, double bRad, double ballPosX,
            double ballPosY, long timestamp);

}
