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

public class GoogleMailSyncActivity extends Activity {
	
	private static final String TAG = "GoogleCalendarSyncActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {

    	super.onCreate(icicle);	
      
    	doSync();
    	getMailEvent();
    }
    
    
    protected void doSync(){
		try{		
			   Uri uri = Uri.parse("content://gmail-ls/messages/");
	            ContentResolver cr = getContentResolver();

	            ContentValues values = new ContentValues();
	            values.put("subject", "new_gmailsync_activity");
	            values.put("messageid", 1);
	            
	            
	            
	            
	            

	            Uri result = cr.insert(uri, values);
	            if(result != null)
	            	Log.e("twask", "Error creating gmail msg!" + result.toString());
	            else
	                Log.e("twask", "Error creating gmail msg!");
	       
			
			
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
	}
    
    protected void getMailEvent(){
		try{		
			    Uri uri = Uri.parse("content://gmail-ls/messages/");	
			    
			    //Uri myEvent = Uri.withAppendedPath(uri, "1");
			    Cursor cur = managedQuery(uri, null, null, 
			    		null, null);
			    
			    if (cur.moveToFirst()) {

			        String subject; 
			       // String address;
			         
			        int subjectColumn = cur.getColumnIndex("subject"); 
			        //int addressColumn = cur.getColumnIndex("eventlocation");
			        //int phoneColumn = cur.getColumnIndex(People.NUMBER);
			        //String imagePath; 
			    
			        do {
			            // Get the field values
			            subject = cur.getString(subjectColumn);
			           // address = cur.getString(addressColumn);
			            //phoneNumber = cur.getString(phoneColumn);
			           
			            
			            Toast.makeText(getBaseContext(), "Subject is : " + subject, Toast.LENGTH_LONG).show();
			            //Toast.makeText(getBaseContext(), "EventLocation is : " + address, Toast.LENGTH_LONG).show();

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