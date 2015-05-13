// a class that represents a geographic location
public class Coordinate {
	private double lat;
	private double lon;

	// constructor of the class
	public Coordinate(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	// return the lat value
	public double getLat() {
		return this.lat;
	}

	// return the lon value
	public double getLon() {
		return this.lon;
	}

	// return the String representation of the class
	public String toString() {
		return "Coordinate("+this.lat+", "+this.lon+")";
	}
}
