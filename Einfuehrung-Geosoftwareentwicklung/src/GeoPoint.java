public class GeoPoint implements Geometry {	// polymorphism

	private double x;	// private attributes x, y and bounding box
	private double y;
	private BoundingBox bboxGeoPoint;

	public GeoPoint(double x1, double y1) {	// constructor
		x = x1;
		y = y1;
	}

	public double getX() {	// method returning x
		return x;
	}
		
	public void setX(double newX) {	// method changing x to newX
		x = newX;
	}

	public double getY() {	// method returning y
		return y;
	}

	public void setY(double newY) {	// method changing y to newY
		y = newY;
	}

	public BoundingBox getBoundingBox() {	// method inherited from Geometry and now implemented for GeoPoint
		bboxGeoPoint = new BoundingBox(new GeoPoint(x, y));
		return bboxGeoPoint;
	}
}