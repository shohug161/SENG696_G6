package mkf.jade.sar.model;

import java.util.ArrayList;

public class TaskModel 
{
	private int taskID;
	public RequestInfoModel requestInfo;
	public String teamName;
	public String teamContact;
	public ArrayList<TaskItemModel> taskItems;
	public boolean isComplete;
}
