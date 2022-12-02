package mkf.jade.sar.taskAgentHelper;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.TrainingData;

/**
 * Waits for training complete messages from the training agent and activates the task agent appropriately
 * @author Rohit
 *
 */
public class TrainingCompleteBehaviour extends TaskAgentBehaviourBase 
{
	public TrainingCompleteBehaviour(TaskAgent taskAgent)
	{
		super("training complete", Constants.TRAINING_COMPLETE, taskAgent);
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 4581822114227895089L;

	/*******************************  SimpleBehaviour Methods   ****************************************/

	/**
	 * Processes the training complete payload
	 */
	@Override
	protected void processPayload(ACLMessage message) throws Exception 
	{
		TrainingData trainingData = (TrainingData)message.getContentObject();
		m_taskAgent.trainingComplete(trainingData);		
	}
}
