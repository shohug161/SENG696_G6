package mkf.jade.sar.view;

import mkf.jade.sar.model.*;

public class ViewController {
	
	private UserInterface ui;
	private LoginView loginUI;
	private NewRequestView requestUI;
	private TaskChecklistView taskUI;
	private ZoneManagerView zoneManagerUI;
	
	public ViewController() 
	{
		loginUI = new LoginView("Login", this);
		requestUI = new NewRequestView(this);
		taskUI = new TaskChecklistView(this);
	}
	
	public void displayLoginInfo()
	{
		loginUI.displayLoginWindow();
	}
	
	
	public void displayRequestInfo(RequestModel rm)
	{
		
	}
	
	public void getRequestInfo(TeamType team)
	{
		
	}
	
	public void addTaskToSelection(TaskModel task)
	{
		
	}
	
	public void userLogon(String teamName)
	{
		
	}
	
	public void newRequestAdded(RequestInfoModel rm)
	{
		// TODO
		// send request to task agent 
		
	}
	
	public void createNewRequest()
	{
		requestUI.newRequest();
	}
	
	
}
