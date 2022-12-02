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
	private JPanel panel;
	
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
		frame.setSize(300, 300);
		
		m_viewController = vc;
		
		panel = new JPanel();
		
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
		panel.setLayout(null);
		this.add(panel);
		
		panel.add(welcome);
		
		usernameLabel.setBounds(50, 8, 70, 20);
		panel.add(usernameLabel);
		
		username.setBounds(50, 28, 170, 28);
		panel.add(username);
		
		passwordLabel.setBounds(50, 55, 70, 20);
		panel.add(passwordLabel);
		
		password.setBounds(50, 75, 170, 28);
		panel.add(password);
		
		loginButton.setBounds(100, 135, 100, 25);
		loginButton.setForeground(Color.BLACK);
		loginButton.addActionListener(new LoginListener());
		panel.add(loginButton);
		frame.add(panel);
		
		frame.setVisible(true);		
	}
	
	public void displayHomePage(String team) {
		
		JButton sar = new JButton("Submit a new Software Request");
		sar.addActionListener(new sarListener());
		sar.setBounds(150, 50, 100, 50);
		sar.setForeground(Color.BLACK);
		
		JButton viewTasks = new JButton("View assigned tasks");
		viewTasks.addActionListener(new viewTaskListener());
		viewTasks.setBounds(150, 200, 100, 50);
		viewTasks.setForeground(Color.BLACK);
		
		panel.add(sar);
		panel.add(viewTasks);
		
		frame.setSize(300,300);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void addErrorMessage()
	{
		password.setText("");
		JLabel error = new JLabel("Incorrect password, please retry.");
		error.setBounds(100, 110, 100, 20);
		error.setForeground(Color.RED);
		panel.add(error);
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
					dispose();
					team = username.getText();
					displayHomePage(team);
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
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			m_viewController.createNewRequest();
			dispose();
		}
	}
	
	public class viewTaskListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			m_viewController.getRequestInfo(team);
			dispose();
		}
	}

}
