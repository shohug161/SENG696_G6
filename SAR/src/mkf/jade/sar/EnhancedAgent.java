package mkf.jade.sar;

import jade.domain.FIPAException;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.Set;
import java.util.HashSet;
import jade.core.Agent;
import jade.core.AID;

/**
 * 
 * @author Desi
 * EnhancedAgent provides more functionality to search for other agents and register services
 */
public class EnhancedAgent extends Agent{

	/**
	 * 
	 * @param serviceName, the service that we are searching for
	 * @return a set of AIDs, where each service is unique
	 */
	protected Set<AID> searchForService(String serviceName) {
		Set<AID> foundAgents = new HashSet<>();
		DFAgentDescription dfd = new DFAgentDescription();		// helps to provide services to search for
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceName.toLowerCase());		// jade calls the name depending on the type
		dfd.addServices(sd);

		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(Long.valueOf(-1));

		try {
			// search for agent with this description, df agent and service description
			DFAgentDescription[] results = DFService.search(this, dfd, sc);
			for(DFAgentDescription result : results) {
				foundAgents.add(result.getName());
			}
			return foundAgents;
		}
		catch (FIPAException ex) { ex.printStackTrace(); return null; }
	}
	
	protected void takeDown() {
		try { DFService.deregister(this); }
		catch (Exception ex) {}
	}

	/**
	 * 
	 * @param serviceName
	 */
	protected void register(String serviceName) {
		DFAgentDescription dfd = new DFAgentDescription();
		// getAID gets a valid ID that is an agent's name
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		// local name gets specific agent name
		sd.setName(getLocalName());
		sd.setType(serviceName.toLowerCase());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException ex) { ex.printStackTrace();
		}
	}
}
