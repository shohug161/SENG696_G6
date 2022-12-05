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
	

	private static final long serialVersionUID = -5357064072022624068L;

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
	private JLabel usernameLabel, passwordLabel, welcome, error;
	
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
	private TeamType team;
	
	public LoginView(String label, ViewController vc) {
		super(label);
		
		setTitle("LOGIN PAGE");
		frame = new JFrame(label);
		frame.setSize(400, 250);
		
		m_viewController = vc;
		
		welcome = new JLabel("Welcome to the Software Acquisition Request portal.");
		
		usernameLabel = new JLabel("Username");
		
		username = new JTextField();
		
		passwordLabel = new JLabel("Password");
		
		password = new JPasswordField();
		
		loginButton = new JButton("Login");
		
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);;
	}
	
	/*******************************  METHODS   ****************************************/	


	public void displayLoginWindow()
	{
		loginPanel = new JPanel();
		loginPanel.setLayout(null);
		this.add(loginPanel);
		
		JLabel welcome2 = new JLabel("Please login to continue.");
		
		welcome.setBounds(20, 8, 360, 20);
		loginPanel.add(welcome);
		
		welcome2.setBounds(20, 28, 360, 20);
		loginPanel.add(welcome2);
		
		usernameLabel.setBounds(80, 68, 70, 20);
		loginPanel.add(usernameLabel);
		
		username.setBounds(80, 88, 170, 28);
		loginPanel.add(username);
		
		passwordLabel.setBounds(80, 115, 70, 20);
		loginPanel.add(passwordLabel);
		
		password.setBounds(80, 135, 170, 28);
		loginPanel.add(password);
		
		loginButton.setBounds(95, 165, 100, 25);
		loginButton.setForeground(Color.BLACK);
		loginButton.addActionListener(new LoginListener());
		loginPanel.add(loginButton);
		frame.add(loginPanel);
		
		error = new JLabel("");
		error.setBounds(50, 190, 250, 20);
		error.setForeground(Color.RED);
		loginPanel.add(error);
		
		frame.setVisible(true);		
	}
	
	
	public void displayHomePage() {
		displayHomePage(team == TeamType.requestor);
	}

	
	public void displayHomePage(boolean isRequestor) {
		
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JButton sar = new JButton("Submit a new Software Request");
		frame = new JFrame("Home Page");
		selectionPanel = new JPanel();
		sar.addActionListener(new sarListener());
		sar.setBounds(50, 50, 200, 20);
		sar.setForeground(Color.BLACK);
		
		JButton viewTasks = new JButton("View assigned tasks");
		viewTasks.addActionListener(new viewTaskListener());
		viewTasks.setBounds(50, 150, 200, 20);
		viewTasks.setForeground(Color.BLACK);
		
		JButton logout = new JButton("Logout");
		logout.addActionListener(new LogoutListener());
		logout.setBounds(50, 200, 200, 20);
		logout.setForeground(Color.BLACK);
		
		selectionPanel.add(sar);		
		if(!isRequestor)selectionPanel.add(viewTasks);
		selectionPanel.add(logout);
		this.add(selectionPanel);
		
		frame.setSize(300,200);
		frame.add(selectionPanel);
		frame.setVisible(true);
	}
	
	private void addErrorMessage(String errorMessage)
	{
		password.setText("");
		error.setText(errorMessage);
		loginPanel.revalidate();
		loginPanel.repaint();
	}
	
	/**
	 * Handles a login request
	 * @author Desiree
	 *
	 */
	public class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String teamStr = username.getText();

			try {
				team = TeamType.valueOf(teamStr);
			
				if (password.getPassword() != null) {
					String pass = new String(password.getPassword());
					
					// check for the correct password
					// all teams will use the same password, "password" for the simplicity of the demo
					if (pass.equals("password")) {
						
						// send team name to the view controller if the password is correct
						frame.dispose();
						dispose();
	
						addErrorMessage("");

						m_viewController.userLogon(team);
					}
					// incorrect password entered
					else {
						// ask for password info again
						team = TeamType.noTeam;
						addErrorMessage("Incorrect password, please retry.");
					}
				}
			}
			catch(Exception e2) 
			{
				addErrorMessage("Incorrect username, please retry.");
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
			m_viewController.displayRequestInfo(team);
			frame.dispose();
		}
	}

	public class LogoutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			m_viewController.userLoggedOut();
			frame.dispose();
		}
	}
}
