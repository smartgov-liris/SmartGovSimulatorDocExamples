package org.liris.smartgov.simulator.examples.scenario;

import java.util.ArrayList;
import java.util.Collection;

import org.liris.smartgov.simulator.core.environment.SmartGovContext;
import org.liris.smartgov.simulator.core.scenario.Scenario;
import org.liris.smartgov.simulator.examples.agent.ShuttleBehavior;
import org.liris.smartgov.simulator.urban.geo.agent.GeoAgent;
import org.liris.smartgov.simulator.urban.geo.agent.GeoAgentBody;
import org.liris.smartgov.simulator.urban.geo.agent.mover.BasicGeoMover;
import org.liris.smartgov.simulator.urban.geo.environment.graph.GeoArc;
import org.liris.smartgov.simulator.urban.geo.environment.graph.GeoNode;
import org.liris.smartgov.simulator.urban.geo.utils.LatLon;

public class GeoScenario extends Scenario {
	
	public static final String name = "GeoTest";

	@Override
	public Collection<GeoNode> buildNodes(SmartGovContext context) {
		ArrayList<GeoNode> nodes = new ArrayList<>();
		
		nodes.add(
			new GeoNode(
				"1",
				new LatLon(45.7829296, 4.8680849)
				)
			);
		nodes.add(
			new GeoNode(
				"2",
				new LatLon(45.7822063, 4.8648563)
				)
			);
		nodes.add(
			new GeoNode(
				"3",
				new LatLon(45.7842329, 4.865034)
				)
			);
		nodes.add(
			new GeoNode(
				"4",
				new LatLon(45.7844936, 4.8673742)
				)
			);
		
		return nodes;
	}

	@Override
	public Collection<GeoArc> buildArcs(SmartGovContext context) {
		ArrayList<GeoArc> arcs = new ArrayList<>();
		
		arcs.add(
			new GeoArc(
				"1",
				(GeoNode) context.nodes.get("1"),
				(GeoNode) context.nodes.get("2")
				)
			);
		
		arcs.add(
			new GeoArc(
				"2",
				(GeoNode) context.nodes.get("2"),
				(GeoNode) context.nodes.get("3")
				)
			);
		
		arcs.add(
			new GeoArc(
				"3",
				(GeoNode) context.nodes.get("3"),
				(GeoNode) context.nodes.get("4")
				)
			);
		
		arcs.add(
			new GeoArc(
				"4",
				(GeoNode) context.nodes.get("4"),
				(GeoNode) context.nodes.get("1")
				)
			);
		
		return arcs;
	}

	@Override
	public Collection<GeoAgent> buildAgents(SmartGovContext context) {
		ArrayList<GeoAgent> agents = new ArrayList<>();
		
		/*
		 * A basic geographical mover that allows agent to move
		 * along arcs.
		 */
		BasicGeoMover mover = new BasicGeoMover();
		GeoAgentBody agentBody = new GeoAgentBody(mover);
		
		// default 1m/s speed
		agentBody.setSpeed(1);
		
		agents.add(new GeoAgent(
				"1",
				agentBody,
				new ShuttleBehavior(
						agentBody,
						context.nodes.get("1"),
						context.nodes.get("3"),
						context)
				));
		
		return agents;
	}

}
