package balle.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.spi.LoggingEvent;

public class StrategyLogPane extends JPanel {

	private final DefaultTableModel model;
	private final JTable table;

    private final static int MAX_ROWS = 20;

    public StrategyLogPane() {
		super();
		model = new DefaultTableModel();
		table = new JTable(model);
		TableCellRenderer renderer = new EvenOddRenderer();
		table.setDefaultRenderer(Object.class, renderer);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		model.addColumn("Message");
		model.addColumn("Level");
		table.getColumnModel().getColumn(0).setWidth(500);
		table.getColumnModel().getColumn(1).setMaxWidth(0);

		this.setLayout(new BorderLayout());

        this.add(new JScrollPane(table), BorderLayout.CENTER);
	}


    public void append(LoggingEvent e) {
		if (model.getRowCount() > MAX_ROWS)
			model.removeRow(0);

		String[] rowData = new String[model.getColumnCount()];
		rowData[1] = e.getLevel().toString();
		rowData[0] = (String) e.getMessage();

        model.addRow(rowData);
	}
}

class EvenOddRenderer implements TableCellRenderer {

	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);

		Color foreground, background;
		row = table.convertRowIndexToModel(row);
		String s = table.getModel().getValueAt(row, 1).toString();
		if (s == "INFO") {
				foreground = Color.blue;
				background = Color.white;
			} else {
			if (s == "DEBUG") {
				background = Color.red;
				foreground = Color.white;
			} else {
				foreground = Color.black;

				background = Color.yellow;
			}}

		renderer.setForeground(foreground);
		renderer.setBackground(background);
		return renderer;
	}
}
