package mkf.jade.sar;

import jade.core.behaviours.SimpleBehaviour;
import java.util.Random;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class NotificationAgent extends EnhancedAgent{
	 
	public NotificationAgent() {
	addBehaviour(new NotificationCommunicator());
	
}
	private class NotificationCommunicator extends SimpleBehaviour {

		private NotificationAgent notificationAgent;
		private boolean done = false;
		private short actionCounter = 0;
		public NotificationCommunicator() {
			super(NotificationAgent.this);
			notificationAgent = NotificationAgent.this;
			
		}
		
		@Override
		   public void action() {
	        ACLMessage msg;
	        MessageTemplate template;

	        switch(actionCounter) {
	        					/*   Notification to all the teams */   
	          case 0:
	        	  	msg = notificationAgent.blockingReceive();
	        	  	/* Add Condition */        	  		
	        	  	  if(msg != null) {
	        		  	  sendMsg("new task is assigned", Constants.NOTIFICATION, ACLMessage.INFORM, notificationAgent.team);
	        		  	  System.out.println("Notifcation is sent to all the teams");
	        		  	  actionCounter = 1;
	        	  	  }
	        	  		break;
	          						/*   Notification to inform that request is cancelled  */   
	          case 1:
		            msg = notificationAgent.blockingReceive();
		            /* Add Condition */   
		          	  if(msg != null) {
		          		  sendMsg("Request is cancelled", Constants.REQUEST_CANCELED, ACLMessage.INFORM, notificationAgent.Requester);
		          		  System.out.println("Software Acquisition request is cancelled");
		          		  actionCounter = 2;  
		          	  }
		          	  	break;
			  
	          case 2:
	        	  					/*   Notification to the vendor  */   
		            msg = notificationAgent.blockingReceive();
		            /* Add Condition */   
			          if(msg != null) {
			        	  sendMsg("Notification to Vendor",Constants.NOTIFY_VENDOR,ACLMessage.INFORM,notificationAgent.vendor);
			              System.out.println("Notification to vendor");
			              actionCounter = 3;  
			          }
			            break;       
	          case 3:
	        	  						/*   Request complete notification to requester  */   
	        	  	msg = notificationAgent.blockingReceive();
	        	  	/* Add Condition */  
	        	  	  if(msg != null) {
			        	  sendMsg("Complete notification to Requester",Constants.REQUEST_COMPLETE,ACLMessage.INFORM,notificationAgent.Requester);
	                   	  System.out.println("Software Acquisition request is complete to the requester");
			        	  done = true;
			        	  notificationAgent.doDelete();
	        	  	  }
	        	  	  	break;
	        }
	      }
		  
		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}
		
		
	}
}






