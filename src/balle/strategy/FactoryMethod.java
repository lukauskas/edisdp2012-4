package balle.strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tells the rest of the system that this method is a factory for the strategy.
 * The system will automatically add this strategy to GUI using this method.
 * 
 * @author saulius
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FactoryMethod {
    /**
     * Designator of the strategy factory method -- how it will be named in the
     * GUI
     * 
     * @return
     */
    String designator();

    String[] parameterNames();
}
