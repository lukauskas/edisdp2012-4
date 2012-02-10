package balle.main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import balle.strategy.AbstractStrategy;

@SuppressWarnings("serial")
public class GUITab extends JPanel {
	
	private static JFrame frame;
	private static JTabbedPane tab_pane;
	private static StratTab strategies;
	
	static {
		frame = new JFrame();
		frame.setSize(770, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
		
		tab_pane = new JTabbedPane();
		frame.getContentPane().add(BorderLayout.CENTER, tab_pane);
		
		strategies = new StratTab();
		((GUITab)strategies).addToFrame("Strategies");
	}
	
	public GUITab() {
		super();
		
		
	}
	
	public final void addToFrame(String x) {
		tab_pane.add(((GUITab)this), x);
	}
	
	public static void addStrategy(AbstractStrategy x) {
		strategies.add(x);
	}

}
