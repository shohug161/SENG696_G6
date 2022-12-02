package mkf.jade.sar.taskAgentHelper;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mkf.jade.sar.TaskAgent;

/**
 * Base class for common task agent class behaviours actions
 * @author Rohit
 *
 */
public abstract class TaskAgentBehaviourBase extends SimpleBehaviour 
{
	public TaskAgentBehaviourBase(String messageName, String conversationID, TaskAgent taskAgent)
	{
		m_messageName = messageName;
		m_taskAgent = taskAgent;
		m_template = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(conversationID));
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * The agent that handles all requests and tasks
	 */
	protected TaskAgent m_taskAgent;
	
	/**
	 * The message template 
	 */
	private MessageTemplate m_template;
	
	/**
	 * The message name the beahaviour is receiving
	 */
	private String m_messageName;

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = -1302983961565818343L;
	
	/*******************************  SimpleBehaviour Methods   ****************************************/

	/**
	 * Action taken by the child class
	 * @param payload The payload to process
	 * @throws Exception Exception from decoding the payload
	 */
	protected abstract void processPayload(ACLMessage message) throws Exception;
	
	/**
	 * Waits for messages that match the template
	 */
	@Override
	public void action() 
	{			
		ACLMessage received = m_taskAgent.blockingReceive(m_template);

		if(received != null)
		{
			try 
			{
				processPayload(received);
			} 
			catch (Exception e) 
			{
				System.err.println("Could not read message payload for " + m_messageName + " message: " + e.getMessage());
			}
		}
	}

	/**
	 * Overriden method for when the task agent is done running. This is always false for
	 * the task agent because it should never stop running
	 */
	@Override
	public boolean done() 
	{		
		return false;
	}
}