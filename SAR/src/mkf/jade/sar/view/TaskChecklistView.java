package mkf.jade.sar.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import mkf.jade.sar.model.*;

public class TaskChecklistView extends JFrame {

	private ViewController m_viewController;
	private JPanel requestPanel, taskPanel;
	private int tasksApproved;
	private JFrame frame;
	private TaskModel taskModel;
	
	public TaskChecklistView(ViewController vc)
	{
		m_viewController = vc;
		tasksApproved = 0;
		frame = new JFrame();
	}
	
	//TODO
	// one window with all of their assigned tasks
	
	public void displayTasks()
	{
		RequestInfoModel requestInfo = taskModel.requestInfo;
		
		JLabel software = new JLabel("Software:\n" + requestInfo.softwareName);
		software.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(software);
		JLabel reason = new JLabel("Reason:\n" + requestInfo.businessReason);
		reason.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(reason);
		JLabel cost = new JLabel("Cost:\n" + String.valueOf(requestInfo.softwareCost));
		requestPanel.add(cost);
		JLabel department = new JLabel("Department:\n" + requestInfo.departmentName);
		department.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(department);
		JLabel infoType = new JLabel("Information Level:\n" + Integer.toString(InformationTypeHelper.convertToInt(requestInfo.informationType)));
		infoType.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(infoType);
		JLabel comments = new JLabel("Comments:\n" + requestInfo.comments);
		comments.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(comments);
		
		frame.add("North", requestPanel);
		
		for (TaskItemModel t: taskModel.taskItems)
		{
			JLabel label = new JLabel(t.taskItemName);
			JButton approve = new JButton("Approve Task");
			approve.addActionListener(new ApproveListener(t, approve));
			taskPanel.add(label);
			label.setLabelFor(approve);
			taskPanel.add(approve);
		}
		
		JLabel label = new JLabel("");
		JButton deny = new JButton("Deny Request");
		deny.addActionListener(new DenyListener());
		taskPanel.add(label);
		label.setLabelFor(deny);
		taskPanel.add(deny);
		
		SpringUtilities.makeCompactGrid(taskPanel,
                taskModel.taskItems.size()+1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		
		// TODO
		// look into the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        taskPanel.setOpaque(true);  //content panes must be opaque
		frame.add("South", taskPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Handles the button press once a user submits their approvals
	 * @author Desi
	 *
	 */
	class ApproveListener implements ActionListener
	{
		
		private TaskItemModel taskItem;

		public ApproveListener(TaskItemModel t, JButton button) {
			// TODO Auto-generated constructor stub
			taskItem = t;
			button.setBackground(Color.GREEN);
		}

		// set task as completed
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			tasksApproved++;
			taskItem.isComplete = true;
		}
	}
	
	/**
	 * Handles the button press if a user denies the SAR
	 * @author Desi
	 *
	 */
	class DenyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			m_viewController.userDeniedRequest(taskModel.requestInfo.requestID);
		}
	}
	
	/**
	 * User has approved one of their assigned tasks. A notification needs to be sent to the Task Agent through the UI Agent.
	 */
	class SubmitTasks implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// send task list to the view controller to send to UI agent
			m_viewController.taskItemComplete(taskModel, allTasksApproved());
		}
	}
	
	public boolean allTasksApproved()
	{
		if (tasksApproved == taskModel.taskItems.size())
		{
			return true;
		}
		return false;
	}

}
