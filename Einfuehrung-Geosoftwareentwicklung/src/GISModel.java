import java.util.LinkedList;

/**
 * Class managing all data structures for a very simple GIS IDW viewer.
 *                   
 */
public class GISModel implements Geometry {

    private LinkedList<Station> stationsList;
    private LinkedList<GeoImage> geoImageList;
    private LinkedList<GeoRaster2D> geoRasterList;

    /**
     * Constructor creating new GISModel containing empty lists for sensor stations, for background images and
     * for geo raster data structures.
     */
    public GISModel() {
        stationsList = new LinkedList<Station>();
        geoImageList = new LinkedList<GeoImage>();
        geoRasterList = new LinkedList<GeoRaster2D>();
    }

    /**
     * Method to add a background geo image to the end of the list of geo images.
     *
     * @param img Geo image to be added to the model
     */
    public void add(GeoImage img) {
        geoImageList.add(img);
    }

    /**
     * Method returning the list of geo images.
     *
     * @return List of geo images
     */
    public LinkedList<GeoImage> getGeoImages() {
        return geoImageList;
    }

    /**
     * Method to add a background image to the top of the list of geo images.
     *
     * @param geoImg Geo image to be added to the model
     */
    public void addFirst(GeoImage geoImg) {
        geoImageList.addFirst(geoImg);
    }

    /**
     * Method to delete the geo image at the top of the list.
     */
    public void removeFirstGeoImage() {
        geoImageList.removeFirst();
    }

    /**
     * Method to delete the last geo image in the list.
     */
    public void removeLastGeoImage() {
        geoImageList.removeLast();
    }

    /**
     * Method to remove the geo image at a given index from the list.
     *
     * @param rasterIndex Raster index for the geo image to be removed
     */
    public void removeGeoImage(int rasterIndex) {
        geoImageList.remove(rasterIndex);
    }

    /**
     * Method to add a geo raster to the end of the list of geo rasters.
     *
     * @param raster Geo raster to be added
     */
    public void add(GeoRaster2D raster) {
        geoRasterList.add(raster);
    }

    /**
     * Method returning the list of geo rasters contained in the model.
     *
     * @return List of geo rasters
     */
    public LinkedList<GeoRaster2D> getGeoRasters() {
        return geoRasterList;
    }

    /**
     * Method to add a geo raster to the top of the list of geo rasters.
     *
     * @param geoRaster Geo raster to be added
     */
    public void addFirst(GeoRaster2D geoRaster) {
        geoRasterList.addFirst(geoRaster);
    }

    /**
     * Method to delete the first geo raster from the list.
     */
    public void removeFirstGeoRaster() {
        geoRasterList.removeFirst();
    }

    /**
     * Method to delete the last geo raster from the list.
     */
    public void removeLastGeoRaster() {
        geoRasterList.removeLast();
    }

    /**
     * Method to delete the geo raster at a given index from the list.
     *
     * @param rasterIndex Raster index for the geo raster to be removed
     */
    public void removeGeoRaster(int rasterIndex) {
        geoRasterList.remove(rasterIndex);
    }

    /**
     * Method to add a sensor station at the end of the list.
     *
     * @param station Sensor station to be added
     */
    public void add(Station station) {
        stationsList.add(station);
    }

    /**
     * Method returning the list of stations in the model.
     *
     * @return List of contained stations
     */
    public LinkedList<Station> getStations() {
        return stationsList;
    }

    /**
     * Method to remove the sensor station at a given list index.
     *
     * @param stationIndex List index of the station to be removed
     */
    public void removeStation(int stationIndex) {
        stationsList.remove(stationIndex);
    }

    /**
     * Method to remove the last sensor station from the list.
     */
    public void removeLastStation() {
        stationsList.removeLast();
    }

    /**
     * Method returning the bounding box of the whole GIS model including all contained geo images and sensor stations.
     *
     * @return Overall bounding box
     */
    public BoundingBox getBoundingBox() {
        LinkedList<GeoPoint> locations = new LinkedList<GeoPoint>();
        for (Station s : stationsList)
            locations.add(s.getLocation());

        for (GeoImage gI : geoImageList) {
            locations.add(gI.getBoundingBox().getMinPoint());
            locations.add(gI.getBoundingBox().getMaxPoint());
        }

        return new BoundingBox(locations);
    }
}
