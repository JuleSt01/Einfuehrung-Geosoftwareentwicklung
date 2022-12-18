import java.util.LinkedList;
import java.util.Arrays;

public class IDW {

	private LinkedList<Station> stations;	// IDW's private attributes
	private GeoRaster2D geoRaster2D;
	private RasterData2D rasterData2D;

	
	public IDW (GeoRaster2D geoRaster2DIn, LinkedList<Station> stationsIn) {	// constructor, using every station
		
		stations = stationsIn;
		geoRaster2D = geoRaster2DIn;
		rasterData2D = geoRaster2D.getData();
		
		calculateIDW();
			
	}
	
	public IDW (GeoRaster2D geoRaster2DIn, LinkedList<Station> stationsIn, int number) {	// constructor, using only a certain number of stations
		
		if (number > 0) {
		stations = stationsIn;
		geoRaster2D = geoRaster2DIn;
		rasterData2D = geoRaster2D.getData();
		
			if (number > stations.size()) {
				
			System.out.println("Es stehen maximal "+ stations.size()+" Stationen für die IDW-Berechnung zur Verfügung!\n");
			}
		
		
			else {
				
			calculateIDW(number);
			}
		}
		
		else {
			System.out.println("Die Berechnung mittels IDW erfordert mindestens 1 Station!\n");
		}
		
	}
		
	public void calculateIDW() {	// method calculating IDW
		
		for (int r=0; r < geoRaster2D.getData().rows(); r++) {	// iterating over rows
			
			double cellcenterY = geoRaster2D.origin().getY() - (r + 0.5) * geoRaster2D.cellSize();	// calculating y of cell center for every single row
			
			for (int c=0; c < geoRaster2D.getData().columns(); c++) {	// iterating over columns
				
				double cellcenterX = (c + 0.5) * geoRaster2D.cellSize() + geoRaster2D.origin().getX();	// calculating x of cell center for every single raster cell of that row
				
				GeoPoint cellcenter = new GeoPoint (cellcenterX, cellcenterY);	// cell center
				
				double numerator = 0.0;
				double denominator = 0.0;
				
				for (int s=0; s < stations.size(); s++) {	// iterating over stations
					
					double d1 = Math.abs(GeoUtil.distance(cellcenter, stations.get(s).getLocation()));	// distance d1 between cell center and station
					double d = Math.max(d1, 0.001 * geoRaster2D.cellSize());	// d is either d1 or 0.001 * cell size if d1 = 0 (position station = cell center) to prevent division by 0
					
					numerator += stations.get(s).getValue() / d;	// sum of values divided by distances
					denominator += 1.0 / d;	// sum
					
				}
					
				if (denominator!= 0) {	// to prevent division by 0 once again
					
					geoRaster2D.getData().setValue(r, c, numerator / denominator);	// value of a cell is calculated value
				}
				
				else {	// if division by 0
					
					geoRaster2D.getData().setValue(r, c, 9999);	// value of a cell is default value
					
				}
				
			}
		}
	
	}
	
	// Calculating IDW with given number using a linked list
	
