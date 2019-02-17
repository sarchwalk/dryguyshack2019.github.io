import java.util.*;
import java.time.LocalDateTime;
import java.io.*;

public class weatherAnalyzer
{	
	public boolean snowCurrently()
	{
		Date date = new Date();
		String dateString = date.toString();
		
		File weatherFile = new File("output.csv");
		if(!weatherFile.exists())
		{
			System.out.println("Error: Cannot find " + "output.csv" + ".");
			return false;
		}
		Scanner fileScanner;
		
		try
		{
			fileScanner = new Scanner(weatherFile).useDelimiter(",|\\r\\n");
		}
		catch (FileNotFoundException f)
		{
			System.out.println("Error: Cannot find " + "output.csv" + ".");
			return false;
		}
		while(fileScanner.hasNext())
		{
			String csvTime = fileScanner.next();
			String csvSnow = fileScanner.next();
			//System.out.println("csvTime = " + csvTime +", dateString = " + dateString);
			//System.out.println("csvTime substring= " + csvTime.substring(0,2) +", dateString substring= " + dateString.substring(11,13));

			if(csvTime.substring(0,2).equals(dateString.substring(11,13)) && csvSnow.equals("True"))
				return true;
		}
		fileScanner.close(); 
		return false;
	}
	
	public weatherAnalyzer()
	{
		
	}
	
	public static void main(String[] Args)
	{
		weatherAnalyzer wA = new weatherAnalyzer();
		System.out.println(wA.snowCurrently());
	}
}