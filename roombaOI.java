import java.util.*;

public class RoombaOI
{
	/**
	 * Asks the user for an int input in a specific range (inclusive)
	 * until the user enters in a valid input.
	 * Returns the valid user input.
	 * 
	 * @param intRangeLow The user input must be >= intRangeLow.
	 * @param intRangeHigh The user input muse be <= intRangeHigh.
	 * @param userPrompt The message to prompt the user for an input.
	 * @return The int that the user has entered in the valid range.
	 */
	private int getUserIntInput(int intRangeLow, int intRangeHigh,
								String userPrompt)
	{
		Scanner userInput = new Scanner(System.in);
		int userInt;
		
		while(true)
		{
			System.out.println(userPrompt);
			try
			{
				userInt = userInput.nextInt();
				if(userInt >= intRangeLow && userInt <= intRangeHigh)
					return userInt;
			}
			catch(InputMismatchException err)
			{
				userInput.nextLine();
			}
		}
	}
	
	public void getVelocity()
	{
		int vel = getUserIntInput(-500, 500, "Please enter in the velocity (decimal)");
		printOI(vel);
	
	}
	
	public void getRadius()
	{
		int rad = getUserIntInput(-2000, 2000, "Please enter in the radius (decimal)");
		printOI(rad);
	}
	
	public String twoBitUnsignedHexStrToInt(String tBUHS)
	{
		tBUHS = "0x" + tBUHS;
		return Integer.decode(tBUHS).toString();
		

	}
	
	public void printOI(int value)
	{
		String hexString = Integer.toHexString(value);
		while(hexString.length() < 4)
			hexString = "0" + hexString;
		System.out.println(hexString + "\n");
		
		if(value >= 0)
		{
			System.out.println("High bytes: 	" + twoBitUnsignedHexStrToInt(hexString.substring(0, 2)));
			System.out.println("Low bytes: 	" + twoBitUnsignedHexStrToInt(hexString.substring(2, 4)));
		}
		if(value < 0)
		{
			System.out.println("High bytes: 	" + twoBitUnsignedHexStrToInt(hexString.substring(4, 6)));
			System.out.println("Low bytes:	 " + twoBitUnsignedHexStrToInt(hexString.substring(6, 8)));	
		}
			
		
	}

	
	public RoombaOI()
	{
	
	}
	
	public static void main(String[] args)
	{
		RoombaOI roomba = new RoombaOI();
		roomba.getVelocity();
		roomba.getRadius();
	}
}