import java.util.*;
import java.io.*;

public class RoombaOI
{
	/** 
	 * n * 2 ArrayList of OpCodeLine that forms the opCodes and their comments
	 */
	private ArrayList<OpCodeLine> opCodeList;
	
	private String defaultOpCodeFileName = "opcode.txt";
	private String opCodeFileName;

	private String oCL, oCLC;
	
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
	
	private void getVelocity()
	{
		int vel = getUserIntInput(-500, 500, "Please enter in the velocity (decimal) (-500 to 500 mm/s)");
		oCLC += Integer.toString(vel) + " mm/s, and a turn radius of "; 
		editOpCode16bits(vel);
		oCL += " ";
	}
	
	private void getRadius()
	{
		int rad = getUserIntInput(-32768, 32767, "Please enter in the radius (decimal) (-32768 to 32767 mm/s)");
		oCLC += Integer.toString(rad) + " mm/s";
		if(rad < 0)
			oCLC += " clockwise.";
		else
			oCLC += " counter-clockwise.";
	
		editOpCode16bits(rad);
	}
	
	private String twoBitUnsignedHexStrToInt(String tBUHS)
	{
		tBUHS = "0x" + tBUHS;
		return Integer.decode(tBUHS).toString();
	}
	
	private void editOpCode16bits(int value)
	{
		String hexString = Integer.toHexString(value);
		while(hexString.length() < 4)
			hexString = "0" + hexString;
		//System.out.println(hexString + "\n");
		
		if(value >= 0)
		{
			//System.out.println("High bytes: 	" + twoBitUnsignedHexStrToInt(hexString.substring(0, 2)));
			//System.out.println("Low bytes: 	" + twoBitUnsignedHexStrToInt(hexString.substring(2, 4)));
			oCL += twoBitUnsignedHexStrToInt(hexString.substring(0, 2)) + " " + twoBitUnsignedHexStrToInt(hexString.substring(2, 4));
		}
		if(value < 0)
		{
			//System.out.println("High bytes: 	" + twoBitUnsignedHexStrToInt(hexString.substring(4, 6)));
			//System.out.println("Low bytes:	 " + twoBitUnsignedHexStrToInt(hexString.substring(6, 8)));
			oCL += twoBitUnsignedHexStrToInt(hexString.substring(4, 6)) + " " + twoBitUnsignedHexStrToInt(hexString.substring(6, 8));			
		}
	}

	private void getWaitDistance()
	{
		int waitDist = getUserIntInput(-32768, 32767, "Please enter in the wait distance (decimal) (-30 000 to 30 000 mm)");
		oCLC += Integer.toString(waitDist) + " mm.";
			
		editOpCode16bits(waitDist);
	}
	
	private void getSong()
	{
		int songNum = getUserIntInput(0, 4, "Please enter in the song number (0 to 4)");
		int songLength = getUserIntInput(1, 16, "Please enter in the song length/ number of notes in the song (1 to 16)");
		
		oCLC += Integer.toString(songNum) + ".";
		oCL += Integer.toString(songNum) + " ";
		oCL += Integer.toString(songLength);
		
		for(int i = 1; i <= songLength; i++)
		{
			oCL += " " + Integer.toString(getUserIntInput(0, 127, "Note " + i + ": Please enter in the note (0 to 30 is a rest, 31 is the lowest in pitch, 127 is the highest.)"));
			oCL += " " + Integer.toString(getUserIntInput(0, 255, "Note " + i + ": Please enter in the note duration ( (0 to 255) * (1/64) seconds)"));
		}
	}	
	
	private void playSong()
	{
		int songNum = getUserIntInput(0, 4, "Please enter in the song number (0 to 4)");
		oCLC += Integer.toString(songNum) + ".";
		oCL += Integer.toString(songNum);
	}
	
