package com.doublesunflower.core.ui.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.doublesunflower.R;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class BusinessSearch extends ListActivity {
	public static final int MAX = 50;
	public static final int ACTIVITY_DIRECTIONS = 1;
	public static final int ACTIVITY_RATE = 2;
	EditText search_q;
	EditText location_q;
	Spinner distance_spinner;
	Context outerRef;
	String [] formatted_addrs;
	String latlong;
	String address; 
	
	
	LocationManager lm;
	

	public void onCreate (Bundle icicle) {
		super.onCreate (icicle);
		setContentView (R.layout.bs_layout);
		setListAdapter (new ArrayAdapter<String> (this, R.layout.locs_row, new ArrayList<String> ()));
		outerRef = this;
		/*Thread wait_on_response_thread = new Thread (this);
		wait_on_response_thread.start ();*/
		distance_spinner = (Spinner) findViewById (R.id.distance_spinner);
		Integer [] distances = {5, 10, 15, 25, 50, 100};
		/*for (int x = 5; x <= MAX; x = x+5) {
			distances [(x/5) - 1] = x;
		}*/
		ArrayAdapter <Integer> distanceAdapter = new ArrayAdapter <Integer> (this, android.R.layout.simple_spinner_item, distances);
		distance_spinner.setAdapter (distanceAdapter);
		search_q = (EditText) findViewById (R.id.search_string);
		location_q = (EditText) findViewById (R.id.location_string);
		
		lm = (LocationManager) getSystemService (LOCATION_SERVICE);
		
		Button search = (Button) findViewById (R.id.search_top_rated);
		search.setOnClickListener (new View.OnClickListener () {
			public void onClick (View v) {
				String search_string = search_q.getText ().toString ();
				String location_string = location_q.getText ().toString ();
				Integer selDistance = ((Integer)distance_spinner.getSelectedItem ());
				if (search_string.equals ("")) {
					Log.d ("DEBUG_NOTIFICATION", "SEARCH STRING IS NULL when search btn is pressed");
				} else {
					search_string = search_string.trim();
					String addresses = "";
					try{
						Socket query_output_input_sock = new Socket ("98.207.50.162", 9379);
						PrintWriter query_output_writer = new PrintWriter (query_output_input_sock.getOutputStream (), true);
						BufferedReader inputFromServer = new BufferedReader (new InputStreamReader (query_output_input_sock.getInputStream ()));
						query_output_writer.println ("r");
						query_output_writer.println (search_string);
						Log.d ("DEBUG_NOTIFICATION", "PRINTED SEARCH_STRING. ABOUT TO PRINT LATLONG");
						latlong = "";
						if (location_string.equals ("")) {
							address = "";
							Location current = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(),
									true)); 	
							latlong = current.getLatitude () + ", " + current.getLongitude ();
						} else {
							address = location_string;
							double [] coords = LocalSearchActivity.getCoordsFromAddress1 (location_string);
							latlong = coords[0] + ", " + coords[1];
						}
						Log.d ("DEBUG_NOTIFICATION", "PRINTING LATLONG");
						query_output_writer.println (latlong);
						Log.d ("DEBUG_NOTIFICATION", selDistance.toString ());
						query_output_writer.println (selDistance.toString ());
						//query_output_writer.close ();
						addresses = inputFromServer.readLine ();
						//Log.d ("ADDRESSES____________", addresses);
						//inputFromServer.close ();
						query_output_writer.close ();
						inputFromServer.close ();


					} catch (Exception e) {
						Log.d ("EXCEPTION THROWN", e.getLocalizedMessage ());
					}
					//Log.d ("DEBUG_NOTIFICATION - ADDRESSES", addresses);
					if (addresses.contains (">") == false) {
					
					} else {
						Log.d ("DEBUG_NOTIFICATION - ADDRESSES", addresses);
						//String [] tkay = " >JUNIPER>BOB".split (">");
						//Log.d ("ARRIVED IN MIDDLE", "SUCCESS_PENDING_WATCH_LOG");
						String [] indiv_addrs = addresses.split (">");
						//String [] formatted_addrs = new String [indiv_addrs.length - 1];
						/*for (int x = 1; x < indiv_addrs.length; x++) {
							String [] indiv_comps = indiv_addrs[x].split("<");
							formatted_addrs[x - 1] = indiv_comps[0] + "; Distance: " + Math.round(Double.parseDouble(indiv_comps[2])) + " miles;" + " Rating: " + Math.round(Double.parseDouble (indiv_comps[1]));
						}*/
						ArrayList<String> ordered_addrs = orderAddresses (indiv_addrs);
						Log.d ("DEBUG_NOTIFICATION", ordered_addrs.toString ());
						formatted_addrs = new String [ordered_addrs.size ()];
						for (int x = 0; x < ordered_addrs.size(); x++) {
							String [] indiv_comps = ordered_addrs.get(x).split("<");
							formatted_addrs[x] = indiv_comps[0] + "; Distance: " + Math.round(Double.parseDouble(indiv_comps[2])) + " miles;" + " Rating: " + Math.round(Double.parseDouble (indiv_comps[1]));
						}
						
							setListAdapter (new ArrayAdapter <String> (outerRef, R.layout.locs_row, formatted_addrs));
						

					}
				}
			}
		});
	}

	/*public void run () {

	}*/
	public static ArrayList<String> orderAddresses (String [] rawSplit) {
		ArrayList<String> formatted_addrs = new ArrayList<String> ();
		//int curFilled = 0;
		for (int x = 1; x < rawSplit.length; x++) {
			//String [] indiv_comps = rawSplit[x].split("<");
			Log.d ("DEBUG_NOTIFICATION", "INFINITE_LOOP_CHECK");
			if (x == 1) {
				formatted_addrs.add(rawSplit[x]);
				//curFilled++;
			} else {
				for (int y = 0; y < formatted_addrs.size (); y++) {
					Log.d ("DEBUG_NOTIFICATION", "INFINLOOP_CHECK_2");
					if ((Math.round(Double.parseDouble(rawSplit[x].split("<")[1])) > Math.round(Double.parseDouble(formatted_addrs.get(y).split("<")[1])))) {
						formatted_addrs.add (y, rawSplit[x]);
						break;
					} else if ((Math.round(Double.parseDouble(rawSplit[x].split("<")[1])) == Math.round(Double.parseDouble(formatted_addrs.get(y).split("<")[1]))) && (Math.round(Double.parseDouble(rawSplit[x].split("<")[2])) <= Math.round(Double.parseDouble(formatted_addrs.get(y).split("<")[2])))){
						formatted_addrs.add (y, rawSplit[x]);
						break;
					} else if (y == (formatted_addrs.size () - 1)) {
						formatted_addrs.add (rawSplit[x]);
						break;
					}


				}
			}
		}
		return formatted_addrs;
	}

	protected void onListItemClick (ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick (l, v, position, id);
		//double [] latlongofaddress = BusinessRate.getCoordsFromAddress1 (formatted_addrs[position].split(";")[0].split ("at")[1].trim ());
		//Intent i = new Intent (this, DrivingDirectionWindow.class);
		String [] coords = latlong.split (",");
		String addr = "";
		if (address.equals ("")) {
			try {
				Location loc = new Location(lm.getBestProvider(new Criteria(),
									true));
				loc.setLatitude(Double.parseDouble (coords[0].trim ()));
				loc.setLongitude(Double.parseDouble (coords[1].trim ()));
				addr = LocalSearchActivity.getCurAddress (loc);
			} catch (Exception e) {
				Log.d ("DEBUG_NOTIFICATION", "EXCEPTION THROWN" + e.getLocalizedMessage ());
			}
		} else {
			addr = address;
		}
		/*i.putExtra("start_addr", addr);
		i.putExtra("end_addr", formatted_addrs[position].split(";")[0].split ("at")[1].trim ());
		startSubActivity (i, ACTIVITY_DIRECTIONS);*/
		String page = getDirectionsPage (addr, formatted_addrs[position].split(";")[0].split ("at")[1].trim ());
		try {
			Intent i = new Intent();

			ComponentName comp = new ComponentName(
					"com.google.android.browser",
			"com.google.android.browser.BrowserActivity");
			i.setComponent(comp);
			i.setAction("android.intent.action.VIEW");
			i.addCategory("android.intent.category.BROWSABLE");
			Uri uri = Uri.parse (page);
			i.setData(uri);
			startActivityForResult(i, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		//http://maps.google.com/maps?f=d&hl=en&geocode=&saddr=1822+Nestorita+Way,+San+Jose,+CA+95124&daddr=1876+Curtner+Ave,+San+Jose,+CA+95124&sll=37.268124,-121.919079&sspn=0.006967,0.014806&ie=UTF8&t=h&z=15


	}
	public static String getDirectionsPage (String start_addr, String end_addr) {
		String form_start = "";
		String form_end = "";
		String [] start_comps = start_addr.split (" ");
		for (int x = 0; x < start_comps.length; x++) {
			if (x == 0) {
				form_start = form_start + start_comps[x];
			} else {
				form_start = form_start + "+" + start_comps[x];
			}
		}
		String [] end_comps = end_addr.split (" ");
		for (int x = 0; x < end_comps.length; x++) {
			if (x == 0) {
				form_end = form_end + end_comps[x];
			} else {
				form_end = form_end + "+" + end_comps[x];
			}
		}
		String pageReq = "http://maps.google.com/maps?f=d&hl=en&geocode=&saddr=" + form_start + "&daddr=" + form_end + "&sll=37.268124,-121.919079&sspn=0.006967,0.014806&ie=UTF8&t=h&z=15";
		Log. d("PAGEREQUEST", pageReq);
		return pageReq;
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			setResult (RESULT_CANCELED);
			finish ();
		}
	}
	
	public boolean onCreateOptionsMenu (Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0,"Rate a Business");
		//menu.add(0, 1, "Exit");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		Log.d ("DEBUG_NOTIFICATION", "ONMENITEMSELECTED");
	    switch (item.getItemId()) {
	    case 0:
	        Intent i = new Intent (this, LocalSearchActivity.class);
	        startActivityForResult (i, ACTIVITY_RATE); break;
	        
	    case 1:
	    	Log.d ("DEBUG_NOTIFICATION", "CASE1");
	        //setResult (RESULT_CANCELED, null, null); 
	        finish ();
	        break;
	    }
	    return true;
	}
}
