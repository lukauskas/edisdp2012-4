package balle.main.fieldinputs;

import javax.swing.JPanel;

public abstract class FieldInput extends JPanel {

    protected final String name;

    public FieldInput(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Object getValue();

}