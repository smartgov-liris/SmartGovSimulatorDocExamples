# Create a SmartGov project

This section assumes that you have already set up your Gradle project, and installed the SmartGovSimulator dependency. Else, check [this section](Get-Started).

## Table Of Contents
1. [Create a Scenario](#create-a-scenario)
2. [Register the Scenario](#register-the-scenario)
3. [Instantiate SmartGov](#instantiate-smartgov)
4. [Introducing the configuration file](#configuration-file)
5. [Advanced Context And Scenarios management](#advanced-context-and-scenarios-management)

## Create a Scenario
The first thing that we need to do is creating a new Scenario.

To do so, you can create this minimum Scenario extension, that doesn't build anything : 
```java
public class TestScenario extends Scenario {

	public static final String name = "TestScenario"

	@Override
	public Collection<? extends Node> buildNodes() {
		return new ArrayList<>();
	}

	@Override
	public Collection<? extends Arc> buildArcs() {
		return new ArrayList<>();
	}

	@Override
	public Collection<? extends Agent<?>> buildAgents() {
		return new ArrayList<>();
	}

}
```
Don't forget to import the required classes.

Even if the name field is not really necessary, we'll see its interest in the next section.

## Register the Scenario
To load our scenario, we need to register it in a new `SmartGovContext`.

To do so, create the following SmartGovContext extension : 
```java
public class TestContext extends SmartGovContext {

	public TestContext(String configFile) {
		super(configFile);
	}
	
	@Override
	public Scenario loadScenario(String name) {
		return new TestScenario();
	}

}
```

## Instantiate SmartGov

Now, we need to create a SmartGov instance from our `Context`, that will load
the specified `Scenario`, that will itself build and add `Nodes`, `Arcs` and
`Agents` to the context.

To do so, create a Java class with a main function as follow : 
```java
public class SmartGovApp {

    public static void main(String[] args) {
    	// Creates a TestContext instance
    	TestContext context = new TestContext();
    	
    	// Creates a SmartGov instance from the previous context, that will
    	// load TestScenario
        SmartGov smartGov = new SmartGov(context);
    }

}
```
You can now run your Java app from this main function, and you should obtain an output as follow : 
```
06:38:04.393 [main] INFO  org.liris.smartgov.simulator.SmartGov - Starting SmartGov
06:38:04.399 [main] INFO  org.liris.smartgov.simulator.SmartGov - Loading World for TestScenario
06:38:04.400 [main] INFO  org.liris.smartgov.simulator.SmartGov - 0 nodes added to SmartGovContext
06:38:04.400 [main] INFO  org.liris.smartgov.simulator.SmartGov - 0 arcs added to SmartGovContext
06:38:04.582 [main] INFO  org.liris.smartgov.simulator.SmartGov - 0 agents added to SmartGovContext
06:38:04.582 [main] INFO  org.liris.smartgov.simulator.SmartGov - Time to process simulation creation: 184 ms.
```

If you do so, you have now built your first SmartGov project! However, it doesn't do much yet.

To learn how to instantiate a simple graph, you can go to the [next section](Simple-Graph).

***

## Configuration file

The SmartGov input configuration file is where you will specify simulation
parameters such as the Scenario, the number of agents, the output folder, etc.

Nothing is necessary in this file, and this file is itself optional : it must
be seen as a convenient way for users to specify input parameters.

The only requirements is that, if it used to load scenarios, the scenario name
should be specified using the "scenario" property.

So let's see how we can modify the previous classes to handle an input
configuration.

For now, we will only specify a Scenario. So, at the root of your project, you
can create a `testConfig.properties` file with the following content :
```
#Scenario
scenario=TestScenario
```

The first line is an optional comment, and the extension doesn't matter. However, the syntax must correspond to [Java properties](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html).

You can now modify you context implementation as follow :
```java
public class TestContext extends SmartGovContext {

	public TestContext() {
		super("./testConfig.properties");
	}
	
	@Override
	public Scenario loadScenario(String name) {
		switch(name) {
			case TestScenario.name:
				return new TestScenario();
			default:
				return null;
		}
	}

}
```

The configuation file will now be loaded, and the scenario will be loaded
according to the specified "scenario" field.

## Advanced Context And Scenarios management
As your project grows, you will probably have different scenarios, or even a scenario hierarchy : you will need to register them in the previously defined `switch` statement of the `loadScenario` function to be able to instantiate them.

Also, you can even create a `Context` hierarchy, in order to structure your scenarios. In this case, you could for example extend `TestContext` and define a new `loadScenario` function as follow : 
```java
@Override
public Scenario loadScenario(String name) {
	switch(name) {
		case TestScenario.name:
			return new TestScenario();
		default:
			return super.loadScenario(name);
	}
}
```
The interest of this structure is that even if the same SmartGov project can handle multiple Scenarios at runtime, depending on the input configuration, only one SmartGovContext instance is used. Such definition of `loadScenario` gives you the ability to instantiate all your scenarios from any SmartGov sub-context.
