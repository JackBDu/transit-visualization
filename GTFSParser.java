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
import java.text.ParseException;

public class GTFSParser {
	public static void main(String[] args) throws Exception {
		InputStream is = new FileInputStream("data/mta-new-york-city-transit_20150404_2233/calendar.txt");
		ArrayList<Map<String, String>> al = readCSV(is);
		System.out.println(al.get(0).get("service_id"));
		parseTrips("data/mta-new-york-city-transit_20150404_2233");
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

	private static ArrayList<Trajectory> parseTrips(String filePath) throws Exception {
		System.out.println("parsing starting");
		ArrayList<Trajectory> trajectoryList = new ArrayList<Trajectory>();
		InputStream isCal = new FileInputStream(filePath+"/calendar.txt");
		InputStream isShapes = new FileInputStream(filePath+"/shapes.txt");
		InputStream isTimes = new FileInputStream(filePath+"/stop_times.txt");
		InputStream isTrips = new FileInputStream(filePath+"/trips.txt");
		InputStream isStops = new FileInputStream(filePath+"/stops.txt");
		System.out.println("parsing cal");
		ArrayList<Map<String, String>> calList = readCSV(isCal);
		System.out.println("parsing shapes");
		ArrayList<Map<String, String>> shapesList = readCSV(isShapes);
		System.out.println("parsing times");
		ArrayList<Map<String, String>> timesList = readCSV(isTimes);
		System.out.println("parsing trips");
		ArrayList<Map<String, String>> tripsList = readCSV(isTrips);
		System.out.println("parsing stops");
		ArrayList<Map<String, String>> stopsList = readCSV(isStops);
		String trip_id = null;
		String service_id = null;
		SortedMap<Long, Coordinate> map = null;
		for (int i = 0; i < tripsList.size(); i++) {
			trip_id = tripsList.get(i).get("trip_id");
			service_id = tripsList.get(i).get("service_id");
			map = new TreeMap<Long, Coordinate>();
			for (int j = 0; j < timesList.size(); j++) {
				String times_trip_id = timesList.get(j).get("trip_id");
				if (times_trip_id.equals(trip_id)) {
					String time = trip_id.substring(1, 9) + timesList.get(j).get("arrival_time");
					Long epoch = toElapsedTime(time);
					String times_stop_id = timesList.get(j).get("stop_id");
					Coordinate coordinate;
					for (int k = 0; k < stopsList.size(); k++) {
						String stop_id = stopsList.get(k).get("stop_id");
						if (stop_id.equals(times_stop_id)) {
							double lat = Double.parseDouble(stopsList.get(k).get("stop_lat"));
							double lon = Double.parseDouble(stopsList.get(k).get("stop_lon"));
							coordinate = new Coordinate(lat, lon);
							map.put(epoch, coordinate);
							break;
						}
					}
					break;
				}
			}
			Trajectory trajectory = new Trajectory(trip_id, service_id, map);
			trajectoryList.add(trajectory);
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
