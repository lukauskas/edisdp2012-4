package balle.main;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.spi.LoggingEvent;

public class StrategyLogPane extends JPanel {

    private final DefaultTableModel model;
    private final JTable            table;

    private final static int        MAX_ROWS = 20;

    public StrategyLogPane() {
        super();
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        model.addColumn("Level");
        model.addColumn("Message");

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        this.setLayout(new BorderLayout());

        this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void append(LoggingEvent e) {
        if (model.getRowCount() > MAX_ROWS)
            model.removeRow(0);

        String[] rowData = new String[model.getColumnCount()];
        rowData[0] = e.getLevel().toString();
        rowData[1] = (String) e.getMessage();

        model.addRow(rowData);
    }
}