	public void calculateIDW(int number) {	// method calculating IDW with a given number of stations
		
		for (int r=0; r < geoRaster2D.getData().rows(); r++) {	// iterating over rows
			
			double cellcenterY = geoRaster2D.origin().getY() - (r + 0.5) * geoRaster2D.cellSize();	// calculating y of cell center for every single row
			
			for (int c=0; c < geoRaster2D.getData().columns(); c++) {	// iterating over columns
				
				double cellcenterX = (c + 0.5) * geoRaster2D.cellSize() + geoRaster2D.origin().getX();	// calculating x of cell center for every single raster cell of that row
				
				GeoPoint cellcenter = new GeoPoint (cellcenterX, cellcenterY);	// cell center
				
				double numerator = 0.0;
				double denominator = 0.0;
				
				// Sorting stations with ascending distances
				
				LinkedList<Station> stationsSorted = new LinkedList<Station>();	// new linked list
				
				stationsSorted.add(stations.get(0));	// adding the first station of the old list to the sorted one

				for (int s=1; s < stations.size(); s++) {	// iterating over stations
					
					int listLength = stationsSorted.size();	// length of sorted list before a new station is added	
					double distanceNew = Math.abs(GeoUtil.distance(cellcenter, stations.get(s).getLocation()));	// distance of one station
						
					for (int i=0; i < stationsSorted.size(); i++) {
							
					// comparison of the distance of one station with the distances of the stations in the sorted list
							
						if (distanceNew <= Math.abs(GeoUtil.distance(cellcenter, stationsSorted.get(i).getLocation()))) {
							
							if (listLength == stationsSorted.size()) {	// new station is only added one time
							stationsSorted.add(i, stations.get(s));
							}
						}
						
						// Station is added at the end of the list
						else if (distanceNew > Math.abs(GeoUtil.distance(cellcenter, stationsSorted.get(stationsSorted.size() - 1).getLocation()))) {
							stationsSorted.add(stations.get(s));
						}
						
					}				
				}
				
				// Calculating
					
				for (int z=0; z < number; z++) {	// iterating over the given number of stations of the sorted list
					
					double d1 = Math.abs(GeoUtil.distance(cellcenter, stationsSorted.get(z).getLocation()));	// distance d1 between cell center and station
					double d = Math.max(d1, 0.001 * geoRaster2D.cellSize());	// d is either d1 or 0.001 * cell size if d1 = 0 (position station = cell center) to prevent division by 0
					
					numerator += stationsSorted.get(z).getValue() / d;	// sum of values divided by distances
					denominator += 1.0 / d;	// sum
					
				}
					
				if (denominator!= 0) {	// to prevent division by 0 once again
					
					geoRaster2D.getData().setValue(r, c, numerator / denominator);	// value of a cell is calculated value
				}
				
				else {	// if division by 0
					
					geoRaster2D.getData().setValue(r, c, 9999);	// value of a cell is default value
					
				}
				
			}
			
		}
	}

	// Second solution: Calculating IDW with given number using an array
	/*
	public void calculateIDW(int number) {	// method calculating IDW with a given number of stations
		
		for (int r=0; r < geoRaster2D.getData().rows(); r++) {	// iterating over rows
			
			double cellcenterY = geoRaster2D.origin().getY() - (r + 0.5) * geoRaster2D.cellSize();	// calculating y of cell center for every single row
			
			for (int c=0; c < geoRaster2D.getData().columns(); c++) {	// iterating over columns
				
				double cellcenterX = (c + 0.5) * geoRaster2D.cellSize() + geoRaster2D.origin().getX();	// calculating x of cell center for every single raster cell of that row
				
				GeoPoint cellcenter = new GeoPoint (cellcenterX, cellcenterY);	// cell center
				
				double numerator = 0.0;
				double denominator = 0.0;
						
				double[][] array = new double [stations.size()][2];	// new 2D array
				
				for (int s=0; s < stations.size(); s++) {	// iterating over stations
					
					double d1 = Math.abs(GeoUtil.distance(cellcenter, stations.get(s).getLocation()));	// distance d1 between cell center and station
					double d = Math.max(d1, 0.001 * geoRaster2D.cellSize());	// d is either d1 or 0.001 * cell size if d1 = 0 (position station = cell center) to prevent division by 0
					
					array[s][0] = d;	// distances are added to first column of the array
					array[s][1] = stations.get(s).getValue();	// values are added to second column of the array		
				}
				
				Arrays.sort(array, (a,b) -> Double.compare(a[0],b[0]));	// Array is sorted in ascending order by comparing the distances given in the first column
				
				for (int i=0; i < number; i++) {
				
					numerator += (array[i][1]) / (array[i][0]);	// sum of values divided by distances
					denominator += 1.0 / (array[i][0]);	// sum
					
				}
					
				if (denominator!= 0) {	// to prevent division by 0 once again
					
					geoRaster2D.getData().setValue(r, c, numerator / denominator);	// value of a cell is calculated value
				}
				
				else {	// if division by 0
					
					geoRaster2D.getData().setValue(r, c, 9999);	// value of a cell is default value
					
				}
				
			}
			
		}
	}
	*/
		
	public GeoRaster2D getGeoRaster2D() {
		return geoRaster2D;
	}
	
}