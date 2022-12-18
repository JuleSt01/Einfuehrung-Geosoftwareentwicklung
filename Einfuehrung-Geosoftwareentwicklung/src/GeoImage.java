import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Class providing wrapper data structure for a geo image.
 *                   
 */
public class GeoImage implements Geometry {

    private BufferedImage img;
    private String filePath;
    private GeoPoint originUL;
    private double cellsizeX;
    private double cellsizeY;

    /**
     * Constructor for a geo image based on an existing file.
     *
     * @param originUL  Location of the upper left corner of the image in geo coordinates
     * @param cellsizeX Raster cell size for the major axis (x) in meters
     * @param cellsizeY Raster cell size for the minor axis (y) in meters
     * @param filePath  Path to the file in the file system
     */
    public GeoImage(GeoPoint originUL, double cellsizeX, double cellsizeY, String filePath) {
        this.filePath = filePath;
        this.originUL = originUL;
        this.cellsizeX = cellsizeX;
        this.cellsizeY = cellsizeY;
        
        try {
            BufferedImage readImg = ImageIO.read(new File(filePath));
            img = readImg;
        } catch (IOException e) {
            System.out.println("Loading image failed: " + e.getMessage());
        }
    }

    /**
     * Constructor for a geo image based on a given BufferedImage.
     *
     * @param originUL  Location of the upper left corner of the image in geo coordinates
     * @param cellsizeX Raster cell size for the major axis (x) in meters
     * @param cellsizeY Raster cell size for the minor axis (y) in meters
     * @param geoImage  Pre-created image
     */
    public GeoImage(GeoPoint originUL, double cellsizeX, double cellsizeY, BufferedImage geoImage) {
        this.filePath = null;
        this.originUL = originUL;
        this.cellsizeX = cellsizeX;
        this.cellsizeY = cellsizeY;
        this.img = geoImage;

    }

    /**
     * Constructor for a geo image based on a given two dimensional geo raster.
     *
     * @param geoRaster Pre-created geo raster
     */
    public GeoImage(GeoRaster2D geoRaster) {
        this.filePath = null;
        this.originUL = geoRaster.origin();
        this.cellsizeX = geoRaster.cellSize();
        this.cellsizeY = geoRaster.cellSize();

        // Get highest and lowest values to determine how to stretch it to 255 values
        double valueMin = Double.POSITIVE_INFINITY;
        double valueMax = Double.NEGATIVE_INFINITY;
        for (int r = 0; r < geoRaster.getData().rows(); r++) {
            for (int c = 0; c < geoRaster.getData().columns(); c++) {
                if (valueMin > geoRaster.getData().getValue(r, c))
                    valueMin = geoRaster.getData().getValue(r, c);
                if (valueMax < geoRaster.getData().getValue(r, c))
                    valueMax = geoRaster.getData().getValue(r, c);
            }
        }

        double conversionFactor = 255 / Math.abs(valueMax - valueMin);

        img = new BufferedImage(geoRaster.getData().columns(), geoRaster.getData().rows(), BufferedImage.TYPE_INT_RGB);

        // convert data to RGB value using a gray scale
        // assuming that raster values are between 0-255 !!
        for (int r = 0; r < geoRaster.getData().rows(); r++) {
            for (int c = 0; c < geoRaster.getData().columns(); c++) {
                //create value between 0 and 255
                int value = (int) ((geoRaster.getData().getValue(r, c) - valueMin) * conversionFactor);
                //create color red (high temp) and blue (low temp)
                Color rgbColor = new Color(value, 0, 255 - value);
                img.setRGB(c, r, rgbColor.getRGB());
            }
        }
    }

    /**
     * Method to return upper left point (origin) in AWT geometry (required for affine transformation).
     *
     * @return Upper left point of the image in AWT coordinates
     */
    public Point2D.Double getUpperLeft() {
        return new Point2D.Double(originUL.getX(), originUL.getY());
    }

    /**
     * Method to return lower right point in AWT geometry (required for affine transformation).
     *
     * @return Lower right point of the image in AWT coordinates
     */
    public Point2D.Double getLowerRight() {
        return new Point2D.Double(originUL.getX() + (img.getWidth() * cellsizeX),
                originUL.getY() - (img.getHeight() * cellsizeY));
    }

    /**
     * Method to return wrapped buffered image.
     *
     * @return Wrapped image
     */
    public BufferedImage getImg() {
        return img;
    }

    /**
     * Method to return the bounding box of the geo image.
     *
     * @return Bounding box of the geo image
     */
    public BoundingBox getBoundingBox() {
        LinkedList<GeoPoint> locations = new LinkedList<GeoPoint>();
        locations.add(new GeoPoint(getUpperLeft().getX(), getUpperLeft().getY()));
        locations.add(new GeoPoint(getLowerRight().getX(), getLowerRight().getY()));
        return new BoundingBox(locations);
    }
}
