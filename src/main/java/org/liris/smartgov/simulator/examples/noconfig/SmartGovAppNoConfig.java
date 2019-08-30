package org.liris.smartgov.simulator.examples.noconfig;

import org.liris.smartgov.simulator.SmartGov;

/**
 * An example main function to show how the scenario can be loaded
 * by default without any configuration.
 *
 */
public class SmartGovAppNoConfig {

    public static void main(String[] args) {
    	// Creates a TestContextNoConfig instance without configuration.
    	TestContextNoConfig context = new TestContextNoConfig();
    	
    	// Creates a SmartGov instance from the previous context, that will
    	// load TestScenario
        SmartGov smartGov = new SmartGov(context);
    }

}
