package mkf.jade.sar.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import mkf.jade.sar.model.*;

public class ZoneManagerView extends JFrame {

	private ViewController m_viewController;
	private JTextField informationType;
	private JPanel requestPanel, updatePanel;
	private JFrame frame;
	
	public ZoneManagerView(ViewController vc)
	{
		super("Zone Manager Approval");
		m_viewController = vc;
		requestPanel = new JPanel();
		requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
		updatePanel = new JPanel(new SpringLayout());
		frame = new JFrame("Verify Information Level");
		frame.setSize(400, 600);
	}
	
	// TODO
	// get request from view controller
	// display software name, department, business reason and information type, comments
	// validate that it is information level 3 or 4
	// update information level if it needs to change
	// 
	
	// create another panel on the frame that will appear if the zone manager wants to change the information level
	
	public void display(TaskModel task)
	{
		RequestInfoModel requestInfo = task.requestInfo;
		
		JLabel software = new JLabel("Software:\n" + requestInfo.softwareName);
		software.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(software);
		JLabel reason = new JLabel("Reason:\n" + requestInfo.businessReason);
		reason.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(reason);
		JLabel department = new JLabel("Department:\n" + requestInfo.departmentName);
		department.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(department);
		JLabel infoType = new JLabel("Information Level:\n" + Integer.toString(InformationTypeHelper.convertToInt(requestInfo.informationType)));
		infoType.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(infoType);
		JLabel comments = new JLabel("Comments:\n" + requestInfo.comments);
		comments.setAlignmentX(LEFT_ALIGNMENT);
		requestPanel.add(comments);
		
		frame.add(requestPanel);
		
		JLabel confirmInfoLevel = new JLabel("Confirm or Update\nInformation Level");
		updatePanel.add(confirmInfoLevel);
		confirmInfoLevel.setLabelFor(informationType);
		updatePanel.add(informationType);
		
		JLabel placeholder = new JLabel("");
		JButton confirmButton = new JButton("OKAY");
		confirmButton.addActionListener(new ConfirmListener());
		updatePanel.add(placeholder);
		placeholder.setLabelFor(confirmButton);
		updatePanel.add(confirmButton);
		
		SpringUtilities.makeCompactGrid(updatePanel,
                2, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		
		// TODO
		// look into the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        updatePanel.setOpaque(true);  //content panes must be opaque
		frame.add(updatePanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	class ConfirmListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// go back to the requests to validate
			int infoLevel = Integer.parseInt(informationType.getText());
			requestInfo.informationType = InformationTypeHelper.convertFromInt(infoLevel);
			m_viewController.requestValidated(requestInfo.requestID, InformationTypeHelper.convertFromInt(infoLevel));
			frame.dispose();
		}
		
	}
}
