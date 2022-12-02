package mkf.jade.sar.trainingAgentHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import mkf.jade.sar.TrainingAgent;
import mkf.jade.sar.model.TrainingData;

/**
 * Thread for communicating with the training module
 * @author Rohit
 *
 */
public class TrainingCommunicationThread extends Thread 
{
	public TrainingCommunicationThread(String traineeName, TrainingAgent agent)
	{
		m_agent = agent;
		m_traineeName = traineeName;
	}
	
	/*******************************  Member Variables   ************************************/

	/**
	 * The training agent
	 */
	private TrainingAgent m_agent;
	
	/**
	 * The trainee name
	 */
	private String m_traineeName;
	
	/**
	 * The port of the system
	 */
	private final int port = 4141;
	
	/*******************************  Methods   ************************************/

	/**
	 * Runs this thread
	 */
	@Override
	public void run()
	{
		sendEnableTraining(m_traineeName);
	}
	
	/*******************************  Helper Methods   ************************************/
	
	/**
	 * Connects to the training module socket, tries until successful 
	 * @return A socket connected to the training module
	 */
	private Socket connectToTrainingModule() 
	{
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
	
	/**
	 * Sends the enable training message over a socket to the training module
	 * @param traineeName The name to enable training for
	 */
	private void sendEnableTraining(String traineeName) 
	{
		Socket socket = connectToTrainingModule();
		
		if(socket != null)
		{
			try
			{
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				writer.println(traineeName);
				writer.flush();				
				
				System.out.println("Sent enable training message to training module for " + traineeName);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String readName = reader.readLine();
				String readID = reader.readLine();
				
				int trainingID = Integer.parseInt(readID);
				
				m_agent.sendCompletedTraining(new TrainingData(trainingID, readName));
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
}
