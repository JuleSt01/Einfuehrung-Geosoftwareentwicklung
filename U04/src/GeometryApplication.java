import java.util.ArrayList;

public class GeometryApplication {

	public static void main(String[] args) {
		
		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();	// creating an empty array list
		
		geoPoints.add(new GeoPoint(1.0, 1.0));	// adding 4 points to the array list
		geoPoints.add(new GeoPoint(1.0, 3.0));
		geoPoints.add(new GeoPoint(3.0, 3.0));
		geoPoints.add(new GeoPoint(3.0, 1.0));
		
		// GeoPoint
		
		System.out.println("1. Teil der Ausgabe:");
		
		for (int i=1; i<=geoPoints.size(); i++) {	// printing every single point of the array list
		System.out.println("Punkt "+ i +": "+ geoPoints.get(i-1).getX() +", "+ geoPoints.get(i-1).getY() +"");
		}
		
		System.out.println("\nBounding Box für Punkt 1:");
		
		BoundingBox bboxGeoPoint = new BoundingBox(geoPoints.get(0));	// bounding box of a point
		
		System.out.println("Der minimale Punkt der Bounding Box liegt bei: ("+ bboxGeoPoint.getMinPoint().getX() +", "+ bboxGeoPoint.getMinPoint().getY() +")");
		System.out.println("Der maximale Punkt der Bounding Box liegt bei: ("+ bboxGeoPoint.getMaxPoint().getX() +", "+ bboxGeoPoint.getMaxPoint().getY() +")");
		System.out.println("Die Breite der Bounding Box ist: "+ bboxGeoPoint.getWidth() +"");
		System.out.println("Die Höhe der Bounding Box ist: "+ bboxGeoPoint.getHeight() +"");
		
		// GeoPolyline
		
		System.out.println("\n\n2. Teil der Ausgabe:");
		
		GeoPolyline polyline = new GeoPolyline(geoPoints);	// creating a polyline
		
		polyline.setName("Polyline 1");	// setting the name of the polyline
		
		BoundingBox bboxGeoPolyline = polyline.getBoundingBox();	// calculating the polyline's bounding box
		
		System.out.println("Die Polylinie heißt: "+ polyline.getName() +"");
		System.out.println("Länge: "+ polyline.getLength() +"");
		System.out.println("Anzahl der Stützpunkte: "+ polyline.getStuetzpunkte() +"");
		
		
		System.out.println("Der minimale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolyline.getMinPoint().getX() +", "+ bboxGeoPolyline.getMinPoint().getY() +")");
		System.out.println("Der maximale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolyline.getMaxPoint().getX() +", "+ bboxGeoPolyline.getMaxPoint().getY() +")");
		System.out.println("Die Breite der Bounding Box ist: "+ bboxGeoPolyline.getWidth() +"");
		System.out.println("Die Höhe der Bounding Box ist: "+ bboxGeoPolyline.getHeight() +"");
		
		// GeoPolygon
		
		GeoPolygon polygon = new GeoPolygon(geoPoints);	// creating a polygon
			
		polygon.setName("Polygon 1");	// setting the name of the polygon
		
		System.out.println("\nDas Polygon heißt: "+ polygon.getName() +"");
		System.out.println("Umfang: "+ polygon.getCircumference() +"");
		System.out.println("Anzahl der Stützpunkte: "+ polygon.getStuetzpunkte() +"");
		
		BoundingBox bboxGeoPolygon = polygon.getBoundingBox();	// calculating the polygon's bounding box
		
		System.out.println("Der minimale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolygon.getMinPoint().getX() +", "+ bboxGeoPolygon.getMinPoint().getY() +")");
		System.out.println("Der maximale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolygon.getMaxPoint().getX() +", "+ bboxGeoPolygon.getMaxPoint().getY() +")");
		System.out.println("Die Breite der Bounding Box ist: "+ bboxGeoPolygon.getWidth() +"");
		System.out.println("Die Höhe der Bounding Box ist: "+ bboxGeoPolygon.getHeight() +"");
		
		// New point
		
		System.out.println("\n\n3. Teil der Ausgabe:");
		
		GeoPoint pointNew = new GeoPoint(2.0, 2.0);	// creating a new point
		
		geoPoints.add(pointNew);	// adding the point to the array list
		
		polyline.addGeoPoint(pointNew, 3);	// adding the new point to the polyline
		polygon.addGeoPoint(pointNew, 4);	// adding the new point to the polygon
		
		BoundingBox bboxGeoPolyline1 = polyline.getBoundingBox();	// calculating the new bounding boxes
		BoundingBox bboxGeoPolygon1 = polygon.getBoundingBox();
		
		System.out.println("Der 5. Punkt hat die Koordinaten: "+ geoPoints.get(4).getX() +", "+ geoPoints.get(4).getY() +" ");
		
		System.out.println("Nach dem Einfügen hat die Polyline die Länge: "+ polyline.getLength() +"");
		System.out.println("Anzahl der Stützpunkte nach dem Einfügen: "+ polyline.getStuetzpunkte() +"");
		
		System.out.println("Der minimale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolyline1.getMinPoint().getX() +", "+ bboxGeoPolyline1.getMinPoint().getY() +")");
		System.out.println("Der maximale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolyline1.getMaxPoint().getX() +", "+ bboxGeoPolyline1.getMaxPoint().getY() +")");
		System.out.println("Die Breite der Bounding Box ist: "+ bboxGeoPolyline1.getWidth() +"");
		System.out.println("Die Höhe der Bounding Box ist: "+ bboxGeoPolyline1.getHeight() +"");
		
		System.out.println("\nNach dem Einfügen hat das Polygon den Umfang: "+ polygon.getCircumference() +"");
		System.out.println("Anzahl der Stützpunkte nach dem Einfügen: "+ polygon.getStuetzpunkte() +"");
		
		System.out.println("Der minimale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolygon1.getMinPoint().getX() +", "+ bboxGeoPolygon1.getMinPoint().getY() +")");
		System.out.println("Der maximale Punkt der Bounding Box liegt bei: ("+ bboxGeoPolygon1.getMaxPoint().getX() +", "+ bboxGeoPolygon1.getMaxPoint().getY() +")");
		System.out.println("Die Breite der Bounding Box ist: "+ bboxGeoPolygon1.getWidth() +"");
		System.out.println("Die Höhe der Bounding Box ist: "+ bboxGeoPolygon1.getHeight() +"");
	}
}