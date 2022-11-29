package mkf.jade.sar;

import java.util.Iterator;
import java.util.ArrayList;
import mkf.jade.sar.model.*;
import mkf.jade.sar.taskAgentHelper.*;

/**
 * Manages all software aquisition requests and works with other agents to complete the 
 * @author Rohit
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
	
	public void requestSubmitted(RequestInfoModel requestInfo) 
	{
		RequestManager manager = new RequestManager(m_loggedIn, requestInfo, m_taskCommunicator, m_taskDatabaseManager);
		m_requestsInProgress.add(manager);
	}
	
	public void userLogon(TeamType team) 
	{
		m_loggedIn = team;
		Iterator<RequestManager> requestManagers = m_requestsInProgress.iterator();
		
		while(requestManagers.hasNext())
		{
			requestManagers.next().userLoggon(team);
		}
	}

	public void requestDenied(int requestID) 
	{
		// TODO send notification 
		// TODO remove request
	}
	
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
}








