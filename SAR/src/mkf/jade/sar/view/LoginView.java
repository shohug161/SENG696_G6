package mkf.jade.sar.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {
	
	private JButton loginButton;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField username;
	private JPasswordField password;
	private JPanel panel;
	
	public LoginView(String label) {
		super(label);
		setTitle("LOGIN PAGE");
		
		panel = new JPanel();
		panel.setLayout(null);
		
		this.add(panel);
		
		usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(100, 8, 70, 20);
		panel.add(usernameLabel);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(100, 28, 170, 28);
		panel.add(passwordLabel);
		
		loginButton = new JButton("Login");
		
		

		
		
	}
	
	
	// when login button is pressed we pull up a new view
	//
	
	
	
}
