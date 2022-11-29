package mkf.jade.sar;

import java.util.Iterator;
import java.util.ArrayList;
import mkf.jade.sar.model.*;
import mkf.jade.sar.taskAgentHelper.*;

public class TaskAgent extends EnhancedAgent 
{
	
	public TaskAgent()
	{
		m_requestsInProgress = new ArrayList<RequestManager>();
		m_taskDatabaseManager = new TaskDatabaseManager();
		m_taskCommunicator = new TaskCommunicator(this);
		
		loggedIn = TeamType.noTeam;
	}
	
	/*******************************  Member Variables   ****************************************/
	
	private ArrayList<RequestManager> m_requestsInProgress;
	
	private TaskCommunicator m_taskCommunicator;
	
	private TaskDatabaseManager m_taskDatabaseManager;
	
	/**
	 * The current team that is logged into the system. 
	 * ASSUMPTION: we will only have a single UI instance that is always running for MVP.
	 */
	private TeamType loggedIn;
	
	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 0xef684b9797998749L;
	
	/*******************************  METHODS   ****************************************/
	
	public void requestSubmitted(RequestInfoModel requestInfo) 
	{
		RequestManager manager = new RequestManager(loggedIn, requestInfo, m_taskCommunicator, m_taskDatabaseManager);
		m_requestsInProgress.add(manager);
	}
	
	public void userLogon(TeamType team) 
	{
		loggedIn = team;
		Iterator<RequestManager> requestManagers = m_requestsInProgress.iterator();
		
		while(requestManagers.hasNext())
		{
			requestManagers.next().userLoggon(team);
		}
	}
	
	public void requestValidated(int requestID) 
	{
		
	}
	
	public void requestDenied(int requestID) 
	{
		
	}
	
	public void trainingComplete(TrainingData trainingData) 
	{
		
	}
	
	public void taskComplete(TaskModel completedTask)
	{
		
	}
}
