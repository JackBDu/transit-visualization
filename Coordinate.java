// a class that represents a geographic location
public class Coordinate {
	private double lat;
	private double lon;

	public Coordinate(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return this.lat;
	}

	public double getLon() {
		return this.lon;
	}

	public String toString() {
		return "Coordinate("+this.lat+", "+this.lon+")";
	}
}
