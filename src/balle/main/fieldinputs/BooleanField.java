package balle.main.fieldinputs;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class BooleanField extends FieldInput {

    JComboBox input;
    public BooleanField(String name) {
        super(name);
        this.setLayout(new BorderLayout());
		input = new JComboBox(new String[] { "false", "true" });
        this.add(new JLabel(name), BorderLayout.WEST);
        this.add(input, BorderLayout.EAST);
    }

    @Override
    public Object getValue() {
        return "true".equals(input.getSelectedItem().toString());
    }

}
