package mkf.jade.sar.taskAgentHelper;

import java.util.ArrayList;
import java.util.Iterator;

import mkf.jade.sar.TaskAgent;
import mkf.jade.sar.model.*;

/**
 * Manages a single request for the task agent
 * @author Rohit
 */
public class RequestManager
{
	public RequestManager(TeamType loggedIn, RequestInfoModel requestInfo, TaskAgent taskCommunicator, TaskDatabaseManager taskDatabaseManager)
	{
		this.m_loggedIn = loggedIn;
		m_taskAgent = taskCommunicator;
		m_taskDatabaseManager = taskDatabaseManager;
		m_requestModel = new RequestModel(requestInfo);
		
		createTask(TeamType.zoneManager);
	}
	
	/*******************************  Member Variables   ****************************************/
	
	/**
	 * The request that this manager is in charge of
	 */
	private RequestModel m_requestModel;

	/**
	 * The current logged in team
	 */
	private TeamType m_loggedIn;
		
	/**
	 * Communicates with other agents and manages all requests and tasks
	 */
	private TaskAgent m_taskAgent;
	
	/**
	 * Manages the database
	 */
	private TaskDatabaseManager m_taskDatabaseManager;
		
	/*******************************  METHODS   ****************************************/
	
	/**
	 * Gets the info of the request that this manager is in charge of 
	 * @return
	 */
	public RequestInfoModel getRequestInfo()
	{
		return m_requestModel.requestInfo;
	}
	
	
	/**
	 * Checks all tasks in the request and send the incompleted ones that are assigned to the logged in
	 * user to the UI
	 * @param team the team that has logged in
	 */
	public void userLoggon(TeamType team)
	{
		Iterator<TaskModel> taskList = m_requestModel.requestTasks.iterator();
		
		while(taskList.hasNext())
		{
			TaskModel task = taskList.next();
			
			// If the task is not complete and is assigned to the user that has logged in
			// send the task to the UI
			if(!task.isComplete && task.team == team)
			{
				m_taskAgent.sendTaskToUI(task);
			}
		}
		
		m_loggedIn = team;
	}
	
	/**
	 * Marks a task or task items as complete
	 * @param completedTask The completed task model, not one tracked in the request object
	 */
	public void taskComplete(TaskModel completedTask)
	{
		System.out.println("Task for team " + completedTask.team + " completed for " + getRequestInfo());
		
		completedTask.updateIsComplete();
		
		if(completedTask.team == TeamType.zoneManager && completedTask.isComplete)
		{
			requestValidated(completedTask.requestInfo);
		}
		
		if(updateTaskCompletion(completedTask))
		{
			checkLatterTaskProgression();
		}
		
		cancelReminders(completedTask);
	}

	/**
	 * Mark the training and TOU of the requestor of this SAR as complete 
	 * @param trainingID The ID of the requestor
	 */
	public void trainingComplete(int trainingID)
	{
		System.out.println("Training complete for the requestor of " + getRequestInfo());
		
		Iterator<TaskModel> taskList = m_requestModel.requestTasks.iterator();
		
		boolean allComplete = false; 
		
		while(taskList.hasNext())
		{
			TaskModel task = taskList.next();
			
			if (task.team == TeamType.requestor)
			{
				// Update task to complete
				for(int i = 0; i < task.taskItems.size(); i++)
				{
					task.taskItems.get(i).isComplete = true;
				}
				task.updateIsComplete();
				
				// check if all tasks are complete, updating the completion is moot
				allComplete = updateTaskCompletion(task);
				break;
			}
		}
		
		getRequestInfo().trainingID = trainingID;
		
		if(allComplete)
		{
			checkLatterTaskProgression();
		}
	}
	
	/**
	 * Cancels the reminders for all tasks in the request
	 * @param manager The manager that controls all tasks with reminders to be canceled
	 */
	public void cancelReminders()
	{
		cancelReminders(false, m_requestModel.requestTasks);
	}
	
	/******************************* HELPER METHODS   ****************************************/
	
	/**
	 * Creates the task for the team
	 * @param team The team to create the task for
	 */
	private void createTask(TeamType team)
	{
		if (team == TeamType.noTeam) 
		{
			System.err.println("ERROR, cannot create task for \"noTeam\" team type.");
		}
		else
		{
			System.out.println("Task created for " + team + " for the request: " + getRequestInfo());
			
			TaskModel task = createTaskModel(team);
			
			m_requestModel.requestTasks.add(task);
			m_taskAgent.sendNotification(task);
			
			if(team == m_loggedIn)
			{
				m_taskAgent.sendTaskToUI(task);
			}
			
		}
	}
	
