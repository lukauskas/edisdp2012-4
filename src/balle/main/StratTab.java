package balle.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.simulator.Simulator;
import balle.strategy.StrategyFactory;
import balle.strategy.StrategyRunner;
import balle.strategy.UnknownDesignatorException;
import balle.world.AbstractWorld;

@SuppressWarnings("serial")
public class StratTab extends JPanel implements ActionListener {

	private static final Logger LOG = Logger.getLogger(StratTab.class);
	// GUI declarations

	private JPanel controlPanel;
	private JLabel greenLabel;
	private JLabel redLabel;
	private JComboBox greenStrategy;
	private JComboBox redStrategy;
	private JButton startButton;
	private JButton switchGoals;
	private JButton switchRobot;
	private JButton noiseButton;
	private JButton randomButton;
	private JButton resetButton;
	private boolean isBlue;

	private StrategyRunner strategyRunner;
	private Controller controllerA;
	private Controller controllerB;
	private AbstractWorld worldA;
	private AbstractWorld worldB;
	private Simulator simulator;

	private final static String GREEN_LABEL_TEXT = "Select X strategy";
	private final static String RED_LABEL_TEXT = "Select Y strategy";

	public StratTab(Controller controllerA, Controller controllerB,
			AbstractWorld worldA, AbstractWorld worldB,
			StrategyRunner strategyRunner,
			Simulator simulator) {
		super();
		this.controllerA = controllerA;
		this.controllerB = controllerB;
		this.worldA = worldA;
		this.worldB = worldB;
		this.simulator = simulator;
		// Initialise strategy runner
		this.strategyRunner = strategyRunner;

		// Declare layout of buttons etc
		// Layout composed of 3 by 6 grid (0 indexed)
		// (GridBagConstraints controls properties of grid
		// for each component. Leftmost column is gridx = 0
		// and topmost row is grid y = 0)
		controlPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		greenLabel = new JLabel(GREEN_LABEL_TEXT);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(1, 5, 1, 5);
		controlPanel.add(greenLabel, c);

		greenStrategy = new JComboBox(StrategyFactory.availableDesignators());
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		controlPanel.add(greenStrategy, c);

		redLabel = new JLabel(RED_LABEL_TEXT);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		controlPanel.add(redLabel, c);

		redStrategy = new JComboBox(StrategyFactory.availableDesignators());
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		controlPanel.add(redStrategy, c);

		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setActionCommand("startstop");
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		controlPanel.add(startButton, c);

		switchGoals = new JButton("Switch Goals");
		switchGoals.addActionListener(this);
		switchGoals.setActionCommand("goals");
		c.gridx = 0;
		c.gridy = 4;
		controlPanel.add(switchGoals, c);

		isBlue = worldA.isBlue();
		if (isBlue) {
			switchRobot = new JButton("Robot: Blue");
		} else {
			switchRobot = new JButton("Robot: Yellow");
		}
		switchRobot.addActionListener(this);
		switchRobot.setActionCommand("robot");
		c.gridx = 1;
		c.gridy = 4;
		controlPanel.add(switchRobot, c);

		noiseButton = new JButton("Noise: On");
		noiseButton.addActionListener(this);
		noiseButton.setEnabled(simulator != null);
		noiseButton.setActionCommand("noise");
		c.gridx = 2;
		c.gridy = 4;
		controlPanel.add(noiseButton, c);

		randomButton = new JButton("Randomise");
		randomButton.addActionListener(this);
		randomButton.setEnabled(simulator != null);
		randomButton.setActionCommand("randomise");
		c.gridx = 2;
		c.gridy = 5;
		controlPanel.add(randomButton, c);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		resetButton.setEnabled(simulator != null);
		resetButton.setActionCommand("reset");
		c.gridx = 0;
		c.gridy = 5;
		controlPanel.add(resetButton, c);

		this.add(controlPanel);
	}

