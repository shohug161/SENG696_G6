package mkf.jade.sar.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
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
	}
	
	
	public void displayTasks(TaskModel task)
	{
		taskModel = task;
		frame = new JFrame();

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
		
		frame.getContentPane().add("North", requestPanel);
		
		taskPanel = new JPanel(new GridLayout(taskModel.taskItems.size() + 1, 2));
		
		for (TaskItemModel t: taskModel.taskItems)
		{
			JLabel label = new JLabel(t.taskItemName);
			JButton approve = new JButton("Approve Task");
			approve.addActionListener(new ApproveListener(t, approve));
			
			if(t.isComplete)
			{
				approve.setBackground(Color.GREEN);
			}
			approve.revalidate();
			approve.repaint();
			
			taskPanel.add(label);
			label.setLabelFor(approve);
			taskPanel.add(approve);
		}
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitTasks());
		submit.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));
		taskPanel.add(submit);
		
		if(taskModel.team != TeamType.accountsPayable && 
		   taskModel.team != TeamType.deskside)
		{
			JButton deny = new JButton("Deny Request");
			deny.addActionListener(new DenyListener());
			deny.setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
			taskPanel.add(deny);
		}
		
		taskPanel.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));
		
		// look into the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        frame.revalidate();
        taskPanel.setOpaque(true);  //content panes must be opaque
		frame.getContentPane().add("South", taskPanel);
		
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
			frame.dispose();
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
			m_viewController.taskItemComplete(taskModel);
			frame.dispose();
			m_viewController.displayHomePage();
		}
	}

}
