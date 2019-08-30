package org.liris.smartgov.simulator.examples.noconfig;

import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.examples.scenario.TestScenario;

public class TestContextNoConfig extends SmartGovContext {

	public TestContextNoConfig() {
		super();
	}
	
	@Override
	public Scenario loadScenario(String name) {
		return new TestScenario();
	}

}