	/**
	 * Called when this request has been validated by the zone manager
	 * @param requestInfo The new request info
	 */
	private void requestValidated(RequestInfoModel requestInfo)
	{
		System.out.println("Request " + getRequestInfo() + " is validated to be level " + requestInfo.informationType);

		m_requestModel.requestInfo = requestInfo;
		
		if (dataLevelRestricted())
		{			
			createTask(TeamType.asrc);
			createTask(TeamType.aws);
			createTask(TeamType.legal);
			createTask(TeamType.privacy);
			createTask(TeamType.supplyChain);	// must be last for checkTaskProgression()
			
			if(!m_taskDatabaseManager.getRequestorTraingStatus(requestInfo.requestorName))
			{
				createTask(TeamType.requestor);
				m_taskAgent.enableTraining(requestInfo.requestorName);
			}
		}
	}
	
	/**
	 * Returns true if the data level of the request is 3 or 4
	 * @return True if the data level of the request is 3 or 4
	 */
	private boolean dataLevelRestricted()
	{
		return getRequestInfo().informationType == InformationType.LEVEL3 || getRequestInfo().informationType == InformationType.LEVEL4;
	}
	
	/**
	 * Checks the progress of the latter tasks of a request. See method requirements below 
	 */
	private void checkLatterTaskProgression() 
	{
		// This method requires that all tasks are already complete. This method is also only called after all the
		// tasks before the vendor notification are completed. That means we can tell what step to progress to based 
		// on the last task added to the task list.
		
		int taskCount = m_requestModel.requestTasks.size();
		TeamType lastTaskTeam = m_requestModel.requestTasks.get(taskCount - 1).team;
		
		if (lastTaskTeam == TeamType.requestor || 
			lastTaskTeam == TeamType.supplyChain ||
			lastTaskTeam == TeamType.zoneManager)
		{
			m_taskAgent.notifyVendor(getRequestInfo());
			createTask(TeamType.accountsPayable);
		}
		else if (lastTaskTeam == TeamType.accountsPayable)
		{
			m_taskAgent.scheduleSoftwareInstall(getRequestInfo());
			createTask(TeamType.deskside);
		}
		else if (lastTaskTeam == TeamType.deskside)
		{
			// REQUEST COMPLETE
			System.out.println("Request " + getRequestInfo() + " COMPLETED");
			
			m_taskDatabaseManager.addCompletedRequest(getRequestInfo());
			m_taskAgent.requestComplete(getRequestInfo());	// Must be last
		}
		else
		{
			System.err.println("ERROR - Last task is not a valid team, it is " + lastTaskTeam);
		}
	}

	/**
	 * Updates our local task with the completed task
	 * @param completedTask The task that was completed
	 * @return True if all tasks have been fully completed 
	 */
	private boolean updateTaskCompletion(TaskModel completedTask) 
	{
		Iterator<TaskModel> tasks =  m_requestModel.requestTasks.iterator();
		boolean allComplete = true;
		
		while(tasks.hasNext())
		{
			TaskModel localTask = tasks.next();
			
			if(localTask.team == completedTask.team)
			{
				localTask.taskItems = completedTask.taskItems;
				localTask.updateIsComplete();
			}
			allComplete = allComplete && localTask.isComplete;
			
		}
		return allComplete;
	}
	
	/**
	 * Cancels the reminders for completed task items for a single task
	 * @param task The task to cancel the reminders for
	 */
	private void cancelReminders(TaskModel task)
	{
		ArrayList<TaskModel> taskList = new ArrayList<TaskModel> ();
		taskList.add(task);
		cancelReminders(true, taskList);
	}
	
