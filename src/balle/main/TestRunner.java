package balle.main;

/**
 * Easier to run in eclipse than Runner.
 */
public class TestRunner {

    public static void main(String[] args) {
        testSimulator(true);
    }

    public static void testRobot(boolean balleIsBlue) {
        Runner.runRobot(balleIsBlue, true, false, null); // TODO: FIX THIS or
                                                         // delete and use
                                                         // Runner
    }

    public static void testSimulator(boolean balleIsBlue) {
        Runner.runSimulator(balleIsBlue, true, null);// TODO: Fix this or delete
                                                     // and use runner class
    }
}
