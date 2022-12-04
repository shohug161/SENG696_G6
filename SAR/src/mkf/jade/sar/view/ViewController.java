package mkf.jade.sar.view;

import mkf.jade.sar.model.*;
import mkf.jade.sar.taskAgentHelper.RequestManager;
import mkf.jade.sar.*;


public class ViewController {
	
	private UserInterfaceAgent uiAgent;
	private LoginView loginUI;
	private NewRequestView requestUI;
	private TaskChecklistView taskUI;
	private ZoneManagerView zoneManagerUI;
	private SelectRequestView selectRequestUI;
	public TaskModel task;
	
	public ViewController(UserInterfaceAgent ui) 
	{
		uiAgent = ui;
		loginUI = new LoginView("Login", this);
		requestUI = new NewRequestView(this);
		taskUI = new TaskChecklistView(this);
		selectRequestUI = new SelectRequestView(this);
		zoneManagerUI = new ZoneManagerView(this);
	}
	
	public void displayLoginInfo()
	{
		System.err.println("Display Login Info");

		loginUI = new LoginView("Login", this);
		loginUI.displayLoginWindow();
	}
	
	/**
	 * Calls SelectRequestView UI to display all of the requests that a user has tasks for.
	 * @param team
	 */
	public void displayRequestInfo(TeamType team)
	{
		selectRequestUI = new SelectRequestView(this);
		selectRequestUI.display();
	}
	
	/**
	 * Calls ZoneManagerView for zone managers and TaskChecklistView UI for all other users when they pick "View Tasks".
	 * Allows users to approve or deny the tasks they are assigned.
	 * @param team
	 */
	public void getRequestInfo(TeamType team)
	{
		// TODO
		// display zone manager view for zone manager or else tasks for other users
		if (team.equals(TeamType.zoneManager))
		{
			zoneManagerReview();
		}
		else {
			// TODO
			// task checklist view for other users
			taskUI.displayTasks(task);
		}
	}
	
	/**
	 * calls the zone manager UI
	 */
	public void zoneManagerReview()
	{
		
		zoneManagerUI.display(task);
	}
	
	public void requestValidated(TaskModel tm)
	{
		// make a new task model
		// update request with the information level 
	}
	
	public void addTaskToSelection(TaskModel task)
	{
		
	}
	
	public void userDeniedRequest(int requestID)
	{
		// TODO
		// select request UI again
		selectRequestUI.removeRequest(requestID);
		uiAgent.reqeuestDenied(requestID);
	}
	
	public void taskItemComplete(TaskModel tm)
	{
		// select request UI again
		task = tm;
		uiAgent.taskComplete(tm);
	}
	
	public void userLogon(String teamName)
	{
		uiAgent.userLogon(TeamType.valueOf(teamName));
	}
	
	public void newRequestAdded(RequestInfoModel rm)
	{
		uiAgent.submitRequest(rm);
		displayLoginInfo();
	}
	
	public void createNewRequest()
	{
		requestUI.newRequest();
	}
	
}