	private void changeOutputBits()
	{
		int outputBits = 0;
		
		int bit0 = getUserIntInput(0, 1, "Please enter in the voltage level for digital-out-0");
		oCLC += "digital-out-0 will be " + Integer.toString(bit0) + ". ";
		outputBits += bit0;
		
		int bit1 = getUserIntInput(0, 1, "Please enter in the voltage level for digital-out-1");
		oCLC += "digital-out-1 will be " + Integer.toString(bit1) + ". ";
		outputBits += bit1 * 2;
		
		int bit2 = getUserIntInput(0, 1, "Please enter in the voltage level for digital-out-2");
		oCLC += "digital-out-2 will be " + Integer.toString(bit2) + ".";
		outputBits += bit2 * 4;

		oCL += Integer.toString(outputBits);
	}
	
	public void run()
	{
		while(true)
		{
			displayMenuPrimary();
			int userChoice = getUserIntInput(1, 7, "Please enter in your choice: ");	
			actOnOptionMenu(userChoice);
		}
	}
	
	public void actOnOptionMenu(int userChoice)
	{
		switch(userChoice)
		{
			case 1: viewOpCodeSingleLine(); break;
			case 2: viewOpCodeMultipleLines(); break;
			case 3: deleteOpCodeSingleLine(); break;
			case 4: deleteAllOpCode(); break;
			case 5: createOpCode(); break;
			case 6: saveOpCodes(); break;
			case 7: System.exit(0); break;
			default: break;
		}
	}
	
	public void displayMenuPrimary()
	{
		System.out.println("\nMenu: ");
		System.out.println("1. View the current opCode in a single line.");
		System.out.println("2. View the current opCode in multiple lines.");
		System.out.println("3. Delete opCode line.");
		System.out.println("4. Delete the entire opCode.");
		System.out.println("5. Add new opCode line.");
		System.out.println("6. Save opCode");
		System.out.println("7. Quit");
	}
	
	public void createOpCode()
	{
		displayOpCodeCreationMenu();
		int userChoice = getUserIntInput(1, 7, "Please enter in your choice: ");
		actOnOptionOpCodeMenu(userChoice);
	}
	
	public void displayOpCodeCreationMenu()
	{
		System.out.println("1. Add custom opCode line (WARNING: DOES NOT CHECK FOR VALIDITY)");
		System.out.println("2. 128, 132 - Add default start and full mode opCodes.");
		System.out.println("3. 137 - Add Drive command."); 
		System.out.println("4. 156 - Add Wait Distance command.");
		System.out.println("5. 140 - Create song.");
		System.out.println("6. 141 - Play song.");
		System.out.println("7. 147 - Change Output Bits.");

	}
	
	public void actOnOptionOpCodeMenu(int userChoice)
	{
		switch(userChoice)
		{
			case 1: oCL = getUserStringInput("Please enter in your custom opCode:");
					oCLC = getUserStringInput("Please enter in your custom opCode comment:");
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			case 2: oCL = "128";
					oCLC = "Starts the OI. Must be sent before any other commands to the OI.";
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					oCL = "132";
					oCLC = "Puts the OI into Full mode.";
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			case 3: oCL = "137 ";
					oCLC = "Makes the roomba move with a velocity of ";
					getVelocity();
					getRadius();
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			case 4: oCL = "156 ";
					oCLC = "Makes the roomba move by ";
					getWaitDistance();
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			case 5: oCL = "140 ";
					oCLC = "Writes a song to Song Number ";
					getSong();
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			case 6: oCL = "141 ";
					oCLC = "Plays song number ";
					playSong();
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			case 7: oCL = "147 ";
					oCLC = "Changes output voltages for the 3 digital output pins (0000_0000 to 0000_0111): ";
					changeOutputBits();
					opCodeList.add(new OpCodeLine(oCL, oCLC));
					break;
			default: break;
		}
	}
	
