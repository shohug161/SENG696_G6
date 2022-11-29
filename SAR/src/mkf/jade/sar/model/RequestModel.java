package mkf.jade.sar.model;

import java.util.ArrayList;

/**
 * Model class for all information related to a request
 * @author Rohit
 *
 */
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
