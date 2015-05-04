import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

public class GTFSParser {
	public static void main(String[] args) throws IOException {
		InputStream is = new FileInputStream("data/mta-new-york-city-transit_20150404_2233/calendar.txt");
		ArrayList<Map<String, String>> al = readCSV(is);
		System.out.println(al.get(0).get("service_id"));
	}

	// reads a CSV stream
	private static ArrayList<Map<String, String>> readCSV(InputStream csv) throws IOException {
		ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
		BufferedReader br = new BufferedReader(new InputStreamReader(csv));
		String[] headerLine = br.readLine().split(","); // reads the header of the file
		String line = br.readLine(); // reads the first line of data
		while (line != null) {
			String[] values = line.split(",");
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < values.length; i++) {
				map.put(headerLine[i], values[i]); // maps the header with corresponding value
			}
			al.add(map); // adds the map to the ArrayList
			line = br.readLine();
		}
		br.close();
		return al;
	}

	private static ArrayList<Trajectory> parseTrips(String filePath) {
		ArrayList<Trajectory> trajectoryList;
		InputStream isCal = new FileInputStream(filePath+"/calendar.txt");
		InputStream isShapes = new FileInputStream(filePath+"/shapes.txt");
		InputStream isTimes = new FileInputStream(filePath+"/stop_times.txt");
		InputStream isTrips = new FileInputStream(filePath+"/trips.txt");
		ArrayList<Map<String, String>> calList = readCSV(isCal);
		ArrayList<Map<String, String>> shapesList = readCSV(isShapes);
		ArrayList<Map<String, String>> timesList = readCSV(isTimes);
		ArrayList<Map<String, String>> tripsList = readCSV(isTrips);
	}
}
