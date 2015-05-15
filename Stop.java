// class that stores a stop
public class Stop {
	private String id;
	private String name;
	private Coordinate cord;
	// type 1 is the parent station
	private int type;
	private String parent;
	
	// constructor of the Stop class
	public Stop(String id, String name, Coordinate cord, int type, String parent) {
		this.id = id;
		this.name = name;
		this.cord = cord;
		this.type = type;
		this.parent = parent;
	}

	// get the id
	public String getId() {
		return this.id;
	}

	// get the name
	public String getName() {
		return this.name;
	}

	// get the cord
	public Coordinate getCord() {
		return this.cord;
	}

	// get the lat
	public double getLat() {
		return this.cord.getLat();
	}

	// get the lon
	public double getLon() {
		return this.cord.getLon();
	}

	// get the type
	public int getType() {
		return this.type;
	}

	// get the parent
	public String getParent() {
		return this.parent;
	}

	// convert to String
	public String toString() {
		return "Stop {\n"
			+ "id: "+this.id+",\n"
			+ "name: "+this.name+",\n"
			+ "cord: "+this.cord+",\n"
			+ "parent: "+this.parent+"\n"
			+ "}";
	}
}
