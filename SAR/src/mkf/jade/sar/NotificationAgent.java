package mkf.jade.sar;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import java.util.Random;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mkf.jade.sar.model.RequestInfoModel;

public class NotificationAgent extends EnhancedAgent {

	public void handleCompletedRequest(RequestInfoModel completeRequest) {
		
		/* send email*/
		
	}
	public NotificationAgent() {
		addBehaviour(new NotificationCommunicator());
		
	}

	private class NotificationCommunicator extends CyclicBehaviour {

		private NotificationAgent notificationAgent;
		private MessageTemplate requestCompleteTemplate;
		public NotificationCommunicator() {
			super(NotificationAgent.this);
			notificationAgent = NotificationAgent.this;
			requestCompleteTemplate = createMessageTemplate(Constants.REQUEST_COMPLETE);
		}

		@Override
		public void action() {
			ACLMessage msg;
			MessageTemplate template;

			/* Notification to all the teams */

			/* Add Condition */
			if (msg != null) {

				System.out.println("Notifcation is sent to all the teams");

			}

			/* Notification to inform that request is cancelled */

			/* Add Condition */
			if (msg != null) {

				System.out.println("Software Acquisition request is cancelled");

				/* Notification to the vendor */

				/* Add Condition */
				if (msg != null) {

					System.out.println("Notification to vendor");
					/* Request complete notification to requester */
				}
				msg = notificationAgent.receive(requestCompleteTemplate); /* template */
				/* Add Condition */
				if (msg != null) {
					try {
						RequestInfoModel completed_request = (RequestInfoModel) msg.getContentObject();
						notificationAgent.handleCompletedRequest(completed_request);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				block();
			}

		}

	}

	private MessageTemplate createMessageTemplate(String conversationID) {
		return MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchConversationId(conversationID));
	}
}
