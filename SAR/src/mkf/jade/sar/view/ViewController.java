package mkf.jade.sar.view;

import mkf.jade.sar.model.*;
import mkf.jade.sar.*;

public class ViewController {
	
	private UserInterfaceAgent uiAgent;
	private LoginView loginUI;
	private NewRequestView requestUI;
	private TaskChecklistView taskUI;
	private ZoneManagerView zoneManagerUI;
	private SelectRequestView selectRequestUI;
	
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
		System.out.println("Display Login Info");

		loginUI = new LoginView("Login", this);
		loginUI.displayLoginWindow();
	}
	
	
	public void displayRequestInfo(TeamType team)
	{
		// TODO
		// get a list of requests for the team
		
		selectRequestUI.setVisible(true);
	}
	
	public void getRequestInfo(TeamType team)
	{
		//
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
