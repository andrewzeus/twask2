package com.doublesunflower.core.ui.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.doublesunflower.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * Map based Activity screen that shows your 
 * current location and each of your google calendar events
 * within 50km with lines connecting you to them.
 * 
 * @author Andrew Xu
 * 
 *
 */
public class GoogleCalendarEventsMap extends MapActivity {

  private MapController mapController;
  private EventLocationOverlay positionOverlay;
  
  private MapView myMapView;
  
  
  private GeoPoint myCurrentGeoPoint;
  private Location myCurrentLocation;
  private MyLocationOverlay myLocationOverlay;
 	
  static final private int MENU_ITEM_LIST = Menu.FIRST;
  static final private int MENU_ITEM_REFRESH = Menu.FIRST + 1;

	@Override
	public void onCreate(Bundle icicle) {
		
      //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	
		
	  super.onCreate(icicle);
	  setContentView(R.layout.map_layout);
	  
	  myCurrentLocation = new Location("gps");
	  
	  Double latitude = 37.422006*1E6;
	  Double longitude = -122.084095*1E6;
	  myCurrentGeoPoint = new GeoPoint(latitude.intValue(),longitude.intValue());   

	  // Get the MapView and its Controller
	  myMapView = (MapView)findViewById(R.id.myMapView);
	  mapController = myMapView.getController();
	    
	  // Configure the map display options
	  myMapView.setSatellite(false);
	  myMapView.setStreetView(false);
	           
	  // Zoom in
	  mapController.setZoom(12);
	  
	  
	  Drawable marker=getResources().getDrawable(R.drawable.icon32);
	  marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());      
    // Add my friends position overlay
	  positionOverlay = new EventLocationOverlay(getApplicationContext(), marker, myCurrentLocation);
	  List<Overlay> overlays = myMapView.getOverlays();
	  overlays.add(positionOverlay);
	
	  // Add the MyLocationOverlay
	  myLocationOverlay = new MyLocationOverlay(this, myMapView);	  
	  overlays.add(myLocationOverlay);
	  myLocationOverlay.enableMyLocation();	  
	  
     // getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
       //       Window.PROGRESS_VISIBILITY_ON);
	  

      // Refresh the hash of contact locations.
	  positionOverlay.refreshEventLocations();
	  
	  View zoomControls = myMapView.getZoomControls();
	  zoomControls.setLayoutParams(new ViewGroup.LayoutParams
			  (ViewGroup.LayoutParams.WRAP_CONTENT,
			  ViewGroup.LayoutParams.WRAP_CONTENT)); 
	  //zoomControls.setGravity(Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL); 
	  myMapView.addView (zoomControls);
	  myMapView.displayZoomControls(true); 
	}

	@Override
	public void onStart() {
		super.onStart();
	
		updateWithNewLocation(myCurrentLocation);
	
		// Update the UI using the last known locations
		//Location location = locationManager.getLastKnownLocation(provider);
		myLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
		  
			myCurrentGeoPoint = myLocationOverlay.getMyLocation();	  

    	    double latitude =  (double) myCurrentGeoPoint.getLatitudeE6() / 1E6 ;
    	    double longitude = (double) myCurrentGeoPoint.getLongitudeE6() / 1E6 ;
    	    
    	    myCurrentLocation.setLatitude(latitude);
    	    myCurrentLocation.setLongitude(longitude);
    	        	    
    	    updateWithNewLocation(myCurrentLocation);
    	    
    	    //getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                   // Window.PROGRESS_VISIBILITY_OFF);
    	}});
    
  
	}
	
	
	@Override 
	public void onResume() {
	  myLocationOverlay.enableMyLocation();
	  super.onResume();
	  //finish();
	}
	
	@Override 
	public void onStop() {
	  myLocationOverlay.disableMyLocation();
	  super.onStop();
	  //finish();
	}
	
  
  /** Update the map with a new location */
	private void updateWithNewLocation(Location location) {
    // Update the map position and overlay
	  if (location != null) {
	    // Update my location marker
	    positionOverlay.setLocation(location);
	    //myMapView.invalidate();

	    // Update the map location.
	    Double geoLat = location.getLatitude()*1E6;
	    Double geoLng = location.getLongitude()*1E6;
	    GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());

	    mapController.animateTo(point);
	  }
	  
	  // Update the text box that displays your current address
	  String latLongString = "";
	  String addressString = "No address found";
    //TextView myLocationText = (TextView)findViewById(R.id.myLocationText);

	  if (location != null) {
      double lat = location.getLatitude();
	    double lng = location.getLongitude();
	    latLongString = "Lat:" + lat + "\nLong:" + lng;

	    Geocoder gc = new Geocoder(this, Locale.getDefault());
	    try {
	      List<Address> addresses = gc.getFromLocation(lat, lng, 1);
	      StringBuilder sb = new StringBuilder();
	      if (addresses.size() > 0) {
	        Address address = addresses.get(0);

          sb.append(address.getLocality()).append("\n");
          sb.append(address.getCountryName());
	      }
	      addressString = sb.toString();
	    } catch (IOException e) {}
	  } else {
	    latLongString = "No location found";
	  }
	 // myLocationText.setText("Your Current Position is:\n" + latLongString + "\n" + addressString);
	}

  @Override
	protected boolean isRouteDisplayed() {
	  return false;
	}
    
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, MENU_ITEM_LIST, Menu.NONE, R.string.menu_item_list);
    menu.add(0, MENU_ITEM_REFRESH, Menu.NONE, R.string.menu_item_refresh);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      // Check for each known menu item
      case (MENU_ITEM_LIST):
        // Display the List View
        startActivity(new Intent(this, GoogleCalendarEventsListShaker.class));
        return true;
      case (MENU_ITEM_REFRESH) :
        // Refresh the contact location hash
        positionOverlay.refreshEventLocations();
        myMapView.invalidate(); 
        return true;
    }
    
    // Return false if you have not handled the menu item.
    return false;
  }
  
  
  
  
  
  
}