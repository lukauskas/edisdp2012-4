package balle.main;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class GUITab extends JPanel {

    private JFrame      frame;
    private JTabbedPane tab_pane;
    private StratTab    strategies;

    public GUITab() {
        super();

        frame = new JFrame();
        frame.setSize(770, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        tab_pane = new JTabbedPane();
        frame.getContentPane().add(BorderLayout.CENTER, tab_pane);
    }

    public final void addToFrame(Component c, String x) {
        tab_pane.add(c, x);
    }

}
