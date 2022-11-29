package mkf.jade.sar.model;

public class TaskItemModel 
{
	
	
	public TaskItemModel(String taskItemName)
	{
		taskItemID = idOffset++;
		this.taskItemName = taskItemName; 
		isComplete = false;
	}
	
	static int idOffset = 0;
	
	public int taskItemID;
	public String taskItemName;
	public boolean isComplete;
}
