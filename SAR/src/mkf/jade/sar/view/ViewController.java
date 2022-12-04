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
	public void getRequestInfo(int requestID, RequestInfoModel rm)
	{
		
		TeamType team = getTaskByRequestID(requestID).team;
		
		if (team.equals(TeamType.zoneManager))
		{
			zoneManagerReview(rm);
		}
		else {
			// TODO
			// task checklist view for other users
			taskUI.displayTasks();
		}
	}
	
	/**
	 * calls the zone manager UI
	 */
	public void zoneManagerReview(RequestInfoModel rm)
	{
		zoneManagerUI.display(rm);
	}
	
	public void requestValidated(int id, InformationType level)
	{
		// make a new task model
		// update request with the information level
		// remove from select request view
		TaskModel task = getTaskByRequestID(id);
		selectRequestUI.removeRequest(id);
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
	 * @param allComplete, boolean determing whether all tasks have been approved
	 */
	public void taskItemComplete(TaskModel tm, boolean allComplete)
	{
		// select request UI again
		if (allComplete) {
			selectRequestUI.removeRequest(tm.requestInfo.requestID);
		}
		uiAgent.taskComplete(tm);
	}
	
	/**
	 * Notifies UI agent that a user has logged on from a certain team
	 * @param teamName
	 */
	public void userLogon(String teamName)
	{
		System.out.println(teamName + " logged on");
		uiAgent.userLogon(TeamType.valueOf(teamName));
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
		// do we have any specific messages?
		loginUI = new LoginView("Login", this);
		selectRequestUI.clearList();
		loginUI.displayLoginWindow();
		
	}
}
