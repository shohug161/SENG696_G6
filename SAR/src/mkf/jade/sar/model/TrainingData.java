package mkf.jade.sar.model;

import java.io.Serializable;

/**
 * Model class for the training data
 * @author Rohit
 *
 */
public class TrainingData implements Serializable
{
	public TrainingData(int trainingID, String traineeName)
	{
		this.trainingID = trainingID;
		this.traineeName = traineeName;
	}
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 501185935743657597L;
	
	public int trainingID;
	public String traineeName;
}
