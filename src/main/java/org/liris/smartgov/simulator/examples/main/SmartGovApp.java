package org.liris.smartgov.simulator.examples.main;

import org.liris.smartgov.simulator.SmartGov;

public class SmartGovApp {
    public static void main(String[] args) {
    	// Creates a TestContextNoConfig instance and load the configuration file.
    	TestContext context = new TestContext();
    	
    	// Creates a SmartGov instance from the previous context, that will
    	// load TestScenario
        SmartGov smartGov = new SmartGov(context);
        
        // Run the simulation for 10 ticks
        SmartGov.getRuntime().start(10);
    }
}
