package balle.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	// GUI
	private JPanel top;
	private JPanel southPanel;
	private JButton button;
	private JButton randomButton;
	private JButton resetButton;
	private JButton noiseButton;
	private JComboBox menu;
	private JLabel label;

	private ArrayList<String> stratTabs;
	private String[] strings = new String[0];

	private StrategyRunner strategyRunner;
	private Controller controller;
	private AbstractWorld world;
	private Simulator simulator;

	private final static String LABEL_TEXT = "Select strategy";

	public StratTab(Controller controller, AbstractWorld world,
			StrategyRunner strategyRunner, Simulator simulator) {
		super();
		this.controller = controller;
		this.world = world;
		this.simulator = simulator;
		// Initialise strategy runner
		this.strategyRunner = strategyRunner;

		// Class Variables
		top = new JPanel();
		southPanel = new JPanel();
		top.setLayout(new BorderLayout());
		southPanel.setLayout(new BorderLayout());

		stratTabs = new ArrayList<String>();

		button = new JButton("Start");
		button.addActionListener(this);
		button.setActionCommand("startstop");

		randomButton = new JButton("Randomise");
		randomButton.addActionListener(this);
		randomButton.setEnabled(simulator != null);
		randomButton.setActionCommand("randomise");

		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		resetButton.setEnabled(simulator != null);
		resetButton.setActionCommand("reset");

		noiseButton = new JButton("Noise: Off");
		noiseButton.addActionListener(this);
		noiseButton.setActionCommand("noise");

		southPanel.add(BorderLayout.WEST, randomButton);
		southPanel.add(BorderLayout.SOUTH, resetButton);
		southPanel.add(BorderLayout.EAST, noiseButton);

		menu = new JComboBox(strings);

		label = new JLabel(LABEL_TEXT);

		top.add(BorderLayout.WEST, menu);
		top.add(BorderLayout.EAST, button);
		top.add(BorderLayout.NORTH, label);
		top.add(BorderLayout.SOUTH, southPanel);

		this.add(top);
	}

	@Override
	public final void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("startstop")) {
			if (button.getText().equals("Start")) {
				String selectedStrategy = stratTabs
						.get(menu.getSelectedIndex());
				try {
					strategyRunner.startStrategy(StrategyFactory
							.createClass(selectedStrategy));
				} catch (UnknownDesignatorException e) {
					LOG.error("Cannot start starategy \"" + selectedStrategy
							+ "\": " + e.toString());
					return;
				}
				button.setText("Stop");

			} else {
				button.setText("Start");
				strategyRunner.stopStrategy();
			}
		} else if (event.getActionCommand().equals("randomise")) {
			try {
				strategyRunner.stopStrategy();
			} catch (NullPointerException e) {
				System.err
						.println("No currently running Strategy. World randomised "
								+ "\": " + e);
			}
			button.setText("Start");
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
			button.setText("Start");
			resetRobots(simulator);
			resetBall(simulator);
		} else if (event.getActionCommand().equals("noise")) {
			if (noiseButton.getText().equals("Noise: Off")) {
				noiseButton.setText("Noise: On");
				simulator.noisy = true;
			} else {
				noiseButton.setText("Noise: Off");
				simulator.noisy = false;
			}
		}
	}

	public String[] getStrings() {
		int size = stratTabs.size();
		String[] out = new String[size];
		for (int i = 0; i < size; i++) {
			out[i] = (i + 1) + ": " + stratTabs.get(i);
		}
		return out;
	}

	public final void addStrategy(String designator) {
		stratTabs.add(designator);
		strings = getStrings();
		this.remove(menu);
		menu = new JComboBox(strings);
		top.add(BorderLayout.WEST, menu);
	}

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
