package mkf.jade.sar;

import java.io.IOException;
import java.util.Hashtable;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import mkf.jade.sar.model.TrainingData;
import mkf.jade.sar.trainingAgentHelper.TrainingAgentBehaviour;
import mkf.jade.sar.trainingAgentHelper.TrainingCommunicationThread;
import mkf.jade.sar.trainingAgentHelper.TrainingDatabaseManager;

/**
 * Bridges other agents of the system with the existing training module system
 * @author Rohit
 *
 */
public class TrainingAgent extends EnhancedAgent 
{
	/**
	 * Sets up the agent
	 */
	@Override 
	protected void setup()
	{
		registerAgent();
		m_trainingDatabaseManager = new TrainingDatabaseManager();
		m_nameToAIDMap = new Hashtable<String, AID>();
		
		addBehaviour(new TrainingAgentBehaviour(this));
	}
	
	/*******************************  Member Variables   ************************************/

	/**
	 * The serilization ID
	 */
	private static final long serialVersionUID = -3502765383161725840L;
	
	/**
	 * Manages the database for the training agent
	 */
	private TrainingDatabaseManager m_trainingDatabaseManager;
	
	/**
	 * Table for mapping traineee names to the AID who requested the training
	 */
	private Hashtable <String, AID> m_nameToAIDMap;
	
	/*******************************  Methods   ****************************************/
	
	/**
	 * Enables training for a user with the following name
	 * @param traineeName The trainee name
	 * @param aid The agent ID of the sender of this message
	 */
	public void enableTraining(String traineeName, AID aid)
	{
		m_nameToAIDMap.put(traineeName, aid);
		TrainingCommunicationThread thread = new TrainingCommunicationThread(traineeName, this);
		
		thread.start();
	}
	
	/**
	 * Sends the completed training data to the other agents
	 * @param trainingData The training data to send
	 */
	public void sendCompletedTraining(TrainingData trainingData)
	{
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(Constants.TRAINING_COMPLETE);
		message.addReceiver(m_nameToAIDMap.get(trainingData.traineeName));
		
		m_nameToAIDMap.remove(trainingData.traineeName);
		
		m_trainingDatabaseManager.updateTrainingStatus(trainingData);
		
		try 
		{
			message.setContentObject(trainingData);
			send(message);
			System.out.println("Training data for " + trainingData.traineeName + " successfully processed from the training module");
		} 
		catch (IOException e) 
		{
			System.err.println("Could not serialize training data: " + e.getMessage());
		}
	}
	
	/*******************************  Helper Methods   ****************************************/

	/**
	 * Registers this agent in the DF
	 */
	private void registerAgent()
	{
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.TRAINING_AGENT);
        sd.setName(getLocalName());
		
        dfd.setName(getAID());  
        dfd.addServices(sd);
        
        try 
        {  
            DFService.register(this, dfd);  
        }
        catch (FIPAException fe) 
        {
            fe.printStackTrace(); 
        }
	}
}
