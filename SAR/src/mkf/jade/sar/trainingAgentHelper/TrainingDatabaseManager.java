package mkf.jade.sar.trainingAgentHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import mkf.jade.sar.model.TrainingData;

public class TrainingDatabaseManager 
{
	public TrainingDatabaseManager()
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserts the training data into the database
	 * @param trainingData The training data to insert
	 */
	public void updateTrainingStatus(TrainingData trainingData)
	{
		try {
			String query = "INSERT INTO Training VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStmt = m_dbConnection.prepareStatement(query);
			preparedStmt.setInt(1, trainingData.trainingID);
			preparedStmt.setString(2, trainingData.traineeName);
			preparedStmt.setBoolean(3, true);
			preparedStmt.setBoolean(4, true);
			preparedStmt.execute();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
