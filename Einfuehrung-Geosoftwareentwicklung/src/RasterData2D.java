/**
 * Class implementing a plain and non spatial raster data structure. The origin of the cell structure is in the top
 * left corner (resembling an AWT image raster).
 */
public class RasterData2D {
    private double[][] values_;
    private double defaultValue_ = 0;

    /**
     * Method to initialize each raster cell with a given value.
     *                   
     * @param rows Number of rows
     * @param columns Number of columns
     * @param value Default value to be given to each raster cell
     */
    private void init(int rows, int columns, double value) {
        values_ = new double[rows][columns];
        for (int r = 0; r < values_.length; r++) {
            for (int c = 0; c < values_[r].length; c++) {
                values_[r][c] = value;
            }
        }
    }

    /**
     * Constructor to create a raster with a "0" in each cell.
     *
     * @param rows Number of rows
     * @param columns Number of columns
     */
    public RasterData2D(int rows, int columns) {
        this.init(rows, columns, defaultValue_);
    }

    /**
     * Constructor to create a raster with a given value in each cell.
     *
     * @param rows Number of rows
     * @param columns Number of columns
     * @param defaultValue Default value to be given to each cell
     */
    public RasterData2D(int rows, int columns, double defaultValue) {
        this.init(rows, columns, defaultValue);
        defaultValue_ = defaultValue;
    }

    /**
     * Method returning the number of raster columns.
     *
     * @return Number of columns
     */
    public int columns() {
        return values_[values_.length - 1].length;
    }

    /**
     * Method returning the number of raster rows.
     *
     * @return Number of rows
     */
    public int rows() {
        return values_.length;
    }

    /**
     * Method returning the value at a given raster cell index.
     *
     * @param row Row index
     * @param column Column index
     * @return Value at the given raster cell index
     */
    public double getValue(int row, int column) {
        assert row < values_.length;
        assert column < values_[row].length;
        return values_[row][column];
    }

    /**
     * Method to set a new value at a given raster cell index.
     *
     * @param row Row index
     * @param column Column index
     * @param value New value to be set to raster cell
     */
    public void setValue(int row, int column, double value) {
        values_[row][column] = value;
    }
}
