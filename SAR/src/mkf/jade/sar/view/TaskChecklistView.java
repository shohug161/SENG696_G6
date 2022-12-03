package mkf.jade.sar.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import mkf.jade.sar.model.*;

public class TaskChecklistView {

	private ViewController m_viewController;
	private ArrayList<TaskItemModel> taskList;
	private JPanel request, tasks;
	
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
	class SubmitTasks implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// send task list to the view controller to send to UI agent
			
		}
	}
	
	public void setTasks(ArrayList<TaskItemModel> list)
	{
		this.taskList = list;
	}
}
