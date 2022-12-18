import java.util.ArrayList;

public class GeoPolyline implements Geometry {	// polymorphism
	
	private ArrayList<GeoPoint> geoPoints;	// attributes
	private String name;
	private double length;
	private int stuetzpunkte;
	private BoundingBox bboxPolyline;
	
	public GeoPolyline(ArrayList<GeoPoint> geoPointsIn) {	// constructor
		
		if (1 < geoPointsIn.size()) {	// polyline is created if there are at least 2 points
		geoPoints = new ArrayList<GeoPoint> (geoPointsIn);	// New array list filled with references of old points
		}
		
		else {	// error if there are less than 2 points
		System.out.println("\nFehler: Mindestens 2 Punkte zum Erstellen einer Polyline nötig!");
		}
	}

	public double getLength() {	// method calculating and returning polyline's length as the sum of the distances between points
		
		length = 0;
		
		for (int i=0; i <= geoPoints.size()-2; i++) {
		length += GeoUtil.distance(geoPoints.get(i), geoPoints.get(i+1));
		}
		
		return length;
	}
		
	public String getName() {	// method returning name
		return name;
	}
	
	public void setName(String nameIn) {	// method setting name
		name = nameIn;
	}
		
	public void addGeoPoint(GeoPoint pointIn) {	// method adding GeoPoint at the end of the array list
		geoPoints.add(pointIn);
	}

	public void addGeoPoint(GeoPoint pointIn, int x) {	// method adding GeoPoint at position x
		
		if (x <= geoPoints.size()) {
		geoPoints.add(x, pointIn);
		}
		
		else {	// error if position x is out of array list's bounds
		System.out.println("\nFehler Polyline: Gewünschte Position überschreitet Länge der Liste!\n");
		}	
	}
	
	public int getStuetzpunkte() {	// method returning the number of points creating the polyline
		
		stuetzpunkte = geoPoints.size();
		return stuetzpunkte;
	}
	
	public BoundingBox getBoundingBox() {	// method inherited from Geometry and now implemented by GeoPolyline
		
		bboxPolyline = new BoundingBox(geoPoints);
		return bboxPolyline;
	}
}