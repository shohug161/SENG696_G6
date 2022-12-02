package mkf.jade.sar;

import java.io.IOException;
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
	public TrainingAgent()
	{
		addBehaviour(new TrainingAgentBehaviour(this));
		enableTraining("Billy Bob Joe");
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
	
	/*******************************  Methods   ****************************************/
	
	/**
	 * Enables training for a user with the following name
	 * @param traineeName The trainee name
	 */
	public void enableTraining(String traineeName)
	{
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
}
