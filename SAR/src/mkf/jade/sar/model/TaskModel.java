package mkf.jade.sar.model;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskModel 
{
	public TaskModel(TeamType team, String teamContact, RequestInfoModel requestInfo, ArrayList<TaskItemModel> taskItems)
	{
		taskID = idOffset++;
		this.requestInfo = requestInfo;
		this.team = team; 
		this.teamContact = teamContact;
		this.taskItems = taskItems;
		isComplete = false;
	}
	
	private static int idOffset = 0; 
	
	public int taskID;
	public RequestInfoModel requestInfo;
	public TeamType team;
	public String teamContact;
	public ArrayList<TaskItemModel> taskItems;
	public boolean isComplete;
	
	
	public void UpdateIsComplete()
	{
		// Only update the taskItems in this method if there are task items
		if(taskItems.size() > 0)
		{
			Iterator<TaskItemModel> allTaskItems = taskItems.iterator(); 
			
			// Should only be true if all task items are complete
			boolean allComplete = true;
			
			while(allTaskItems.hasNext())
			{
				allComplete = allComplete && allTaskItems.next().isComplete;
			}
			isComplete = allComplete;
		}
	}
}
