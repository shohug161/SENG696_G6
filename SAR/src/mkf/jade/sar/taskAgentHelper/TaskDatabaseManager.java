package mkf.jade.sar.taskAgentHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mkf.jade.sar.model.InformationTypeHelper;
import mkf.jade.sar.model.RequestInfoModel;

/**
 * Manages the database interactions for the task agent
 * @author Rohit
 *
 */
public class TaskDatabaseManager
{
	public TaskDatabaseManager()
	{
		connectToDatabase();
	}
	
	/*******************************  DB Variables   ****************************************/
	
	/**
	* URL of the database
	*/
	static final String DB_URL = "jdbc:mysql://localhost/seng696";

	/**
	* Database username
	*/
	static final String USERNAME = "root";

	/**
	* Database password
	*/
	static final String PASSWORD = "password";
	
	/**
	 * Connection to the database
	 */
	private Connection m_dbConnection;
	
	/*******************************  Methods   ****************************************/

	/**
	 * Connects to the database
	 */
	private void connectToDatabase() {
		try {
			m_dbConnection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the requestor training status from the database
	 * @param name The name of the requestor
	 * @return True if the requestor has completed the training, false otherwise
	 */
	public boolean getRequestorTraingStatus(String name)
	{
		String query = "SELECT trainingStatus FROM training WHERE requestorName = '" + name + "'";
		ResultSet result = null;
		try {
			Statement stmt = m_dbConnection.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result != null;
	}
	
	/**
	 * Updates the database with the request information of a completed SAR
	 * @param request The completed SAR info
	 */
	public void addCompletedRequest(RequestInfoModel request)
	{
		try {
			String query = "INSERT INTO SAR VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = m_dbConnection.prepareStatement(query);
			preparedStmt.setInt(1, request.requestID);
			preparedStmt.setString(2, request.requestorName);
			preparedStmt.setString(3, request.softwareName);
			preparedStmt.setString(4, request.businessReason);
			preparedStmt.setString(5, request.departmentName);
			preparedStmt.setInt(6, request.numberOfUsers);
			preparedStmt.setDouble(7, request.softwareCost);
			preparedStmt.setInt(8, InformationTypeHelper.convertToInt(request.informationType));
			preparedStmt.setString(9, "Complete");
			preparedStmt.setString(10, "Complete");
			preparedStmt.setString(11, request.vendorName);
			
			preparedStmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
