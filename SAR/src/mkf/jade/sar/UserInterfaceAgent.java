package mkf.jade.sar;

import java.io.IOException;
import java.io.Serializable;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;
import mkf.jade.sar.model.*;
import mkf.jade.sar.view.*;
import jade.lang.acl.ACLMessage;

// TODO
// add a new behavior with ticker that is run every period to search for new messages from agents
// ticker behaviour to see if people have responded via the user interface
// search for Task Manager until we get one, then go to the rest of the behaviour
// conversation ID creates a unique conversation with your agents
public class UserInterfaceAgent extends EnhancedAgent {

	public ViewController m_viewController;
	public RequestInfoModel rim;
	public UserInterface gui;
	
	public UserInterfaceAgent() {
		System.out.printf("Hello! My name is %s%n", getLocalName());
		addBehaviour(new UserInterfaceCommunicator());
		m_viewController = new ViewController();
	}
	
	// TODO create UI for the user to input
	// switch case for each agent??
	// receive messages fromt task agent
	// send messages to task agent
	// use setContent() to set the content of the message
	public class UserInterfaceCommunicator extends SimpleBehaviour {

		private UserInterfaceAgent uiAgent;
		private boolean done = false;
		private short actionCounter = 0;
		
		public UserInterfaceCommunicator() {
			super(UserInterfaceAgent.this);
			uiAgent = UserInterfaceAgent.this;
			uiAgent.gui = new UserInterface("Software Acquisition Request System");
		}
		
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			// cases:
			// sending message with a new request
			// request approved
			// request cancelled
			// new request
			
			ACLMessage msg, reply;
			MessageTemplate template;
			
			switch(actionCounter) {
			
			case 0:
				// waiting for the user to submit a SAR
				// display the UI to the user to submit a request
				m_viewController.displayLoginInfo();
				if (uiAgent.gui.submittedSAR == true) {
					actionCounter++;
					uiAgent.rim = uiAgent.gui.getRequestInfoModel();
				}
				
			case 1:
				// send message to task agent
				msg = new ACLMessage(ACLMessage.INFORM);
				msg.setConversationId(Constants.SUBMIT_REQUEST);
				try {
					msg.setContentObject((Serializable) uiAgent.rim);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				uiAgent.send(msg);
				actionCounter++;
				
			case 2:
				// listening for request from task agent
		        template = MessageTemplate.and(
		                  MessageTemplate.MatchPerformative(ACLMessage.REQUEST),		// players only responding with accept proposal, specify that
		                  MessageTemplate.MatchConversationId(Constants.SUBMIT_REQUEST));				// players will respond with a request
		        msg = myAgent.blockingReceive(template);
		        // display info on the GUI
		        // continue listening until we receive an update from the stakeholders
			}
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}
		
	}
	
	/*******************************  METHODS   ****************************************/

	
	
	public void submitRequest(RequestInfoModel requestInfo)
	{
		// TODO
	}
	
	public void userLogon(TeamType team)
	{
		// TODO
	}
	
	public void taskComplete(TaskModel task)
	{
		// TODO
	}

	
	public void reqeuestDenied(int requestID)
	{
		// TODO
	}
	
	
	
	
}
