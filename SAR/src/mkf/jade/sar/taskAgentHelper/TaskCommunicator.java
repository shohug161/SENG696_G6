package mkf.jade.sar.taskAgentHelper;

import jade.core.behaviours.SimpleBehaviour;
import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.*;

/** 
 * Handles communication with other agents for the task agent
 * @author Rohit
 *
 */
public class TaskCommunicator extends SimpleBehaviour 
{
	public TaskCommunicator(TaskAgent taskAgent)
	{
		m_taskAgent = taskAgent;
	}

	/*******************************  Member Variables   ****************************************/

	/**
	 * The agent that handles all requests and tasks
	 */
	private TaskAgent m_taskAgent;

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 0xff5a4b9797909149L;

	/*******************************  SimpleBehaviour Methods   ****************************************/

	@Override
	public void action() 
	{
		// TODO Handle request submitted
		// TODO Handle user logon
		// TODO Handle training complete
		// TODO Handle task complete
		// TODO Handle request denied		
	}

	@Override
	public boolean done() 
	{
		// TODO figure out what this is and how to use it
		
		return false;
	}
	
	/*******************************  Methods   ****************************************/
	
	
	public void sendTaskToUI(TaskModel task)
	{
		// TODO Send message
	}
	
	
	public void sendNotification(TaskModel task)
	{
		// TODO send task notification
	}
	
	
	public void enableTraining(String requestorName)
	{
		// TODO send enable training
	}
	
	
	public void cancelTaskReminder(int taskID)
	{
		// TODO This is a nice to have feature, only implement if there is time
	}
	
	
	public void notifyVendor(RequestInfoModel request)
	{
		// TODO send notify vendor 
	}
	
	public void scheduleSoftwareInstallation(RequestInfoModel requestInfo)
	{
		// TODO send schedule software installation 
	}
	
	
	public void requestCanceled(RequestInfoModel request)
	{
		// TODO Send request cancelled message
	}
	
	
	public void requestComplete(RequestInfoModel request)
	{
		// TODO send request complete
		m_taskAgent.requestCompleted(request.requestID);
	}
}