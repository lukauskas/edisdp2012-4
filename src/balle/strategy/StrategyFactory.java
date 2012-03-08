package balle.strategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import balle.strategy.planner.AbstractPlanner;

public class StrategyFactory {

    private static final Logger LOG = Logger.getLogger(StrategyFactory.class);

    private static HashMap<String, Method> runnableStrategies = null;

    private HashMap<String, Method> getRunnableStrategies() {
        if (runnableStrategies != null)
            return runnableStrategies;

        runnableStrategies = new HashMap<String, Method>();
        // Reflections reflections = new Reflections("balle.strategy");
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                ClasspathHelper.forPackage("balle.strategy")).setScanners(
                new MethodAnnotationsScanner()));
        Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(FactoryMethod.class);
        for (Method m : annotatedMethods)
        {
            String designator = m.getAnnotation(FactoryMethod.class).designator();
            if (m.getDeclaringClass().isInstance(AbstractPlanner.class))
            {
                LOG.debug("Skipping designator " + designator
                        + " as it is not defined in an instance of AbstractPlanner");
                continue;
            }
            runnableStrategies.put(designator, m);
        }
        LOG.debug("Found " + runnableStrategies.size() + " runnable strategies");
        return runnableStrategies;

    }

    public ArrayList<String> availableDesignators() {
        runnableStrategies = getRunnableStrategies();

        ArrayList<String> titles = new ArrayList<String>();

        Set<String> keys = runnableStrategies.keySet();
        for (String key : keys)
            titles.add(key);
        return titles;

	}

    public AbstractPlanner createClass(String designator)
            throws UnknownDesignatorException {
        runnableStrategies = getRunnableStrategies();
        Method m = runnableStrategies.get(designator);

        if (m == null)
            throw new UnknownDesignatorException("Don't know strategy \"" + designator + "\"");

        AbstractPlanner strategy;
        try {
            strategy = (AbstractPlanner) m.invoke(null, new Object[] {});
        } catch (IllegalArgumentException e) {
            LOG.error("IllegalArgumentException while trying to invoke " + designator);
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            LOG.error("IllegalAccessException while trying to invoke " + designator);
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            LOG.error("InvocationTargetException while trying to invoke " + designator);
            e.printStackTrace();
            return null;
        }
        
        return strategy;
    }
}
