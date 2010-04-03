package com.doublesunflower.core.ui.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.doublesunflower.R;

import com.doublesunflower.core.ui.sensor.SensorEvent;
import com.doublesunflower.core.ui.sensor.SensorGestureDetector;
import com.doublesunflower.core.ui.sensor.SensorGestureDetector.OnSensorGestureListener;
import com.doublesunflower.twask.usertask.UserTask;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * List based Activity screen that displays your 
 * current location and a list of each of your calendar events
 * and your distance from them.
 * 
 * @author Andrew Xu
 *
 */
public class GoogleCalendarEventsListShaker extends ListActivity 
		implements SensorListener, OnSensorGestureListener
{

  private HashMap<String, Location> eventLocations;
  
  private ArrayList<String> 	eventDistanceList;
  private ArrayList<Location> 	eventLocationList;
  private static HashMap<String, Location> updatedEventLocations;
  
  
  private Location currentLocation;
  
  private ArrayAdapter<String> aa;
  private LocationManager locationManager;
  private Criteria criteria;
  
  private boolean mIsLoading = true; 
  private MyDataSetObserver mObserver = new MyDataSetObserver();
  
  private ProgressBar mProgress;
  
  private BusinessSearchItem bi = new BusinessSearchItem ((double) 32.0, (double)-172.0, "hengx", "hengx" );
  
  
  /**
   * Observers interested in changes to the current search results
   */
  private ArrayList<WeakReference<DataSetObserver>> mObservers = 
          new ArrayList<WeakReference<DataSetObserver>>();
  

  static final private int MENU_ITEM_MAP = Menu.FIRST;
  static final private int MENU_ITEM_REFRESH = Menu.FIRST + 1;

  
  	SensorManager mSensorManager;
	SensorGestureDetector mSensorGestureDetector;
  
  /**
   * Observer used to turn the progress indicator off when the {@link CalendarEventsList} is
   * done downloading.
   */
  private class MyDataSetObserver extends DataSetObserver {
      @Override
      public void onChanged() {
          if (!mIsLoading) {
              getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                      Window.PROGRESS_VISIBILITY_OFF);
          }
      }

      @Override
      public void onInvalidated() {
      }
  }
  
  
	@Override
	public void onCreate(Bundle icicle) {
		
		  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			
		  super.onCreate(icicle);
		  setContentView(R.layout.wherearemyfriends_main);
		  
		  mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
	      mSensorGestureDetector = new SensorGestureDetector(this);
		  
		  eventDistanceList = new ArrayList<String>();
		  eventLocationList  = new ArrayList<Location>();
		  
		  updatedEventLocations = new HashMap<String, Location>();
	
		 aa = new ArrayAdapter<String>(getApplicationContext(), 
		                                android.R.layout.simple_list_item_1,
		                                eventDistanceList);
		 setListAdapter (aa);
	  	  
	  
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    
	    //getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
	       //     Window.PROGRESS_VISIBILITY_ON);
	    
	    mProgress = (ProgressBar) findViewById(R.id.progress);
	   
	    if (mIsLoading) {
            getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                    Window.PROGRESS_VISIBILITY_ON);
            addObserver(mObserver);
        }
	    
	    refreshEventLocations();
	}
	
	
	 /**
     * Adds an observer to be notified when the set of items held by this ImageManager changes.
     */
    public void addObserver(DataSetObserver observer) {
        WeakReference<DataSetObserver> obs = new WeakReference<DataSetObserver>(observer);
        mObservers.add(obs);
    }
	
    /**
     * Called when something changes in our data set. Cleans up any weak references that
     * are no longer valid along the way.
     */
    private void notifyObservers() {
        final ArrayList<WeakReference<DataSetObserver>> observers = mObservers;
        final int count = observers.size();
        for (int i = count - 1; i >= 0; i--) {
            WeakReference<DataSetObserver> weak = observers.get(i);
            DataSetObserver obs = weak.get();
            if (obs != null) {
                obs.onChanged();
            } else {
                observers.remove(i);
            }
        }
        
    }
    
	
    
    
    private void hideProgress() {
        if (mProgress.getVisibility() != View.GONE) {
            final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            mProgress.setVisibility(View.GONE);
            mProgress.startAnimation(fadeOut);
        }
    }

    private void showProgress() {
        if (mProgress.getVisibility() != View.VISIBLE) {
            final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            mProgress.setVisibility(View.VISIBLE);
            mProgress.startAnimation(fadeIn);
        }
    }
	

  /**
   * Update the current location, reevaluating the distances between each 
   * event and your current location.
   * 
   * @param location Your current physical Location
   */
	private void updateWithNewLocation(Location location) {
		// Update your current location
		currentLocation = location;
	
		  // Refresh the ArrayList of events
	    if (location != null)
	      refreshListViewHash();
	
		  // Geocode your current location to find an address.
		  String latLongString = "";
		  String addressString = "No address found";
		  
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
	
	    // Update the TextView to show your current address.
	    TextView myLocationText = (TextView)findViewById(R.id.myLocationText);
	    myLocationText.setText("Your Current Position is:\n" + latLongString + "\n" + addressString);
	}

	/**
	 * Update the ArrayList that's bound to the ListView
	 * to show the current distance of each contact to
	 * your current physical location.
	 */
   private void refreshListView() {
    
	eventDistanceList.clear();   
    eventLocationList.clear();
    
    if (eventLocations.size() > 0) {	      
    	  Iterator<String> e = eventLocations.keySet().iterator();
    	  do {
	        String name = e.next();          
	        Location location = eventLocations.get(name);
	        	        
	        
	        if ( (location.getLatitude() == 0 ) &&  (location.getLongitude() == 0) )
	        {
	        	Location eventLocalSearches = new Location("gps"); 	  
	        	//HashMap<String, Location> businessToLocationHash =new HashMap<String, Location>();
	        	
	        	
	        	eventLocalSearches = getClosestBusinessLocation(name);
	        	//businessToLocationHash = getClosestBusinessLocationHash(name);
	        	
	        	if (eventLocalSearches == null){
					Log.d ("DEBUG_NOTIFICATION", name + " bizlocs == null...............");				
				} 
				else
				{	
		        	String str = name + " (" + eventLocalSearches.getLatitude() +
		        	eventLocalSearches.getLongitude() + ")"; 
		        	eventDistanceList.add(str);           	
		            eventLocationList.add(eventLocalSearches);	
		            updatedEventLocations.put(str, eventLocalSearches);
				}
			}
	        else
	        {	        
		        int distance = (int)currentLocation.distanceTo(location);
		        String str = name + " (" + String.valueOf(distance) + "m)";
		        eventDistanceList.add(str);       		        
		        eventLocationList.add(location);
		        updatedEventLocations.put(str, location);
	        }
	      } while (e.hasNext());
    }
    
    
    //getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
      //      Window.PROGRESS_VISIBILITY_OFF);
    
    aa.notifyDataSetChanged();
  }
   
   
   private void refreshListViewHash() {
	    
		eventDistanceList.clear();   
	    eventLocationList.clear();
	    
	    if (eventLocations.size() > 0) {	      
	    	  Iterator<String> e = eventLocations.keySet().iterator();
	    	  
	    	  do {
		        String name = e.next();          
		        Location location = eventLocations.get(name);
		        	        
		        
		        if ( (location.getLatitude() == 0 ) &&  (location.getLongitude() == 0) )
		        { 
		        	
		        	new GetClosestBusinessLocationTask().execute(name);
		        
				}
		        else
		        {	        
			        int distance = (int)currentLocation.distanceTo(location);
			        String str = name + " (" + String.valueOf(distance) + "m)";
			        eventDistanceList.add(str);       		        
			        eventLocationList.add(location);
			        updatedEventLocations.put(str, location);
		        }
		      } while (e.hasNext());
	    }
	    
	    
	    //getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
	      //      Window.PROGRESS_VISIBILITY_OFF);
	    
	    mIsLoading = false;
	    notifyObservers();
	    
	    aa.notifyDataSetChanged();
	  }
   
   
   
   
   /**
    * Background task used to load the user's NSID. The task begins by showing the
    * progress bar, then loads the user NSID from the network and finally open
    * PhotostreamActivity.
    */
   private class GetClosestBusinessLocationTask extends UserTask<String, Integer, BusinessSearchItem> {
       @Override
       public void onPreExecute() {
           showProgress();
       }

       public BusinessSearchItem doInBackground(String... params) {
           //final String name = params[0].trim();
           String query = params[0];          
           if (query.length() == 0) return null;

           // Reverse geocode the postal address to get a location.
         	Location eventLocation = new Location("reverseGeocoded"); 
         	eventLocation.set(currentLocation);
         	
         	//HashMap<String, Location> businessToLocationHash = new HashMap<String, Location>();	
         	String businessName = "";
         	
         	
         	
   		ArrayList<String> locations = new ArrayList<String>();
   		
   		String origQuery = query;		
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
   			currentLocation.getLatitude() + "," + currentLocation.getLongitude() + 
   			"&sspn=0.003756,0.007403&ie=UTF8&t=h&z=17&attrid=&ei=96bqR_qmDYqwjgGSlM3wCw";
   		
   		
   		try{
   			URL url = new URL(page);
   			URLConnection urlc = url.openConnection();
   			
   			BufferedReader buffr = new BufferedReader(
   					new InputStreamReader(urlc.getInputStream()));
   			
   			String in;
   			while ((in = buffr.readLine()) != null){
   				Log.d ("DEBUG_NOTIFICATION", query + " ENTERED WHILE LOOP...............");
   				
   				if (in.contains("class=\"fn org\" dir=ltr>")){
   					Log.d ("IFPASSED__", "BIGSuCESS____+++_____+++___+++");
   					String [] locs = in.split("class=\"fn org\" dir=ltr>");
   						
   					if (locs == null)
   						Log.d ("DEBUG_NOTIFICATION", query + " locs is NULL");
   					
   					for (int x = 1; x < locs.length; x++){ //TODO: Check accuracy
   						String loc = locs[x];
   						
   						String businessNameSubstr = loc.substring (0, loc.indexOf("</a>"));
   						
   						String businessPostalAddress = "";
   						
   						businessNameSubstr = stripTags (businessNameSubstr);
   						
   						businessName = businessNameSubstr;
   						
   						businessNameSubstr = businessNameSubstr + " at";
   						
   						
   						String [] addressParts = loc.split("<span class=");
   						for (int y = 0; y < addressParts.length; y++){
   							String addrCmpnt = addressParts[y];
   							if (addrCmpnt.contains("street-address") || addrCmpnt.contains("value")){
   								businessPostalAddress = businessPostalAddress + 
   										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
   										addrCmpnt.indexOf("</span>"));
   								businessNameSubstr = businessNameSubstr + " " + 
   										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
   										addrCmpnt.indexOf("</span>"));

   							}else if (addrCmpnt.contains("locality") || addrCmpnt.contains("region")){
   								businessPostalAddress = businessPostalAddress +
   										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
   										addrCmpnt.indexOf("</span>"));
   								businessNameSubstr = businessNameSubstr + ", " + 
   										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
   										addrCmpnt.indexOf("</span>"));
   							}
   							
   						}
   						
   						locations.add(businessNameSubstr);
   						
   						
   			            //Location eventLocation = new Location("reverseGeocoded");
   			        	Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
   			        	//String address = "200 Broadway, San Diego, CA";
   			            try {
   			            	Log.d ("IFPASSED__", businessPostalAddress + " BIGSuCESS____+++_____+++___+++");
   			            	List<Address> addressResult = geocoder.getFromLocationName(
   			                		businessPostalAddress, 1);
   			                if (!addressResult.isEmpty()) {
   			                  Address resultAddress = addressResult.get(0);
   			                  eventLocation.setLatitude(resultAddress.getLatitude());
   			                  eventLocation.setLongitude(resultAddress.getLongitude());	
   			                  
   			                  
   			                  
   			                }
   			              } catch (IOException exception) {
   			               Log.d("Calendar Location Lookup Failed", exception.getMessage());
   			              }						
   					}
   					break;//class="fn org" dir=ltr>
   				}
   			}			

   		}catch (Exception e){
   			return null;
   		}
   		
   		BusinessSearchItem businessitem = new BusinessSearchItem(
         		  eventLocation.getLatitude(), eventLocation.getLongitude(),
         		  origQuery, businessName
         		  );
           
           //businessToLocationHash.put(businessName, eventLocation);
   		return businessitem;

           
       }
       
       public void onProgressUpdate(Integer... progress){
    	   setProgress(progress[0]);
    	   //setProgressPercent(progress[0]);
       }

       @Override
       public void onPostExecute(BusinessSearchItem businessitem) {
           if (businessitem == null) {
               //onError();
           } else {
               //mAdapter.refresh();
        	   bi = businessitem;
        	   
        	    Location eventLocalSearches = new Location("gps");
        	    eventLocalSearches = bi.getLocation();
	        	String businessname = bi.getBusinessName();
	        	String eventtitle = bi.getEventTitle();
	        	
	        	
	        	if (eventLocalSearches == null){
					Log.d ("DEBUG_NOTIFICATION", businessname + " bizlocs == null...............");				
				} 
				else
				{	
		        	//String str = name + " (" + eventLocalSearches.getLatitude() +
		        	//eventLocalSearches.getLongitude() + ")"; 
		        	eventDistanceList.add(eventtitle + "(" + businessname + ")");           	
		            eventLocationList.add(eventLocalSearches);	
		            updatedEventLocations.put( eventtitle + "(" + businessname + ")", eventLocalSearches);
				}
        	   
        	   
        	   aa.notifyDataSetChanged();
               hideProgress();
           }
       }
   }
   
   
   
   
  
  
  /**
   * Refresh the hash of contact names / physical locations.
   */
  public void refreshEventLocations() {
    eventLocations = EventLocationLookup.getEventLocations(getApplicationContext());
  }
    
  
  @Override
  public void onStart() {
    super.onStart();
   
    Location location = locationManager.getLastKnownLocation("gps");
    updateWithNewLocation(location);
    locationManager.requestLocationUpdates("gps", 
                                           60000, // 1min
                                           1000,  // 1km 
    	                                     locationListener);
  }
  
  @Override
  public void onPause(){
	  super.onPause();
	 
  }
  
	@Override
	protected void onResume() {
		super.onResume();

        mSensorManager.registerListener(this, 
                SensorManager.SENSOR_ACCELEROMETER | 
                SensorManager.SENSOR_MAGNETIC_FIELD | 
                SensorManager.SENSOR_ORIENTATION,
                SensorManager.SENSOR_DELAY_FASTEST);
                
	}

  
  @Override 
  public void onStop() {
    // Unregister the LocationListener to stop updating the
    // GUI when the Activity isn't visible.
    locationManager.removeUpdates(locationListener);
    
    mSensorManager.unregisterListener(this);
    super.onStop();
   
  }
    
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, MENU_ITEM_MAP, Menu.NONE, R.string.menu_item_map);
    menu.add(0, MENU_ITEM_REFRESH, Menu.NONE, R.string.menu_item_refresh);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      // Check for each known menu item
      case (MENU_ITEM_MAP):
        // Start the Map Activity
        startActivity(new Intent(this, GoogleCalendarEventsMap.class));
        return true;
      case (MENU_ITEM_REFRESH) :
        // Refresh the Friend Location hash
        refreshEventLocations();
        refreshListViewHash();
        return true;
    }
    
    // Return false if you have not handled the menu item.
    return false;
  }
  
  
  

	@Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
      
	  super.onListItemClick(l, v, position, id);
     
	   startActivity(new Intent(Intent.ACTION_VIEW,  
			   Uri.parse("http://maps.google.com/maps?f=d&saddr=" + currentLocation.getLatitude() + "," 
					   + currentLocation.getLongitude() 
					   + "&daddr=" + eventLocationList.get(position).getLatitude() + "," 
					   			   + eventLocationList.get(position).getLongitude() 
					   + "&hl=en")));  

      
  }
  
  
  
  
  private final LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      updateWithNewLocation(location);
	    }
	   
	    public void onProviderDisabled(String provider){
	      updateWithNewLocation(null);
	    }

	    public void onProviderEnabled(String provider) {}

	    public void onStatusChanged(String provider, int status, Bundle extras) {}
  };
  
  
  
  
  public BusinessSearchItem  getClosestBusinessLocationHash (String query){
	  
	  // Reverse geocode the postal address to get a location.
      	Location eventLocation = new Location("reverseGeocoded"); 
      	eventLocation.set(currentLocation);
      	
      	//HashMap<String, Location> businessToLocationHash = new HashMap<String, Location>();	
      	String businessName = "";
      	
      	
      	
		ArrayList<String> locations = new ArrayList<String>();
		
		String origQuery = query;		
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
			currentLocation.getLatitude() + "," + currentLocation.getLongitude() + 
			"&sspn=0.003756,0.007403&ie=UTF8&t=h&z=17&attrid=&ei=96bqR_qmDYqwjgGSlM3wCw";
		
		
		try{
			URL url = new URL(page);
			URLConnection urlc = url.openConnection();
			
			BufferedReader buffr = new BufferedReader(
					new InputStreamReader(urlc.getInputStream()));
			
			String in;
			while ((in = buffr.readLine()) != null){
				Log.d ("DEBUG_NOTIFICATION", query + " ENTERED WHILE LOOP...............");
				
				if (in.contains("class=\"fn org\" dir=ltr>")){
					Log.d ("IFPASSED__", "BIGSuCESS____+++_____+++___+++");
					String [] locs = in.split("class=\"fn org\" dir=ltr>");
						
					if (locs == null)
						Log.d ("DEBUG_NOTIFICATION", query + " locs is NULL");
					
					for (int x = 1; x < locs.length; x++){ //TODO: Check accuracy
						String loc = locs[x];
						
						String businessNameSubstr = loc.substring (0, loc.indexOf("</a>"));
						
						String businessPostalAddress = "";
						
						businessNameSubstr = stripTags (businessNameSubstr);
						
						businessName = businessNameSubstr;
						
						businessNameSubstr = businessNameSubstr + " at";
						
						
						String [] addressParts = loc.split("<span class=");
						for (int y = 0; y < addressParts.length; y++){
							String addrCmpnt = addressParts[y];
							if (addrCmpnt.contains("street-address") || addrCmpnt.contains("value")){
								businessPostalAddress = businessPostalAddress + 
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));
								businessNameSubstr = businessNameSubstr + " " + 
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));

							}else if (addrCmpnt.contains("locality") || addrCmpnt.contains("region")){
								businessPostalAddress = businessPostalAddress +
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));
								businessNameSubstr = businessNameSubstr + ", " + 
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));
							}
							
						}
						
						locations.add(businessNameSubstr);
						
						
			            //Location eventLocation = new Location("reverseGeocoded");
			        	Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			        	//String address = "200 Broadway, San Diego, CA";
			            try {
			            	Log.d ("IFPASSED__", businessPostalAddress + " BIGSuCESS____+++_____+++___+++");
			            	List<Address> addressResult = geocoder.getFromLocationName(
			                		businessPostalAddress, 1);
			                if (!addressResult.isEmpty()) {
			                  Address resultAddress = addressResult.get(0);
			                  eventLocation.setLatitude(resultAddress.getLatitude());
			                  eventLocation.setLongitude(resultAddress.getLongitude());	
			                  
			                  
			                  
			                }
			              } catch (IOException exception) {
			               Log.d("Calendar Location Lookup Failed", exception.getMessage());
			              }						
					}
					break;//class="fn org" dir=ltr>
				}
			}			

		}catch (Exception e){
			return null;
		}
		
		BusinessSearchItem businessitem = new BusinessSearchItem(
      		  eventLocation.getLatitude(), eventLocation.getLongitude(),
      		  origQuery, businessName
      		  );
        
        //businessToLocationHash.put(businessName, eventLocation);
		return businessitem;

	}
  
  public Location  getClosestBusinessLocation(String query){
			  
	  // Reverse geocode the postal address to get a location.
      	Location eventLocation = new Location("reverseGeocoded"); 
      	eventLocation.set(currentLocation);
      	
      	
      	
		ArrayList<String> locations = new ArrayList<String>();
		
		//String origQuery = query;		
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
			currentLocation.getLatitude() + "," + currentLocation.getLongitude() + 
			"&sspn=0.003756,0.007403&ie=UTF8&t=h&z=17&attrid=&ei=96bqR_qmDYqwjgGSlM3wCw";
		
		
		try{
			URL url = new URL(page);
			URLConnection urlc = url.openConnection();
			
			BufferedReader buffr = new BufferedReader(
					new InputStreamReader(urlc.getInputStream()));
			
			String in;
			while ((in = buffr.readLine()) != null){
				Log.d ("DEBUG_NOTIFICATION", query + " ENTERED WHILE LOOP...............");
				
				if (in.contains("class=\"fn org\" dir=ltr>")){
					Log.d ("IFPASSED__", "BIGSuCESS____+++_____+++___+++");
					String [] locs = in.split("class=\"fn org\" dir=ltr>");
						
					if (locs == null)
						Log.d ("DEBUG_NOTIFICATION", query + " locs is NULL");
					
					for (int x = 1; x < locs.length; x++){ //TODO: Check accuracy
						String loc = locs[x];
						
						String businessNameSubstr = loc.substring (0, loc.indexOf("</a>"));
						
						String businessPostalAddress = "";
						
						businessNameSubstr = stripTags (businessNameSubstr);
						businessNameSubstr = businessNameSubstr + " at";
						
						String [] addressParts = loc.split("<span class=");
						for (int y = 0; y < addressParts.length; y++){
							String addrCmpnt = addressParts[y];
							if (addrCmpnt.contains("street-address") || addrCmpnt.contains("value")){
								businessPostalAddress = businessPostalAddress + 
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));
								businessNameSubstr = businessNameSubstr + " " + 
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));

							}else if (addrCmpnt.contains("locality") || addrCmpnt.contains("region")){
								businessPostalAddress = businessPostalAddress +
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));
								businessNameSubstr = businessNameSubstr + ", " + 
										addrCmpnt.substring(addrCmpnt.indexOf('>') + 1, 
										addrCmpnt.indexOf("</span>"));
							}
							
						}
						
						locations.add(businessNameSubstr);
						
						
			            //Location eventLocation = new Location("reverseGeocoded");
			        	Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			        	//String address = "200 Broadway, San Diego, CA";
			            try {
			            	Log.d ("IFPASSED__", businessPostalAddress + " BIGSuCESS____+++_____+++___+++");
			            	List<Address> addressResult = geocoder.getFromLocationName(
			                		businessPostalAddress, 1);
			                if (!addressResult.isEmpty()) {
			                  Address resultAddress = addressResult.get(0);
			                  eventLocation.setLatitude(resultAddress.getLatitude());
			                  eventLocation.setLongitude(resultAddress.getLongitude());	
			                  
			                  //addressLocationMap.put(origQuery, eventLocation);
			      			  return eventLocation;
			                  
			                }
			              } catch (IOException exception) {
			               Log.d("Calendar Location Lookup Failed", exception.getMessage());
			              }						
					}
					break;//class="fn org" dir=ltr>
				}
			}
			
			
			

		}catch (Exception e){
			return null;
		}
		
		return eventLocation;

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
	
	public static HashMap<String, Location> getEventLocations(Context ctx)
	{
		return updatedEventLocations;
		
	}


