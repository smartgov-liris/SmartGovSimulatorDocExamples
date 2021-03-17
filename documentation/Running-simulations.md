# Running simulations

Until now, you should be able to [instantiate a SmartGov
instance](Create-a-SmartGov-project.md) that loads a `Scenario` with Nodes, Arcs
and Agents as explained in the [previous sections](Simple-Graph.md).

Now that we have agents that can *live*, we will see how to run a simulation.

## Run a simulation

To run a simulation for a given amount of *ticks*, just modify your main function as follow : 
```java
public static void main(String[] args) {
    // Create a TestContext instance.
    TestContext context = new TestContext("./testConfig.properties");
    	
    // Create a SmartGov instance from the previous context, that will
    // load TestScenario
    SmartGov smartGov = new SmartGov(context);
        
    // Run the simulation for 10 ticks
    SmartGov.getRuntime().start(10);
}
```

## Monitoring shuttles using Events

Nodes, Arcs and Agents can handle many events, that will be described in detail later.

For now, we will focus on Agents' DestinationReachedEvent and NodeReachedEvent to monitor the trajectory of our shuttles.

To do so, create the following function in your `TestScenario` : 
```java
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
```

> To build the project from the Gradle CLI, you might need to add
>
> `implementation 'org.apache.logging.log4j:log4j-core:2.12.1'`
>
> to your `build.gradle` file.

and call it from your `buildAgents` function : 
```java
	@Override
	public Collection<? extends Agent<?>> buildAgents(SmartGovContext context) {
		ArrayList<ShuttleAgent> agents = new ArrayList<>();
		
		// New body for the first shuttle
		ShuttleBody shuttleBody = new ShuttleBody();
		registerShuttleEventListeners(shuttleBody);
		
		// The first shuttle will perform round trips between nodes 1 and 5.
		agents.add(new ShuttleAgent(
				"1",
				shuttleBody,
				new ShuttleBehavior(
						shuttleBody,
						context.nodes.get("0"),
						context.nodes.get("4"),
						(TestContext) context
					)
				)
			);
			
		// New body for the second shuttle
		shuttleBody = new ShuttleBody();
		registerShuttleEventListeners(shuttleBody);
			
		// The first shuttle will perform round trips between nodes 2 and 4.
		agents.add(new ShuttleAgent(
				"2",
				shuttleBody,
				new ShuttleBehavior(
						shuttleBody,
						context.nodes.get("1"),
						context.nodes.get("3"),
						(TestContext) context
					)
				)
			);
		
		return agents;
	}
```

Now, run the application, and here is the kind of output you should obtain : 

```
09:41:51.469 [main] INFO  org.liris.smartgov.simulator.SmartGov - Loading config from ./testConfig.properties
09:41:51.502 [main] INFO  org.liris.smartgov.simulator.SmartGov - Starting SmartGov
09:41:51.505 [main] INFO  org.liris.smartgov.simulator.SmartGov - Loading World for TestScenario
09:41:51.507 [main] INFO  org.liris.smartgov.simulator.SmartGov - 5 nodes added to SmartGovContext
09:41:51.507 [main] INFO  org.liris.smartgov.simulator.SmartGov - 10 arcs added to SmartGovContext
09:41:51.507 [main] INFO  org.liris.smartgov.simulator.SmartGov - Creating the simulation Graph
09:41:51.610 [main] INFO  org.liris.smartgov.simulator.SmartGov - [0] Agent 1 has reached node 0
09:41:51.610 [main] INFO  org.liris.smartgov.simulator.SmartGov - [0] Agent 2 has reached node 1
09:41:51.610 [main] INFO  org.liris.smartgov.simulator.SmartGov - 2 agents added to SmartGovContext
09:41:51.610 [main] INFO  org.liris.smartgov.simulator.SmartGov - Time to process simulation creation: 105 ms.
09:41:51.611 [main] INFO  org.liris.smartgov.simulator.core.simulation.SimulationRuntime - Start simulation
09:41:51.622 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [0] Agent 1 has reached node 1
09:41:51.622 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [0] Agent 2 has reached node 2
09:41:51.622 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] Agent 1 has reached node 4
09:41:51.622 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] Agent 1 has reached destination 4
09:41:51.622 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] Agent 1 has reached node 4
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] Agent 2 has reached node 3
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] Agent 2 has reached destination 3
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] Agent 2 has reached node 3
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [2] Agent 1 has reached node 2
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [2] Agent 2 has reached node 0
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] Agent 1 has reached node 0
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] Agent 1 has reached destination 0
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] Agent 1 has reached node 0
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] Agent 2 has reached node 1
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] Agent 2 has reached destination 1
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] Agent 2 has reached node 1
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [4] Agent 1 has reached node 1
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [4] Agent 2 has reached node 2
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] Agent 1 has reached node 4
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] Agent 1 has reached destination 4
09:41:51.623 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] Agent 1 has reached node 4
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] Agent 2 has reached node 3
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] Agent 2 has reached destination 3
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] Agent 2 has reached node 3
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [6] Agent 1 has reached node 2
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [6] Agent 2 has reached node 0
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] Agent 1 has reached node 0
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] Agent 1 has reached destination 0
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] Agent 1 has reached node 0
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] Agent 2 has reached node 1
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] Agent 2 has reached destination 1
09:41:51.624 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] Agent 2 has reached node 1
09:41:51.625 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [8] Agent 1 has reached node 1
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [8] Agent 2 has reached node 2
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [9] Agent 1 has reached node 4
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [9] Agent 1 has reached destination 4
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [9] Agent 1 has reached node 4
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [9] Agent 2 has reached node 3
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [9] Agent 2 has reached destination 3
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [9] Agent 2 has reached node 3
09:41:51.626 [Thread-1] INFO  org.liris.smartgov.simulator.core.simulation.SimulationRuntime - Stop simulation after 10 ticks.
```
If we sum up, the shuttle 1 does : 
- 0 -> 1 -> 4
- 4 -> 2 -> 0

