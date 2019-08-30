package org.liris.smartgov.simulator.examples.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.liris.smartgov.simulator.SmartGov;
import org.liris.smartgov.simulator.core.agent.core.Agent;
import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.environment.graph.Arc;
import org.liris.smartgov.simulator.core.environment.graph.Node;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.examples.agent.ShuttleAgent;
import org.liris.smartgov.simulator.examples.agent.ShuttleBehavior;
import org.liris.smartgov.simulator.examples.agent.ShuttleBody;
import org.liris.smartgov.simulator.examples.main.TestContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestScenario extends Scenario {
	
	public static final String name = "TestScenario";

	@Override
	public Collection<? extends Node> buildNodes(SmartGovContext context) {
		ArrayList<Node> nodes = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			nodes.add(new Node(String.valueOf(i)));
		}
		return nodes;
	}

	@Override
	public Collection<? extends Arc> buildArcs(SmartGovContext context) {
		Map<String, Node> nodes = context.nodes;
		
		// Creating Arcs
		ArrayList<Arc> testArcs = new ArrayList<>();
		testArcs.add(new Arc("1", nodes.get("0"), nodes.get("1"), 1));
		testArcs.add(new Arc("2", nodes.get("1"), nodes.get("4"), 1));
		testArcs.add(new Arc("3", nodes.get("1"), nodes.get("2"), 1));
		testArcs.add(new Arc("4", nodes.get("4"), nodes.get("2"), 1));
		testArcs.add(new Arc("5", nodes.get("2"), nodes.get("3"), 1));
		testArcs.add(new Arc("6", nodes.get("3"), nodes.get("0"), 1));
		testArcs.add(new Arc("7", nodes.get("0"), nodes.get("2"), 1));
		testArcs.add(new Arc("8", nodes.get("2"), nodes.get("0"), 1));
		testArcs.add(new Arc("9", nodes.get("1"), nodes.get("2"), 1));
		testArcs.add(new Arc("10", nodes.get("2"), nodes.get("1"), 1));

		return testArcs;
	}

	@Override
	public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
		ArrayList<ShuttleAgent> agents = new ArrayList<>();
		
		// New body for the first shuttle
		ShuttleBody shuttleBody = new ShuttleBody();
		
		// Uncomment to print normal logs
		// registerShuttleEventListeners(shuttleBody);
		
		// Json logs
		registerJsonShuttleEventListeners(shuttleBody);
		
		// The first shuttle will perform round trips between nodes 1 and 5.
		agents.add(new ShuttleAgent(
				"1",
				shuttleBody,
				new ShuttleBehavior(
						shuttleBody,
						context.nodes.get("0"),
						context.nodes.get("4"),
						context
					)
				)
			);
			
		// New body for the second shuttle
		shuttleBody = new ShuttleBody();
		
		// Uncomment to print normal logs
		// registerShuttleEventListeners(shuttleBody);
		
		// Json logs
		registerJsonShuttleEventListeners(shuttleBody);
			
		// The first shuttle will perform round trips between nodes 2 and 4.
		agents.add(new ShuttleAgent(
				"2",
				shuttleBody,
				new ShuttleBehavior(
						shuttleBody,
						context.nodes.get("1"),
						context.nodes.get("3"),
						context
					)
				)
			);
		
		return agents;
	}
	
	private void registerShuttleEventListeners(ShuttleBody body) {
		body.addOnDestinationReachedListener((event) ->
			SmartGov.logger.info(
					"[" + SmartGov.getRuntime().getTickCount() + "] "
					+ "Agent " + body.getAgent().getId()
					+ " has reached destination "
					+ event.getNode().getId())
			);
		
		body.addOnNodeReachedListener((event) ->
		SmartGov.logger.info(
				"[" + SmartGov.getRuntime().getTickCount() + "] "
				+ "Agent " + body.getAgent().getId()
				+ " has reached node "
				+ event.getNode().getId())
		);
	}
	
	private void registerJsonShuttleEventListeners(ShuttleBody body) {
		ObjectMapper mapper = new ObjectMapper();
		body.addOnDestinationReachedListener((event) -> {
			try {
				SmartGov.logger.info(
						"[" + SmartGov.getRuntime().getTickCount() + "] "
						+ mapper.writeValueAsString(body.getAgent())
						);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

}
