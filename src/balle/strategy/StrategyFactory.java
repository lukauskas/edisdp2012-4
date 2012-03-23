package balle.strategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javassist.Modifier;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import balle.strategy.planner.AbstractPlanner;

public class StrategyFactory {

    private static final Logger LOG = Logger.getLogger(StrategyFactory.class);

    private static HashMap<String, Method> runnableStrategies = null;
    private static HashMap<String, String[]> runnableStrategyParameterNames = null;

    public static String[] getParameterNames(String designator)
            throws UnknownDesignatorException {
        String[] ans = runnableStrategyParameterNames.get(designator);
        if (ans == null)
            throw new UnknownDesignatorException("Don't know strategy \""
                    + designator + "\"");

        return ans;
    }


    private HashMap<String, Method> getRunnableStrategies() {
        if (runnableStrategies != null)
            return runnableStrategies;

        runnableStrategies = new HashMap<String, Method>();
        runnableStrategyParameterNames = new HashMap<String, String[]>();
        // Reflections reflections = new Reflections("balle.strategy");
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                ClasspathHelper.forPackage("balle.strategy")).setScanners(
                new MethodAnnotationsScanner()));
        Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(FactoryMethod.class);
        for (Method m : annotatedMethods)
        {
            FactoryMethod annotation = m.getAnnotation(FactoryMethod.class);
            String designator = annotation.designator();
            String[] parameterNames = annotation.parameterNames();
            if (!AbstractPlanner.class.isAssignableFrom(m.getReturnType()))
            {
                LOG.warn("Skipping designator " + designator
                        + " as it is does not return an instance of AbstractPlanner");
                continue;
            } else if (!Modifier.isStatic(m.getModifiers())) {
                LOG.warn("Skipping designator " + designator + " as it is not static");
                continue;
            } else if (m.getParameterTypes().length != annotation
                    .parameterNames().length) {
                LOG.warn("Skipping designator " + designator
                        + " as it's parameter names count "
                        + "match the parameter count");
                continue;
            }

            runnableStrategies.put(designator, m);
            runnableStrategyParameterNames.put(designator, parameterNames);
        }
        LOG.debug("Found " + runnableStrategies.size() + " runnable strategies");
        return runnableStrategies;

    }

    public Class[] getArguments(String designator)
            throws UnknownDesignatorException {
        runnableStrategies = getRunnableStrategies();
        Method m = runnableStrategies.get(designator);

        if (m == null)
            throw new UnknownDesignatorException("Don't know strategy \""
                    + designator + "\"");

        return m.getParameterTypes();
    }

    public String[] getArgumentNames(String designator) {
        return runnableStrategyParameterNames.get(designator);
    }
    public ArrayList<String> availableDesignators() {
        runnableStrategies = getRunnableStrategies();

        ArrayList<String> titles = new ArrayList<String>();

        Set<String> keys = runnableStrategies.keySet();
        for (String key : keys)
            titles.add(key);
        return titles;
	}

    public AbstractPlanner createClass(String designator, Object[] arglist)
            throws UnknownDesignatorException {

        runnableStrategies = getRunnableStrategies();
        Method m = runnableStrategies.get(designator);

        if (m == null)
            throw new UnknownDesignatorException("Don't know strategy \"" + designator + "\"");

        AbstractPlanner strategy;
        try {
            strategy = (AbstractPlanner) m.invoke(null, arglist);
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
