package mkf.jade.sar.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import mkf.jade.sar.model.InformationType;
import mkf.jade.sar.model.InformationTypeHelper;
import mkf.jade.sar.model.RequestInfoModel;

/**
 * Shows a UI that can be used to submit a new software request
 * @author Desiree
 *
 */
public class NewRequestView extends JFrame {
	
	private ViewController m_viewController;
	private JLabel nameLabel, softwareLabel, informationLevelLabel, departmentLabel, reasonLabel, costLabel, numUsersLabel, emailLabel, 
					vendorLabel, vendorEmailLabel, commentsLabel;
	private JTextField name, software, informationLevel, department, reason, cost, numUsers, email, vendor, vendorEmail, comments;
	private JButton submitRequest;
	private JPanel panel;
	private JFrame frame;

	public NewRequestView(ViewController vc)
	{
		m_viewController = vc;
		setSize(500, 800);
		
		frame = new JFrame("New Request");
		frame.setSize(400, 800);
		
		nameLabel = new JLabel("Name");
		softwareLabel = new JLabel("Software Name");
		informationLevelLabel = new JLabel("Information Level");
		departmentLabel = new JLabel("Department");
		reasonLabel = new JLabel("Reason");
		costLabel = new JLabel("Software Cost");
		numUsersLabel = new JLabel("Number of Users");
		emailLabel = new JLabel("Email");
		vendorLabel = new JLabel("Vendor");
		vendorEmailLabel = new JLabel("Vendor Email");
		commentsLabel = new JLabel("Comments");
		
		name = new JTextField();
		software = new JTextField();
		informationLevel = new JTextField();
		department = new JTextField();
		reason = new JTextField();
		cost = new JTextField();
		numUsers = new JTextField();
		email = new JTextField();
		vendor = new JTextField();
		vendorEmail = new JTextField();
		comments = new JTextField();
		submitRequest = new JButton("Submit Request");
		
		panel = new JPanel(new SpringLayout());
	}
	
	public void newRequest() {
		frame.setSize(600, 900);
		
		panel.add(nameLabel);
		nameLabel.setLabelFor(name);
		panel.add(name);
		
		panel.add(emailLabel);
		emailLabel.setLabelFor(email);
		panel.add(email);
		
		panel.add(softwareLabel);
		softwareLabel.setLabelFor(software);
		panel.add(software);
		
		panel.add(reasonLabel);
		reasonLabel.add(reason);
		panel.add(reason);
		
		panel.add(costLabel);
		costLabel.setLabelFor(cost);
		panel.add(cost);
		
		panel.add(departmentLabel);
		departmentLabel.setLabelFor(department);
		panel.add(department);
		
		panel.add(numUsersLabel);
		numUsersLabel.setLabelFor(numUsers);
		panel.add(numUsers);
		
		panel.add(informationLevelLabel);
		informationLevelLabel.setLabelFor(informationLevel);
		panel.add(informationLevel);
		
		panel.add(reasonLabel);
		reasonLabel.setLabelFor(reason);
		panel.add(reason);
		
		panel.add(departmentLabel);
		departmentLabel.setLabelFor(department);
		panel.add(department);
		
		panel.add(vendorLabel);
		vendorLabel.setLabelFor(vendor);
		panel.add(vendor);
		
		panel.add(vendorEmailLabel);
		vendorEmailLabel.setLabelFor(vendorEmail);
		panel.add(vendorEmail);
		
		panel.add(commentsLabel);
		commentsLabel.setLabelFor(comments);
		panel.add(comments);
		
		JLabel requestLabel = new JLabel("");
		panel.add(requestLabel);
		requestLabel.setLabelFor(submitRequest);
		panel.add(submitRequest);
		submitRequest.addActionListener(new RequestListener());
		
		SpringUtilities.makeCompactGrid(panel,
                12, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		
		// TODO
		// look into the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        panel.setOpaque(true);  //content panes must be opaque
        frame.setContentPane(panel);
 
        //Display the window.
        //frame.pack();
        frame.setVisible(true);
	}
	
	public RequestInfoModel generateRequestInfo()
	{
		// public RequestInfoModel (String swName, String depName, int numUsers, double swCost, String busReason, InformationType iType, String reqName, String reqEmail, 
		// String vName, String vEmail, String comm)
		// InformationTypeHelper.convertFromInt(Integer.parseInt(informationLevel.getText()))
		
		RequestInfoModel rm = new RequestInfoModel(software.getText(), department.getText(), Integer.parseInt(numUsers.getText()), Double.parseDouble(cost.getText()), reason.getText(),
				InformationType.valueOf(informationLevel.getText()), name.getText(), email.getText(), vendor.getText(), vendorEmail.getText(), comments.getText());
		
		return rm;
	}
	
	class RequestListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// message saying request was submitted and they will be notified with updates
			m_viewController.newRequestAdded(generateRequestInfo());
			// TODO
			// take them back to the login page
			frame.dispose();
			m_viewController.displayLoginInfo();

		}
		
	}
	
}
