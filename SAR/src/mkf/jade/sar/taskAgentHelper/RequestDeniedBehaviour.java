package mkf.jade.sar.taskAgentHelper;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TaskAgent;

/**
 * Waits for request denied messages from the UI agent and activates the task agent appropriately
 * @author Rohit
 *
 */
public class RequestDeniedBehaviour extends TaskAgentBehaviourBase 
{
	public RequestDeniedBehaviour(TaskAgent taskAgent)
	{
		super("request denied", Constants.REQUEST_DENIED, taskAgent);
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 0xff5a4b9797909149L;

	/*******************************  Methods   ****************************************/

	/**
	 * Processes the payload from the message
	 */
	@Override
	protected void processPayload(ACLMessage message) throws Exception 
	{
		int requestID = (int)message.getContentObject();
		m_taskAgent.requestDenied(requestID);
	}
}
