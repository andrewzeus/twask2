package com.doublesunflower.core.ui.calendar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/**
 * Overlay class used to draw circles on the locations
 * of each calendar event in the event list, along with their 
 * name and a line connecting them to your current
 * position.
 * 
 * @author Andrew Xu
 */
public class EventLocationOverlay extends ItemizedOverlay <OverlayItem> {

  private Context context;
  private HashMap<String, Location> eventLocations;
  private Location location;
  private GeoPoint mylocationPoint;
  
  private Paint paint;
  private Paint backPaint;

  private static int markerRadius = 7;
  
  private List<OverlayItem> items=new ArrayList<OverlayItem>();  
  private Location myCurrentLocation = new Location("gps");

  
  /**
   * Create a new EventLocationOverlay to show your event's locations on a map
   * @param _context Parent application context
   */
  public EventLocationOverlay(Context _context, Drawable marker, Location l) {
    super(marker);
   
    context = _context;
    eventLocations = new HashMap<String, Location>();
    myCurrentLocation = l;
    
    Double latitude = myCurrentLocation.getLatitude()*1E6;
    Double longitude = myCurrentLocation.getLongitude()*1E6;
    
    mylocationPoint = new GeoPoint(latitude.intValue(),longitude.intValue()); 
    
    items.add(new OverlayItem(mylocationPoint,  
    		"current Point", "Current starting Point"));  
       		
    populate();  
    
    
    //refreshFriendLocations();

    // Create the paint objects
    backPaint = new Paint();
    backPaint.setARGB(200, 200, 200, 200);
    backPaint.setAntiAlias(true);
    
    paint = new Paint();
    paint.setARGB(255, 10, 10, 255);
    paint.setAntiAlias(true);
    paint.setFakeBoldText(true);    
  }
  
  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow) {	  
    // Get the map projection to convert lat/long to screen coordinates
    Projection projection = mapView.getProjection();
    
    Point lPoint = new Point();
    projection.toPixels(mylocationPoint, lPoint);
    
    // Draw the overlay
    if (shadow == false) {
      if (eventLocations.size() > 0) {
        Iterator<String> e = eventLocations.keySet().iterator();
        do {
          // Get the name and location of each contact
          String name = e.next();          
          Location location = eventLocations.get(name);
          
          // Convert the lat / long to a Geopoint
          Double latitude = location.getLatitude()*1E6;
          Double longitude = location.getLongitude()*1E6;
          GeoPoint geopoint = new GeoPoint(latitude.intValue(),longitude.intValue());

          // Ensure each contact is within 10km
          float dist = location.distanceTo(getLocation()); 
          if (dist < 50000) {
            Point point = new Point();
            projection.toPixels(geopoint, point);
            
            
            items.add(new OverlayItem(geopoint,  
            		name, name)); 
            
            
            // Draw a line connecting the contact to your current location.
            canvas.drawLine(lPoint.x, lPoint.y, point.x, point.y, paint);
            
            // Draw a marker at the contact's location.
            RectF oval = new RectF(point.x-markerRadius,
                                   point.y-markerRadius,
                                   point.x+markerRadius,
                                   point.y+markerRadius);
            
            canvas.drawOval(oval, backPaint);
            oval.inset(2, 2);
            canvas.drawOval(oval, paint);
            
            // Draw the contact's name next to their position.
            float textWidth = paint.measureText(name);
            float textHeight = paint.getTextSize();
            RectF textRect = new RectF(point.x+markerRadius, point.y-textHeight,
                                       point.x+markerRadius+8+textWidth, point.y+4);
            canvas.drawRoundRect(textRect, 3, 3, backPaint);
            canvas.drawText(name, point.x+markerRadius+4, point.y, paint);
          }
        } while (e.hasNext());
        
        populate();
      }
    }
    super.draw(canvas, mapView, shadow);
  }
	  
	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
	 return false;
	}
	
	@Override 
	protected boolean onTap (int i){
		
		Intent intent = new Intent(Intent.ACTION_VIEW,  
				   Uri.parse("http://maps.google.com/maps?f=d" + 
						   "&saddr=" + myCurrentLocation.getLatitude() + "," 
						   + myCurrentLocation.getLongitude() 
						   + "&daddr=" + (double) (items.get(i).getPoint().getLatitudeE6()/1E6) + "," 
						   		   + (double) (items.get(i).getPoint().getLongitudeE6()/1E6)	   
						   + "&hl=en"));
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);  		
		return true;		
	}
	
	
	  /** Get your current location */
	public Location getLocation() {
		return location;
	}
	
	
  /** Set your current location */
	public void setLocation(Location location) {
	  this.location = location;
		
    Double latitude = location.getLatitude()*1E6;
    Double longitude = location.getLongitude()*1E6;
    
    mylocationPoint = new GeoPoint(latitude.intValue(),longitude.intValue());      
	}  
    
	/** Refresh the locations of each of the events */
  public void refreshEventLocations() {
    //eventLocations = EventLocationLookup.getEventLocations(context);
	  eventLocations = GoogleCalendarEventsListShaker.getEventLocations(context);
  
  }

	@Override
	protected OverlayItem createItem(int i) {
		return(items.get(i)); 
	}

	@Override
	public int size() {
		return(items.size()); 
	}
	
	public ArrayList<String> getClosestBusinessLocation(String query){
		
		ArrayList<String> locations = new ArrayList<String>();
		
		query = query.trim();
		String [] queryComponents = query.split(" ");

		String formattedQuery = "";
		for (int x = 0; x < queryComponents.length; x++){
			if (x == 0){
				formattedQuery = formattedQuery + queryComponents[x];
			}else{
				formattedQuery = formattedQuery + "+" + queryComponents[x];
			}
		}
		
		String page = "http://maps.google.com/maps?f=q&hl=en&geocode=&view=text&q=" 
			+ formattedQuery + "&sll=" + 
			myCurrentLocation.getLatitude() + "," + myCurrentLocation.getLongitude() + 
			"&sspn=0.003756,0.007403&ie=UTF8&t=h&z=17&attrid=&ei=96bqR_qmDYqwjgGSlM3wCw";
		Log.d ("___PAGEADDRESS___", page);
		try{
			URL url = new URL(page);
			URLConnection urlc = url.openConnection();
			
			BufferedReader buffr = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
			Log.d ("DEBUG_NOTIFICATION", "...........BUFFERED READER PROPERLY CREATED...........");
			String in;
			while ((in = buffr.readLine()) != null){
				Log.d ("DEBUG_NOTIFICATION", "...............ENTERED WHILE LOOP...............");
				
				if (in.contains("class=\"fn org\" dir=ltr>")){
					Log.d ("IFPASSED__", "BIGSuCESS____+++_____+++___+++");
					String [] locs = in.split("class=\"fn org\" dir=ltr>");
					if (locs == null){
						Log.d ("DEBUG_NOTIFICATION", "locs is NULL");
					}
					
					for (int x = 1; x < locs.length; x++){ //TODO: Check accuracy
						String loc = locs[x];
						
						String businessNameSubstr = loc.substring (0, loc.indexOf("</a>"));
						
						businessNameSubstr = stripTags (businessNameSubstr);
						businessNameSubstr = businessNameSubstr + " at";
						String [] addressParts = loc.split("<span class=");
						
						for (int y = 0; y < addressParts.length; y++){
							String addrCmpnt = addressParts[y];
							if (addrCmpnt.contains("street-address") || addrCmpnt.contains("value")){
								businessNameSubstr = businessNameSubstr + " " + addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, addrCmpnt.indexOf("</span>"));

							}else if (addrCmpnt.contains("locality") || addrCmpnt.contains("region")){
								businessNameSubstr = businessNameSubstr + ", " + addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, addrCmpnt.indexOf("</span>"));
							}
						}
						locations.add(businessNameSubstr);
					}
					break;//class="fn org" dir=ltr>
				}
			}
			return locations;


		}catch (Exception e){
			return null;
		}
	}

	public static String stripTags(String html){
		String stripped = "";
		html = html.trim();
		String [] components = html.split("<");
	
		for (int x = 0; x < components.length; x++){
			components[x] = components[x].trim();
			String [] sectors = components[x].split(">");
	
			if (x != components.length - 1 || html.charAt(html.length() - 1) != '>'){
				stripped = stripped + sectors[(sectors.length - 1)];
			}
	
		}
		return stripped;
	}

	
}