package mkf.jade.sar;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mkf.jade.sar.model.RequestInfoModel;
import mkf.jade.sar.model.TaskModel;
import mkf.jade.sar.model.TeamType;

public class NotificationAgent extends EnhancedAgent {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7755405458454866155L;
	private final String COMPANY_NAME = "University of Calgary";

	public NotificationAgent() {
		addBehaviour(new NotificationCommunicator());
	}

	public void handleCompletedRequest(RequestInfoModel completeRequest) {
		
		String contents = "Hello " +  completeRequest.requestorName + ",\n\n"
				+ "This is an automated notification to inform you that your aquisistion request "
				+ "for the " + completeRequest.softwareName + " software has been COMPLETED."
				+ "\n\nThanks\n" 
				+ COMPANY_NAME + "\n"
				+ "Software Aquisition Request System\n"; 
		
		sendEmail(completeRequest.requestorEmail, contents);
	}
	
	
	public void handleCanceledRequest(RequestInfoModel canceledRequest) {
		
		String contents = "Hello " +  canceledRequest.requestorName + ",\n\n"
				+ "This is an automated notification to inform you that your aquisistion request "
				+ "for the " + canceledRequest.softwareName + " software has been DENIED."
				+ "\n\nThanks\n" 
				+ COMPANY_NAME + "\n"
				+ "Software Aquisition Request System\n"; 
		
		sendEmail(canceledRequest.requestorEmail, contents);
	}
	
	public void notifyNewTask(TaskModel task) {

		String contents;
		String recipient;
		if(task.team == TeamType.requestor)
		{
			contents = makeRequestorTrainingReminder(task);
			recipient = task.requestInfo.requestorEmail;
		}
		else
		{
			contents = makeGenericTaskNotification(task);
			recipient = task.teamContact;
		}
		
		sendEmail(recipient, contents);
	}
	
	public String makeRequestorTrainingReminder(TaskModel task)
	{
		String contents = "Hello " + task.requestInfo.requestorName + ",\n\n"
				+ "This is an automated notification to inform you that you must agree to the "
				+ "terms of use and complete a mandatory training module in order to fufill "
				+ "the aquisition of the " + task.requestInfo.softwareName + " software that "
				+ "you requested."
				+ "\n\nThanks\n" 
				+ COMPANY_NAME + "\n"
				+ "Software Aquisition Request System\n";
		
		return contents;
	}
	
	public String makeGenericTaskNotification(TaskModel task)
	{
		String taskItemCount = Integer.toString(task.taskItems.size());
		String contents = "Hello " +  teamTypeToStringConverter(task.team) + ",\n\n"
				+ "This is an automated notification to inform you that there is " + taskItemCount 
				+ " new tasks related to the aquisition of the " + task.requestInfo.softwareName 
				+ " software from " + task.requestInfo.vendorName + ". The following tasks have "
				+ "been assigned to your team:\n";
		
		for(int i = 0; i < task.taskItems.size(); i++)
		{
			contents += "  - " + task.taskItems.get(i).taskItemName + "\n";
		}
		contents += "\nPlease complete these within three weeks."
				+ "\n\nThanks\n" 
				+ COMPANY_NAME + "\n"
				+ "Software Aquisition Request System\n";
		
		return contents;
	}
	
	public void notifyVendor(RequestInfoModel requestInfo) {
		
		String contents = "Hello " + requestInfo.vendorName + ",\n\n"
				+ "This automated email notification was sent to inform you that the " 
				+ requestInfo.departmentName + " department at the " 
				+ COMPANY_NAME + " is interested in purchasing  and licensing your \"" 
				+ requestInfo.softwareName + "\" software. "
				+ "A representative from our accounts payable team will be contacting "
				+ "you with to complete the payment followed by a deskside technician "
				+ "who will install the software."
				+ "\n\nThanks\n" 
				+ COMPANY_NAME + "\n"
				+ "Software Aquisition Request System\n"; 
		
		sendEmail(requestInfo.vendorEmail, contents);
	}
	
	
	private void sendEmail(String recipient, String contents)
	{
		// Because of time constraints and technical complexities we could not get emails 
		// working in time for the demo. Instead we are printing the intended emaail to 
		// the console
		System.out.println("EMAIL \nTo: " + recipient + "\nCONTENT:\n" + contents);
	}
	
	/**
	 * Gets a human readable string for each team type 
	 * @param team The team type
	 * @return Human readable string of the team
	 */
	private String teamTypeToStringConverter(TeamType team)
	{
		String result;
		switch(team)
		{
			case zoneManager:
				result = "Zone Manager";
				break;
			case asrc:
				result = "Architecture and Security, Risk and Compliance";
				break;
			case aws:
				result = "Apps and Web Services";
				break;
			case legal:
				result = "Legal Services";
				break;
			case privacy:
				result = "Privacy Office";
			case supplyChain:
				result = "Supply Chain";
				break;
			case accountsPayable:
				result = "Accounts Payable";
				break;
			case deskside:
				result = "Deskside Team";
				break;
			default:
				result = "";
				break;
		}
		
		return result;
	}
	
	private class NotificationCommunicator extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;
		private NotificationAgent notificationAgent;
		private MessageTemplate requestCompleteTemplate;
		private MessageTemplate requestCanceledTemplate;
		private MessageTemplate newTaskTemplate;
		private MessageTemplate notifyVendorTemplate;
		
		public NotificationCommunicator() {
			super(NotificationAgent.this);
			notificationAgent = NotificationAgent.this;
			requestCompleteTemplate = createMessageTemplate(Constants.REQUEST_COMPLETE);
			requestCanceledTemplate = createMessageTemplate(Constants.REQUEST_CANCELED);
			newTaskTemplate = createMessageTemplate(Constants.NOTIFICATION);
			notifyVendorTemplate = createMessageTemplate(Constants.NOTIFY_VENDOR);
		}

		/**
		 * Recieves all messages from the other agents
		 */
		@Override
		public void action() {
			ACLMessage msg;

			/* Notification to all the teams */
			msg = notificationAgent.receive(newTaskTemplate);
			if (msg != null) {
				try {
					TaskModel task = (TaskModel) msg.getContentObject();
					notificationAgent.notifyNewTask(task);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/* Notification to inform that request is cancelled */
			msg = notificationAgent.receive(requestCanceledTemplate);
			if (msg != null) {
				try {
					RequestInfoModel requestInfo = (RequestInfoModel) msg.getContentObject();
					notificationAgent.handleCanceledRequest(requestInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/* Request complete template */
			msg = notificationAgent.receive(requestCompleteTemplate);
			if (msg != null) {
				try {
					RequestInfoModel completed_request = (RequestInfoModel) msg.getContentObject();
					notificationAgent.handleCompletedRequest(completed_request);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			/* Notification to the vendor */
			msg = notificationAgent.receive(notifyVendorTemplate);
			if (msg != null) {
				try {
					RequestInfoModel requestInfo = (RequestInfoModel) msg.getContentObject();
					notificationAgent.notifyVendor(requestInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			block();
		}

	}

	private MessageTemplate createMessageTemplate(String conversationID) {
		return MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchConversationId(conversationID));
	}
}
