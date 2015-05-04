// a class that represents a geographic location
public class Coordinate {
	private double lan;
	private double lon;

	public Coordinate(double lan, double lon) {
		this.lan = lan;
		this.lon = lon;
	}

	public double getLan() {
		return this.lan;
	}

	public double getLon() {
		return this.lon;
	}

	public String toString() {
		return "Coordinate("+this.lan+", "+this.lon+")";
	}
}
