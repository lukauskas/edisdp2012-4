package balle.main;

import balle.brick.Controller;
import balle.controller.DummyController;
import balle.io.reader.ScannerReader;
import balle.strategy.AbstractStrategy;
import balle.strategy.DummyStrategy;
import balle.world.AbstractWorld;
import balle.world.BasicWorld;

/**
 * This is where the main executable code for BALL-E lies. It is responsible of
 * initialising various subsystems of the code and make sure they interact.
 * 
 * 
 * @author s0909773
 * 
 */
public class Runner {
    protected AbstractWorld    world;
    protected ScannerReader    visionInput;
    protected Controller       controller;
    protected AbstractStrategy strategy;

    public Runner(boolean balleIsBlue) {

        // Initialise world
        world = new BasicWorld(balleIsBlue);
        // Create visionInput buffer
        visionInput = new ScannerReader();
        visionInput.addListener(world);
        visionInput.setPriority(Thread.MAX_PRIORITY);
        // visionInput.setPriority(Thread.MAX_PRIORITY);
        visionInput.start();

        // Initialise world
        world = new BasicWorld(balleIsBlue);

        // Initialise controller
        controller = new DummyController();
        strategy = new DummyStrategy(controller, world);
        strategy.setPriority(Thread.MAX_PRIORITY);
        strategy.start();

    }

    // ---------------------------------------------------------------

    private static void print_usage() {
        System.out.println("Usage: java balle.main.Runner <balle_colour>");
        System.out
                .println("Where <balle_colour> is either \"blue\" or \"yellow\"");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Check the usage
        if (args.length != 1) {
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

        new Runner(balleIsBlue);
    }
}
