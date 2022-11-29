package mkf.jade.sar.model;

/**
 * Model class for tasks assigned to teams
 * @author Rohit
 *
 */
public class TaskItemModel 
{
	
	
	public TaskItemModel(String taskItemName)
	{
		taskItemID = idOffset++;
		this.taskItemName = taskItemName; 
		isComplete = false;
	}
	
	/**
	 * Used to create unique IDs. Assuming the task agent will not crash or be restarted no request data is stored between different 
	 * runtimes. Because of this the static variable will work
	 */
	static int idOffset = 0;
	
	public int taskItemID;
	public String taskItemName;
	public boolean isComplete;
}
