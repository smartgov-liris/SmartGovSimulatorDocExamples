package org.liris.smartgov.simulator.examples.agent;

import org.liris.smartgov.simulator.core.agent.moving.MovingAgentBody;

public class ShuttleBody extends MovingAgentBody {

	@Override
	public void handleMove() {
		this.getPlan().reachNextNode();
	}

	@Override
	public void handleWait() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleWander() {
		// TODO Auto-generated method stub
		
	}

}