//////////////////////////////////
    // SensorListener
    
	
	public void onSensorChanged(int sensor, float[] values) {
		
		mSensorGestureDetector.onSensorChanged(sensor, values);
		
		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
			//mCurrent.setText(values[0] + ", " + values[1] + ", " + values[2]);
		}
		
	}
    
	
	public void onAccuracyChanged(int sensor, int accuracy) {
		//mSensorGestureDetector.onAccuracyChanged(sensor, accuracy);
	}
	
	//////////////////////////////////
    // OnSensorGestureListener

	
	public boolean onIdle(SensorEvent event) {
		//mText.append("\nIdle " /*+ event.getValueLength()*/);
		return false;
	}

	
	public boolean onRotate(SensorEvent idleEvent, SensorEvent event) {
		//String rotation = getRotationString(event.getRoughRotation(idleEvent));
		//mText.append("\nRotate " + rotation /*+ " " + event.getValueLength()*/);
		//mText.append("\n" + getValueString(event.getValues()));
		//mText.append("\n" + getValueString(idleEvent.getValues()));
		return false;
	}
	
	
	public boolean onShake(SensorEvent idleEvent, SensorEvent event) {
		
		//String direction = getDirectionString(event.getRoughDirection(idleEvent));
		//mText.append("\nShake " + direction + " " + event.getValueLength());
		startActivity(new Intent(this, GoogleCalendarEventsMap.class));
		finish();
		return false;
	}
    

	
	public boolean onDrop(SensorEvent idleEvent, SensorEvent event) {
		
		//mText.append("\nDrop " + event.getValueLength());
		
		return false;
	}

	
	public boolean onCatch(SensorEvent idleEvent, SensorEvent event) {
		
		//mText.append("\nCatch " + event.getValueLength());
		
		return false;
	}
	
	public String getDirectionString(int direction) {
		switch(direction) {
		case SensorEvent.DIRECTION_UP:
			return "up";
		case SensorEvent.DIRECTION_DOWN:
			return "down";
		case SensorEvent.DIRECTION_LEFT:
			return "left";
		case SensorEvent.DIRECTION_RIGHT:
			return "right";
		case SensorEvent.DIRECTION_FORWARD:
			return "forward";
		case SensorEvent.DIRECTION_BACKWARD:
			return "backward";
		default:
			return "unknown";
		}
	}


	public String getRotationString(int direction) {
		switch(direction) {
		case SensorEvent.ROTATION_YAW_LEFT:
			return "yaw left";
		case SensorEvent.ROTATION_YAW_RIGHT:
			return "yaw right";
		case SensorEvent.ROTATION_PITCH_UP:
			return "pitch up";
		case SensorEvent.ROTATION_PITCH_DOWN:
			return "pitch down";
		case SensorEvent.ROTATION_ROLL_LEFT:
			return "roll left";
		case SensorEvent.ROTATION_ROLL_RIGHT:
			return "roll right";
		default:
			return "unknown";
		}
	}
	
	String getValueString(float[] values) {
		return "" + values[0] + ", " + values[1] + ", " + values[2];
	}
	
	   
}