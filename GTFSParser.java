import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TimeZone;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class GTFSParser {
	/*
	public static void main(String[] args) throws Exception {
		ArrayList<Trajectory> trajectories = parseTrips("data/mta-new-york-city-transit_20150404_2233");
		System.out.println("Parsing DONE");
		writeOut(trajectories);
		System.out.println("Writing DONE");
		
	}
	*/
	/*public static void main(String[] args) throws Exception {
		ArrayList<Trajectory> trajectories = parseTrips("data/mta-new-york-city-transit_20150404_2233");
		for (Trajectory trajectory : trajectories) {
			long a = 27000;
			System.out.println(trajectory.getPosition(a));
		}
	}
	*/

	// reads a CSV stream
	public static ArrayList<Map<String, String>> readCSV(String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
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

	private static String getRouteIdFromTripId(String tripId) {
		String routeId = "";
		for (int i = 20; tripId.charAt(i) != '.'; i++) {
			routeId += tripId.charAt(i);
		}
		return routeId;
	}

	private static String getServiceIdFromTripId(String tripId) {
		return tripId.substring(0, 12);
	}

	public static ArrayList<Trajectory> parseTrips(String folderPath) throws Exception {
		System.out.println("parsing starting");
		ArrayList<Trajectory> trajectoryList = new ArrayList<Trajectory>();
		String calendarFilePath = folderPath+"/calendar.csv";
		String shapesFilePath = folderPath+"/shapes.csv";
		String stopTimesFilePath = folderPath+"/stop_times.csv";
		String tripsFilePath = folderPath+"/trips.csv";
		String stopsFilePath = folderPath+"/stops.csv";
		/*System.out.println("parsing cal");
		ArrayList<Map<String, String>> calList = readCSV(calendarFilePath);
		System.out.println("parsing shapes");
		ArrayList<Map<String, String>> shapesList = readCSV(shapesFilePath);
		*/
		System.out.println("Parsing times");
		ArrayList<Map<String, String>> timesList = readCSV(stopTimesFilePath);
		System.out.println("times parsed");
		/*
		System.out.println("parsing trips");
		ArrayList<Map<String, String>> tripsList = readCSV(tripsFilePath);
		*/
		System.out.println("Parsing stops");
		ArrayList<Map<String, String>> stopsList = readCSV(stopsFilePath);
		System.out.println("stops parsed");
		Map<String, Stop> stopMap = new HashMap<String, Stop>();
		for (Map<String, String> stop : stopsList) {
			String stopId = stop.get("stop_id");
			String stopName = stop.get("stop_name");
			double stopLat = Double.parseDouble(stop.get("stop_lat"));
			double stopLon = Double.parseDouble(stop.get("stop_lon"));
			Coordinate cord = new Coordinate(stopLat, stopLon);
			int locationType = Integer.parseInt(stop.get("location_type"));
			String parentStation = stop.get("parent_station");
			stopMap.put(stopId, new Stop(stopId, stopName, cord, locationType, parentStation));
		}
		System.out.println("stopMap created");

		SortedMap<Long, Stop> trajectory = null;
		long prevTime = 0;
		long arrivalTime;
		long departureTime;
		String tripId = "";
		String routeId = "";
		String serviceId = "";
		Coordinate cord = null;

		System.out.println("creating trajectoryList");
		for (Map<String, String> time : timesList) {
			arrivalTime = toSeconds(time.get("arrival_time"));
			departureTime = toSeconds(time.get("departure_time"));
			if (arrivalTime < prevTime || prevTime == 0) {
				if (prevTime != 0) {
					System.out.println("trajectory created");
					trajectoryList.add(new Trajectory(tripId, serviceId, routeId, trajectory));
				}
				tripId = time.get("trip_id");
				routeId = getRouteIdFromTripId(tripId);
				serviceId = getServiceIdFromTripId(tripId);
				trajectory = new TreeMap<Long, Stop>();
			}
			String stopId = time.get("stop_id");
			trajectory.put(arrivalTime, stopMap.get(stopId));
			if (arrivalTime != departureTime) {
				trajectory.put(departureTime, stopMap.get(stopId));
			}
			prevTime = arrivalTime;
		}
		System.out.println("trajectoryList created");

		/*
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
				}
			}
			Trajectory trajectory = new Trajectory(trip_id, service_id, map);
			trajectoryList.add(trajectory);
		}
		*/
		return trajectoryList;
	}

	/*private static Long toElapsedTime(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+5"));
		Date date = sdf.parse(time);
		Long epoch = date.getTime() / 1000; // millisecond to second
		return epoch;
	}
	*/
	private static long toSeconds(String time) {
		long hours = Long.parseLong(time.substring(0, 2));
		long minutes = Long.parseLong(time.substring(3, 5));
		long seconds = Long.parseLong(time.substring(6, 8));
		return hours * 3600 + minutes * 60 + seconds;
	}

	/*
	private static void writeOut(ArrayList<Trajectory> trajectories) throws Exception {
		PrintWriter pw = new PrintWriter(
			new OutputStreamWriter(
			new FileOutputStream("data/"+"translatedData.csv"), "UTF-8"));
		
		pw.println("trip_id,service_id,time,lat,lon");
		for (Trajectory trajectory : trajectories) {
			String tripId = trajectory.getTripId();
			String serviceId = trajectory.getServiceId();
			for (Map.Entry<Long, Coordinate> entry : trajectory.getMap().entrySet()) {
				Long time = entry.getKey();
				Coordinate coordinate = entry.getValue();
				pw.println(tripId+","+serviceId+","+time+","+coordinate.getLat()+","+coordinate.getLon());
			}
		}
		pw.close();
	}
	*/
}
