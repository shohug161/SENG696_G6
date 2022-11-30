package mkf.jade.sar.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import mkf.jade.sar.model.*;

/**
 * 
 * @author Desi
 *
 */
public class UserInterface extends JFrame {

	// 1. display start up page that the requestor logs into to submit a request
	// 2. request is submitted and that gets sent to the Task Service Agent
	// 3. Task Service Agent divides the tasks, sends them back and they are added to the UI
	// 4. 
	private JLabel teamName_label, password_label;
	private JTextField name_text, password_text;
	private JButton okay_button, cancel_button;
	private JPasswordField password;
	public RequestInfoModel rim;
	public boolean submittedSAR = false;
	
	public UserInterface(String label)
	{
		super(label);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 800);
		
	}
	
	public void displayRequestWindow()
	{
		
		submittedSAR = true;
	}
	
	public void displayLoginWIndow()
	{
		
	}
	
	public RequestInfoModel getRequestInfoModel()
	{
		return rim;
	}
	
}
