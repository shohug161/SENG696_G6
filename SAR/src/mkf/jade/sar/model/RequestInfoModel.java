package mkf.jade.sar.model;

public class RequestInfoModel 
{
	public RequestInfoModel()
	{
		requestID = idOffset++;
	}
	
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
	
	@Override
	public String toString()
	{
		return "Software Name: \"" + softwareName + "\"  Request ID: " + requestID; 
	}
}
