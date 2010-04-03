package com.doublesunflower.core.ui.calendar;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.net.Uri;
import android.util.Log;


/**
 * Static class used to find the location of each calendar event
 * in the Calendar Event Provider by reverse-geocoding their
 * event location address.
 * 
 * @author Andrew Xu
 *
 */
public class EventLocationLookup {
 
  /**
   * Calendar event list query result cursor.
   */
  public static Cursor cursor;
  
  /**
   * Return a hash of calendar events and the
   * reverse-geocoded location of their event locations.
   * 
   * @param context Calling application's context
   * @return Hash of event titles with their physical locations
   */
  public static HashMap<String, Location> getEventLocations(Context context) {
    
	HashMap<String, Location> result = new HashMap<String, Location>();

	 Uri uri = Uri.parse("content://calendar/events");	
	 
	 //get all of the future events later than current time. "?" is the wildcard in SQLite
	 cursor = context.getContentResolver().query(uri, null, "dtstart > ? AND dtend < ?", 
			 new String[] { Long.toString(System.currentTimeMillis()), 
			 Long.toString(System.currentTimeMillis() + 24*3600*1000L) },
			 null);
	 
	  if (cursor.moveToFirst()) {

	        String title; 
	        String address;
	         
	        int titleColumn = cursor.getColumnIndex("title"); 
	        int addressColumn = cursor.getColumnIndex("eventlocation");
	    
	        do {
	            // Get the field values
	            title = cursor.getString(titleColumn);
	            address = cursor.getString(addressColumn);
	            
	         // Reverse geocode the postal address to get a location.
	            Location eventLocation = new Location("reverseGeocoded");
	            
	            if (address != null) {
	              Geocoder geocoder = new Geocoder(context, Locale.getDefault());
	              try {
	                List<Address> addressResult = geocoder.getFromLocationName(address, 1);
	                if (!addressResult.isEmpty()) {
	                  Address resultAddress = addressResult.get(0);
	                  eventLocation.setLatitude(resultAddress.getLatitude());
	                  eventLocation.setLongitude(resultAddress.getLongitude());
	                  
	                  
	  	            // Populate the result hash
	  	            result.put(title, eventLocation);
	                  
	                  
	                }
	              } catch (IOException e) {
	               Log.d("Calendar Location Lookup Failed", e.getMessage());
	              }
	            } 
	            else
	            {
	           
	               eventLocation.setLatitude(0);
	               eventLocation.setLongitude(0);
	               
	               result.put(title, eventLocation); 
	            
	            }
	            	        
	     
	        } while (cursor.moveToNext());

	    }
	 
    // Cleanup the cursor.
    cursor.close();
    return result;
  }
}