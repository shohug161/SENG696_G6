package mkf.jade.sar;

import java.util.Iterator;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import mkf.jade.sar.model.*;
import mkf.jade.sar.taskAgentHelper.*;

/**
 * Manages all software aquisition requests and works with other agents to complete the 
 * @author Rohit
 * 
 * We are assuming that there will only be one instance of the UI for MVP, therefore this 
 * agent does not need to be multi threaded
 */
public class TaskAgent extends EnhancedAgent 
{
	
	/**
	 * Sets up the agent
	 */
	@Override 
	protected void setup() 
	{
		registerAgent();
		
		m_requestsInProgress = new ArrayList<RequestManager>();
		m_taskDatabaseManager = new TaskDatabaseManager();
		
		m_loggedIn = TeamType.noTeam;
				
		addBehaviour(new TaskAgentBehaviour(this));
	}	
	
	/*******************************  Member Variables   ****************************************/
	
	/**
	 * A list of all requests currently in progress, each one being managed by  asingle request manager
	 */
	private ArrayList<RequestManager> m_requestsInProgress;
	
	/**
	 * Manages the database
	 */
	private TaskDatabaseManager m_taskDatabaseManager;
	
	/**
	 * The current team that is logged into the system. 
	 * ASSUMPTION: we will only have a single UI instance that is always running for MVP.
	 */
	private TeamType m_loggedIn;
	
	/**
	 * The AID of the current UI agent that sent the loggon message
	 * ASSUMPTION: As above we start with only supporting a single UI instance
	 */
	private AID m_loggedInUIAgent;
	
	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 0xef684b9797998749L;
	
	/*******************************  INCOMING MESSAGE METHODS   ****************************************/
	
	/**
	 * Starts a new request manager for the new request
	 * @param requestInfo The request information
	 */
	public void requestSubmitted(RequestInfoModel requestInfo) 
	{
		RequestManager manager = new RequestManager(m_loggedIn, requestInfo, this, m_taskDatabaseManager);
		m_requestsInProgress.add(manager);
	}
	
	/**
	 * Informs all managers that a new team logged on so they can send that user interface agent the tasks
	 * @param team The team that is logged on
	 */
	public void userLogon(TeamType team, AID uiAgent) 
	{
		m_loggedInUIAgent = uiAgent;
		m_loggedIn = team;
		Iterator<RequestManager> requestManagers = m_requestsInProgress.iterator();
		
		while(requestManagers.hasNext())
		{
			requestManagers.next().userLoggon(team);
		}
	}

	/**
	 * Completes the training for all requests that the requestor requested
	 * @param trainingData The trainee name and training ID
	 */
	public void trainingComplete(TrainingData trainingData) 
	{
		System.out.println("Training complete for " + trainingData.traineeName);
		
		Iterator<RequestManager> requestManagers = m_requestsInProgress.iterator();
		while(requestManagers.hasNext())
		{
			RequestManager manager = requestManagers.next();
			
			if (manager.getRequestInfo().requestorName == trainingData.traineeName)
			{
				manager.trainingComplete(trainingData.trainingID);
			}
		}
	}
	
	/**
	 * The completes a task in the request manager that it belongs to 
	 * @param completedTask The task that has been completed
	 */
	public void taskComplete(TaskModel completedTask)
	{
		Iterator<RequestManager> requestManagers = m_requestsInProgress.iterator();
		int requestID = completedTask.requestInfo.requestID;
		boolean requestFound = false;
		
		while(requestManagers.hasNext())
		{
			RequestManager manager = requestManagers.next();
			
			if(manager.getRequestInfo().requestID == requestID)
			{
				manager.taskComplete(completedTask);
				requestFound = true;
				break;
			}
		}
		
		if(!requestFound)
		{
			System.err.println("ERROR - Could not find request for that this task belongs to");
		}
	}
	
	/**
	 * Removes the request and instructs the communicator to send a request cancelled message
	 * @param requestID The ID of the denied request
	 */
	public void requestDenied(int requestID) 
	{
		Iterator<RequestManager> requestManagers = m_requestsInProgress.iterator();
		RequestManager deniedManager = null;
		
		while(requestManagers.hasNext())
		{
			RequestManager manager = requestManagers.next();
			if(manager.getRequestInfo().requestID == requestID)
			{
				deniedManager = manager;
				break;
			}
		}
		
		if(deniedManager != null)
		{
			cancelRequest(deniedManager.getRequestInfo());
			System.out.println("Denied request with the requestID: " + requestID);
		}
		else
		{
			System.err.println("ERROR - Could not find denied request with the request ID: " + requestID);
		}
		
		removeRequest(requestID);
	}
	
	/*******************************  OUTGOING MESSAGE METHODS   *************************************/

	/**
	 * Sends a task to the UI agent
	 * @param task The task to send
	 */
	public void sendTaskToUI(TaskModel task)
	{
		sendMessage(false, Constants.SEND_TASK_TO_UI, task, m_loggedInUIAgent);
	}
	
	/**
	 * Sends a task to the notification agent so it can send a notification
	 * @param task The task to send
	 */
	public void sendNotification(TaskModel task)
	{
		AID recipient = searchForAgent(Constants.NOTIFICATION_AGENT);
		sendMessage(true, Constants.NOTIFICATION, task, recipient);
	}
	
	/**
	 * Requests the training agent enables training for the requestor
	 * @param requestorName The requestor to enable the training for
	 */
	public void enableTraining(String requestorName)
	{
		AID recipient = searchForAgent(Constants.TRAINING_AGENT);
		sendMessage(true, Constants.ENABLE_TRAINING, requestorName, recipient);
	}
	
