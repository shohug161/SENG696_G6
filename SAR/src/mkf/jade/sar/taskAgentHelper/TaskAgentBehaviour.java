package mkf.jade.sar.taskAgentHelper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.RequestInfoModel;
import mkf.jade.sar.model.TaskModel;
import mkf.jade.sar.model.TeamType;
import mkf.jade.sar.model.TrainingData;

/**
 * Handles all  message receiving behaviours of the task agent
 * @author Rohit
 *
 */
public class TaskAgentBehaviour extends CyclicBehaviour 
{
	public TaskAgentBehaviour(TaskAgent taskAgent)
	{
		m_taskAgent = taskAgent;
		
		m_trainingCompleteTemplate = createMessageTemplate(Constants.TRAINING_COMPLETE);
		m_taskCompleteTemplate = createMessageTemplate(Constants.TASK_COMPLETE);
		m_userLogonTemplate = createMessageTemplate(Constants.LOGON);
		m_requestSubmittedTemplate = createMessageTemplate(Constants.SUBMIT_REQUEST);
		m_requestDeniedTemplate = createMessageTemplate(Constants.REQUEST_DENIED);
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * The agent that handles all requests and tasks
	 */
	protected TaskAgent m_taskAgent;
	
	/**
	 * The message templates
	 */
	private MessageTemplate m_userLogonTemplate;
	private MessageTemplate m_taskCompleteTemplate;
	private MessageTemplate m_requestDeniedTemplate;
	private MessageTemplate m_requestSubmittedTemplate;
	private MessageTemplate m_trainingCompleteTemplate;

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = -1302983961565818343L;
	
	/*******************************  CyclicBehaviour Methods   ****************************************/
	
	/**
	 * Waits for messages that match the template
	 */
	@Override
	public void action() 
	{		
		ACLMessage userLogonMessage = m_taskAgent.receive(m_userLogonTemplate);
		ACLMessage taskCompleteMessage = m_taskAgent.receive(m_taskCompleteTemplate);
		ACLMessage requestDeniedMessage = m_taskAgent.receive(m_requestDeniedTemplate);
		ACLMessage requestSubmittedMessage = m_taskAgent.receive(m_requestSubmittedTemplate);
		ACLMessage trainingCompleteMessage = m_taskAgent.receive(m_trainingCompleteTemplate);

		if(userLogonMessage != null)
		{
			processUserLogonMessage(userLogonMessage);
		}
		
		if(taskCompleteMessage != null)
		{
			processTaskCompleteMessage(taskCompleteMessage);
		}
		
		if(requestDeniedMessage != null)
		{
			processRequestDeniedMessage(requestDeniedMessage);
		}
		
		if(requestSubmittedMessage != null)
		{
			processRequestSubmittedMessage(requestSubmittedMessage);
		}
		
		if(trainingCompleteMessage != null)
		{
			processTrainingCompleteMessage(trainingCompleteMessage);
		}
		
		block();
	}

	/*******************************  Helper Methods   ****************************************/
	
	/// PROCESS MESSAGE METHODS
	
	private void processUserLogonMessage(ACLMessage userLogonMessage) 
	{
		try 
		{
			TeamType team = (TeamType)userLogonMessage.getContentObject();
			m_taskAgent.userLogon(team, userLogonMessage.getSender());		
		} 
		catch (Exception e)
		{
			System.err.println("ERROR - Could not read user logon message payload "  + e.getMessage());
		}
	}

	
	private void processTaskCompleteMessage(ACLMessage taskCompleteMessage) 
	{
		try 
		{
			TaskModel task = (TaskModel)taskCompleteMessage.getContentObject();
			m_taskAgent.taskComplete(task);		
		} 
		catch (Exception e) 
		{
			System.err.println("ERROR - Could not read task complete message payload " + e.getMessage());
		}
	}
	
	private void processRequestDeniedMessage(ACLMessage requestDeniedMessage) 
	{
		try 
		{
			int requestID = (int)requestDeniedMessage.getContentObject();
			m_taskAgent.requestDenied(requestID);		
		} 
		catch (Exception e) 
		{
			System.err.println("ERROR - Could not read request denied message payload " + e.getMessage());
		}
	}

	private void processRequestSubmittedMessage(ACLMessage requestSubmittedMessage) 
	{
		try 
		{
			RequestInfoModel requestInfo = (RequestInfoModel)requestSubmittedMessage.getContentObject();
			m_taskAgent.requestSubmitted(requestInfo);		
		} 
		catch (Exception e) 
		{
			System.err.println("ERROR - Could not read request submitted message payload " + e.getMessage());
		}
	}

	private void processTrainingCompleteMessage(ACLMessage trainingCompleteMessage) 
	{
		try 
		{
			TrainingData trainingData = (TrainingData)trainingCompleteMessage.getContentObject();
			m_taskAgent.trainingComplete(trainingData);		
		} 
		catch (Exception e) 
		{
			System.err.println("ERROR - Could not read training complete message payload "  + e.getMessage());
		}
	}
	
	/**
	 * Creates message templates
	 * @param conversationID The conversation ID of the message
	 * @return A message template with the conversation ID
	 */
	private MessageTemplate createMessageTemplate(String conversationID)
	{
		return MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(conversationID));
	}
}