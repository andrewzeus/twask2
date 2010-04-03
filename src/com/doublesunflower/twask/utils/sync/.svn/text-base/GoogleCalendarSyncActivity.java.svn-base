package com.doublesunflower.twask.utils.sync;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.doublesunflower.core.ui.util.Util;

public class GoogleCalendarSyncActivity extends Activity {
	
	private static final String TAG = "GoogleCalendarSyncActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {

    	super.onCreate(icicle);	
      
    	doSync();
    	getCalendarEvent();
    }
    
    
    protected void doSync(){
		try{		
			   Uri uri = Uri.parse("content://calendar/events");
	            ContentResolver cr = getContentResolver();

	            ContentValues values = new ContentValues();
	            values.put("title", "new_sync_activity");
	            values.put("calendar_id", 1);
	            values.put("description", "A test sync activity to Google Calendar");
	            values.put("hasAlarm", 1);
	            values.put("transparency", 0);
	            values.put("visibility", 1);
	            values.put("eventlocation", "425 West Beech street, san diego, CA");
	            
	            
	            
	            Long startDate = null;
	            startDate = System.currentTimeMillis();
	            values.put("dtstart", startDate);

	            Long deadlineDate = null;
	            deadlineDate = System.currentTimeMillis() + 24*3600*1000L;
	            values.put("dtend", deadlineDate);

	            Uri result = cr.insert(uri, values);
	            if(result != null)
	            	Log.e("twask", "Error creating calendar event!" + result.toString());
	            else
	                Log.e("twask", "Error creating calendar event!");
	       
			
			
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
	}
    
    protected void getCalendarEvent(){
		try{		
			    Uri uri = Uri.parse("content://calendar/events");	
			    
			    //Uri myEvent = Uri.withAppendedPath(uri, "1");
			    Cursor cur = managedQuery(uri, null, "dtstart" + ">?", 
			    		new String[] { Long.toString(System.currentTimeMillis()) }, null);
			    
			    if (cur.moveToFirst()) {

			        String title; 
			        String address;
			         
			        int titleColumn = cur.getColumnIndex("title"); 
			        int addressColumn = cur.getColumnIndex("eventlocation");
			        //int phoneColumn = cur.getColumnIndex(People.NUMBER);
			        //String imagePath; 
			    
			        do {
			            // Get the field values
			            title = cur.getString(titleColumn);
			            address = cur.getString(addressColumn);
			            //phoneNumber = cur.getString(phoneColumn);
			           
			            
			           // Toast.makeText(getBaseContext(), "Title is : " + title, Toast.LENGTH_LONG).show();
			           // Toast.makeText(getBaseContext(), "EventLocation is : " + address, Toast.LENGTH_LONG).show();

			        } while (cur.moveToNext());

			    }



	            //ContentResolver cr = getContentResolver();

	            //ContentValues values = new ContentValues();
	            //String title = (String) values.get("title");
	          
	            //values.put("calendar_id", 1);
	            //values.put("description", "A test sync activity to Google Calendar");
	            //values.put("hasAlarm", 1);
	            //values.put("transparency", 0);
	            //values.put("visibility", 1);
	            
	            
	           /* 
	            Long startDate = null;
	            startDate = System.currentTimeMillis();
	            values.put("dtstart", startDate);

	            Long deadlineDate = null;
	            deadlineDate = System.currentTimeMillis() + 24*3600*1000L;
	            values.put("dtend", deadlineDate);
                */
	            
	            //Uri result = cr.insert(uri, values);
	            
	           
			
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
	}
    
    

    @Override
    public void onNewIntent(final Intent newIntent)
	{
		super.onNewIntent(newIntent);
	}

}