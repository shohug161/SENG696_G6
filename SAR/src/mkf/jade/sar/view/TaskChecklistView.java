package mkf.jade.sar.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import mkf.jade.sar.model.*;

public class TaskChecklistView extends JFrame {


	private static final long serialVersionUID = -5715719695591068744L;
	private ViewController m_viewController;
	private JPanel requestPanel, taskPanel;
	private JFrame frame;
	private TaskModel taskModel;
	
	public TaskChecklistView(ViewController vc)
	{
		m_viewController = vc;
		frame = new JFrame();
	}
	
	
	public void displayTasks(TaskModel task)
	{
		taskModel = task;
		RequestInfoModel requestInfo = taskModel.requestInfo;
		
		requestPanel = new JPanel();
		requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));	
		
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
		
		taskPanel = new JPanel(new SpringLayout());
		
		for (TaskItemModel t: taskModel.taskItems)
		{
			JLabel label = new JLabel(t.taskItemName);
			JButton approve = new JButton("Approve Task");
			approve.addActionListener(new ApproveListener(t, approve));
			taskPanel.add(label);
			label.setLabelFor(approve);
			taskPanel.add(approve);
		}
		
		//JLabel label = new JLabel("");
		JButton deny = new JButton("Deny Request");
		deny.addActionListener(new DenyListener());
		//taskPanel.add(label);
		//label.setLabelFor(deny);
		taskPanel.add(deny);
		
		//JLabel slabel = new JLabel("");
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitTasks());
		//taskPanel.add(slabel);
		//slabel.setLabelFor(submit);
		taskPanel.add(submit);
		
		SpringUtilities.makeCompactGrid(taskPanel,
				taskModel.taskItems.size() + 1, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		
		// look into the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        taskPanel.setOpaque(true);  //content panes must be opaque
		frame.add("South", taskPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public boolean allTasksApproved()
	{
		if (taskModel.isComplete)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Handles the button press once a user submits their approvals
	 * @author Desi
	 *
	 */
	class ApproveListener implements ActionListener
	{
		
		private TaskItemModel taskItem;
		private JButton button;

		public ApproveListener(TaskItemModel t, JButton button) {
			taskItem = t;
			this.button = button;
		}

		// set task as completed
		@Override
		public void actionPerformed(ActionEvent e) {

			taskItem.isComplete = !taskItem.isComplete;
			taskModel.updateIsComplete();
			if(taskItem.isComplete)
			{
				button.setBackground(Color.GREEN);				
			}
			else
			{
				button.setBackground(Color.RED);
			}
			
			button.setOpaque(true);
			button.revalidate();
			button.repaint();
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
			m_viewController.userDeniedRequest(taskModel.requestInfo.requestID);
			dispose();
			m_viewController.displayHomePage();
		}
	}
	
	/**
	 * User has approved one of their assigned tasks. A notification needs to be sent to the Task Agent through the UI Agent.
	 */
	class SubmitTasks implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// send task list to the view controller to send to UI agent
			m_viewController.taskItemComplete(taskModel, allTasksApproved());
			dispose();
			m_viewController.displayHomePage();
		}
	}

}
