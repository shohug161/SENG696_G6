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
	private RequestManager requestManager;
	
	public ViewController(UserInterfaceAgent ui) 
	{
		uiAgent = ui;
		loginUI = new LoginView("Login", this);
		requestUI = new NewRequestView(this);
		taskUI = new TaskChecklistView(this);
	}
	
	public void displayLoginInfo()
	{
		System.out.println("Display Login Info");

		loginUI = new LoginView("Login", this);
		loginUI.displayLoginWindow();
	}
	
	
	public void displayRequestInfo(TeamType team)
	{
		// TODO
		// get a list of requests for the team
		
		selectRequestUI = new SelectRequestView(this);
	}
	
	public void getRequestInfo(TeamType team, RequestInfoModel rm)
	{
		// TODO
		// display zone manager view for zone manager or else tasks for other users
		if (team.equals(TeamType.valueOf("zoneManager")))
		{
			zoneManagerReview(rm);
		}
		else {
			// TODO
			// task checklist view for other users
		}
	}
	
	public void zoneManagerReview(RequestInfoModel rm)
	{
		zoneManagerUI = new ZoneManagerView(this, rm);
		zoneManagerUI.display();
	}
	
	public void zoneManagerApproval(RequestInfoModel rm)
	{
		// make a new task model
		// TaskModel tm = RequestManager.createTaskModel(TeamType.valueOf("zoneManager"));
		
	}
	
	public void addTaskToSelection(TaskModel task)
	{
		
	}
	
	public void userLogon(String teamName)
	{
		uiAgent.userLogon(TeamType.valueOf(teamName));
	}
	
	public void newRequestAdded(RequestInfoModel rm)
	{
		// TODO
		uiAgent.submitRequest(rm);
		// take back to home page??
	}
	
	public void createNewRequest()
	{
		requestUI.newRequest();
	}
	
	
}
