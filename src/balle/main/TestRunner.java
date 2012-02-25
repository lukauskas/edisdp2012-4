package balle.main;

/**
 * Easier to run in eclipse than Runner.
 */
public class TestRunner {

    public static void main(String[] args) {
        testSimulator(true);
    }

    public static void testRobot(boolean balleIsBlue) {
        StrategyLogPane strategyLog = new StrategyLogPane();
        Runner.initialiseLogging(strategyLog, false);
        Runner.runRobot(balleIsBlue, true, false, strategyLog); // TODO: FIX
                                                                // THIS or
        // delete and use
        // Runner
    }

    public static void testSimulator(boolean balleIsBlue) {
        StrategyLogPane strategyLog = new StrategyLogPane();
        Runner.initialiseLogging(strategyLog, false);
        Runner.runSimulator(balleIsBlue, true, strategyLog);// TODO: Fix this or
                                                            // delete
        // and use runner class
    }
}
