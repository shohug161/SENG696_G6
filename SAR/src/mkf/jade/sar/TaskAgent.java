package mkf.jade.sar;

import java.util.Iterator;

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
	
	public TaskAgent()
	{
		m_requestsInProgress = new ArrayList<RequestManager>();
		m_taskDatabaseManager = new TaskDatabaseManager();
		
		m_loggedIn = TeamType.noTeam;
		
		addBehaviour(new RequestDeniedBehaviour(this));
		addBehaviour(new RequestSubmittedBehaviour(this));
		addBehaviour(new TaskCompleteBehaviour(this));
		addBehaviour(new TrainingCompleteBehaviour(this));
		addBehaviour(new UserLogonBehaviour(this));
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
	public void userLogon(TeamType team) 
	{
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
		sendMessage(false, Constants.SEND_TASK_TO_UI, task);
	}
	
	/**
	 * Sends a task to the notification agent so it can send a notification
	 * @param task The task to send
	 */
	public void sendNotification(TaskModel task)
	{
		sendMessage(true, Constants.NOTIFICATION, task);
	}
	
	/**
	 * Requests the training agent enables training for the requestor
	 * @param requestorName The requestor to enable the training for
	 */
	public void enableTraining(String requestorName)
	{
		sendMessage(true, Constants.ENABLE_TRAINING, requestorName);
	}
	
	/**
	 * Requests that the notification agent informs the vendor of intent to purchase
	 * @param request The request to notify the vendor about
	 */
	public void notifyVendor(RequestInfoModel request)
	{
		sendMessage(true, Constants.NOTIFY_VENDOR, request);
	}
	
	/**
	 * Requests that the scheduler agent schedule a software isntallation date
	 * @param request The request to schedule an installation for
	 */
	public void scheduleSoftwareInstall(RequestInfoModel request)
	{
		sendMessage(true, Constants.SCHEDULE_INSTALL, request);
	}
	
	/**
	 * Request the notification agent sends a notification that the request is complete
	 * @param request The request to send the notification for.
	 */
	public void requestComplete(RequestInfoModel request)
	{
		sendMessage(true, Constants.REQUEST_COMPLETE, request);		
		removeRequest(request.requestID);
	}
	
	/**
	 * Requests that the schedular agent cancel all reminders for the task IDs
	 * @param reminders Array of all task item IDs to cancel the reminders for 
	 */
	public void cancelReminder(int[] reminders)
	{
		// TODO Reminders not yet implemented - NICE TO HAVE FEATURE
		// sendMessage(true, Constants.CANCEL_REMINDER, reminders);
	}
	
	/*******************************  Helper METHODS   *************************************/

	/**
	 * Sends a message to other agents
	 * @param isRequest True if the performative is REQUEST, false if it is INFORM
	 * @param conversationID The identifyer of the message type
	 * @param payload The payload of the message
	 */
	private void sendMessage(boolean isRequest, String conversationID, Serializable payload)
	{
		int performative = isRequest ? ACLMessage.REQUEST : ACLMessage.INFORM;
		ACLMessage msg = new ACLMessage(performative);
		msg.setConversationId(conversationID);
		
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
}
