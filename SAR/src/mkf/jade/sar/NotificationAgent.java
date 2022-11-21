package mkf.jade.sar;

import jade.core.behaviours.SimpleBehaviour;

public class NotificationAgent extends EnhancedAgent{

	
	
	private class NotificationCommunicator extends SimpleBehaviour {

		private NotificationAgent notificationAgent;
		private boolean done;
		
		public NotificationCommunicator() {
			
			
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			
			
			// if ACLMessage.INFORM then the program is done and the agent can be deleted
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}
		
	}
}
