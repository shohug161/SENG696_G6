package mkf.jade.sar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import jade.lang.acl.ACLMessage;
import mkf.jade.sar.model.TrainingData;
import mkf.jade.sar.trainingAgentHelper.TrainingAgentBehaviour;

/**
 * Bridges other agents of the system with the existing training module system
 * @author Rohit
 *
 */
public class TrainingAgent extends EnhancedAgent 
{
	public TrainingAgent()
	{
		addBehaviour(new TrainingAgentBehaviour(this));
	}
	
	/*******************************  Member Variables   ************************************/

	/**
	 * The serilization ID
	 */
	private static final long serialVersionUID = -3502765383161725840L;
	
	/**
	 * The port of the system
	 */
	private final int port = 4141;
	
	/*******************************  Methods   ****************************************/
	
	/**
	 * Enables training for a user with the following name
	 * @param traineeName The trainee name
	 */
	public void enableTraining(String traineeName)
	{
		//TODO make this into its own thread?
		
		Socket socket = connectToTrainingModule();
		
		if(socket != null)
		{
			try
			{
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				writer.println(traineeName);
				
				System.out.println("Sent enable training message to training module");
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String readName = reader.readLine();
				String readID = reader.readLine();
				
				int trainingID = Integer.parseInt(readID);
				
				sendCompletedTraining(new TrainingData(trainingID, readName));
			}
			catch(IOException e)
			{
				System.err.println("ERROR - Failed writing or reading to the training module socket" + e.getMessage());
			}
			catch(NumberFormatException e)
			{
				System.err.println("ERROR - Failed to parse the training ID that the training module sent" + e.getMessage());
			}
			
			
		}		
	}
	
	/*******************************  Helper Methods   ****************************************/

	/**
	 * Sends the completed training data to the other agents
	 * @param trainingData The training data to send
	 */
	private void sendCompletedTraining(TrainingData trainingData)
	{
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setConversationId(Constants.TRAINING_COMPLETE);
		
		try 
		{
			message.setContentObject(trainingData);
			send(message);
			System.out.println("Training data for " + trainingData.traineeName + " successfully processed from the training module");
		} 
		catch (IOException e) 
		{
			System.err.println("Could not serialize training data: " + e.getMessage());
		}
	}
	
	/**
	 * Connects to the training module socket, tries until successful 
	 * @return A socket connected to the training module
	 */
	private Socket connectToTrainingModule() {
		Socket socket = null;
		boolean connected = false;
		do
		{
			try
			{
				socket = new Socket("localhost", port);
				connected = true;
			}
			catch(IOException e)
			{
				System.out.println("Failed to connect to the training module, retrying in 10 seconds");
				try 
				{
					Thread.sleep(10000);
				} 
				catch (InterruptedException e1) 
				{
					System.err.println("ERROR - 10 second sleep interrupted");
				}
			}
		}
		while (!connected);
		
		return socket;
	}
}
