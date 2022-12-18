import java.util.LinkedList;

public class IDWApplication {

	public static void main(String[] args) {
		
		Station station1 = new Station(1.0, 1.0, "Station 1", 1.0);	// creating stations
		Station station2 = new Station(3.0, 3.0, "Station 2", 3.5);
		Station station3 = new Station(0.5, 0.5, "Station 3", 0.5);
		Station station4 = new Station(1.3, 2.2, "Station 4", 2.6);

		LinkedList<Station> stations = new LinkedList<Station>();	// new linked list
		
		stations.add(station1);	// adding stations to the linked list
		stations.add(station2);
		stations.add(station3);
		stations.add(station4);
		
		int rows = 4;
		int cols = 5;
		int cellSize = 1;
		
		GeoRaster2D geoRaster2D = new GeoRaster2D(new GeoPoint(0*cellSize, rows*cellSize), 1, rows, cols);

		IDW idw = new IDW (geoRaster2D, stations);	// IDW is calculated based on geo raster and stations
		
		for (int row = 0; row < idw.getGeoRaster2D().getData().rows(); row++) {	// iterating over rows
			
			for (int col = 0; col < idw.getGeoRaster2D().getData().columns(); col++) {	// iterating over columns
				System.out.print(idw.getGeoRaster2D().getData().getValue(row, col) +"	");	// printing the values of the columns of one row
			}
			
			System.out.println();	// new line for every row
		}
	}
}