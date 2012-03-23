package balle.main.fieldinputs;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class TextField extends FieldInput {

    private final JTextField input;

    public TextField(String name) {
        super(name);
        this.setLayout(new BorderLayout());
        input = new JTextField(20);
        this.add(new JLabel(name), BorderLayout.WEST);
        this.add(input, BorderLayout.EAST);
    }

    public Object getValue() {
        return input.getText();
    }

}