	/**
	 * Requests that the notification agent informs the vendor of intent to purchase
	 * @param request The request to notify the vendor about
	 */
	public void notifyVendor(RequestInfoModel request)
	{
		AID recipient = searchForAgent(Constants.NOTIFICATION_AGENT);
		sendMessage(true, Constants.NOTIFY_VENDOR, request, recipient);
	}
	
	/**
	 * Requests that the scheduler agent schedule a software isntallation date
	 * @param request The request to schedule an installation for
	 */
	public void scheduleSoftwareInstall(RequestInfoModel request)
	{
		// Add delay here so that the task agent has time to insert the request into the database
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		AID recipient = searchForAgent(Constants.SCHEDULER_AGENT);
		sendMessage(true, Constants.SCHEDULE_INSTALL, request, recipient);
	}
	
	/**
	 * Request the notification agent sends a notification that the request is complete
	 * @param request The request to send the notification for.
	 */
	public void requestComplete(RequestInfoModel request)
	{
		AID recipient = searchForAgent(Constants.NOTIFICATION_AGENT);
		sendMessage(true, Constants.REQUEST_COMPLETE, request, recipient);		
		removeRequest(request.requestID);
	}
	
	/**
	 * Cancels the request for all agents
	 * @param requestID The request that's been canceled
	 */
	private void cancelRequest(RequestInfoModel requestInfo)
	{
		AID notificationAgent = searchForAgent(Constants.NOTIFICATION_AGENT);
		AID[] uiAgents =  searchForAllAgents(Constants.UI_AGENT);
		
		int length = uiAgents.length;
		
		AID[] recipients = new AID[length + 1];
		
		for(int i = 0; i < length; i++)
		{
			recipients[i] = uiAgents[i];
		}
		recipients[length] = notificationAgent;

		sendMessage(false, Constants.REQUEST_CANCELED, requestInfo, recipients);
	}
	
	/**
	 * Requests that the schedular agent cancel all reminders for the task IDs
	 * @param reminders Array of all task item IDs to cancel the reminders for 
	 */
	public void cancelReminder(int[] reminders)
	{
		// TODO Reminders not yet implemented - NICE TO HAVE FEATURE
//		 AID recipient = searchForAgent(Constants.SCHEDULER_AGENT);
//		 sendMessage(true, Constants.CANCEL_REMINDER, reminders, recipient);
	}
	
	/*******************************  Helper METHODS   *************************************/

	/**
	 * Sends a message to other agents
	 * @param isRequest True if the performative is REQUEST, false if it is INFORM
	 * @param conversationID The identifyer of the message type
	 * @param payload The payload of the message
	 * @param recipient The recipients of the message
	 */
	private void sendMessage(boolean isRequest, String conversationID, Serializable payload, AID recipient)
	{
		AID[] recipients =  { recipient };
		sendMessage(isRequest, conversationID, payload, recipients);
	}

	
	/**
	 * Sends a message to other agents
	 * @param isRequest True if the performative is REQUEST, false if it is INFORM
	 * @param conversationID The identifyer of the message type
	 * @param payload The payload of the message
	 * @param recipient An array of all recipients of the message
	 */
	private void sendMessage(boolean isRequest, String conversationID, Serializable payload, AID[] recipients)
	{
		int performative = isRequest ? ACLMessage.REQUEST : ACLMessage.INFORM;
		ACLMessage msg = new ACLMessage(performative);
		msg.setConversationId(conversationID);
		
		for(int i = 0; i < recipients.length; i++)
		{
			msg.addReceiver(recipients[i]);			
		}
		
		try 
		{
			msg.setContentObject(payload);
		} 
		catch (IOException e) 
		{
			System.err.println("Error Serializing type " + payload.getClass() + " " + e.getMessage());
		}
		send(msg);
	}
	
	/**
	 * Searches for an agent that meets the description
	 * @param description A description of the required services 
	 * @return Null if none are found, otherwise the first agent found
	 */
	private AID searchForAgent(String description)
	{
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(description);
        dfd.addServices(sd);
        
        AID aid = null;
        
        try 
        {
			DFAgentDescription[] result = DFService.search(this, dfd);
			
			if(result.length <= 0) throw new Exception("No Agent found for description: " + description);
			
			aid = result[0].getName();
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
        
        return aid;
	}
	
	/**
	 * Search for all agents matching the description
	 * @param description The description of the agents
	 * @return The AIDs of all agents matching the description
	 */
	private AID[] searchForAllAgents(String description)
	{
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(description);
        dfd.addServices(sd);
        
        AID[] recipients;
        
        try 
        {
        	DFAgentDescription[] result = DFService.search(this, dfd);
			
			if(result.length <= 0) throw new Exception("No Agent found for description: " + description);
			
			recipients = new AID[result.length];
			for (int i = 0; i < result.length; i++)
			{
				recipients[i] = result[i].getName();
			}
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
			recipients = new AID[0];
		}
        return recipients;
	}
	
	/**
	 * Removes all request managers with a request matching the ID
	 * @param requestID The request ID to remove
	 */
	private void removeRequest(int requestID)
	{
		boolean requestFound = false;
		
		// Iterate from the back to avoid deletion errors
		for(int i = m_requestsInProgress.size() - 1; i >= 0; i--)
		{
			if(m_requestsInProgress.get(i).getRequestInfo().requestID == requestID)
			{
				m_requestsInProgress.get(i).cancelReminders();
				m_requestsInProgress.remove(i);
				requestFound = true;
				
				// Don't break as a fallback for duplicate request managers
			}
		}
		
		if(!requestFound)
		{
			System.err.println("ERROR - Could not remove request ID: " + requestID + " because it was not found");
		}
	}
	
	/**
	 * Registers this agent in the DF
	 */
	private void registerAgent()
	{
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.TASK_AGENT);
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