	// Listener for button clicks
	@Override
	public final void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("startstop")) {
			if (startButton.getText().equals("Start")) {
				String selectedStrategyA = (String) greenStrategy
						.getSelectedItem();
				String selectedStrategyB = (String) redStrategy
						.getSelectedItem();
				try {
					strategyRunner.startStrategy(
							StrategyFactory.createClass(selectedStrategyA),
							StrategyFactory.createClass(selectedStrategyB));
				} catch (UnknownDesignatorException e) {
					LOG.error("Cannot start starategy \"" + selectedStrategyA
							+ "\": " + e.toString());
					return;
				}
				startButton.setText("Stop");
				switchGoals.setEnabled(false);
				switchRobot.setEnabled(false);
			} else {
				startButton.setText("Start");
				strategyRunner.stopStrategy();
				switchGoals.setEnabled(true);
				switchRobot.setEnabled(true);
			}
		} else if (event.getActionCommand().equals("randomise")) {
			try {
				strategyRunner.stopStrategy();
			} catch (NullPointerException e) {
				System.err
						.println("No currently running Strategy. World randomised "
								+ "\": " + e);
			}
			startButton.setText("Start");
			strategyRunner.stopStrategy();
			switchGoals.setEnabled(true);
			switchRobot.setEnabled(true);
			randomiseRobots(simulator);
			randomiseBall(simulator);
		} else if (event.getActionCommand().equals("reset")) {
			try {
				strategyRunner.stopStrategy();
			} catch (NullPointerException e) {
				System.err
						.println("No currently running Strategy. World reset "
								+ "\": " + e);
			}
			startButton.setText("Start");
			strategyRunner.stopStrategy();
			switchGoals.setEnabled(true);
			switchRobot.setEnabled(true);
			resetRobots(simulator);
			resetBall(simulator);
		} else if (event.getActionCommand().equals("noise")) {
			if (noiseButton.getText().equals("Noise: Off")) {
				noiseButton.setText("Noise: On");
				simulator.isNoisy(true);
			} else {
				noiseButton.setText("Noise: Off");
				simulator.isNoisy(false);
			}
		} else if (event.getActionCommand().equals("goals")) {
			if (worldA.getGoalPosition()) {
				worldA.setGoalPosition(false);
			} else {
				worldA.setGoalPosition(true);
			}
		} else if (event.getActionCommand().equals("robot")) {

			if (worldA.isBlue()) {
				switchRobot.setText("Robot: Yellow");
				worldA.setIsBlue(false);
				if (simulator != null) {
					strategyRunner.setController(simulator.getYellowSoft(),
							simulator.getBlueSoft());
				}
			} else {
				switchRobot.setText("Robot: Blue");
				worldA.setIsBlue(true);
				if (simulator != null) {
					strategyRunner.setController(simulator.getBlueSoft(),
							simulator.getYellowSoft());
				}
			}
		}
	}

	// Jon: NOT NEEDED?
	// Using getSelectedItem() in ActionPerformed instead

	// public String[] getStrings() {
	// int size = stratTabs.size();
	// String[] out = new String[size];
	// for (int i = 0; i < size; i++) {
	// out[i] = (i + 1) + ": " + stratTabs.get(i);
	// }
	// return out;
	// }
	//
	// public final void addStrategy(String designator) {
	// stratTabs.add(designator);
	// strings = getStrings();
	// this.remove(greenStrategy);
	// greenStrategy = new JComboBox(strings);
	// top.add(BorderLayout.WEST, greenStrategy);
	// }

	// Called from reset/randomise buttons
	public void randomiseBall(Simulator s) {
		s.randomiseBallPosition();
	}

	public void randomiseRobots(Simulator s) {
		s.randomiseRobotPositions();
	}

	public void resetBall(Simulator s) {
		s.resetBallPosition();
	}

	public void resetRobots(Simulator s) {
		s.resetRobotPositions();
	}

}
