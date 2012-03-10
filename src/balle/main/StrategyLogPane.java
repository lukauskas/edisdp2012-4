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

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class StrategyLogPane extends JPanel {

	private final LoggingEventTableModel model;
	private final JTable table;

	private final static int MAX_ROWS = 15;

	public StrategyLogPane() {
		super();
		model = new LoggingEventTableModel();
		table = new JTable(model);
		TableCellRenderer renderer = new EvenOddRenderer();
		table.setDefaultRenderer(Object.class, renderer);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		model.addColumn("Message");
		table.getColumnModel().getColumn(0).setWidth(500);

		this.setLayout(new BorderLayout());

		this.add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void append(LoggingEvent e) {
		if (model.getRowCount() > MAX_ROWS)
			model.removeRow(0);

		LoggingEvent[] rowData = new LoggingEvent[model.getColumnCount()];
		rowData[0] = e;

		model.addRow(rowData);
	}
}

class LoggingEventTableModel extends DefaultTableModel {

	@Override
	public Object getValueAt(int row, int column) {
		return ((LoggingEvent) super.getValueAt(row, column)).getMessage()
				.toString();
	}

	public Level getLevel(int row, int column) {
		return ((LoggingEvent) super.getValueAt(row, column)).getLevel();
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
		Level level = ((LoggingEventTableModel) table.getModel()).getLevel(row,
				0);

		if (level.equals(Level.WARN)) {
			foreground = Color.black;
			background = Color.yellow;
		} else if (level.equals(Level.DEBUG)) {
			background = Color.gray;
			foreground = Color.white;
		} else if (level.equals(Level.ERROR)) {
			background = Color.red;
			foreground = Color.white;
		} else if (level.equals(Level.INFO)) {
			foreground = Color.blue;
			background = Color.white;
		} else {
			foreground = Color.black;
			background = Color.white;
		}

		renderer.setForeground(foreground);
		renderer.setBackground(background);
		return renderer;
	}
}