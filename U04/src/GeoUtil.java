public class GeoUtil
{
	public static double distance(GeoPoint p1, GeoPoint p2) {	// static method

		double deltaX = p1.getX() - p2.getX();		// differences x and y
		double deltaY = p1.getY() - p2.getY();

		double square1 = deltaX * deltaX;		// squares
		double square2 = deltaY * deltaY;

		double sum = square1 + square2;
		
		double d = Math.sqrt(sum);		// square root
		
		return d;
	}
}