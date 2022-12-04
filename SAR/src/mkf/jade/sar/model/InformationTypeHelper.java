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
	
	public static InformationType convertFromInt(int infoType)
	{
		switch(infoType)
		{
			case 1:
				return InformationType.LEVEL1;
			case 2:
				return InformationType.LEVEL2;
			case 3:
				return InformationType.LEVEL3;
			case 4:
				return InformationType.LEVEL4;
		}
		// default to LEVEL4 to make sure that the security is not overlooked
		return InformationType.LEVEL4;
	}
}
