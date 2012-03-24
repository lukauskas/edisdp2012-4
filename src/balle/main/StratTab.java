package balle.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import balle.controller.Controller;
import balle.memory.ConfigFile;
import balle.misc.Globals;
import balle.simulator.Simulator;
import balle.strategy.StrategyFactory;
import balle.strategy.StrategyRunner;
import balle.strategy.UnknownDesignatorException;
import balle.world.AbstractWorld;

@SuppressWarnings("serial")
public class StratTab extends JPanel implements ActionListener {

	private static final Logger LOG = Logger.getLogger(StratTab.class);

	private final Config config;

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
	private JButton saveButton;
	private boolean isBlue;
	private ArrayList<String> stratTabs;
	private String[] strings = new String[0];

    private StrategyConstructorSelector parametersPanelGreen = null;
    private StrategyConstructorSelector parametersPanelRed = null;

	GridBagConstraints c = new GridBagConstraints();
	private StrategyRunner strategyRunner;
	private AbstractWorld worldA;
	private Simulator simulator;
    private StrategyFactory     strategyFactory;

	private final static String GREEN_LABEL_TEXT = "Select Green strategy";
	private final static String RED_LABEL_TEXT = "Select Red strategy";

	public StratTab(Config config, Controller controllerA,
			Controller controllerB,
			AbstractWorld worldA, AbstractWorld worldB,
			StrategyRunner strategyRunner, Simulator simulator,
			StrategyFactory strategyFactory) {

		super();

		this.config = config;
		this.worldA = worldA;
		this.simulator = simulator;
		// Initialise strategy runner
		this.strategyRunner = strategyRunner;
        this.strategyFactory = strategyFactory;

		// Declare layout of buttons etc
		// Layout composed of 3 by 6 grid (0 indexed)
		// (GridBagConstraints controls properties of grid
		// for each component. Leftmost column is gridx = 0
		// and topmost row is grid y = 0)

		String[] names = new String[strategyFactory.availableDesignators()
				.size()];
		for (int count = 0; count < strategyFactory.availableDesignators()
				.size(); count++) {
			names[count] = strategyFactory.availableDesignators().get(count);
		}

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		controlPanel = new JPanel(new GridBagLayout());
		stratTabs = new ArrayList<String>();
		greenLabel = new JLabel(GREEN_LABEL_TEXT);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(1, 5, 1, 5);
		controlPanel.add(greenLabel, c);

		greenStrategy = new JComboBox(names);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		controlPanel.add(greenStrategy, c);

		redLabel = new JLabel(RED_LABEL_TEXT);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		controlPanel.add(redLabel, c);

		redStrategy = new JComboBox(names);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		redStrategy.setEnabled(simulator != null);
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

		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setActionCommand("save");
		c.gridx = 2;
		c.gridy = 2;
		controlPanel.add(saveButton, c);

        greenStrategy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxChanged")) {
                    generateStrategyConstructorSelector();
                }
            }
        });
        redStrategy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxChanged")) {
                    generateStrategyConstructorSelector();
                }
            }
        });
        this.add(controlPanel);
        
        generateStrategyConstructorSelector();

		// Interprett Config object

		greenStrategy.setSelectedItem(config.get(Config.GREEN_STRATEGY));
		if (simulator != null)
			redStrategy.setSelectedItem(config.get(Config.RED_STRATEGY));

	}

    public void generateStrategyConstructorSelector() {
        if (parametersPanelGreen != null) {
            this.remove(parametersPanelGreen);
            this.validate();
        }
        
        String designator = greenStrategy.getSelectedItem().toString();
        try {
            parametersPanelGreen = new StrategyConstructorSelector(designator,
                    strategyFactory.getArgumentNames(designator),
                    strategyFactory.getArguments(designator));
        } catch (UnknownDesignatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.add(parametersPanelGreen);

        if (parametersPanelRed != null) {
            this.remove(parametersPanelRed);
            this.invalidate();
        }

        String designatorRed = redStrategy.getSelectedItem().toString();
        try {
            parametersPanelRed = new StrategyConstructorSelector(designatorRed,
                    strategyFactory.getArgumentNames(designatorRed),
                    strategyFactory.getArguments(designatorRed));
        } catch (UnknownDesignatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.add(parametersPanelRed);

        this.validate();

    }

	// Listener for button clicks
	@Override
	public final void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("startstop")) {
			if (startButton.getText().equals("Start")) {

				String selectedStrategyA = (String) greenStrategy
						.getSelectedItem();

				String selectedStrategyB;
				if (simulator == null) {
					selectedStrategyB = (String) "NullStrategy";
				} else {
					selectedStrategyB = (String) redStrategy.getSelectedItem();
					// selectedStrategyB = (String)
					// redStrategy.getSelectedItem();
				}

				config.set(Config.GREEN_STRATEGY, selectedStrategyA);
				if (simulator != null)
					config.set(Config.RED_STRATEGY, selectedStrategyB);

				try {
                    strategyRunner.startStrategy(
                            strategyFactory.createClass(selectedStrategyA,
                                    parametersPanelGreen.getValues()),
                            strategyFactory.createClass(selectedStrategyB,
                                    parametersPanelRed.getValues()));

				} catch (UnknownDesignatorException e) {
					LOG.error("Cannot start strategy \"" + selectedStrategyA
							+ "\": " + e.toString());
					System.out.println("Valiant effort, chaps");
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
				simulator.setIsNoisy(true);
			} else {
				noiseButton.setText("Noise: Off");
				simulator.setIsNoisy(false);
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
		} else if (event.getActionCommand().equals("save")) {
			try {
				ConfigFile cf = new ConfigFile(Globals.resFolder,
						Globals.configFolder);
				cf.write(config);
			} catch (IOException e) {
				System.err.println("Couldn't save configurations.");
			}

		}
	}

	public boolean isSimulator() {
		return simulator != null;
	}

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
