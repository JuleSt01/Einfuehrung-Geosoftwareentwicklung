import java.util.ArrayList;
import java.util.LinkedList;

public class BoundingBox {

	private GeoPoint minPoint;	// attributes
	private GeoPoint maxPoint;
	private double height;
	private double width;

	public BoundingBox(ArrayList<GeoPoint> geoPoints) {	// constructor, array list of points given
		minPoint = findMinPoint(geoPoints);	// methods called up by constructor
		maxPoint = findMaxPoint(geoPoints);
		
		height = maxPoint.getY() - minPoint.getY();	// methods calculating height and width
		width = maxPoint.getX() - minPoint.getX();
	}
	
	public BoundingBox(GeoPoint geoPoint) {	// constructor, a single point given
		minPoint = new GeoPoint (geoPoint.getX(), geoPoint.getY());	// minPoint = maxPoint = point
		maxPoint = minPoint;
		
		height = maxPoint.getY() - minPoint.getY();	// methods calculating height and width, height = width = 0
		width = maxPoint.getX() - minPoint.getX();
	}
	
	public BoundingBox(LinkedList<GeoPoint> geoPoints) {
		minPoint = findMinPoint(geoPoints);	// methods called up by constructor
		maxPoint = findMaxPoint(geoPoints);
		
		height = maxPoint.getY() - minPoint.getY();	// methods calculating height and width
		width = maxPoint.getX() - minPoint.getX();
	}
	
	public GeoPoint findMaxPoint(ArrayList<GeoPoint> geoPoints) {
		
		double xMax1 = geoPoints.get(0).getX();	// xMax1 is random to allow comparison
		double yMax1 = geoPoints.get(0).getY();	// yMax1 is random to allow comparison
		
			for (int i=0; i <= geoPoints.size()-1; i++) {	// all points of the array
				if (geoPoints.get(i).getX() > xMax1) {	// if condition is true xMax1 is replaced by higher x
					xMax1 = geoPoints.get(i).getX();
				}
				if (geoPoints.get(i).getY() > yMax1) {	// if condition is true yMax1 is replaced by higher y
					yMax1 = geoPoints.get(i).getY();
				}
			}
			
		GeoPoint maxPoint = new GeoPoint(xMax1, yMax1);	// new point maxPoint is created, upper right of the Bbox
		return maxPoint;
	}
	
	public GeoPoint findMinPoint(ArrayList<GeoPoint> geoPoints) {
		
		double xMin1 = geoPoints.get(0).getX();	// xMin1 is random to allow comparison
		double yMin1 = geoPoints.get(0).getY();	// yMin1 is random to allow comparison
		
		for (int i=0; i <= geoPoints.size()-1; i++) {	// all points of the array
			if (geoPoints.get(i).getX() < xMin1) {	// if condition is true xMin1 is replaced by lower x
				xMin1 = geoPoints.get(i).getX();
			}
			if (geoPoints.get(i).getY() < yMin1) {	// if condition is true yMin1 is replaced by lower y
				yMin1 = geoPoints.get(i).getY();
			}
		}

	GeoPoint minPoint = new GeoPoint(xMin1, yMin1);	// new point minPoint is created, lower left of the Bbox
	return minPoint;
	}

	public GeoPoint getMinPoint() {	// method returning minPoint
		return minPoint;
	}

	public GeoPoint getMaxPoint() {	// method returning maxPoint
		return maxPoint;
	}

	public GeoPoint findMaxPoint(LinkedList<GeoPoint> geoPoints) {
		
		double xMax1 = geoPoints.get(0).getX();	// xMax1 is random to allow comparison
		double yMax1 = geoPoints.get(0).getY();	// yMax1 is random to allow comparison
		
			for (int i=0; i <= geoPoints.size()-1; i++) {	// all points of the linked list
				if (geoPoints.get(i).getX() > xMax1) {	// if condition is true xMax1 is replaced by higher x
					xMax1 = geoPoints.get(i).getX();
				}
				if (geoPoints.get(i).getY() > yMax1) {	// if condition is true yMax1 is replaced by higher y
					yMax1 = geoPoints.get(i).getY();
				}
			}
			
		GeoPoint maxPoint = new GeoPoint(xMax1, yMax1);	// new point maxPoint is created
		return maxPoint;
	}
	
	public GeoPoint findMinPoint(LinkedList<GeoPoint> geoPoints) {
		
		double xMin1 = geoPoints.get(0).getX();	// xMin1 is random to allow comparison
		double yMin1 = geoPoints.get(0).getY();	// yMin1 is random to allow comparison
		
		for (int i=0; i <= geoPoints.size()-1; i++) {	// all points of the linked list
			if (geoPoints.get(i).getX() < xMin1) {	// if condition is true xMin1 is replaced by lower x
				xMin1 = geoPoints.get(i).getX();
			}
			if (geoPoints.get(i).getY() < yMin1) {	// if condition is true yMin1 is replaced by lower y
				yMin1 = geoPoints.get(i).getY();
			}
		}

	GeoPoint minPoint = new GeoPoint(xMin1, yMin1);	// new point minPoint is created
	return minPoint;
	}
	
	public double getHeight() {	// method returning height
		return height;
	}

	public double getWidth() {	// method returning width
		return width;
	}
}