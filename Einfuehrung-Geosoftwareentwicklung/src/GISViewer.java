import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.event.*;
import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * View class to paint the contents of the GIS model.
 *                   
 */
public class GISViewer extends JPanel {

    private AffineTransform af;
    private GISModel gisModel;
    private boolean active;	// Boolean to "activate" or "deactivate" certain methods

    /**
     * Constructor to create viewer from given GIS model.
     *
     * @param model Underlying GIS model
     */
    public GISViewer(GISModel model) {
        this.gisModel = model;
        this.af = new AffineTransform();
        
        active = false;
        this.addMouseListener(new GISMouseListener());	// GISMouseListener is added to viewer
    }

    /**
     * Method to update transformation. Please find conceptual background in the lecture slides.
     */
    private void updateTransformation() {

        af.setToIdentity();

        BoundingBox bb = gisModel.getBoundingBox();

        double minX = bb.getMinPoint().getX();
        double minY = bb.getMinPoint().getY();
        double maxX = bb.getMaxPoint().getX();
        double maxY = bb.getMaxPoint().getY();


        double dx = maxX - minX;
        double dy = maxY - minY;

        double scaleX = getWidth() / dx;
        double scaleY = getHeight() / dy;

        if (scaleX < scaleY)
            af.scale(scaleX, -scaleX);
        else
            af.scale(scaleY, -scaleY);
        af.translate(-minX, -maxY);
    }

    /**
     * Method to paint a geo image onto the GIS viewer.
     *
     * @param geoImage Geo image to be painted
     * @param g        Graphics object to paint geo image upon
     */
    public void paintGeoImage(GeoImage geoImage, Graphics2D g) {

        // Create corner points for geo image for affine transformation.
        Point2D.Double ulImgGeoPoint = geoImage.getUpperLeft();
        Point2D.Double lrImgGeoPoint = geoImage.getLowerRight();
        Point2D.Double ulImgDrawingPoint = new Point2D.Double();
        Point2D.Double lrImgDrawingPoint = new Point2D.Double();

        // Transform geo points into raster points.
        af.transform(ulImgGeoPoint, ulImgDrawingPoint);
        af.transform(lrImgGeoPoint, lrImgDrawingPoint);

        // Create new transformation an transform image.
        AffineTransform imgTransformer = new AffineTransform();
        imgTransformer.translate(ulImgDrawingPoint.getX(), ulImgDrawingPoint.getY());
        imgTransformer.scale((lrImgDrawingPoint.getX() - ulImgDrawingPoint.getX()) / (double) geoImage.getImg().getWidth(),
        (lrImgDrawingPoint.getY() - ulImgDrawingPoint.getY()) / (double) geoImage.getImg().getHeight());

        // Draw image onto the GIS viewer.
        g.drawImage(geoImage.getImg(), imgTransformer, this);
        
    }

