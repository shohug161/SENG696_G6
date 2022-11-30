package mkf.jade.sar.taskAgentHelper;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.TeamType;

/**
 * Waits for user logon messages from the UI agent and activates the task agent appropriately
 * @author Rohit
 *
 */
public class UserLogonBehaviour extends TaskAgentBehaviourBase 
{
	public UserLogonBehaviour(TaskAgent taskAgent)
	{
		super("user logon", Constants.LOGON, taskAgent);
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = -1213961769418221405L;

	/*******************************  Methods   **********************************/
	
	/**
	 * Processes the logon message payload
	 */
	@Override
	protected void processPayload(ACLMessage message) throws Exception 
	{
		TeamType team = (TeamType)message.getContentObject();
		m_taskAgent.userLogon(team);		
	}
}