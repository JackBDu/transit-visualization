import java.util.SortedMap;
import java.util.Map;

// this class describes the complete path a vehicle takes through time and space
public class Trajectory {
	private String tripId;
	private String serviceId;
	private String routeId;
	private SortedMap<Long, Stop> trajectory;

	public Trajectory(String tripId, String serviceId, String routeId, SortedMap<Long, Stop> trajectory) {
		this.tripId = tripId;
		this.serviceId = serviceId;
		this.routeId = routeId;
		this.trajectory = trajectory;
	}

	public String getTripId() {
		return this.tripId;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public String getRouteId() {
		return this.routeId;
	}

	// returns the position of the vehicle at timestamp time
	public Coordinate getPosition(long time) {
		Stop nextStop = null;
		Stop prevStop = null;
		long prevTime = 0;
		long nextTime = 0;
		if (this.isActive(time)) {
			for (Map.Entry<Long, Stop> entry : this.trajectory.entrySet()) {
				nextTime = entry.getKey();
				if (nextTime == time) { // return that cord if time is in the entries
					return entry.getValue().getCord();
				} else if (nextTime > time) { // wait to calculate cord if time is in between
					nextStop = entry.getValue();
					break;
				}
				prevStop = entry.getValue();
				prevTime = nextTime;
			}
		} else { // doesn't show the stop if not active
			return new Coordinate(0, 0);
		}
		double prevLat = prevStop.getLat();
		double prevLon = prevStop.getLon();
		double nextLat = nextStop.getLat();
		double nextLon = nextStop.getLon();
		double lat = prevLat + (nextLat - prevLat) * (time - prevTime) / (nextTime - prevTime);
		double lon = prevLon + (nextLon - prevLon) * (time - prevTime) / (nextTime - prevTime);
		return new Coordinate(lat, lon);
	}
	
	// returns whether the vehicle is active at timestamp time
	public boolean isActive(long time) {
		return time <= this.trajectory.lastKey() && time >= this.trajectory.firstKey();
	}

	public SortedMap<Long, Stop> getTrajectory() {
		return this.trajectory;
	}

	public String toString() {
		return "Trajectory {\n"
			+ "tripId: "+this.tripId+",\n"
			+ "serviceId: "+this.serviceId+",\n"
			+ "}\n";
	}
}
