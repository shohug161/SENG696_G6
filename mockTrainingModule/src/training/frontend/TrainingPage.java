package training.frontend;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Mock page for training materials
 * @author Rohit
 *
 */
public class TrainingPage extends JPanel 
{
	public TrainingPage(TrainingModuleController controller)
	{
		m_controller = controller;
		
		JButton trainingCompleteButton = new JButton("Complete Training");
		JLabel title = new JLabel("Training Materials");
		JTextArea textArea = new JTextArea("Imagine a bunch of training here");
		JScrollPane scroll = new JScrollPane(textArea);
		
		trainingCompleteButton.setFont(new Font("Serif", Font.PLAIN, 24));
		
		trainingCompleteButton.addActionListener(new AgreeButtonPressed(this));
		title.setFont(new Font("Serif", Font.PLAIN, 24));

		setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		
		textArea.setFont(new Font("Serif", Font.PLAIN, 24));

		setLayout(new BorderLayout());
		add(title, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(trainingCompleteButton, BorderLayout.SOUTH);
	}
	
	/*******************************  Member Variable   ****************************************/
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -6838232231162781404L;
	
	/**
	 * Controls the UI
	 */
	private TrainingModuleController m_controller;
		
	/*******************************  Methods   ****************************************/

	/**
	 * Called when the user agrees to the TOU
	 */
	public void completeTraining()
	{
		m_controller.trainingComplete();
	}
	
	/*******************************  Helper Class   ****************************************/
	
	/**
	 * Action listener for when the agree button is pressed
	 * @author Rohit
	 *
	 */
	private class AgreeButtonPressed implements ActionListener
	{
		
		public AgreeButtonPressed(TrainingPage parent)
		{
			m_parent = parent;
		}

		private TrainingPage m_parent;
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			m_parent.completeTraining();
		}
	}
}
