package training.frontend;

import java.awt.Font;

import javax.swing.JFrame;
import training.backend.ModuleCommunicator;

/**
 * Controls the UI for the mock module
 * @author Rohit
 *
 */
public class TrainingModuleController 
{
	public TrainingModuleController()
	{
		m_communicator = new ModuleCommunicator(this);
		
		m_chooseUserPage = new ChooseUserPage(this);
		
		m_mainWindow  = new JFrame();
		m_mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_mainWindow.setSize(1000, 700);
		m_mainWindow.setContentPane(m_chooseUserPage);
		
		m_communicator.start();
		
		m_mainWindow.setVisible(true);
	}
	
	/*******************************  Member Variables   ****************************************/
	
	/**
	 * The communicator for the mock module
	 */
	private ModuleCommunicator m_communicator; 
	
	/**
	 * Main window of the mock module
	 */
	private JFrame m_mainWindow;
	
	/**
	 * Page for choosing who does the training
	 */
	private ChooseUserPage m_chooseUserPage;
	
	/**
	 * The current training selected in m_chooseUserPage
	 */
	private String m_currentTrainee;
	
	/*******************************  Methods   ****************************************/

	/**
	 * Enables training for a user with the name
	 * @param name The name of the user to enable training for
	 */
	public void enableTraining(String name)
	{
		m_chooseUserPage.addName(name);
		System.out.println("Training enabled for " + name);
	}
	
	/**
	 * Called when the trainee name is selected
	 * @param name The name selected
	 */
	public void traineeSelected(String name)
	{
		m_currentTrainee = name;
		
		m_mainWindow.setContentPane(new TOUPage(this));
		m_mainWindow.revalidate();
		m_mainWindow.repaint();

		System.out.println("User selected " + name + " to complete their training");
	}
	
	/**
	 * Called when the user agrees to the TOU
	 */
	public void touAgreed()
	{
		m_mainWindow.setContentPane(new TrainingPage(this));
		m_mainWindow.revalidate();
		m_mainWindow.repaint();
	}
	
	/**
	 * Called when a user completes their training
	 */
	public void trainingComplete()
	{
		m_communicator.trainingComplete((int) Math.random(), m_currentTrainee);
		
		System.out.println("Training completed for " + m_currentTrainee);
		
		m_chooseUserPage.removeName(m_currentTrainee);
		m_currentTrainee = "";

		m_mainWindow.setContentPane(m_chooseUserPage);
		m_mainWindow.revalidate();
		m_mainWindow.repaint();
	}
}
