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
		Stop stop = null;
		if (this.isActive(time)) {
			for (Map.Entry<Long, Stop> entry : this.trajectory.entrySet()) {
				if (entry.getKey() >= time) {
					stop = entry.getValue();
					System.out.println("haha"+stop);
					break;
				}
			}
		} else {
			stop = this.trajectory.get(this.trajectory.firstKey());
		}
		return stop.getCord();
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