    /**
     * Paint GIS viewer onto graphics element. To be implemented by you.
     *
     * @param g Graphics element to be painted upon.
     */
    public void paintComponent(Graphics g) {

        // Invoke paintComponent from the super class (JPanel).
        super.paintComponent(g);

        // Update transformation to take a potential new image size into account.
        updateTransformation();

        Graphics2D g2D = (Graphics2D) g;

        // Paint all raster from the list of raster from the GIS model.
        
		/* Your input is required here */
        
        for (int s=0; s < gisModel.getGeoRasters().size(); s++) {	// For-loop to paint every single geo raster
        	
        	GeoImage img = new GeoImage(gisModel.getGeoRasters().get(s));	// Creating a geo image out of a geo raster
        	paintGeoImage(img, g2D);	// Paint geo image
        	
        }  
        
        // Paint all images from the list of geo images from the GIS model.

   		/* Your input is required here */
        
        for (int s=0; s < gisModel.getGeoImages().size(); s++) {	// For-loop to paint every single geo image

        	GeoImage img = gisModel.getGeoImages().get(s);	// New geo image
        	paintGeoImage(img, g2D);	// Paint geo image
        	
        }

        // Paint Points including label from the list of stations from the GIS model.
   		
		/* Your input is required here */
        
        for (int s=0; s < gisModel.getStations().size(); s++) {	// For-loop to paint every single station
        	
        	Point2D.Double stationPoint = new Point2D.Double(gisModel.getStations().get(s).getLocation().getX(), gisModel.getStations().get(s).getLocation().getY());	// New point for affine transformation = geo coordinates of the station
        	Point2D.Double drawingPoint = new Point2D.Double();	// New point for affine transformation
        	
        	af.transform(stationPoint, drawingPoint);	// Transform geo points into raster points
        	
        	// A square with side length a: Its upper left corner has the coordinates x and y. To make x and y the center of the square it has to be translated by -0.5a in both directions X and Y
        	// A circle with radius r is painted inside a square: Its upper left corner has the coordinates x and y. To make x and y the center of the circle it has to be translated by -0.5r in both directions X and Y
        	
        	g.setColor(Color.WHITE);
        	g.fillOval((int) drawingPoint.getX()-6, (int) drawingPoint.getY()-6, 12, 12);	// White, filled circle with radius 12
        	
        	g.setColor(Color.RED);
        	g.drawOval((int) drawingPoint.getX()-6, (int) drawingPoint.getY()-6, 12, 12);	// Red circle with radius 12
        	g.fillRect((int) drawingPoint.getX()-3, (int) drawingPoint.getY()-3, 6, 6);	// Red square with side length 6
        	
        	g.setColor(Color.BLACK);
            g.drawString(gisModel.getStations().get(s).getName(), (int) drawingPoint.getX()+12, (int) drawingPoint.getY()+10);	// Painting the name of the station
 
        }
    }

    public class GISMouseListener extends MouseAdapter {	// Inner class GISMouseListener because it is specific to GISViewer
    	
    	public void mousePressed (MouseEvent e) {	// Mouse is pressed
    		
    		if (active == true) {	// If method is "active"
    		
    		// Coordinates of new station
    			
    		Point2D digitalP = new Point2D.Double (e.getX(), e.getY());	// Screen coordinates
    		Point2D invP = new Point2D.Double();	// Real world coordinates
    		
    		try {
    			af.inverseTransform(digitalP, invP);	// Transform screen coordinates into real world coordinates
    		}
    		catch (Exception NoninvertibleTransformException) {	// Exception
    			System.out.print("Transformation failed");
    		}
    		
    		GeoPoint location = new GeoPoint(invP.getX(), invP.getY());	// Location of the new station in real world coordinates
    		
    		// Pop up window to add name and value of the new station
    		
    		JTextField nameF = new JTextField();
    		JTextField valueF = new JTextField();
    		
    		Object[] message = {"Name:", nameF, "Value", valueF};	// Text and fields of the pop up window
    		
    		ImageIcon icon = new ImageIcon ("./images/thermometer.png");	// Icon of the pop up window
    		
    		int option = JOptionPane.showConfirmDialog(null, message, "Create new Station", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
    		
    		if (option == JOptionPane.OK_OPTION) {	// If "Ok" is pressed by user
    				
    			if (!(nameF.getText().equals("")) && !(valueF.getText().equals(""))) {	// If name and value have been added by user
    				
    				String name = nameF.getText();	// Name = added name
    			
    				try {
    					double value = Double.valueOf(valueF.getText());	// Transform added value (String) into a Double value
    					gisModel.add(new Station(location.getX(), location.getY(), name, value));	// Adding new station to model
    					repaint();	// Repaint GISViewer
    				}
        		
    				catch (Exception NumberFormatException) {
    					System.out.println("Caught NumberFormatException: Invalid value!");	// Transformation String - Double failed
    				}
        		
    			}
    			
    			if (nameF.getText().equals("")) {	// No name is added
    				
    				System.out.println("Please add name!");
    			}
    			
    			if (valueF.getText().equals("")) {	// No value is added
    				
    				System.out.println("Please add value!");
    			}
    		}  
    		
    		active = false;	// Deactivating so this method has to be activated again by clicking on "Create Station"
    		
    	}
    	}
    	
    }

    public void setActive(boolean activeIn) {	// Method to change boolean "active"
    	
    	active = activeIn;	
    }
}