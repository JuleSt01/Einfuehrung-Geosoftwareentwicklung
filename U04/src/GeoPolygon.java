import java.util.ArrayList;

public class GeoPolygon implements Geometry {	// polymorphism
	
	private ArrayList<GeoPoint> geoPoints;	// attributes
	private String name;
	private double circumference;
	private int stuetzpunkte;
	private BoundingBox bboxPolygon;
	
	public GeoPolygon(ArrayList<GeoPoint> geoPointsIn) {	// constructor
		
		if (2 < geoPointsIn.size()) {	// polygon is created if there are at least 3 points
		geoPoints = new ArrayList<GeoPoint> (geoPointsIn);
		}
		
		else {	// error if there are less than 3 points
		System.out.println("\nFehler: Mindestens 3 Punkte zum Erstellen eines Polygons nötig!");
		}
	}

	public double getCircumference() {	// method calculating and returning circumference as the sum of the distances between points
		
		double sum = 0;
		
		for (int i=0; i <= geoPoints.size()-2; i++) {
		sum += GeoUtil.distance(geoPoints.get(i), geoPoints.get(i+1));
		}
		
		circumference = sum + GeoUtil.distance(geoPoints.get(geoPoints.size()-1), geoPoints.get(0));	// sum of "sum" and the distance between the last and the first point of the array list
		
		return circumference;
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
		System.out.println("\nFehler Polygon: Gewünschte Position überschreitet Länge der Liste!\n");
		}
	}
	
	public int getStuetzpunkte() {	// method returning the number of points creating the polygon
		
		stuetzpunkte = geoPoints.size();
		return stuetzpunkte;
	}
	
	public BoundingBox getBoundingBox() {	// method inherited from Geometry and now implemented by GeoPolygon
		
		bboxPolygon = new BoundingBox(geoPoints);
		return bboxPolygon;
	}
}