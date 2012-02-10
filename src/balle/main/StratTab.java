package balle.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import balle.strategy.AbstractStrategy;

@SuppressWarnings("serial")
public class StratTab extends GUITab implements ActionListener {

	// GUI
	private JPanel top;
	private JButton button;
	private JComboBox menu;

	private ArrayList<AbstractStrategy> stratTabs;
	private String[] strings = new String[0];

	public StratTab() {
		// Class Variables
		top = new JPanel();
		stratTabs = new ArrayList<AbstractStrategy>();

		top.setLayout(new BorderLayout());

		button = new JButton("Stop");
		button.addActionListener(this);
		
		menu = new JComboBox(strings);

		top.add(BorderLayout.WEST, menu);
		top.add(BorderLayout.EAST, button);

		this.add(top);
	}

	@Override
	public final void actionPerformed(ActionEvent e) {
		if (button.getText().equals("Stop")) {
			button.setText("Start");
			stratTabs.get(menu.getSelectedIndex()).pause(true);
		} else {
			button.setText("Stop");
			stratTabs.get(menu.getSelectedIndex()).pause(false);
		}
	}
	
	public String[] getStrings() {
		int size = stratTabs.size();
		String[] out = new String[size];
		for (int i = 0; i < size; i++) {
			out[i] = (i + 1) + ": " + stratTabs.get(i).toString();
		}
		return out;
	}

	public final void add(AbstractStrategy x) {
		stratTabs.add(x);
		strings = getStrings();
		this.remove(menu);
		menu = new JComboBox(strings);
		top.add(BorderLayout.WEST, menu);
	}

}
