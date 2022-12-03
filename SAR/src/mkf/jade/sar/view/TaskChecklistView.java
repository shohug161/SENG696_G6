package mkf.jade.sar.view;

import java.util.ArrayList;
import mkf.jade.sar.model.*;

public class TaskChecklistView {

	private ViewController m_viewController;
	private ArrayList<TaskItemModel> tasks;
	
	public TaskChecklistView(ViewController vc)
	{
		m_viewController = vc;
	}
	
	//TODO
	// one window with all of their assigned tasks
	// each task has the option to approve or deny it
	// button to go back to home page??
	
	
	/**
	 * User has approved one of their assigned tasks. A notification needs to be sent to the Task Agent through the UI Agent.
	 */
	public void taskApproved()
	{
		
	}
	
	public void setTasks(ArrayList<TaskItemModel> taskList)
	{
		this.tasks = taskList;
	}
}
