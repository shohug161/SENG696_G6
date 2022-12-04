package mkf.jade.sar.view;

import mkf.jade.sar.model.*;

import java.util.ArrayList;

import mkf.jade.sar.*;


public class ViewController {
	
	private UserInterfaceAgent uiAgent;
	private LoginView loginUI;
	private NewRequestView requestUI;
	private TaskChecklistView taskUI;
	private ZoneManagerView zoneManagerUI;
	private SelectRequestView selectRequestUI;
	private ArrayList<TaskModel> tasks;
	
	public ViewController(UserInterfaceAgent ui) 
	{
		uiAgent = ui;
		loginUI = new LoginView("Login", this);
		requestUI = new NewRequestView(this);
		taskUI = new TaskChecklistView(this);
		selectRequestUI = new SelectRequestView(this);
		zoneManagerUI = new ZoneManagerView(this);
		tasks = new ArrayList<TaskModel>();
		displayLoginInfo();
	}
	
	/*******************************  Methods   ****************************************/	
	
	/**
	 * 
	 * @param reqID
	 * @return
	 */
	public TaskModel getTaskByRequestID(int reqID)
	{
		for (TaskModel t: tasks)
		{
			if (t.requestInfo.requestID == reqID)
			{
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Calls the login UI for users to login
	 */
	public void displayLoginInfo()
	{
		loginUI.repaint();
		loginUI.displayLoginWindow();
	}
	
	/**
	 * Calls SelectRequestView UI to display all of the requests that a user has tasks for.
	 * @param team
	 */
	public void displayRequestInfo(TeamType team)
	{
		selectRequestUI.display();
	}
	
	/**
	 * Calls ZoneManagerView for zone managers and TaskChecklistView UI for all other users when they pick "View Tasks".
	 * Allows users to approve or deny the tasks they are assigned.
	 * @param team
	 */
	public void chooseRequest(int requestID, RequestInfoModel rm)
	{
		
		TaskModel task = getTaskByRequestID(requestID);
		TeamType team = task.team;
		
		if (team.equals(TeamType.zoneManager))
		{
			zoneManagerReview(rm);
		}
		else {
			// TODO
			// task checklist view for other users
			// create task for the user
			taskUI.displayTasks(task);
		}
	}
	
	/**
	 * calls the zone manager UI
	 */
	public void zoneManagerReview(RequestInfoModel rm)
	{
		zoneManagerUI.display(rm);
	}
	
	/**
	 * Request is validated by the zone manager
	 * @param id The request id that's been validated
	 * @param level The level that's been confirmed for the request by the zone manager
	 */
	public void requestValidated(int id, InformationType level)
	{
		displayHomePage();
		TaskModel task = getTaskByRequestID(id);
		task.requestInfo.informationType = level;
		completeTask(task);
		
		uiAgent.taskComplete(task);
		
		selectRequestUI.removeRequest(id);
	}
	
	/**
	 * Completes all tasks items in a task - Should only be used for zone manager task
	 * @param task The task to complete
	 */
	private void completeTask(TaskModel task)
	{
		for(TaskItemModel ti : task.taskItems)
		{
			ti.isComplete = true;
		}
		task.updateIsComplete();
	}
	
	/**
	 * Adds a task to the list of requests that are added to the SelectRequestView UI 
	 * @param task
	 */
	public void addTaskToSelection(TaskModel task)
	{
		tasks.add(task);
		selectRequestUI.addRequest(task.requestInfo);
	}
	
	/**
	 * Handles a user denying a software request
	 * @param requestID
	 */
	public void userDeniedRequest(int requestID)
	{
		// TODO
		// select request UI again
		selectRequestUI.removeRequest(requestID);
		uiAgent.reqeuestDenied(requestID);
	}
	
	/**
	 * Handles the completion of a task(s)
	 * @param tm, task model with the request information
	 */
	public void taskItemComplete(TaskModel task)
	{
		// select request UI again
		if (task.isComplete) {
			selectRequestUI.removeRequest(task.requestInfo.requestID);
		}
		uiAgent.taskComplete(task);
	}
	
	/**
	 * Notifies UI agent that a user has logged on from a certain team
	 * @param teamName
	 */
	public void userLogon(TeamType team)
	{
		uiAgent.userLogon(team);
		loginUI.displayHomePage(team == TeamType.requestor);
	}
	
	public void displayHomePage()
	{
		loginUI.displayHomePage();
	}
	
	/**
	 * Submits a new request to the UI agent to be sent to the task agent.
	 * @param rm
	 */
	public void newRequestAdded(RequestInfoModel rm)
	{
		uiAgent.submitRequest(rm);
		displayLoginInfo();
	}
	
	/**
	 * Calls the UI to submit a new request.
	 */
	public void createNewRequest()
	{
		requestUI.newRequest();
	}
	
	/**
	 * Clear the list of tasks in the SelectRequestView 
	 */
	public void userLoggedOut()
	{
		loginUI = new LoginView("Login", this);
		tasks.clear();
		uiAgent.userLogon(TeamType.noTeam);
		selectRequestUI.clearList();
		loginUI.displayLoginWindow();
	}
}
