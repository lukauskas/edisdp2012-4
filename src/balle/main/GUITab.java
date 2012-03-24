package balle.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUITab extends JPanel {

    private JFrame frame;
    private JPanel main;
    private JPanel sidebar;
    private JPanel mainPanel;

	public GUITab() {
        super();

        frame = new JFrame();
		frame.setSize(1100, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        main = new JPanel();
        main.setLayout(new BorderLayout(5, 0));
        sidebar = new JPanel();
		sidebar.setPreferredSize(new Dimension(350, 500));
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(700, 500));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		main.add(BorderLayout.WEST, sidebar);
		main.add(BorderLayout.CENTER, mainPanel);

        frame.getContentPane().add(BorderLayout.CENTER, main);
    }

    public final void addToMainPanel(Component c) {
        mainPanel.add(c, BorderLayout.CENTER);
        mainPanel.validate();
        main.validate();
    }

    public final void addToSidebar(Component c) {
        sidebar.add(c);
        sidebar.validate();
        main.validate();
    }

}
