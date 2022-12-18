public class Station {
	
	private String name;	// attributes
	private double value;
	private GeoPoint location;
	
	public Station(double xIn, double yIn, String nameIn, double valueIn) {	// constructor
		location = new GeoPoint(xIn, yIn);
		name = nameIn;
		value = valueIn;
	}
	
	public void setName(String nameIn) {	// method setting name
		name = nameIn;
	}
	
	public String getName() {	// method returning name
		return name;
	}
	
	public void setValue(double valueIn) {	// method setting value
		value = valueIn;
	}
	
	public double getValue() {	// method returning value
		return value;
	}
	
	public void setLocation(double xIn, double yIn) {	// method setting location which is a GeoPoint
		location = new GeoPoint(xIn, yIn);
	}
	
	public GeoPoint getLocation() {	// method returning location
		return location;
	}
}