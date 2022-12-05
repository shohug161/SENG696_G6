package mkf.jade.sar.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
		
	}
	
	// TODO
	// get request from view controller
	// display software name, department, business reason and information type, comments
	// validate that it is information level 3 or 4
	// update information level if it needs to change
	// 
	
	// create another panel on the frame that will appear if the zone manager wants to change the information level
	
	public void display(RequestInfoModel requestInfo)
	{
		requestPanel = new JPanel();
		requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
		updatePanel = new JPanel(new SpringLayout());
		frame = new JFrame("Verify Information Level");
		frame.setSize(400, 600);
		frame.setTitle("Zone Manager Approval");
		informationType = new JTextField();
		
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
		
		frame.add("North", requestPanel);
		
		JLabel confirmInfoLevel = new JLabel("Confirm or Update\nInformation Level");
		updatePanel.add(confirmInfoLevel);
		confirmInfoLevel.setLabelFor(informationType);
		updatePanel.add(informationType);
		
		JLabel placeholder = new JLabel("");
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ConfirmListener(requestInfo.requestID));
		updatePanel.add(placeholder);
		placeholder.setLabelFor(confirmButton);
		updatePanel.add(confirmButton);
		
		SpringUtilities.makeCompactGrid(updatePanel,
                2, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 
        updatePanel.setOpaque(true);  //content panes must be opaque
		frame.add("South", updatePanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	class ConfirmListener implements ActionListener
	{
		
		private int requestID;

		public ConfirmListener(int id)
		{
			requestID = id;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// go back to the requests to validate
			int infoLevel = Integer.parseInt(informationType.getText());
			frame.dispose();
			m_viewController.requestValidated(requestID, InformationTypeHelper.convertFromInt(infoLevel));
		}
		
	}
}
