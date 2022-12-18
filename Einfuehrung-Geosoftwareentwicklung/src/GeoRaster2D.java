/**
 * Class providing a two dimensional geo raster data structure.
 *                   
 */
public class GeoRaster2D implements GeoField2D {

    private RasterData2D data;
    private GeoPoint originUL;
    private double cellSize;
    
    /**
     * Method to request the underlying/wrapped two dimensional non-spatial raster data.
     *
     * @return Non-spatial raster data
     */
    public RasterData2D getData() {
        return data;
    }

    /**
     * Constructor requiring the real world coordinates of the  raster origin (upper left corner in an assumed
     * geo-coordinate system without rotation (= parallel to coordinate axes of the raster) in that system), an equidistant cell size
     * and the number of rows and columns.
     *
     * @param originUL GeoPoint representing the origin (upper left corner!)
     * @param cellSize Equidistant cell size in meters
     * @param rows     Number of rows
     * @param cols     Number of columns
     */
    public GeoRaster2D(GeoPoint originUL, double cellSize, int rows, int cols) {
        this.cellSize = cellSize;
        this.originUL = new GeoPoint(originUL.getX(), originUL.getY());
        data = new RasterData2D(rows, cols);
    }

    /**
   	 * Constructor requiring the real world coordinates of the  raster origin (upper left corner in an assumed
     * geo-coordinate system without rotation (= parallel to coordinate axes of the raster) in that system), an equidistant cell size
     * and the a predefined non-spatial raster data structure.
     *
     * @param originUL GeoPoint representing the origin (upper left corner!)
     * @param cellSize Equidistant cell size in meters
     * @param data     Predefined non spatial raster data structure
     */
    public GeoRaster2D(GeoPoint originUL, double cellSize, RasterData2D data) {
        this.cellSize = cellSize;
        this.originUL = new GeoPoint(originUL.getX(), originUL.getY());
        this.data = data; // uses the reference
    }

    /**
     * Method returning the cell size of the geo raster.
     *
     * @return Raster cell size in meters
     */
    public double cellSize() {
        return cellSize;
    }

    /**
     * Method returning the origin upper left point of the raster with coordinates of the underlying coordinate system.
     *
     * @return Origin of the underlying coordinate system
     */
    public GeoPoint origin() {
        return originUL;
    }

    /**
     * Method returning the raster cell value at a given location using a very simple nearest neighbor approach.
     *
     * @param p Location to request value from
     * @return Value at the requested location.
     */
    public double getValue(GeoPoint p) {
        int row = (int) ((originUL.getY() - p.getY()) / cellSize);
        int col = (int) ((p.getX() - originUL.getX()) / cellSize);
        return data.getValue(row, col);
    }
    
    /**
     * This Method is to be implemented in Exercise 5
     * Checks if a given geopoint is within a given cell of the raster
     * @param r cell row
     * @param c cell column
     * @param p geopoint that is checked
     * @return true, if the geopoint is within the given rastercell 
     */   
    public boolean isInCell(int r, int c, GeoPoint p){
		if (( r== (int) ( (originUL.getY()-p.getY()) / cellSize) ) &&
		    ( c== (int) ( (p.getX() - originUL.getX()) / cellSize))){
			return true;
			
		}
		else return false;
    }
}
