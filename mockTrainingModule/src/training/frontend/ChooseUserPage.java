package training.frontend;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Page for choosing the what trainee is completing training
 * @author Rohit
 *
 */
public class ChooseUserPage extends JPanel
{
	public ChooseUserPage(TrainingModuleController controller)
	{
		m_controller = controller;
		
		m_names = new DefaultListModel<String>();
		m_traineeNames = new JList<String>(m_names);
		JLabel title = new JLabel("Choose Trainee");
		JButton continueButton = new JButton("Continue");
		
		m_traineeNames.setFont(new Font("Serif", Font.PLAIN, 24));
		
		continueButton.addActionListener(new ContinueButtonPressed(this));
		continueButton.setFont(new Font("Serif", Font.PLAIN, 24));
		
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Serif", Font.PLAIN, 24));
		
		setLayout(new BorderLayout());
		add(title, BorderLayout.NORTH);
		add(m_traineeNames, BorderLayout.CENTER);
		add(continueButton, BorderLayout.SOUTH);

		setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
	}

	/*******************************  Member Variable   ****************************************/
	
	/**
	 * Controls the UI
	 */
	private TrainingModuleController m_controller;
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 3831954854127099275L;

	/**
	 * List of names that training has been enabled for
	 */
	private DefaultListModel<String> m_names;
	
	/**
	 * JList of names that training has been enabled for
	 */
	private JList <String> m_traineeNames;
	
	/*******************************  Methods   ****************************************/

	/**
	 * Adds a name to the list
	 * @param name The name added to the list
	 */
	public void addName(String name)
	{
		m_names.add(0, name);
		m_traineeNames.setSelectedIndex(0);
		m_traineeNames.repaint();
	}
	
	/**
	 * Removes a name from the list
	 * @param name The name to remove
	 */
	public void removeName(String name)
	{
		for(int i = m_names.size() - 1; i >= 0; i--)
		{
			if(m_names.get(i).equals(name))
			{
				m_names.remove(i);
			}
		}
		
		if(m_names.size() > 0)
		{
			m_traineeNames.setSelectedIndex(0);
		}
		
		m_traineeNames.repaint();
	}
	
	/**
	 * Called when the user pressed the continue button
	 */
	public void continueButtonPressed()
	{
		if(m_names.size() > 0)
		{
			int index = m_traineeNames.getSelectedIndex();
			m_controller.traineeSelected(m_names.get(index));
		}
	}
	
	/*******************************  Helper Class   ****************************************/
	
	/**
	 * Action listener for when the continue button is pressed
	 * @author Rohit
	 *
	 */
	private class ContinueButtonPressed implements ActionListener
	{
		
		public ContinueButtonPressed(ChooseUserPage parent)
		{
			m_parent = parent;
		}

		private ChooseUserPage m_parent;
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			m_parent.continueButtonPressed();
		}
	}
}
