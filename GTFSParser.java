import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TimeZone;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;

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

	private static ArrayList<Trajectory> parseTrips(String filePath) throws IOException {
		ArrayList<Trajectory> trajectoryList;
		InputStream isCal = new FileInputStream(filePath+"/calendar.txt");
		InputStream isShapes = new FileInputStream(filePath+"/shapes.txt");
		InputStream isTimes = new FileInputStream(filePath+"/stop_times.txt");
		InputStream isTrips = new FileInputStream(filePath+"/trips.txt");
		InputStream isStops = new FileInputStream(filePath+"/stops.txt");
		ArrayList<Map<String, String>> calList = readCSV(isCal);
		ArrayList<Map<String, String>> shapesList = readCSV(isShapes);
		ArrayList<Map<String, String>> timesList = readCSV(isTimes);
		ArrayList<Map<String, String>> tripsList = readCSV(isTrips);
		ArrayList<Map<String, String>> stopsList = readCSV(isStops);
		for (int i = 0; i < tripsList.size(); i++) {
			String trip_id = tripsList.get(i).get("trip_id");
			String service_id = tripsList.get(i).get("service_id");
			SortedMap<Long, Coordinate> map = new TreeMap();
			for (int j = 0; j < timesList.size(); j++) {
				String times_trip_id = timesList.get(j).get("trip_id");
				if (times_trip_id == trip_id) {
					String time = trip_id.substring(1, 9) + timesList.get(j).get("arrival_time");
					Lang epoch = toElapsedTime(time);
					Coordinate coordinate = new Coordinate(stopsList.get);
				}
			}
			Trajectory trajectory = new Trajectory(trip_id, service_id, , );
		}
		return trajectoryList;
	}

	private static Long toElapsedTime(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));
		Date date = sdf.parse(time);
		Long epoch = date.getTime() / 1000; // millisecond to second
		return epoch;
	}
}
