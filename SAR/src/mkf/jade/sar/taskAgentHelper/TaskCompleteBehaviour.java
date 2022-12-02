package mkf.jade.sar.taskAgentHelper;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.TaskModel;

/**
 * Waits for task complete messages from the UI agent and activates the task agent appropriately
 * @author Rohit
 *
 */
public class TaskCompleteBehaviour extends TaskAgentBehaviourBase 
{
	public TaskCompleteBehaviour(TaskAgent taskAgent)
	{
		super("task complete", Constants.TASK_COMPLETE, taskAgent);
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = -4316420309619019714L;
	
	/*******************************  SimpleBehaviour Methods   ****************************************/

	/**
	 * Processes the task complete payload 
	 */
	@Override
	protected void processPayload(ACLMessage message) throws Exception 
	{
		TaskModel task = (TaskModel)message.getContentObject();
		m_taskAgent.taskComplete(task);		
	}
}