	public void viewOpCodeSingleLine()
	{
		String oneLineOpCode = "";
		for(OpCodeLine s: opCodeList)
			oneLineOpCode += s.getOpCode() + " ";
		System.out.println("The current opCode: ");
		System.out.println(oneLineOpCode);
	}
	
	public void viewOpCodeMultipleLines()
	{
		int i = 1;
		for(OpCodeLine s: opCodeList)
		{
			System.out.println("Ln# " + i + ":	" + s.getOpCode() + " 	# " + s.getOpCodeComment());
			i++;
		}
	}
	
	public void deleteOpCodeSingleLine()
	{
		if(opCodeList.size() == 0)
		{
			System.out.println("No opCodes to delete.");
			return;
		}
		opCodeList.remove(getUserIntInput(1, opCodeList.size(), "Please enter the line number of the opCode you wish to delete.") - 1);
	}
	
	public void deleteAllOpCode()
	{
		opCodeList.clear();
	}
	
	public void saveOpCodes()
	{
		try
		{
			File opCodeFile = new File(opCodeFileName);
			if((!opCodeFile.createNewFile() && !opCodeFile.canWrite()) || opCodeList.size() == 0)
			{
				System.out.println("Error: Cannot write opCodes to the file: " + opCodeFileName);
				return;
			}
			try
			{
				FileWriter outStream = new FileWriter(opCodeFile);
		
				for(OpCodeLine forOpCode: opCodeList)			
					outStream.write(forOpCode.toFileString() + "\r\n");
				outStream.close();
				System.out.println("OpCodes saved successfully.");
			}
			catch(IOException err)
			{
				System.out.println("Error: Could not write OpCodes to the file.");
			}				
		}
		catch(IOException err)
		{
			System.out.println("Error: Cannot write Opcodes to the file: " + opCodeFileName);			
		}
	}
	
	public void loadOpCodes()
	{
		File attemptFile;
		System.out.println("Would you like to use a different opCode file than " +
						   defaultOpCodeFileName + "?");
		if(getUserIntInput(0, 1, "Enter 0 for no, and 1 for yes") == 1)
		{
			do
			{			
				opCodeFileName = getUserStringInput(
											"Please enter your desired file name: ");
				 attemptFile = new File(opCodeFileName);
			}while(!attemptFile.canRead());
		}
		else
			opCodeFileName = defaultOpCodeFileName;
		
		File opCodeFile = new File(opCodeFileName);
		if(!opCodeFile.exists())
		{
			System.out.println("Error: Cannot find " + opCodeFileName + ".");
			return;
		}
		
		Scanner fileScanner;
		try
		{
			fileScanner = new Scanner(opCodeFile).useDelimiter(";|\\r\\n");
		}
		catch(FileNotFoundException err)
		{
			System.out.println("Error: Cannot read the given opCode file.");
			return;
		}
		
		while(fileScanner.hasNext())
		{
			String opCodeLineStr = fileScanner.next();
			String opCodeLineCommentStr = fileScanner.next();
			
			OpCodeLine scannedOpCodeLine = new OpCodeLine(opCodeLineStr, opCodeLineCommentStr);
			opCodeList.add(scannedOpCodeLine);
		}
		fileScanner.close(); 
	}
	
	/**
	 * Asks the user for String line input.
	 * Returns the user input.
	 * 
	 * @param userPrompt The message to prompt the user for an input.
	 * @return The String that the user has entered.
	 */
	private String getUserStringInput(String userPrompt)
	{
		Scanner userInput = new Scanner(System.in);
		String userString;

		System.out.println(userPrompt);
		userString = userInput.nextLine();
		return userString;
	}
	
	
	/**
	 * RoombaOI default constructor
	 */
	public RoombaOI()
	{
		opCodeList = new ArrayList<OpCodeLine>();
		loadOpCodes();
	}
	
	public static void main(String[] args)
	{
		RoombaOI roomba = new RoombaOI();
		roomba.run();
	}
}