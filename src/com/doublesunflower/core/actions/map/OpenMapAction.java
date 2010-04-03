package com.doublesunflower.core.actions.map;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.util.Log;

import com.doublesunflower.core.actions.base.AbstractAction;
import com.doublesunflower.core.common.exceptions.BusinessException;
import com.google.android.maps.GeoPoint;

public class OpenMapAction extends AbstractAction {


	@Override
	public Object perform(Activity activity, Object params) throws BusinessException {
		try{
			
			/*
			Intent i = new Intent();
			i.setClassName( "com.doublesunflower", "com.doublesunflower.core.ui.maps.LockCast" );
		    activity.startActivity(i);
		    */
			
			LocationManager mLocationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
			
			Criteria c = new Criteria();
			c.setAccuracy(Criteria.ACCURACY_FINE);
			String provider = mLocationManager.getBestProvider(c, true); 
			LocationProvider locationProvider = mLocationManager
					.getProvider(provider);
			Location l = mLocationManager
					.getLastKnownLocation(locationProvider.getName());
			/*
		
			   activity.startActivity(new Intent(Intent.ACTION_VIEW,  
					   Uri.parse("http://maps.google.com/maps?f=d&saddr=" + l.getLatitude() + "," + l.getLongitude() 
							   + "&daddr=" + "San Diego, CA" + "&hl=en")));  
			
			*/
		    //i.setClassName( "com.doublesunflower", "com.doublesunflower.twask.utils.location.MockLocationService" );
		    //activity.startService(i);
			   
			   
			   // Launch the radar activity (if it is installed)
	            Intent i = new Intent("com.google.android.radar.SHOW_RADAR");
	            //GeoPoint location = mItem.getLocation();
	            i.putExtra("latitude", (float)(l.getLatitude()));
	            i.putExtra("longitude", (float)(l.getLongitude()));
	            try {
	                activity.startActivity(i);
	            } catch (ActivityNotFoundException ex) {
	                //showDialog(DIALOG_NO_RADAR);
	            }
	          
		    
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}
