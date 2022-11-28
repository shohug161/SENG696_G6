package mkf.jade.sar.model;

import java.util.ArrayList;

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
	
	static int idOffset = 0; 
	
	private int taskID;
	public RequestInfoModel requestInfo;
	public TeamType team;
	public String teamContact;
	public ArrayList<TaskItemModel> taskItems;
	public boolean isComplete;
}
