package balle.main;

import static java.util.Arrays.asList;

import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import balle.bluetooth.Communicator;
import balle.controller.BluetoothController;
import balle.controller.Controller;
import balle.controller.DummyController;
import balle.io.reader.SocketVisionReader;
import balle.logging.StrategyLogAppender;
import balle.misc.Globals;
import balle.simulator.Simulator;
import balle.simulator.SoftBot;
import balle.strategy.StrategyRunner;
import balle.world.AbstractWorld;
import balle.world.BasicWorld;
import balle.world.filter.HeightFilter;

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
		try {
			getOptionParser().printHelpOn(System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static OptionParser getOptionParser() {
		OptionParser parser = new OptionParser();
		parser.acceptsAll(asList("s", "simulator"));
		parser.acceptsAll(asList("d", "dummy-controller"));
		parser.acceptsAll(asList("c", "colour", "color")).withRequiredArg()
				.ofType(String.class);
		parser.acceptsAll(asList("g", "goal")).withRequiredArg()
				.ofType(String.class);
		parser.acceptsAll(asList("v", "verbose"));
		return parser;
	}

	public static void initialiseLogging(StrategyLogPane strategyLogPane,
			boolean verbose) {

		// For all other Log messages. Throws error for some reason
		// don't know if causing any real problems
		PropertyConfigurator.configure("log4j.properties");

		if (verbose) {
			Logger.getRootLogger().setLevel(Level.TRACE);
		} else {
			Logger.getRootLogger().setLevel(Level.DEBUG);
		}
		// Make sure to log strategy logs to the GUI as well
		Logger strategyLogger = Logger.getLogger("balle.strategy");
		Appender strategyAppender = new StrategyLogAppender(strategyLogPane);
		strategyLogger.addAppender(strategyAppender);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OptionParser parser = getOptionParser();
		OptionSet options = parser.parse(args);

		// Get the colour
		boolean balleIsBlue;
		if ("blue".equals(options.valueOf("colour")))
			balleIsBlue = true;
		else if ("yellow".equals(options.valueOf("colour")))
			balleIsBlue = false;
		else {
			System.out
					.println("Invalid colour provided, try one of the following:");
			System.out.println("javac balle.main.Runner -c blue");
			System.out.println("javac balle.main.Runner -c yellow");
			print_usage();
			System.exit(-1);
			balleIsBlue = false; // This is just to fool Eclipse about
									// balleIsBlue initialisation
		}

		boolean goalIsLeft;
		if ("left".equals(options.valueOf("goal")))
			goalIsLeft = true;
		else if ("right".equals(options.valueOf("goal")))
			goalIsLeft = false;
		else {
			System.out
					.println("Invalid goal provided, try one of the following:");
			System.out.println("javac balle.main.Runner -g left");
			System.out.println("javac balle.main.Runner -g right");
			print_usage();
			System.exit(-1);
			goalIsLeft = false; // This is just to fool Eclipse about
								// goalIsLeft initialisation
		}
		StrategyLogPane strategyLog = new StrategyLogPane();

		initialiseLogging(strategyLog, options.has("verbose"));

		if (options.has("simulator"))
			runSimulator(balleIsBlue, goalIsLeft, strategyLog);
		else
			runRobot(balleIsBlue, goalIsLeft, options.has("dummy-controller"),
					strategyLog);
	}

	public static void initialiseGUI(Controller controllerA,
			Controller controllerB, AbstractWorld worldA, AbstractWorld worldB,
			StrategyLogPane strategyLog, Simulator simulator) {
		SimpleWorldGUI gui = new SimpleWorldGUI(worldA);
		GUITab mainWindow = new GUITab();

		StrategyRunner strategyRunner = new StrategyRunner(controllerA,
				controllerB, worldA, worldB, gui);

		StratTab strategyTab = new StratTab(controllerA, controllerB, worldA,
				worldB, strategyRunner, simulator);

		// for (String strategy : StrategyFactory.availableDesignators())
		// strategyTab.addStrategy(strategy);

		mainWindow.addToSidebar(strategyTab);
		mainWindow.addToSidebar(strategyLog);

		mainWindow.addToMainPanel(gui.getPanel());
		gui.start();

		strategyRunner.start();

	}

	public static void runRobot(boolean balleIsBlue, boolean goalIsLeft,
			boolean useDummyController, StrategyLogPane strategyLog) {

		AbstractWorld world;
		SocketVisionReader visionInput;
		Controller controllerA;

		// Initialise world
		world = new BasicWorld(balleIsBlue, goalIsLeft, Globals.getPitch());
		world.addFilter(new HeightFilter(world.getPitch().getPosition(),
				Globals.P0_CAMERA_HEIGHT));

		// Moving this forward so we do not start a GUI until controller is
		// initialised
		// If you're getting a merge conflict here leave this before
		// SimpleWorldGUI start!

		if (useDummyController)
			controllerA = new DummyController();
		else
			controllerA = new BluetoothController(new Communicator());

		// Wait for controller to initialise
		while (!controllerA.isReady()) {
			continue;
		}

		initialiseGUI(controllerA, null, world, null, strategyLog, null);
		System.out.println("NOOOOOOO THIS SHOULDN'T HAPPEN");
		// Create visionInput buffer
		visionInput = new SocketVisionReader();
		visionInput.addListener(world);
	}

	public static void runSimulator(boolean balleIsBlue, boolean goalIsLeft,
			StrategyLogPane strategyLog) {
		Simulator simulator = Simulator.createSimulator();

		BasicWorld worldA = new BasicWorld(balleIsBlue, goalIsLeft,
				Globals.getPitch());
		simulator.addListener(worldA);

		BasicWorld worldB = new BasicWorld(!balleIsBlue, !goalIsLeft,
				Globals.getPitch());
		simulator.addListener(worldB);

		SoftBot botA, botB;
		if (!balleIsBlue) {
			botA = simulator.getYellowSoft();
			botB = simulator.getBlueSoft();
		} else {
			botA = simulator.getBlueSoft();
			botB = simulator.getYellowSoft();
		}

		System.out.println(botA);
		System.out.println(botB);

		initialiseGUI(botA, botB, worldA, worldB, strategyLog, simulator);
		System.out.println("THIS SHOULD HAPPEN");
	}
}
