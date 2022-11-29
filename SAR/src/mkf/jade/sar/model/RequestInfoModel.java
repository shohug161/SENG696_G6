package mkf.jade.sar.model;

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
}