And shuttle 2 does : 
- 1 -> 2 -> 3
- 3 -> 0 -> 1

What is perfectly consistent with our test graph : 

![Simple Graph Example](simpleGraph.png)

An interesting thing to note is that event handlers can cumulate, and can be
defined from any place in the code.

## Monitoring shuttle using JSON outputs

The previous approach is quite verbose and not really readable, even if it can
be useful to handle events programmatically and to perform
[JUnit](https://junit.org/junit5/) tests for example.

Using JSON outputs can be a smarter approach in some cases. Actually, Arcs,
Nodes and Agents are compliant with the Jackson library, and you are free to
use [Jackson](https://github.com/FasterXML/jackson-databind) annotations in
your extending classes.

First, add the Jackson dependency to your `build.gradle` :

```
dependencies {
    ...
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.9.9'
    ...
}
```

Now create the `registerJsonOutputListeners` function, that will print the
Agent state at each `DestinationReachedEvent` : 

```java
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
```

And replace the calls to `registerShuttleListeners` by this new function in
your `buildAgents` function, in the `TestScenario`.

You should now have such an output : 
```
...
09:52:20.609 [main] INFO  org.liris.smartgov.simulator.core.simulation.SimulationRuntime - Start simulation
09:52:20.648 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] {"id":"1","body":{"plan":{"nodes":["0","1","4"],"currentNode":"4","currentArc":"2","complete":true}},"behavior":{"origin":"0","destination":"4"}}
09:52:20.651 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [1] {"id":"2","body":{"plan":{"nodes":["1","2","3"],"currentNode":"3","currentArc":"5","complete":true}},"behavior":{"origin":"1","destination":"3"}}
09:52:20.652 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] {"id":"1","body":{"plan":{"nodes":["4","2","0"],"currentNode":"0","currentArc":"8","complete":true}},"behavior":{"origin":"4","destination":"0"}}
09:52:20.652 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [3] {"id":"2","body":{"plan":{"nodes":["3","0","1"],"currentNode":"1","currentArc":"1","complete":true}},"behavior":{"origin":"3","destination":"1"}}
09:52:20.652 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] {"id":"1","body":{"plan":{"nodes":["0","1","4"],"currentNode":"4","currentArc":"2","complete":true}},"behavior":{"origin":"0","destination":"4"}}
09:52:20.652 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [5] {"id":"2","body":{"plan":{"nodes":["1","2","3"],"currentNode":"3","currentArc":"5","complete":true}},"behavior":{"origin":"1","destination":"3"}}
09:52:20.652 [Thread-1] INFO  org.liris.smartgov.simulator.SmartGov - [7] {"id":"1","body":{"plan":{"nodes":["4","2","0"],"currentNode":"0","currentArc":"8","complete":true}},"behavior":{"origin":"4","destination":"0"}}
...
```

As before, the nodes "plan" and "behavior" are consistent. The main interest of
those JSON serializations is that you can easily write them to files to apply
external processes, or even handle them using a web application.
