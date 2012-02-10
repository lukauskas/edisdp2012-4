package balle.main;

import balle.bluetooth.Communicator;
import balle.controller.BluetoothController;
import balle.controller.Controller;
import balle.io.reader.SocketVisionReader;
import balle.simulator.Simulator;
import balle.simulator.SoftBot;
import balle.strategy.AbstractStrategy;
import balle.strategy.DummyStrategy;
import balle.world.AbstractWorld;
import balle.world.BasicWorld;
import balle.world.SimpleWorldGUI;

/**
 * This is where the main executable code for 4s lies. It is responsible of
 * initialising various subsystems of the code and make sure they interact.
 * 
 * 
 * @author s0909773
 * 
 */
public class Runner {

    private static void print_usage() {
        System.out
                .println("Usage: java balle.main.Runner <balle_colour> [<run_in_simulator>]");
        System.out
                .println("Where <balle_colour> is either \"blue\" or \"yellow\"");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Check the usage
        if ((args.length != 1) && (args.length != 2)) {
            print_usage();
            System.exit(-1);
        }

        // Get the colour
        boolean balleIsBlue;
        if (args[0].equals("blue"))
            balleIsBlue = true;
        else if (args[0].equals("yellow"))
            balleIsBlue = false;
        else {
            System.out.println("Invalid colour provided");
            print_usage();
            System.exit(-1);
            balleIsBlue = false; // This is just to fool Eclipse about
                                 // balleIsBlue initialisation
        }

        if ((args.length == 2) && (args[1].equals("1")))
            runSimulator(balleIsBlue);
        else
            runRobot(balleIsBlue);
    }

    public static void runRobot(boolean balleIsBlue) {

        AbstractWorld world;
        SocketVisionReader visionInput;
        Controller controller;
        SimpleWorldGUI gui;

        // Initialise world
        world = new BasicWorld(balleIsBlue);

        // Moving this forward so we do not start a GUI until controller is
        // initialised
        // If you're getting a merge conflict here leave this before
        // SimpleWorldGUI start!

        controller = new BluetoothController(new Communicator());
        // controller = new DummyController();
        // Wait for controller to initialise
        while (!controller.isReady()) {
            continue;
        }
        GUITab.setWorld(world);
        GUITab.setController(controller);
        GUITab.addStrategy("DummyStrategy");
        GUITab.addStrategy("GoToBall");

        gui = new SimpleWorldGUI(world);

        gui.start();

        // Create visionInput buffer
        visionInput = new SocketVisionReader();
        visionInput.addListener(world);
    }

    public static void runSimulator(boolean balleIsBlue) {
        Simulator simulator = Simulator.createSimulator();
        BasicWorld world = new BasicWorld(balleIsBlue);
        AbstractStrategy s;
        SimpleWorldGUI gui;
        simulator.addListener(world);

        gui = new SimpleWorldGUI(world);
        gui.start();

        SoftBot bot;
        if (balleIsBlue)
            bot = simulator.getYellowSoft();
        else
            bot = simulator.getBlueSoft();

        System.out.println(bot);

        s = new DummyStrategy(bot, world);
        s.start();

    }
}
