package mkf.jade.sar.model;

/**
 * Model class for the request information
 * @author Rohit
 *
 */
public class RequestInfoModel 
{
	public RequestInfoModel()
	{
		requestID = idOffset++;
	}
	
	/*
	 * Used to create unique IDs. Assuming the task agent will not crash or be restarted no request data is stored between different 
	 * runtimes. Because of this the static variable will work
	 */
	private static int idOffset = 0;
	
	public int requestID;
	public String softwareName;
	public String departmentName;
	public int numberOfUsers;
	public double softwareCost;
	public String businessReason;
	public InformationType informationType;
	public String requestorName;
	public String requestorEmail;
	public int trainingID;
	public String vendorName;
	public String vendorEmail;
	public String comments;
	
	public RequestInfoModel (int reqID, String swName, String depName, int numUsers, double swCost, String busReason, InformationType iType, String reqName, String reqEmail, 
			String vName, String vEmail, String comm)
	{
		requestID = reqID;
		softwareName = swName;
		departmentName = depName;
		numberOfUsers = numUsers;
		softwareCost = swCost;
		businessReason = busReason;
		informationType = iType;
		requestorName = reqName;
		requestorEmail = reqEmail;
		vendorName = vName;
		vendorEmail = vEmail;
		comments = comm;
	}
	/**
	 * Overriden to String Method
	 */
	@Override
	public String toString()
	{
		return "Software Name: \"" + softwareName + "\"  Request ID: " + requestID;
	}
}
