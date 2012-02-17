package balle.main;

/**
 * Easier to run in eclipse than Runner.
 */
public class TestRunner {

    public static void main(String[] args) {
        testSimulator(true);
    }

    public static void testRobot(boolean balleIsBlue) {
        Runner.runRobot(balleIsBlue, true, false);
    }

    public static void testSimulator(boolean balleIsBlue) {
        Runner.runSimulator(balleIsBlue, true);
    }
}
