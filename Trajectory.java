import java.util.SortedMap;
import java.util.TreeMap;

// this class describes the complete path a vehicle takes through time and space
public class Trajectory {
	private String trip_id;
	private String service_id;
	private SortedMap<Long, Coordinate> trajectory = new TreeMap<Long, Coordinate>();

	public Trajectory(String trip_id, String service_id, Long[] times, Coordinate[] coordinates) {
		this.trip_id = trip_id;
		this.service_id = service_id;
		for (int i = 0; i < times.length; i++) {
			this.trajectory.put(times[i], coordinates[i]);
		}
	}

	// returns the position of the vehicle at timestamp time
	private Coordinate getPosition(Long time) {
		return this.trajectory.get(time);
	}
	
	// returns whether the vehicle is active at timestamp time
	boolean isActive(Long time) {
		return time < this.trajectory.lastKey() && time > this.trajectory.firstKey();
	}
}