	/**
	 * Cancels the reminders for all tasks that match remove complete
	 * @param removeCompleted True if we want to cancel for completed tasks, false for incompleted ones
	 * @param tasks The tasks to cancel the reminders for
	 */
	private void cancelReminders(boolean removeCompleted, ArrayList<TaskModel> tasks) 
	{
		ArrayList<Integer> remindersToCancelList = new ArrayList<Integer>();
		
		for(int i = 0; i < tasks.size(); i++)
		{
			addCompletedItemsToList(removeCompleted, remindersToCancelList, tasks.get(i).taskItems);
		}
		
		// Convert array list<Integer> to int[]
		int length = remindersToCancelList.size();
		int[] remindersToCancelArray = new int [length];
		
		for(int i = 0; i < length; i++)
		{
			remindersToCancelArray[i] = remindersToCancelList.get(i);
		}
		
		m_taskAgent.cancelReminder(remindersToCancelArray);
	}

	/**
	 * Adds task items who's completion matchs removeCompleted to the remindersToCancelList list
	 * @param removeCompleted True if we want to cancel for completed tasks, false for incompleted ones
	 * @param remindersToCancelList The list to add task item IDs to if they match
	 * @param taskItems The task items to check
	 */
	private void addCompletedItemsToList(boolean removeCompleted,
										 ArrayList<Integer> remindersToCancelList, 
										 ArrayList<TaskItemModel> taskItems) 
	{
		Iterator<TaskItemModel> itemIterator = taskItems.iterator();

		// Collect all completed task items
		while(itemIterator.hasNext())
		{
			TaskItemModel item = itemIterator.next();
			
			if(item.isComplete == removeCompleted)
			{
				remindersToCancelList.add(item.taskItemID);
			}
		}
	}
	
	/**
	 * Creates a TaskModel for the team specified
	 * @param team The team to create the task for
	 * @return A new Task Model for the team
	 */
	private TaskModel createTaskModel(TeamType team)
	{				
		ArrayList<TaskItemModel> taskItems = new ArrayList<TaskItemModel>();
		switch(team)
		{
			case requestor:
				taskItems.add(new TaskItemModel("Agree to Terms of Use"));
				taskItems.add(new TaskItemModel("Complete Privacy and Security Training Module"));
				break;
				
			case zoneManager:
				taskItems.add(new TaskItemModel("Review and Validate Software Aquisition Request"));
				break;
							
			case asrc:
				taskItems.add(new TaskItemModel("Complete Threat Risk Assessment"));
				taskItems.add(new TaskItemModel("Tracking the Completion of the Treament Plan"));
				taskItems.add(new TaskItemModel("Ensure Univeristy Infrastucture is Secure from Vulnerabilities"));
				taskItems.add(new TaskItemModel("Confirm Data Level and Data Storage Zone"));
				break;
				
			case aws:
				taskItems.add(new TaskItemModel("Confirm the Software Install Location Field if the Software is Not Installed on Laptop"));
				taskItems.add(new TaskItemModel("System Design, Build and Integration with Existing Systems/Applications"));
				break;
				
			case legal:
				taskItems.add(new TaskItemModel("Review Contracts and Statment of Work"));
				taskItems.add(new TaskItemModel("Review liability, Agreement Issues and Other Legal Concerns"));
				taskItems.add(new TaskItemModel("Negotiate Vendors and Suppliers on Agreements and Amendments"));
				break;
				
			case privacy:
				taskItems.add(new TaskItemModel("Confirmation of Data Level for the Software Requested"));
				taskItems.add(new TaskItemModel("Ensure Compliance with Standards and Guidelines within the FOIP and HIPPA Acts"));
				taskItems.add(new TaskItemModel("Complete Privacy Impact Assessment (PIA)"));
				break;
				
			case supplyChain:
				taskItems.add(new TaskItemModel("Obtain EULA Agreements"));
				taskItems.add(new TaskItemModel("Obtain Requestor Signatures on Export Control Documents"));
				taskItems.add(new TaskItemModel("Confirm Software Name and Vendor with Ticket"));
				taskItems.add(new TaskItemModel("Dispatch Purchase Order"));
				break;
				
			case accountsPayable:
				taskItems.add(new TaskItemModel("Process Payment Invoices received from the Vendor/Supplier"));
				break;
				
			case deskside:
				taskItems.add(new TaskItemModel("Install the Software or Assist with Installation"));
				break;
				
			default:
				System.err.println("ERROR, cannot create task for team type: " + team);
				break;
		}
		String teamContact = "rohityeast+" + team + "@gmail.com";	// Mock Email Address

		TaskModel task = new TaskModel(team, teamContact, getRequestInfo(), taskItems);
		
		return task;
	}
}
