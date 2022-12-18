import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;

/**
 * Controller/view class containing the actual GIS IDW application including the main() method.
 *                   
 */
public class IDWApplicationNew {

    private JFrame frame;
    private GISModel model;

    /**
     * Constructor to create application based on given GIS model.
     *
     * @param model GIS model containing all background images, sensor stations, and geo rasters.
     */
    public IDWApplicationNew(GISModel model) {
        this.model = model;
        initialize();
        this.frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // Configure JFrame.
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 470);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and add GIS viewer to application frame.
        final GISViewer gisViewer = new GISViewer(model);
        frame.getContentPane().add(gisViewer, BorderLayout.CENTER);

        // Create and add panel containing the GUI widgets (e.g. run IDW button).
        JPanel optionPanel = new JPanel();
        frame.getContentPane().add(optionPanel, BorderLayout.SOUTH);

        // Create and set grid layout with one row and one column for the option panel.
        optionPanel.setLayout(new GridLayout(1, 1));
        
        // Create text field
    	JTextField numberF = new JTextField();
    	
		numberF.setText("Number of stations used for IDW");
    	numberF.setForeground(Color.GRAY);
    	numberF.addFocusListener(new FocusListener() {
    		
    		public void focusGained (FocusEvent e) {	// If user clicks into text field
    				numberF.setText("");
    				numberF.setForeground(Color.BLACK);
    		}
    		
    		public void focusLost(FocusEvent e) {	// If user clicks somewhere else and has not added a number of stations "Number of stationsused for IDW" appears
    			if (numberF.getText().isEmpty()) {
    				numberF.setText("Number of stations used for IDW");
    				numberF.setForeground(Color.GRAY);
    			}
    		}
    		
    	});
    	
        // Create and configure "Run IDW" button (Including action listener as inner class).
        JButton btnRunIdw = new JButton("Run IDW");
        btnRunIdw.addActionListener(new ActionListener() {
        	
            public void actionPerformed(ActionEvent e) {
                
				/* Your input is required here. You can use the following hints:
				
				1. Create new IDW instance.
                2. Use GIS model sensor stations for IDW.
                3. Create empty GeoRaster2D (in a first iteration you can use a cell size of 1 m, then switch to a 10 km raster
				   and think about the required logic to extract the number of required cells). Extract the origin from the bounding box
				   of the model.
                4. Assign geo raster to idw instance.
				5. Calculate IDW.
                6. Remove existing geo raster from model (Be careful: for the first run the geo raster in the model may be empty, try to avoid the
				   resulting NullPointerException).
				7. Add new IDW raster to model. */
            	
            	// New geo raster
            	double originX = model.getBoundingBox().getMinPoint().getX();
            	double originY = model.getBoundingBox().getMaxPoint().getY();
            	
            	GeoPoint origin = new GeoPoint(originX, originY);	// Origin of the new raster is the upper left corner of the model's bounding box
            	
            	double cellSize = 1000;	// Cell size of 1000 m
            	
            	int rows = (int) Math.ceil(model.getBoundingBox().getHeight() / (int) cellSize);	// Height divided by cell size has to be rounded up to get number of rows
            	int columns = (int) Math.ceil(model.getBoundingBox().getWidth() / (int) cellSize);	// Width divided by cell size has to be rounded up to get number of rows
            	
            	GeoRaster2D geoRaster = new GeoRaster2D(origin, cellSize, rows, columns);
            	
            	// New IDW
            	
            	if (numberF.getText().equals("Number of stations used for IDW")) {	// No number of stations added by user so that every station is used for IDW
            	
            		IDW idw = new IDW(geoRaster, model.getStations());	// Calculate IDW
            	  	
            		if (model.getGeoRasters().size() > 0) {	// Every existing geo raster is removed from model if there are any
            		
            			while (model.getGeoRasters().size() > 0) {		
            				model.removeLastGeoRaster();
            			}
            		}
            	
            		model.add(idw.getGeoRaster2D());	// New geo raster calculated with IDW is added to model
            	
            		// Repaint the viewer.
            		gisViewer.repaint();
            	}
            	
            	else {
            		
    				try {
    					
    					int nmb = Integer.valueOf(numberF.getText());	// Transform String added by user into an integer number of stations
    				}
        		
    				catch (Exception NumberFormatException) {
    					System.out.println("CauNumberFormatException: Invalid number of stations!\n");	// If transformation fails
    				}
    				
            		
            		IDW idw = new IDW(geoRaster, model.getStations(), Integer.valueOf(numberF.getText()));	// Calculate IDW
                	        	
            		if (model.getGeoRasters().size() > 0) {	// Every existing geo raster is removed from model if there are any
            		
            			while (model.getGeoRasters().size() > 0) {		
            				model.removeLastGeoRaster();
            			}
            		}
            	
            		model.add(idw.getGeoRaster2D());	// New geo raster calculated with IDW is added to model
            	
            		// Repaint the viewer.
            		gisViewer.repaint();	
            	}
            
            }});
        
        // Add "Run IDW" button and "Add number to stations of be used for IDW" to frame.
        optionPanel.add(btnRunIdw);
        optionPanel.add(numberF);
        
        // Add menu to frame.
        JMenuBar mBar = new JMenuBar();
        JMenu menu = new JMenu("Data");
        
        // Create and add menu items.
        JMenuItem iRead = new JMenuItem ("Read CSV");
        JMenuItem iCreate = new JMenuItem ("Create Station");
        JMenuItem iDatabase = new JMenuItem ("Read from Database");
        JMenuItem iShapefile = new JMenuItem ("Export to Shapefile");
        JMenuItem iExit = new JMenuItem ("Exit");
        
