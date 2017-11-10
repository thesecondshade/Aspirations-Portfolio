import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner {
	// Data about flights: To and From
	static File doc = new File("data/flight_schedules2.txt");
	// ArrayList of Flight objects
	ArrayList<Flight> flights = new ArrayList<Flight>();

	public static void main(String[] args) {
		//System.out.println("Step1");
		Runner viz = new Runner();

	}

	public Runner() {
		// ArrayList of the line of data starting with To and From
		ArrayList<String> listPlaces = Scanned();
		
		Flight f = null;
		for (int i = 0; i < listPlaces.size(); i++) {
			f = new Flight(listPlaces.get(i));
			flights.add(f);
		}
		writer();
		//loop thru listPlaces and fill flights with flight objects
	}

	public static ArrayList<String> Scanned() {
		String line1 = "";
		String line2 = "";
		ArrayList<String> object = new ArrayList<String>();
		Scanner sc = null;
		try {
			sc = new Scanner(doc);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		//Scanner to loop through flightSchedules data
		Scanner sc2 = null;
		try {
			sc2 = new Scanner(doc);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		//Scanner to loop through flightSchedules data but one line behind
		//So when we find the correct To line, we can collect the line behind it as well
		
		line1 = sc.nextLine();
		//bump the line1 one ahead then line2
		while (sc2.hasNext() && sc.hasNext()) 
		{
			
			line1 = sc.nextLine();
			line2 = sc2.nextLine();
			if (line1.contains("To ")&&line1.contains("Compass")) {
				object.add(line1 + " \n" + line2);
			}
		}
		
		return object;

	}

	public void writer() {

		PrintWriter logFile = null;
		try {
			// create a new data file
			logFile = new PrintWriter("data/finalFile.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String to = "";
		String fro = "";
		double latTo = 0.0;
		double latFro = 0.0;
		double lonTo = 0.0;
		double lonFro = 0.0;
		
		for (int i = 0; i < flights.size(); i++)
		// loop thru arrayList of flights and collect to, fro, lat and long
		{
			to = flights.get(i).getTo();
			fro = flights.get(i).getFro();
			latTo = flights.get(i).getLatTo();
			latFro = flights.get(i).getLatFro();
			lonTo = flights.get(i).getLongTo();
			lonFro = flights.get(i).getLongFro();
			System.out.println(to+", "+fro+", "+latTo+", "+lonTo+", "+latFro+", "+lonFro+"\n");
			logFile.println(to+", "+fro+", "+latTo+", "+lonTo+", "+latFro+", "+lonFro+"\n");

		}
		// Close the writer regardless of what happens...
		logFile.close();

	}
}
