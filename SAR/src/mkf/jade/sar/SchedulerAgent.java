package mkf.jade.sar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mkf.jade.sar.model.RequestInfoModel;

public class SchedulerAgent extends EnhancedAgent 
{
	@Override
	public void setup()
	{
		addBehaviour(new SchedulerCommunicator(this));
		registerAgent();
		m_databaseManager = new SchedulerDatabaseManager();
	}
	
	private static final long serialVersionUID = 96370673295L;
	private SchedulerDatabaseManager m_databaseManager;
	
	/*******************************  Methods   ****************************************/

	/**
	 * Schedules the software installation in the database and the scheduling system
	 * @param requestInfo The request to schedule
	 */
	public void scheduleSoftwareInstall(RequestInfoModel requestInfo)
	{
		m_databaseManager.scheduleSoftwareInstallation(requestInfo.requestID, findInstallerAndTime());
		
		System.out.println("Schedule agent has received request to schedule software installation");
	}
	
	/**
	 * Finds the installation time and the installer
	 * @return the installation time and the installer
	 */
	private InstallationInfo findInstallerAndTime()
	{
		// Due to time constraints we could not mock a proper scheduling system, but this is where we would look for 
		// the soonest available installer to install system
		return new InstallationInfo(new Timestamp(ZonedDateTime.now().toInstant().toEpochMilli() + 604800000), "Kevin Gates");
	}
	
	/*******************************  Helper Methods   ****************************************/

	/**
	 * Registers this agent in the DF
	 */
	private void registerAgent()
	{
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.SCHEDULER_AGENT);
        sd.setName(getLocalName());
		
        dfd.setName(getAID());  
        dfd.addServices(sd);
        
        try 
        {  
            DFService.register(this, dfd);  
        }
        catch (FIPAException fe) 
        {
            fe.printStackTrace(); 
        }
	}
	
	/*******************************  Helper Classes   ****************************************/


	public class SchedulerCommunicator extends CyclicBehaviour {

		private static final long serialVersionUID = 934276031678473L;
		private SchedulerAgent m_agent;
		
		private MessageTemplate m_scheduleInstall;
		
		public SchedulerCommunicator(SchedulerAgent agent)
		{
			m_agent = agent;
			
			m_scheduleInstall = MessageTemplate.and(
									MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
									MessageTemplate.MatchConversationId(Constants.SCHEDULE_INSTALL));
		}
		
		@Override
		public void action() {
			
			ACLMessage received = m_agent.receive(m_scheduleInstall);
			
			if(received != null)
			{
				
				try {
					RequestInfoModel requestInfo = (RequestInfoModel)received.getContentObject();
					m_agent.scheduleSoftwareInstall(requestInfo);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			block();
		}
	}
	
	/**
	 * Information for the software install
	 * @author Rohit
	 *
	 */
	public class InstallationInfo
	{
		private Timestamp timestamp;
		private String installer;

		public InstallationInfo(Timestamp timestamp, String installer)
		{
			this.timestamp = timestamp;
			this.installer = installer;
		}
	}
	
	public class SchedulerDatabaseManager {
		
		public SchedulerDatabaseManager()
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
		 * Updates the database with the software installation information
		 * @param requestID The request ID the install is for
		 * @param installInfo The install info
		 */
		public void scheduleSoftwareInstallation(int requestID, InstallationInfo installInfo)
		{
			try {
				String query = "UPDATE SAR SET installationScheduledTime = ?, softwareInstallee = ? WHERE requestID = ?";
				PreparedStatement preparedStmt = m_dbConnection.prepareStatement(query);
				preparedStmt.setInt(1, requestID);
				preparedStmt.setTimestamp(2, installInfo.timestamp);
				preparedStmt.setString(3, installInfo.installer);
				preparedStmt.execute();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