        menu.add(iRead);
        menu.add(iDatabase);
        menu.addSeparator();
        menu.add(iCreate);
        menu.addSeparator();
        menu.add(iShapefile);
        menu.addSeparator();
        menu.add(iExit);
        
        // Create and add Action Listeners for the menu items
        
        iRead.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
                
            	// File chooser
            	JFileChooser chooser = new JFileChooser();
            	int response = chooser.showOpenDialog(null);
            	
            	// Buffered Reader
            	BufferedReader in = null;
            		
                if (response == JFileChooser.APPROVE_OPTION) {	// If user has chosen a file
                	
                	if (chooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
                		
                		// Remove existing stations from model
                    	while (model.getStations().size() > 0) {
                    		model.removeLastStation();
                        }
                		
                    	try {
                    		
                		String path = chooser.getSelectedFile().getAbsolutePath();	// File path
                		in = new BufferedReader (new FileReader(path));	// Add file to buffered reader
                		
                		String nameStation;	// Local variables
                		double x, y;
                		double value;
                	
                		in.readLine();	// Read first line and ignore
                		String line = in.readLine(); // Read second line
	                		
                		while (line != null) {	// While there are lines left in file
	                			
                			String[] subStrings = line.split(",");	// Substrings in file are divided by ","
	                			
                			if (subStrings.length == 4) {
	                				
                				nameStation = subStrings[0];	// Name is first substring
                				x = Double.valueOf(subStrings[1]);	// ...
                				y = Double.valueOf(subStrings[2]);
                				value = Double.valueOf(subStrings[3]);
	                				
                				Station station = new Station(x, y, nameStation, value);	// Create a new station
                				model.add(station);	// Add the new station to list
                			}
	                			
                		line = in.readLine(); // Read next line
								
                		}
                
                		in.close();	// Close file
        		
                    	}
	               
                    	catch (FileNotFoundException ex) {	// Exception
            		
                    		JOptionPane.showMessageDialog(null, "Error: File not found!");
                    	}
        		
                    	catch (IOException ex) {	// Exception
	            	
                    		JOptionPane.showMessageDialog(null, "Error: Invalid file!");
                    	}
                    	
                		// Repaint viewer
                        gisViewer.repaint();
                	}
                	
                else {
                	
                	JOptionPane.showMessageDialog(null, "Error: Wrong file type! .csv only!");
                }
                
                }
             
        	}
        	
        });
        
        iCreate.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		
        		gisViewer.setActive(true);	// GisViewer's method to create stations is "activated"
        	}
        	
        });
        
        iDatabase.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		
        		DBExampleReader db = new DBExampleReader();	// New data base reader
        		
        		try {
        			db.initConnection();	// Connect
        			
            		// Remove existing stations from model
                	while (model.getStations().size() > 0) {
                		
                		model.removeLastStation();
                    }
        			
        			for (int i=0; i < db.readStations().size(); i++) {
        			
        				model.add(db.readStations().get(i));	// Add stations
        			}
        			
        			db.closeConnection();	// Close connection
        			
        		} catch (Exception ex){
        			ex.printStackTrace();
        		} finally {
        			db.closeConnection();
        		}
        		
        		gisViewer.repaint();
        	}
    	});
        
        iShapefile.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		
            	// File chooser
            	JFileChooser chooser = new JFileChooser();
            	int response = chooser.showSaveDialog(null);
            	
            	if (response == JFileChooser.APPROVE_OPTION) {	// If user has chosen a file
            		
            		if (chooser.getSelectedFile().getAbsolutePath().endsWith(".shp")) {
            			
            			try {
            			
            				String fileName = chooser.getSelectedFile().getAbsolutePath();	// Pathname
            				Iterator<Station> it = model.getStations().iterator();	// Iterator for list of stations
                		
            				ShapefileWriter shapefileWriter = new ShapefileWriter();
            				shapefileWriter.createStationsShapeFile(fileName, it);	// Create shapefile
            			
            			}
            		
            			catch (Exception ex) {
            			
            				ex.printStackTrace();
            			}
            		
            		}
            		
            		else {
            			
                    	JOptionPane.showMessageDialog(null, "Error: File name has to end with .shp!");	
            		}
            	}		
        	}
        		 		
    	});
        
        iExit.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		frame.dispose();	// Close frame
        	}
        	
        });
           
        mBar.add(menu);	// Add menu to menu bar
        frame.setJMenuBar(mBar);	// Set menu bar
    }

    /**
     * Method to start the application.
     *
     * @param args No runtime arguments required.
     * @throws IOException 
     */
    public static void main(String[] args) {

    	/*
    	int a = 1;
    	int b = 2;
    	
    	double c = (double) a/b;
    	
    	System.out.println(c);
    	*/
    	
        // Create GIS model.
        GISModel model = new GISModel();
        
        /*Add background image to model based on the following world file:
        http://egb13.net/2009/03/worldfile-calculator/ :
  		430.6622742406465
  		0.00000
  		0.00000
  		-424.79562665335135
  		4479652.787948427
  		5729936.833744343*/
    
        model.add(new GeoImage(new GeoPoint(4479652.787948427, 5729936.833744343), 430.6622742406465, 424.79562665335135, "./images/saxony_bw_transparent.png"));

        // Finally start application.
        new IDWApplicationNew(model);
    }
}