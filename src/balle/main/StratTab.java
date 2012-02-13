package balle.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import balle.controller.Controller;
import balle.strategy.AbstractStrategy;
import balle.strategy.StrategyFactory;
import balle.strategy.UnknownDesignatorException;
import balle.world.AbstractWorld;

@SuppressWarnings("serial")
public class StratTab extends JPanel implements ActionListener {

    // GUI
    private JPanel              top;
    private JButton             button;
    private JComboBox           menu;
    private JLabel              label;

    private ArrayList<String>   stratTabs;
    private String[]            strings                = new String[0];

    private AbstractStrategy    currentRunningStrategy = null;
    private Controller          controller;
    private AbstractWorld       world;

    private final static String LABEL_TEXT             = "Select strategy";

    public StratTab(Controller controller, AbstractWorld world) {
        super();
        this.controller = controller;
        this.world = world;

        // Class Variables
        top = new JPanel();
        stratTabs = new ArrayList<String>();

        top.setLayout(new BorderLayout());

        button = new JButton("Start");
        button.addActionListener(this);

        menu = new JComboBox(strings);

        label = new JLabel(LABEL_TEXT);

        top.add(BorderLayout.WEST, menu);
        top.add(BorderLayout.EAST, button);
        top.add(BorderLayout.NORTH, label);

        this.add(top);
    }

    @Override
    public final void actionPerformed(ActionEvent event) {
        if (button.getText().equals("Start")) {
            String selectedStrategy = stratTabs.get(menu.getSelectedIndex());
            try {
                currentRunningStrategy = StrategyFactory.createClass(
                        selectedStrategy, controller, world);
            } catch (UnknownDesignatorException e) {
                System.err.println("Could not start strategy \""
                        + currentRunningStrategy + "\": " + e);
                return;
            }
            button.setText("Stop");
            currentRunningStrategy.start();

        } else {
            button.setText("Start");
            currentRunningStrategy.cancel();
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

}
