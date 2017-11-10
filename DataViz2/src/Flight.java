import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Flight {

	String toLocation = "";
	String fromLocation = "";
	String[] pieces2 = null;
	String[] pieces = null;
	String[] initial = null;
	
	public Flight(String line) {
		initial = line.split("\n");
		pieces = initial[0].split("\\ +");
		pieces2 = initial[1].split("\\ +");
		
		// System.out.println("MAIN");
		getTo();
		getFro();
		getLatTo();
		getLongTo();
		getLatFro();
		getLongFro();

	}

	public String getTo() {
		for (int i = 0; i < pieces.length-1; i++) {

			if (pieces[i].matches("[(][A-Z][A-Z][A-Z][)]")
					&& pieces[0].contains("To")) {
				toLocation = pieces[i];
				toLocation = toLocation.substring(1, 4);
			}
			
		}
		return toLocation;
	}

	public String getFro() {
		for (int i = 0; i < pieces2.length; i++) {

			if (pieces2[i].matches("[(][A-Z][A-Z][A-Z][)]")
					&& pieces2[0].contains("From")) {
				fromLocation = pieces2[i];
				fromLocation = fromLocation.substring(1, 4);
			}
		}

		return fromLocation;
	}

	public Double getLatTo() {
		String[][] tagList = new String[12][1698];
		Scanner scan = null;
		try {
			scan = new Scanner(new File("data/tagFiledtxt.txt"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String[] coords = new String[12];
		Double latitudeT = 0.0;
		while (scan.hasNext()) {
			coords = scan.nextLine().split(",");
			for (int i = 0; i < coords.length; i++) {
				if (coords[i].contains(getTo())) {
					latitudeT = Double.parseDouble(coords[6]);
				}
			}

		}

		return latitudeT;

	}

	public Double getLongTo() {
		String[][] tagList = new String[12][1698];
		Scanner scan = null;
		try {
			scan = new Scanner(new File("data/tagFiledtxt.txt"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String[] coords = new String[12];
		Double longitudeT = 0.0;
		while (scan.hasNext()) {
			coords = scan.nextLine().split(",");
			for (int i = 0; i < coords.length; i++) {
				if (coords[i].contains(getTo())) {

					longitudeT = Double.parseDouble(coords[7]);
				}
			}

		}
		return longitudeT;

	}

	public Double getLatFro() {
		String[][] tagList = new String[12][1698];
		Scanner scan = null;
		try {
			scan = new Scanner(new File("data/tagFiledtxt.txt"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String[] coords = new String[12];
		Double latitudeF = 0.0;
		while (scan.hasNext()) {
			coords = scan.nextLine().split(",");
			for (int i = 0; i < coords.length; i++) {
				if (coords[i].contains(getFro())) {
					latitudeF = Double.parseDouble(coords[6]);
				}
			}

		}
		return latitudeF;

	}

	public Double getLongFro() {
		String[][] tagList = new String[12][1698];
		Scanner scan = null;
		try {
			scan = new Scanner(new File("data/tagFiledtxt.txt"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String[] coords = new String[12];
		Double longitudeF = 0.0;
		while (scan.hasNext()) {
			coords = scan.nextLine().split(",");
			for (int i = 0; i < coords.length; i++) {
				if (coords[i].contains(getFro())) {
					longitudeF = Double.parseDouble(coords[7]);
				}
			}

		}
		return longitudeF;

	}

}
