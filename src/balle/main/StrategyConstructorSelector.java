package balle.main;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import balle.main.fieldinputs.BooleanField;
import balle.main.fieldinputs.DoubleField;
import balle.main.fieldinputs.FieldInput;

public class StrategyConstructorSelector extends JPanel {
    private final static Logger LOG = Logger
            .getLogger(StrategyConstructorSelector.class);

    private final Class<?>[] args;
    private final String[] argNames;
    private FieldInput[] fields;

    public StrategyConstructorSelector(String strategyName, String[] argNames,
            Class<?>[] args) {
        super();
        this.argNames = argNames;
        this.args = args;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(strategyName + " arguments:", SwingConstants.CENTER);
        name.setPreferredSize(new Dimension(300, 20));
        this.add(name);
        generateArgFields();
        this.validate();
    }

    private void generateArgFields()
    {
        FieldInput[] fs = new FieldInput[args.length];
        for (int i = 0; i < args.length; i++)
        {
            Class<?> arg = args[i];
            String argName = argNames[i];
            if (double.class.isAssignableFrom(arg)) {
                fs[i] = new DoubleField(argName);
                this.add(fs[i]);
            } else if (boolean.class.isAssignableFrom(arg)) {
                fs[i] = new BooleanField(argName);
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
