package mkf.jade.sar.taskAgentHelper;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.RequestInfoModel;

/**
 * Waits for request submitted messages from the UI agent and activates the task agent appropriately
 * @author Rohit
 *
 */
public class RequestSubmittedBehaviour extends TaskAgentBehaviourBase 
{
	public RequestSubmittedBehaviour(TaskAgent taskAgent)
	{
		super("submit request", Constants.SUBMIT_REQUEST, taskAgent);
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 2822322482427261802L;
	
	/*******************************  Methods   ****************************************/

	/**
	 * Processes the submitted request
	 */
	@Override
	protected void processPayload(ACLMessage message) throws Exception 
	{
		RequestInfoModel requestInfo = (RequestInfoModel)message.getContentObject();
		m_taskAgent.requestSubmitted(requestInfo);		
	}
}
