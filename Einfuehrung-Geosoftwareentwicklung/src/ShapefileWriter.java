import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
//import javax.measure.Unit.*;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ShapefileWriter  {
	/*
     * Geosoftware 2020 Aufgabe 8-2
	 *
     */
	public void createStationsShapeFile(String fileName, Iterator<Station> pIterator) throws Exception {
		
		/*
         * Create FeatureType to describe the data schema of the shapefile
         * This needs to be adjusted to the attributes of the stations
		 *
		 * !!! The string parameter that defines the attributes is comma separated list WITHOUT space !!!
		 * Die Liste der Attribute wird nur richtig interpretiert wenn sie keine Leerzeichen enthält
         */
		
		final SimpleFeatureType POINTTYPE = DataUtilities.createType("Points", "the_geom:Point:srid:4326," + "name:String," + "value:Double");
		
		System.out.println("TYPE:" + POINTTYPE);
		
		
		// Create feature List in which all stations are stored
		List<SimpleFeature> features = new ArrayList<SimpleFeature>();
		
		
		// Necessary to create geometry attribute for each feature
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(POINTTYPE);

		// Iterate through all points from pointlayer
		while (pIterator.hasNext()) { 
			Station currentStation = pIterator.next();
			
			// Create point by coordinates of the current point. Attention: This point is NOT of type Point you've created in the exercises!
			Point point = geometryFactory.createPoint(new Coordinate(currentStation.getLocation().getX(), currentStation.getLocation().getY()));
			featureBuilder.add(point);
			
			// Name and value are added as attributes
			featureBuilder.add(currentStation.getName());
			featureBuilder.add(currentStation.getValue());

			// Create feature from point
			SimpleFeature feature = featureBuilder.buildFeature(null);
			
			// Add the point to the feature list
			features.add(feature);
		}
		
		// Create output file (Pathname)
		File newFile = new File(fileName);
		
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		
		
		Map<String, Serializable> params = new HashMap<>();
		 
		params.put("url", newFile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);
		
		
		ShapefileDataStore newDataStore= null;
		
		
		newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		newDataStore.createSchema(POINTTYPE);
		
//		CRS definition does not work with the current geotools library version 21.1
//		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:31468");
//		newDataStore.forceSchemaCRS(sourceCRS);
		
		/*
		 * Ich habe versucht, das CRS zu wechseln. Fehlermeldung: java.lang.NoClassDefFoundError: si/uom/SI
		 * Wenn ich die Fehlermeldung richtig deute, geht es dabei um eine .jar für die SI-Einheiten, die unter Referenced Libraries noch hinzugefügt werden muss?
		 * Vielleicht könnten Sie mir sagen, wo der Fehler liegt?
		 * 
		 * Das Tutorial, das ich genutzt habe: https://docs.geotools.org/latest/userguide/library/referencing/crs.html
		 * EPSG in WKT (Well Known Text): https://epsg.io/31468
		 * 
		 */
		String wkt = " PROJCS [\"DHDN / 3-degree Gauss-Kruger zone 4\",GEOGCS[" + "\"DHDN\"," + "  DATUM[" + "\"Deutsches Hauptdreiecksnetz\","
		        + "    SPHEROID[\"Bessel 1841\",6377397.155,299.1528128,AUTHORITY[\"EPSG\",\"7004\"]],"
		        + "    TOWGS84[598.1,73.7,418.2,0.202,0.045,-2.455,6.7]," + "  AUTHORITY[\"EPSG\",\"6314\"]],"
		        + "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
		        + "  UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],"
		        + "  AXIS[\"Lat\",NORTH]," + "  AXIS[\"Long\",EAST],"
		        + "  AUTHORITY[\"EPSG\",\"31468\"]]]";
		
		CoordinateReferenceSystem sourceCRS = CRS.parseWKT(wkt);
		newDataStore.forceSchemaCRS(sourceCRS);
		
		
		// Write the feature collection to the shape file
		Transaction transaction = new DefaultTransaction("create");
		String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

		if (featureSource instanceof SimpleFeatureStore) {
	            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
	            SimpleFeatureCollection collection = new ListFeatureCollection(POINTTYPE, features);
	            
	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();
	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();
	            } finally {
	                transaction.close();
	            }
	            
	        } else {
	            System.out.println(typeName + " does not support read/write access");
	        }
		
	}
}