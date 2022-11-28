package mkf.jade.sar.model;

import java.util.ArrayList;

public class RequestModel 
{
	public RequestModel(RequestInfoModel requestInfo)
	{
		this.requestInfo = requestInfo; 
		requestTasks = new ArrayList<TaskModel>();
	}
	
	public RequestInfoModel requestInfo;
	public ArrayList<TaskModel> requestTasks;
}
