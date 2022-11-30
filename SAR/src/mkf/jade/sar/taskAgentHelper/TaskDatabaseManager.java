package mkf.jade.sar.taskAgentHelper;

import mkf.jade.sar.model.RequestInfoModel;

/**
 * Manages the database interactions for the task agent
 * @author Rohit
 *
 */
public class TaskDatabaseManager
{
	/**
	 * Gets the requestor training status from the database
	 * @param name The name of the requestor
	 * @return True if the requestor has completed the training, false otherwise
	 */
	public boolean getRequestorTraingStatus(String name)
	{
		// TODO get requestor training status
		
		return false;
	}
	
	/**
	 * Updates the database with the request information of a completed SAR
	 * @param request The completed SAR info
	 */
	public void addCompletedRequest(RequestInfoModel request)
	{
		// TODO add completed request
	}
}
