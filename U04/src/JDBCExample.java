import java.sql.*;

public class JDBCExample  {
	/*
     * Geosoftware 2020 Aufgabe 8-1
	 *
     */
	static Connection geoDBConnection;
	public static void main(String[] args) {
		// Connect to database 'java_exercise' (PostgreSQL) with a JDBC driver
		try {
			//Load PostgreSQL driver
			Class.forName("org.postgresql.Driver");
			geoDBConnection = DriverManager.getConnection("jdbc:postgresql://geoinf-lehre.geo.tu-dresden.de:5432/java_exercise", "java", "java14");
			// Create an Object for the SQL query
			Statement stmt = geoDBConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Build a simple SQL statement
			ResultSet result = stmt.executeQuery("SELECT * FROM ex7.stations");
			
			while (result.next()) {
				
				int sid = result.getInt("sid");
				String stationname = result.getString("stationname");
				double x = result.getDouble("east");
				double y = result.getDouble("north");
				double value = result.getDouble("TEMP_value");
				
				System.out.println("Station: "+ sid +", "+ stationname +", "+ x +", "+ y +", "+ value +"");
			}
			
			geoDBConnection.close(); // Finally close the DB
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}