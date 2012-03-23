package balle.main;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import balle.main.fieldinputs.BooleanField;
import balle.main.fieldinputs.DoubleField;
import balle.main.fieldinputs.FieldInput;

public class StrategyConstructorSelector extends JPanel {
    private final static Logger LOG = Logger
            .getLogger(StrategyConstructorSelector.class);

    private final Class<?>[] args;
    private FieldInput[] fields;

    public StrategyConstructorSelector(String strategyName, Class<?>[] args) {
        super();
        this.args = args;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel(strategyName + " arguments:"));
        generateArgFields();
        this.validate();
    }

    private void generateArgFields()
    {
        FieldInput[] fs = new FieldInput[args.length];
        for (int i = 0; i < args.length; i++)
        {
            Class<?> arg = args[i];
            if (double.class.isAssignableFrom(arg)) {
                fs[i] = new DoubleField(Integer.toString(i));
                this.add(fs[i]);
            } else if (boolean.class.isAssignableFrom(arg)) {
                fs[i] = new BooleanField(Integer.toString(i));
                this.add(fs[i]);
            }
            else
            {
                LOG.error("Cannot assign arguments of type " + arg.getName());
            }
        }
        fields = fs;

        if (fields.length == 0)
            this.add(new JLabel("none"));
    }

    public Object[] getValues() {
        Object[] ans = new Object[args.length];
        for (int i = 0; i < fields.length; i++) {
            ans[i] = fields[i].getValue();
        }
        return ans;
    }

}
