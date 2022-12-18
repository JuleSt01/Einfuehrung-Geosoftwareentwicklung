import java.sql.*;
import java.util.*;

public class DBExampleReader {
   
	private Connection connection;

	public void initConnection() {	// Connect
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://geoinf-lehre.geo.tu-dresden.de:5432/java_exercise", "java", "java14");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Calls query on the database for the layer of {@link Point}s and returns a filled {@link ArrayList<GeoPoint>}
	 *
	 * @return {@link ArrayList<GeoPoint>} a filled layer of points from the database
	 * 
	 */
	
	public ArrayList<GeoPoint> readPoints() {
		// Create new layer for the points
		ArrayList<GeoPoint> pointLayer = new ArrayList<GeoPoint>();
		try {
			// Create query
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = stmt.executeQuery("...");

			// Processing the results
			while (result.next()) {

				int sid = result.getInt("...");
				String name = result.getString("...");
				double x = result.getDouble("...");
				double y = result.getDouble("...");

				GeoPoint newOne = new GeoPoint(x, y);
				pointLayer.add(newOne);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pointLayer;
	}
	
	public LinkedList<Station> readStations() {
		
		LinkedList<Station> stationLayer = new LinkedList<Station>();	// New linked list
		
		try {
			
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = stmt.executeQuery("SELECT * FROM ex7.stations");	// Select all attributes from "Stations"
			
			while (result.next()) {	// Iterating over every single line
				
				int sid = result.getInt("sid");	// Column "sid"
				String name = result.getString("stationname");	// Column "stationname"
				double x = result.getDouble("east");	// Column "east"
				double y = result.getDouble("north");	// Column "north"
				double value = result.getDouble("TEMP_value");	// Column "TEMP_value"
				
				Station newOne = new Station(x, y, name, value);
				stationLayer.add(newOne);	// Add to linked list
			}		
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stationLayer;	
	}

	/*
	 * Close the database connection
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}