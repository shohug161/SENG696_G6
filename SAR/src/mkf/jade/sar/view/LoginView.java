package mkf.jade.sar.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import mkf.jade.sar.model.TeamType;

/**
 * Creates and controls the login user interface for the SAR system.
 * @author Desi
 *
 */
public class LoginView extends JFrame {
	
	/*******************************  Member Variables   ****************************************/
	
	/**
	 * Used to display the login information and user selection
	 */
	private JFrame frame;
	
	/**
	 * Button to confirm user login
	 */
	private JButton loginButton;
	
	/**
	 * Text that will be on the panel for Username, Password and a welcome message
	 */
	private JLabel usernameLabel, passwordLabel, welcome;
	
	/**
	 * Holds the username that the user logs in with
	 */
	private JTextField username;
	
	/**
	 * Holds the user's password
	 */
	private JPasswordField password;
	
	/**
	 * Populated with all the buttons and labels
	 */
	private JPanel loginPanel, selectionPanel;
	
	/**
	 * Controls the Login UI
	 */
	private ViewController m_viewController;
	
	/**
	 * Holds the team name that the user logs in with
	 */
	private String team;
	
	public LoginView(String label, ViewController vc) {
		super(label);
		
		setTitle("LOGIN PAGE");
		frame = new JFrame(label);
		frame.setSize(250, 200);
		
		m_viewController = vc;
		
		welcome = new JLabel("Welcome to the Software Acquisition Request portal.\nPlease login to continue.");
		
		usernameLabel = new JLabel("Username");
		
		username = new JTextField();
		
		passwordLabel = new JLabel("Password");
		
		password = new JPasswordField();
		
		loginButton = new JButton("Login");
	}
	
	/*******************************  METHODS   ****************************************/	

	public void displayLoginWindow()
	{
		// TODO Auto-generated method stub
		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		this.add(loginPanel);
		
		loginPanel.add(welcome);
		
		usernameLabel.setBounds(50, 8, 70, 20);
		loginPanel.add(usernameLabel);
		
		username.setBounds(50, 28, 170, 28);
		loginPanel.add(username);
		
		passwordLabel.setBounds(50, 55, 70, 20);
		loginPanel.add(passwordLabel);
		
		password.setBounds(50, 75, 170, 28);
		loginPanel.add(password);
		
		loginButton.setBounds(75, 115, 100, 25);
		loginButton.setForeground(Color.BLACK);
		loginButton.addActionListener(new LoginListener());
		loginPanel.add(loginButton);
		frame.add(loginPanel);
		
		frame.setVisible(true);		
	}
	
	public void displayHomePage() {
		
		JButton sar = new JButton("Submit a new Software Request");
		frame = new JFrame();
		selectionPanel = new JPanel();
		sar.addActionListener(new sarListener());
		sar.setBounds(50, 50, 200, 20);
		sar.setForeground(Color.BLACK);
		
		JButton viewTasks = new JButton("View assigned tasks");
		viewTasks.addActionListener(new viewTaskListener());
		viewTasks.setBounds(50, 150, 200, 20);
		viewTasks.setForeground(Color.BLACK);
		
		this.add(selectionPanel);
		selectionPanel.add(sar);
		selectionPanel.add(viewTasks);
		
		frame.setSize(300,200);
		frame.add(selectionPanel);
		frame.setVisible(true);
	}
	
	public void addErrorMessage()
	{
		password.setText("");
		JLabel error = new JLabel("Incorrect password, please retry.");
		error.setBounds(100, 100, 100, 20);
		error.setForeground(Color.RED);
		loginPanel.add(error);
	}
	
	/**
	 * Handles a login request
	 * @author Desiree
	 *
	 */
	public class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			
			if (password.getPassword() != null) {
				String pass = new String(password.getPassword());
				// check for the correct password
				// all teams will use the same password, "password" for the simplicity of the demo
				if (pass.equals("password")) {
					// send team name to the view controller if the password is correct
					frame.dispose();
					dispose();
					team = username.getText();
					displayHomePage();
				}
				// incorrect password entered
				else {
					// ask for password info again
					addErrorMessage();
				}
			}
		}
	}
	
	public class sarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			m_viewController.createNewRequest();
			frame.dispose();
		}
	}
	
	public class viewTaskListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e)
		{
			m_viewController.displayRequestInfo(TeamType.valueOf(team));
			frame.dispose();
		}
	}

}
