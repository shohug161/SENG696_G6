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

	public NewRequestView(ViewController vc)
	{
		m_viewController = vc;
		
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
		submitRequest = new JButton("Submit");
		
		panel = new JPanel(new SpringLayout());
	}
	
	public void newRequest()
	{
		panel.add(nameLabel);
		panel.add(emailLabel);
		
		SpringUtilities.makeCompactGrid(panel, 11, 2, 6, 6, 6, 6);
		setVisible(true);
	}
	
	class requestListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// send request to view controller
			
		}
		
	}
	
	public static class SpringUtilities
	{
		
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
	            Spring width = Spring.constant(0);
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
