package mkf.jade.sar;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ModelAndUtility 
{

	public class RequestModel 
	{
		
		public RequestInfoModel requestInfo;
		public ArrayList<TaskModel> requestTasks;
	}
	
	public class RequestInfoModel 
	{
		public int requestID;
		public String softwareName;
		public String departmentName;
		public int numberOfUsers;
		public double softwareCost;
		public String businessReason;
		public InformationType informationType;
		public String requestorName;
		public String requestorEmail;
		public String vendorName;
		public String vendorEmail;
		public String comments;
	}
	
	public class TaskModel 
	{
		private int taskID;
		public RequestInfoModel requestInfo;
		public String teamName;
		public String teamContact;
		public ArrayList<TaskItemModel> taskItems;
		public boolean isComplete;
		
	}
	
	public class TaskItemModel 
	{
		public int taskItemID;
		public String taskItemName;
		public boolean isComplete;
		
	}
	
	public class TrainingData
	{
		public int trainingID;
		public String traineeName;
	}
	
	public enum InformationType 
	{
		LEVEL1, 
		LEVEL2, 
		LEVEL3, 
		LEVEL4;
	}
}
