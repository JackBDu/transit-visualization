// class that stores a stop
public class Stop {
	private String id;
	private String name;
	private Coordinate cord;
	// type 1 is the parent station
	private int type;
	private int parent;

	public Stop(String id, String name, Coordinate cord, int type, int parent) {
		this.id = id;
		this.name = name;
		this.cord = cord;
		this.type = type;
		this.parent = parent;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Coordinate getCord() {
		return this.cord;
	}

	public int getType() {
		return this.type;
	}

	public int getParent() {
		return this.parent;
	}

	public String toString() {
		return "Stop {\n"
			+ "id: "+this.id+",\n"
			+ "name: "+this.name+",\n"
			+ "cord: "+this.cord+",\n"
			+ "parent: "+this.parent+"\n"
			+ "}";
	}
}
