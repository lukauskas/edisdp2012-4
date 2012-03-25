package balle.strategy;

import java.util.ArrayList;

import org.junit.Test;

public class StrategyFactoryTest {

    /**
     * This is more an integration test than a unit test. Given available
     * designators, the strategy factory should be able to create all of them
     * and not raise UnknownDesignatorException
     * 
     * @throws UnknownDesignatorException
     */
    @Test
    public void testAvailableDesignators() throws UnknownDesignatorException {
        StrategyFactory sf = new StrategyFactory();

        ArrayList<String> availableDesignators = sf.availableDesignators();
        for (String designator : availableDesignators) {
            // An actual test
            sf.createClass(designator, new Object[] {});
        }
    }

}
