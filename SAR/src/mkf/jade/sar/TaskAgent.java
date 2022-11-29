package mkf.jade.sar;

import java.util.Iterator;
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
		m_taskCommunicator = new TaskCommunicator(this);
		
		m_loggedIn = TeamType.noTeam;
	}
	
	/*******************************  Member Variables   ****************************************/
	
	/**
	 * A list of all requests currently in progress, each one being managed by  asingle request manager
	 */
	private ArrayList<RequestManager> m_requestsInProgress;
	
	/**
	 * Communicates with the other agents
	 */
	private TaskCommunicator m_taskCommunicator;
	
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
	
	/*******************************  METHODS   ****************************************/
	
	/**
	 * Starts a new request manager for the new request
	 * @param requestInfo The request information
	 */
	public void requestSubmitted(RequestInfoModel requestInfo) 
	{
		RequestManager manager = new RequestManager(m_loggedIn, requestInfo, m_taskCommunicator, m_taskDatabaseManager);
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
			m_taskCommunicator.requestCanceled(deniedManager.getRequestInfo());
			System.out.println("Denied request with the requestID: " + requestID);
		}
		else
		{
			System.err.println("ERROR - Could not find denied request with the request ID: " + requestID);
		}
		
		removeRequest(requestID);
	}
	
	/**
	 * Removes a request with the request ID. 
	 * 
	 * NOTE: the request completion is determined by the request manager. This method is only to stop 
	 * tracking the request by removing the manager.
	 * @param requestID The request ID
	 */
	public void requestCompleted(int requestID) 
	{
		// Don't send the notification because the task communicator calls this method
		// Don't log because the request manager does that
		removeRequest(requestID);
	}
	
	/*******************************  Helper METHODS   *************************************/

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








