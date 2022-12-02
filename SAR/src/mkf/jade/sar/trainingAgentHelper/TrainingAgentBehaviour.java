package mkf.jade.sar.trainingAgentHelper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mkf.jade.sar.Constants;
import mkf.jade.sar.TrainingAgent;

/**
 * The waits for messages from the task agent and activates the training agent appropriately
 * @author Rohit
 *
 */
public class TrainingAgentBehaviour extends CyclicBehaviour
{

	public TrainingAgentBehaviour(TrainingAgent trainingAgent)
	{
		m_trainingAgent = trainingAgent;
		m_template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
						MessageTemplate.MatchConversationId(Constants.ENABLE_TRAINING));
	}
	
	/*******************************  Member Variables   ****************************************/
	
	/**
	 * The training agent
	 */
	private TrainingAgent m_trainingAgent;
	
	/**
	 * The message template that the training agent needs to respond to 
	 */
	private MessageTemplate m_template;
	
	/**
	 * Serilization ID
	 */
	private static final long serialVersionUID = -6496922138799941490L;
	
	/*******************************  Simple Behaviour Methods   ****************************************/

	/**
	 * Action defining this bahaviour
	 */
	@Override
	public void action() 
	{
		ACLMessage message = m_trainingAgent.blockingReceive(m_template);
		
		if (message != null)
		{
			try
			{
				String traineeName = (String)message.getContentObject();
				m_trainingAgent.enableTraining(traineeName, message.getSender());
			}
			catch (Exception e)
			{
				System.err.println("Could not read payload of enable training message " + e.getMessage());
			}
		}
	}
}
