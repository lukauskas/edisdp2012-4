package balle.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import balle.controller.Controller;
import balle.strategy.AbstractStrategy;
import balle.strategy.StrategyFactory;
import balle.world.AbstractWorld;

@SuppressWarnings("serial")
public class StratTab extends GUITab implements ActionListener {

    // GUI
    private JPanel            top;
    private JButton           button;
    private JComboBox         menu;

    private ArrayList<String> stratTabs;
    private String[]          strings                = new String[0];

    private AbstractStrategy  currentRunningStrategy = null;
    public Controller         controller;
    public AbstractWorld      world;

    public StratTab() {

        // Class Variables
        top = new JPanel();
        stratTabs = new ArrayList<String>();

        top.setLayout(new BorderLayout());

        button = new JButton("Start");
        button.addActionListener(this);

        menu = new JComboBox(strings);

        top.add(BorderLayout.WEST, menu);
        top.add(BorderLayout.EAST, button);

        this.add(top);
    }

    public void setCurrentWorld(AbstractWorld world) {
        this.world = world;
    }

    public void setCurrentController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        if (button.getText().equals("Start")) {
            button.setText("Stop");
            currentRunningStrategy = StrategyFactory.createClass(
                    stratTabs.get(menu.getSelectedIndex()), controller, world);
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

    public final void add(String designator) {
        stratTabs.add(designator);
        strings = getStrings();
        this.remove(menu);
        menu = new JComboBox(strings);
        top.add(BorderLayout.WEST, menu);
    }

}
