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

import mkf.jade.sar.model.InformationTypeHelper;
import mkf.jade.sar.model.RequestInfoModel;

public class ZoneManagerView extends JFrame {

	private ViewController m_viewController;
	private JTextField informationType;
	private JPanel requestPanel, updatePanel;
	private JFrame frame;
	private RequestInfoModel requestInfo;
	
	public ZoneManagerView(ViewController vc, RequestInfoModel rm)
	{
		m_viewController = vc;
		requestInfo = rm;
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
	
	public void display()
	{
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
			m_viewController.zoneManagerApproval(requestInfo);
			frame.dispose();
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
