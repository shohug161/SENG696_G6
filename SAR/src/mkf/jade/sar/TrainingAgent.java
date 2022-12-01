package mkf.jade.sar;

import java.io.IOException;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.model.TrainingData;
import mkf.jade.sar.trainingAgentHelper.TrainingAgentBehaviour;

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
	}
	
	/*******************************  Member Variables   ************************************/

	/**
	 * The serilization ID
	 */
	private static final long serialVersionUID = -3502765383161725840L;
	
	/*******************************  Methods   ****************************************/
	
	/**
	 * Enables training for a user with the following name
	 * @param traineeName The trainee name
	 */
	public void enableTraining(String traineeName)
	{
		// TODO send message to training Module
		
		// TODO start listening for a response
	}
	
	/*******************************  Helper Methods   ****************************************/

	private void sendCompletedTraining(TrainingData trainingData)
	{
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(Constants.TRAINING_COMPLETE);
		
		try 
		{
			message.setContentObject(trainingData);
			send(message);
		} 
		catch (IOException e) 
		{
			System.err.println("Could not serialize training data: " + e.getMessage());
		}
	}
	
}
