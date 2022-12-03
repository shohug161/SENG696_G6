package mkf.jade.sar;

public class Constants 
{
	// MESSAGE Constants
	public static final String SUBMIT_REQUEST = "submit";				// UI -> Task (RequestInfoModel)INFORM
	public static final String LOGON = "logon";							// UI -> Task (TeamType)INFORM
	public static final String TASK_COMPLETE = "taskComplete";			// UI -> Task (TaskModel)INFORM
	public static final String REQUEST_DENIED = "denied";				// UI -> Task (int)INFORM
	
	public static final String SEND_TASK_TO_UI = "toUI";				// Task -> UI (TaskModel)INFORM
	public static final String REQUEST_CANCELED = "canceled";			// Task -> UI, Notification (RequestInfoModel)INFORM
	
	public static final String REQUEST_COMPLETE = "requestComplete";	// Task -> Notification (RequestInfoModel)REQUEST 
	public static final String NOTIFICATION = "notification";			// Task -> Notification (TaskModel)REQUEST - NICE TO HAVE -> also send to scheduler
	public static final String NOTIFY_VENDOR = "vendor";				// Task -> Notification (RequestInfoModel)REQUEST
	
	public static final String ENABLE_TRAINING = "enableTraining";		// Task -> Training (String)REQUEST
	public static final String TRAINING_COMPLETE = "trainingComplete";	// Training -> Task (TrainingData)INFORM

	public static final String SCHEDULE_INSTALL = "scheduleInstall";	// Task -> Scheduler (RequestInfoModel)REQUEST
	public static final String CANCEL_REMINDER = "cancelReminder";		// Task -> Scheduler (int)REQUEST - NICE TO HAVE
	
	public static final String REMINDER = "reminder";					// Scheduler -> Notification (TaskModel)REQUEST - NICE TO HAVE
	
	// AGENT DESCRIPTION
	public static final String TRAINING_AGENT = "training";
	public static final String TASK_AGENT = "task";
	public static final String UI_AGENT = "ui";
	public static final String NOTIFICATION_AGENT = "notification";
	public static final String SCHEDULER_AGENT = "scheduler";
}
