
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parse {
	static File doc = new File("data/flight_schedules2.txt");

    public static void main(String[] args) {
        String line1 = "";
        	Scanner sc = null;
			try {
				sc = new Scanner(doc);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	PrintWriter logFile = null;
			try {
				logFile = new PrintWriter("data/logFile.txt", "UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	 	
       	 	while(sc.hasNext())
       	 		
            { 
            	line1 = sc.nextLine();
            	if(line1.contains("Compass"))
            	{
            		logFile.println(line1);
            	}
            }
                // Close the writer regardless of what happens...
                logFile.close();
                Scanner scan = null;
                try {
    				scan = new Scanner(doc);
    			} catch (FileNotFoundException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
                
                String [][] table = dataTable(scan);
                
          
        }
    public static String[][] dataTable(Scanner sc)
    {	String[][] twoD = new String[3][4];
    	while(sc.hasNext())
    	{
    	
    	}
    	
    	return twoD;
    }
    }

