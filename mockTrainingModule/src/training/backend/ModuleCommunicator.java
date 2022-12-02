package training.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import training.frontend.TrainingModuleController;

/**
 * Mock connection to the sar agent based system
 * @author Rohit
 *
 */
public class ModuleCommunicator extends Thread
{
	public ModuleCommunicator(TrainingModuleController controller)
	{
		m_controller = controller;
		
		m_keepRunning = true;
		try 
		{
			m_serverSocket = new ServerSocket(m_port);
			System.out.println("Opened server socket on port " + m_port);
		} 
		catch (IOException e) 
		{
			System.err.println("Could not open server socket on port " + m_port);
		}
	}
		
	/*******************************   Member Variables   ************************************/

	/**
	 * The controller of the mock training module UI
	 */
	private TrainingModuleController m_controller;
	
	/**
	 * The server socket that accepts connections
	 */
	private ServerSocket m_serverSocket;
	
	/**
	 * The server keeps running while true
	 */
	private boolean m_keepRunning;
	
	/**
	 * The local port to open the training module on
	 */
	private final int m_port = 4141;
	
	/**
	 * Object used to wait for the training to be completed
	 */
	private Object syncObj = new Object();
	
	/**
	 * The completed trainee name
	 */
	private String m_completedTraineeName;
	
	/**
	 * The completed training ID
	 */
	private int m_completedTrainingID;
	
	/*******************************  Methods  ************************************/

	/*
	 * Runs the server allowing connections from training agents
	 */
	@Override
	public void run()
	{
		System.out.println("Server socket accepting connections on port 4141");
		
		// TODO remove?
		m_controller.enableTraining("Arjuna");
		m_controller.enableTraining("Kenny");
		m_controller.enableTraining("Wayn");
		m_controller.enableTraining("Lakshmi");

		while(m_keepRunning)
		{
			try 
			{
				Socket clientSocket = m_serverSocket.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String name = reader.readLine();
				m_controller.enableTraining(name);
				
				// wait for when the training is complete
				synchronized(syncObj)
				{
					syncObj.wait();
				}
				
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				
				writer.println(m_completedTraineeName);
				writer.println(m_completedTrainingID);
				
				writer.close();
				reader.close();
				clientSocket.close();
			} 
			catch (IOException e) 
			{
				System.err.println("ERROR - opening socket with client " + e.getMessage());
			}
			catch(InterruptedException e)
			{
				System.err.println("ERROR - thread inturupted waiting for front end response " + e.getMessage());
			}
		}
	}
	
	/**
	 * Informs the client the training for a user is complete
	 * @param completedTrainingID The ID of completed training
	 * @param completedTraineeName The name of the one who completed the training
	 */
	public void trainingComplete(int completedTrainingID, String completedTraineeName)
	{
		m_completedTrainingID = completedTrainingID;
		m_completedTraineeName = completedTraineeName;
		
		// Allow thread to proceed
		synchronized(syncObj)
		{
			syncObj.notify();			
		}
	}
}
