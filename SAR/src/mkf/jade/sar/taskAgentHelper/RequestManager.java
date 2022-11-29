package mkf.jade.sar.taskAgentHelper;

import java.util.ArrayList;
import java.util.Iterator;

import mkf.jade.sar.model.*;

/**
 * Manages a single request for the task agent
 * @author Rohit
 */
public class RequestManager
{
	public RequestManager(TeamType loggedIn, RequestInfoModel requestInfo, TaskCommunicator taskCommunicator, TaskDatabaseManager taskDatabaseManager)
	{
		this.loggedIn = loggedIn;
		m_taskCommunicator = taskCommunicator;
		m_taskDatabaseManager = taskDatabaseManager;
		m_requestModel = new RequestModel(requestInfo);
		
		
		createTask(TeamType.zoneManager);
	}
	
	/*******************************  Member Variables   ****************************************/
	
	private TeamType loggedIn;

	private RequestModel m_requestModel;
		
	private TaskCommunicator m_taskCommunicator;
	
	private TaskDatabaseManager m_taskDatabaseManager;
		
	/*******************************  METHODS   ****************************************/
	
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
				m_taskCommunicator.sendTaskToUI(task);
			}
		}
		
		loggedIn = team;
	}
	
	/**
	 * Creates the task for the team
	 * @param team The team to create the task for
	 */
	public void createTask(TeamType team)
	{
		if (team == TeamType.noTeam) 
		{
			System.err.println("ERROR, cannot create task for \"noTeam\" team type.");
		}
		else
		{
			TaskModel task = createTaskModel(team);
			
			m_requestModel.requestTasks.add(task);
			m_taskCommunicator.sendNotification(task);
			
			if(team == loggedIn)
			{
				m_taskCommunicator.sendTaskToUI(task);
			}
		}
	}
	
	
	/******************************* HELPER METHODS   ****************************************/

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
				taskItems.add(new TaskItemModel("Review Constracts and Statment of Work"));
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
				taskItems.add(new TaskItemModel("Process Payment Invoices received From the Vendor/Supplier"));
				break;
				
			case deskside:
				taskItems.add(new TaskItemModel("Schedule Software Installion Job in the Job Scheduler"));
				taskItems.add(new TaskItemModel("Install the Software or Assist with Installation"));
				break;
				
			default:
				System.err.println("ERROR, cannot create task for team type: " + team);

				break;
		}
		String teamContact = "rohityeast" + team + "@gmail.com";	// Mock Email Address

		TaskModel task = new TaskModel(team, teamContact, m_requestModel.requestInfo, taskItems);
		
		return task;
	}
}
