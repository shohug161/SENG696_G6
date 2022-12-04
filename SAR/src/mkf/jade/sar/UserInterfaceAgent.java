package mkf.jade.sar;

import java.io.IOException;
import java.io.Serializable;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mkf.jade.sar.model.*;
import mkf.jade.sar.view.*;
import jade.lang.acl.ACLMessage;

// TODO
// add a new behavior with ticker that is run every period to search for new messages from agents
// ticker behaviour to see if people have responded via the user interface
// search for Task Manager until we get one, then go to the rest of the behaviour
// conversation ID creates a unique conversation with your agents
public class UserInterfaceAgent extends EnhancedAgent {

	private static final long serialVersionUID = 8340628674987619684L;
	private AID m_taskAgentID;
	public ViewController m_viewController;
	public RequestInfoModel rim;
	
	/**
	 * Sets up this agent
	 */
	@Override
	protected void setup()
	{
		registerAgent();
		
		addBehaviour(new UserInterfaceCommunicator());
		m_viewController = new ViewController(this);
	}
	
	
	/**
	 * Receives messages from the other agents
	 * @author Desi
	 *
	 */
	private class UserInterfaceCommunicator extends CyclicBehaviour {

		private static final long serialVersionUID = 2775022485827203862L;
		private UserInterfaceAgent m_uiAgent;
		private MessageTemplate m_receivedTask;
		private MessageTemplate m_requestCanceled;
		
		public UserInterfaceCommunicator() {
			super(UserInterfaceAgent.this);
			m_uiAgent = UserInterfaceAgent.this;
			
			m_receivedTask = MessageTemplate.and(
	                  			MessageTemplate.MatchPerformative(ACLMessage.INFORM),
	                  			MessageTemplate.MatchConversationId(Constants.SEND_TASK_TO_UI));
			
			m_requestCanceled = MessageTemplate.and(
          							MessageTemplate.MatchPerformative(ACLMessage.INFORM),
          							MessageTemplate.MatchConversationId(Constants.REQUEST_CANCELED));
		}
		
		/**
		 * Process all possible incoming messages
		 */
		@Override
		public void action() {

			m_viewController.displayLoginInfo();
			ACLMessage receivedTaskMsg = m_uiAgent.receive(m_receivedTask);
			ACLMessage requestCanceledMsg = m_uiAgent.receive(m_requestCanceled);
			
			boolean noMsgReceived = true;
			
			if(receivedTaskMsg != null){
				
				try {
					TaskModel task = (TaskModel)receivedTaskMsg.getContentObject();
					m_uiAgent.receivedTask(task);
					
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				noMsgReceived = false;
			}
			
			if(requestCanceledMsg != null) {
				
				try {
					RequestInfoModel requestInfo = (RequestInfoModel)requestCanceledMsg.getContentObject();
					m_uiAgent.requestCanceled(requestInfo.requestID);
					
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				noMsgReceived = false;
			}
			
			if(noMsgReceived) {
				block();				
			}
		}
	}
	
	/*******************************  METHODS   ****************************************/

	/**
	 * Called when a new task is recieved
	 * @param task The new task
	 */
	public void receivedTask(TaskModel task) {
		// TODO receive task
		m_viewController.task = task;
	}
	
	/**
	 * Called when a request is canceled
	 * @param requestID The canceled request
	 */
	public void requestCanceled(int requestID) {
		// TODO request canceled
		// do we need this?
	}
	
	/**
	 * Submits a new request
	 * @param requestInfo The new request's information
	 */
	public void submitRequest(RequestInfoModel requestInfo) {
		sendMessage(Constants.SUBMIT_REQUEST, requestInfo);
	}
	
	/**
	 * Informs of a new user logon
	 * @param team
	 */
	public void userLogon(TeamType team) {
		sendMessage(Constants.LOGON, team);
	}
	
	/**
	 * Informs of a completed task
	 * @param task The task that was completed
	 */
	public void taskComplete(TaskModel task) {
		sendMessage(Constants.LOGON, task);
	}

	/**
	 * Informs that a request was denied
	 * @param requestID The ID of the denied request
	 */
	public void reqeuestDenied(int requestID) {
		sendMessage(Constants.REQUEST_DENIED, requestID);
	}
	
	/*******************************  HELPER METHODS   ****************************************/

	/**
	 * Sends a message to other agents
	 * @param isRequest True if the performative is REQUEST, false if it is INFORM
	 * @param conversationID The identifyer of the message type
	 * @param payload The payload of the message
	 * @param recipient An array of all recipients of the message
	 */
	private void sendMessage(String conversationID, Serializable payload)
	{
		if(m_taskAgentID == null)
		{
			searchForTaskAgent();
		}
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId(conversationID);
		
		msg.addReceiver(m_taskAgentID);			
		
		try 
		{
			msg.setContentObject(payload);
		} 
		catch (IOException e) 
		{
			System.err.println("Error Serializing type " + payload.getClass() + " " + e.getMessage());
		}
		send(msg);
	}
	
	/**
	 * Searches for an agent that meets the description
	 * @param description A description of the required services 
	 * @return Null if none are found, otherwise the first agent found
	 */
	private void searchForTaskAgent()
	{
		DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(Constants.TASK_AGENT);
        dfd.addServices(sd);
                
        try 
        {
			DFAgentDescription[] result = DFService.search(this, dfd);
			
			if(result.length <= 0) throw new Exception("No task agent found");
			
			m_taskAgentID = result[0].getName();
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
   	}
	
	/**
	 * Registers this agent in the DF
	 */
	private void registerAgent()
	{
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.UI_AGENT);
        sd.setName(getLocalName());
		
        dfd.setName(getAID());  
        dfd.addServices(sd);
        
        try 
        {  
            DFService.register(this, dfd);  
        }
        catch (FIPAException fe) 
        {
            fe.printStackTrace(); 
        }
	}
}
