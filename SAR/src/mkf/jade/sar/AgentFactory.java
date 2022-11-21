package mkf.jade.sar;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class AgentFactory extends Agent {

	protected void setup() {
		
		createAgent("UserInterfaceAgent", "mkf.jade.sar.UserInterfaceAgent");
		createAgent("TaskAgent", "mkf.jade.sar.TaskAgent");
		createAgent("NotificationAgent", "mkf.jade.sar.NotificationAgent");
		createAgent("SchedulerAgent", "mkf.jade.sar.SchedulerAgent");
		createAgent("TrainingAgent", "mkf.jade.sar.TrainingAgent");
		
	}

	private void createAgent(String name, String className) {
      	AID agentID = new AID( name, AID.ISLOCALNAME );
      	AgentContainer controller = getContainerController();
      	try {
      		AgentController agent = controller.createNewAgent(name, className, null );
      		agent.start();
      		System.out.println("+++ Created: " + agentID);
      	}
      	catch (Exception e){ e.printStackTrace(); }
	}

}
