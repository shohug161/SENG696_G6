package mkf.jade.sar.model;

import java.io.Serializable;

/**
 * Model class for the request information
 * @author Rohit
 *
 */
public class RequestInfoModel implements Serializable
{
	/**
	 * Serilization ID
	 */
	private static final long serialVersionUID = 486295439L;

	public RequestInfoModel()
	{
		requestID = idOffset++;
	}
	
	/**
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
	
	/**
	 * Overriden to String Method
	 */
	@Override
	public String toString()
	{
		return "Software Name: \"" + softwareName + "\"  Request ID: " + requestID; 
	}
}
