package mkf.jade.sar.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
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
	
	public static class SpringUtilities {
		/* Used by makeCompactGrid. */
	    private static SpringLayout.Constraints getConstraintsForCell(
	                                                int row, int col,
	                                                Container parent,
	                                                int cols) {
	        SpringLayout layout = (SpringLayout) parent.getLayout();
	        Component c = parent.getComponent(row * cols + col);
	        return layout.getConstraints(c);
	    }
		
		/**
	     * Aligns the first <code>rows</code> * <code>cols</code>
	     * components of <code>parent</code> in
	     * a grid. Each component in a column is as wide as the maximum
	     * preferred width of the components in that column;
	     * height is similarly determined for each row.
	     * The parent is made just big enough to fit them all.
	     *
	     * @param rows number of rows
	     * @param cols number of columns
	     * @param initialX x location to start the grid at
	     * @param initialY y location to start the grid at
	     * @param xPad x padding between cells
	     * @param yPad y padding between cells
	     */
	    public static void makeCompactGrid(Container parent,
	                                       int rows, int cols,
	                                       int initialX, int initialY,
	                                       int xPad, int yPad) {
	        SpringLayout layout;
	        try {
	            layout = (SpringLayout)parent.getLayout();
	        } catch (ClassCastException exc) {
	            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
	            return;
	        }

	        //Align all cells in each column and make them the same width.
	        Spring x = Spring.constant(initialX);
	        for (int c = 0; c < cols; c++) {
	            Spring width = Spring.constant(200);
	            for (int r = 0; r < rows; r++) {
	                width = Spring.max(width,
	                                   getConstraintsForCell(r, c, parent, cols).
	                                       getWidth());
	            }
	            for (int r = 0; r < rows; r++) {
	                SpringLayout.Constraints constraints =
	                        getConstraintsForCell(r, c, parent, cols);
	                constraints.setX(x);
	                constraints.setWidth(width);
	            }
	            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
	        }

	        //Align all cells in each row and make them the same height.
	        Spring y = Spring.constant(initialY);
	        for (int r = 0; r < rows; r++) {
	            Spring height = Spring.constant(0);
	            for (int c = 0; c < cols; c++) {
	                height = Spring.max(height,
	                                    getConstraintsForCell(r, c, parent, cols).
	                                        getHeight());
	            }
	            for (int c = 0; c < cols; c++) {
	                SpringLayout.Constraints constraints =
	                        getConstraintsForCell(r, c, parent, cols);
	                constraints.setY(y);
	                constraints.setHeight(height);
	            }
	            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
	        }

	        //Set the parent's size.
	        SpringLayout.Constraints pCons = layout.getConstraints(parent);
	        pCons.setConstraint(SpringLayout.SOUTH, y);
	        pCons.setConstraint(SpringLayout.EAST, x);
	    }
	}
	
}
