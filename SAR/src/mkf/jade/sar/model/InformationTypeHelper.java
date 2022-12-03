package mkf.jade.sar.model;

public class InformationTypeHelper
{
	
	public static int convertToInt(InformationType infoType)
	{
		switch(infoType)
		{
			case LEVEL1:
				return 1;
			case LEVEL2: 
				return 2;
			case LEVEL3:
				return 3;
			case LEVEL4: 
				return 4;
		}
		return 0;
	}	
}
