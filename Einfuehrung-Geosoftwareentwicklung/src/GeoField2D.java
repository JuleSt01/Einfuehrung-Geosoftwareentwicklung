/**
 * Interface for a two dimensional field data structure.
 */
public interface GeoField2D {
    /**
     * Method to request field value at a given GeoPoint.
     *                   
     * @param p Location to request value from
     * @return Field value
     */
    public double getValue(GeoPoint p);

}