package balle.main.fieldinputs;

public class DoubleField extends TextField {

    public DoubleField(String name) {
        super(name);
    }

    public Object getValue() {
        String superValue = (String) super.getValue();
        if ("".equals(superValue))
            return 0;
        return Double.parseDouble(superValue);
    }

}
