package balle.main.fieldinputs;

public class DoubleField extends TextField {

    public DoubleField(String name) {
        super(name);
    }

    public Object getValue() {
        String superValue = (String) super.getValue();
        return Double.parseDouble(superValue);
    }

}
