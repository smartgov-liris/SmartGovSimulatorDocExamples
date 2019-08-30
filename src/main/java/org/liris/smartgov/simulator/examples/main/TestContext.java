package org.liris.smartgov.simulator.examples.main;

import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.examples.scenario.GeoScenario;
import org.liris.smartgov.simulator.examples.scenario.TestScenario;

public class TestContext extends SmartGovContext {

	public TestContext() {
		super("./testConfig.properties");
	}
	
	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
			case TestScenario.name:
				return new TestScenario();
			case GeoScenario.name:
				return new GeoScenario();
			default:
				return null;
		}
	}

}
