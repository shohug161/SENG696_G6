package mkf.jade.sar;

import jade.domain.FIPAException;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.Set;
import java.util.HashSet;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.AID;


public class TrainingAgent extends EnhancedAgent {

	public class TrainingCommunicator extends SimpleBehaviour {
		
		public void action() {
			
		}
		
		public boolean done() {
			return false;
		}
		
	}
	
	public class TrainingDatabaseManager {
		
	}
	
}
