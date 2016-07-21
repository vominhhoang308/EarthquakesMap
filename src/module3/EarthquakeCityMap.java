package module3;

//Java utilities libraries
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	//Predefined Colour
	int yellow = color(255, 255, 0);
    int blue = color(0,0,255);
    int red = color(255,0,0);

	
	public void setup() {
		// Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    HashMap<String, Float> magnitudeMap= new HashMap<String, Float>();
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    for (PointFeature earthquakePoints : earthquakes){
	    	SimplePointMarker simpleMarker;
	    	simpleMarker = createMarker(earthquakePoints);
	    	map.addMarker(simpleMarker);
	    	Object magObj= earthquakePoints.getProperty("magnitude");
	    	float magLevel = Float.parseFloat(magObj.toString());
	    	if(magLevel < 4.0){
	    		simpleMarker.setColor(blue);
	    		simpleMarker.setRadius(10);
	    	}
	    	else if (4.0 <= magLevel && magLevel <= 4.9){
	    		simpleMarker.setColor(yellow);
	    		simpleMarker.setRadius(15);;
	    	}
	    	else if (magLevel >= 5.0){
	    		simpleMarker.setColor(red);
	    		simpleMarker.setRadius(20);;
	    	}
	    	else{
	    		simpleMarker.setColor(color(150,150,150));
	    		simpleMarker.setRadius(5);
	    	}
	    }
	    
	    
	    
	    //TODO: Add code here as appropriate
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		SimplePointMarker simpleMarker = new SimplePointMarker(feature.getLocation());
		return simpleMarker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		//Rectangle boxes
		fill(255,255,255);
		rect(width/45,height/4,width/6,height/2);
		
		//red key
		fill(red);
		ellipse(width/20,height/3,20,20);
		textSize(11);
		fill(0, 102, 153);
		text("Big Earthquakes", width/15, height/3);
		
		//Yellow key
		fill(yellow);
		ellipse(width/20,height/2,15,15);
		textSize(11);
		fill(0, 102, 153);
		text("Medium Earthquakes", width/15, height/2);
		
		//Blue key
		fill(blue);
		ellipse(width/20,(float) (height/1.5),10,10);
		textSize(11);
		fill(0, 102, 153);
		text("Light Earthquakes", width/15, (float) (height/1.5));
	}
}
