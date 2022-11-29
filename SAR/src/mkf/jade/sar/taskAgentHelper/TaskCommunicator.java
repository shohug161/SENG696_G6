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

	private TaskAgent m_taskAgent;

	/**
	 * Serial ID for the class
	 */
	private static final long serialVersionUID = 0xff5a4b9797909149L;

	/*******************************  SimpleBehaviour Methods   ****************************************/

	@Override
	public void action() 
	{
		
		
		
		
	}

	@Override
	public boolean done() {
		
		
		return false;
	}
	
	/*******************************  Methods   ****************************************/
	
	
	public void sendTaskToUI(TaskModel task)
	{
		// TODO
	}
	
	
	public void sendNotification(TaskModel task)
	{
		// TODO
	}
	
	
	public void enableTraining(String requestorName)
	{
		// TODO
	}
	
	
	public void cancelTaskReminder(int taskID)
	{
		// TODO This is a nice to have feature, only implement if there is time
	}
	
	
	public void scheduleSoftwareInstallation(RequestInfoModel requestInfo)
	{
		// TODO
	}
	
	
	public void requestCanceled(RequestInfoModel request)
	{
		// TODO
	}
	
	
	public void requestComplete(RequestInfoModel request)
	{
		// TODO
	}
}