package com.doublesunflower.core.ui.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.doublesunflower.R;

import android.app.ListActivity;
import android.content.Intent;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LocalSearchActivity extends ListActivity {
	
	public static final int INFINITY = 1000000000;
	public static final int ACTIVITY_RATE = 1;
	public static final int ACTIVITY_SEARCH = 2;
	public static final int ACTIVITY_CHOOSE_TYPE = 3;
	private static final String TAG = "LocalSearchActivity";
		
	EditText et1;
	LocationManager mLocationManager;
	Button btnContinue;
	Button btnShowAll;
	TextView mTextViewAddrInf;
	ArrayList<String> bizlocs;
	
	
	
	@Override
	public void onCreate(Bundle icicle) {
		
		Log.d ("LocalSearchActivity", "ENTERING ONCREATE");
		
		super.onCreate(icicle);
		setContentView(R.layout.main_businessrate);
		
		
		bizlocs = new ArrayList<String> ();
		
		setListAdapter (new ArrayAdapter <String> (
				this, 
				android.R.layout.simple_list_item_1, 
				bizlocs));
		
		btnContinue = (Button)findViewById(R.id.continuex);
		btnShowAll = (Button)findViewById(R.id.show_full);
		btnContinue.setVisibility (View.INVISIBLE);
		btnShowAll.setVisibility(View.INVISIBLE);
		mTextViewAddrInf = (TextView) findViewById (R.id.address);
		et1 = (EditText)findViewById(R.id.tb);

		mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);


		Button go = (Button)findViewById(R.id.go);
		go.setOnClickListener(new View.OnClickListener (){
			public void onClick (View v){
				
				setListAdapter (new ArrayAdapter <String> (getBaseContext(), 
						R.layout.locs_row, new ArrayList<String> ()));
				
				bizlocs = getClosestBusinessLocation (et1.getText ().toString ());
				
				if (bizlocs == null){
					Log.d ("DEBUG_NOTIFICATION", "...........bizlocs == null...............");
					
				} else if (bizlocs.isEmpty() == true){
					Log.d ("DEBUG_NOTIFICATION", "............bizlocs is empty...............");
				}

				if (bizlocs.isEmpty () == true || bizlocs == null){
					return;
				}
				Log.d ("DEBUG_NOTIFICATION", "APPROACHING END OF METHOD (PRIMARY BUTTON HANDLER)");
				
				btnContinue.setVisibility(View.VISIBLE);
				btnShowAll.setVisibility(View.VISIBLE);
				
				mTextViewAddrInf.setText(bizlocs.get(0));
				bizlocs.remove(0); 
				
				btnShowAll.setOnClickListener(new View.OnClickListener (){
					public void onClick (View v){
						ArrayAdapter<String> additionalLocsAdapter = 
							new ArrayAdapter<String> (getBaseContext(), 
									R.layout.locs_row, 
									bizlocs);
						setListAdapter(additionalLocsAdapter);
					}
				});
				
				btnContinue.setOnClickListener(new View.OnClickListener () {
					public void onClick (View v){
						Intent i = new Intent (getBaseContext(), BusinessTypeChooser.class);
						String fullAddr = mTextViewAddrInf.getText ().toString ();
						i.putExtra("chosen_biz", fullAddr);
						String [] comps = fullAddr.split ("at");
						i.putExtra("coords", getCoordsFromAddress1 (comps[1].trim ()));
						Log.d ("DEBUG_NOTIFICATION", "++++++++++STARTING SUBACTIVITY++++++++");
						startActivityForResult (i, ACTIVITY_CHOOSE_TYPE);
					}
				});
			}
		});

	}
	

	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick (l, v, position, id);
		String t = bizlocs.get(position);
		String prev = mTextViewAddrInf.getText().toString();
		mTextViewAddrInf.setText(t);
		ArrayAdapter<String> placebo = new ArrayAdapter<String> (this, R.layout.locs_row, new ArrayList<String> ());
		setListAdapter(placebo);
		bizlocs.remove(position);
		bizlocs.add(prev);
		
	}


	public static String getCurAddress (Location l) throws Exception{
	
		if(l == null){
			Log.d("DEBUG_NOTIFICATION", "Location is NULL");
		}
	
		String longitude = l.getLongitude() + "";
		String latitude = l.getLatitude() + "";
		Log.d("BRSERVICE:", latitude + "/" + longitude);

		String webaddress = "http://geocoder.ibegin.com/geolive.php?viewx=reverse" + 
				"&latitude=" +  latitude + 
				"&longitude=" + longitude;
		Log.d("YAHOOAPIPAGEADDRESS", webaddress);
	
		URL url = new URL(webaddress);
		URLConnection urlc = url.openConnection();

		BufferedReader buffr = new BufferedReader(new InputStreamReader(
										urlc.getInputStream()));
		String st;
		String finaladdress = "";
		while((st = buffr.readLine()) != null){
			if(st.contains("<td width=")){
				int f = st.indexOf("Geocoding Results:</strong><br />");
				//f = f+6;
				int lf = st.indexOf("<br />City:");
				String addressrt = st.substring(f+33, lf);
				String [] subs = addressrt.split(":");
				addressrt = subs[1];
				addressrt = addressrt.trim();
				String[] components = addressrt.split(" ");
				String addressr = "";
				for (int x = 0; x < components.length; x++){
					addressr = addressr + " " + components[x]; //+
				}
				addressr = addressr + ",";

				int cit = st.indexOf("y:");
				int end = st.indexOf("<br />State");
				String citye = st.substring(cit+2, end);
				citye = citye.trim();
				String [] citComps = citye.split(" ");
				String city = "";
				for (int y = 0; y < citComps.length; y++){
					city = city + " " + citComps[y]; //+
				}
				city = city + ",";

				int beginState = st.indexOf("ince:");
				int endState = st.indexOf("<br />ZIP");
				String state = st.substring(beginState+5, endState);
				state = state.trim();
				int beginZip = st.indexOf("Code:");
				int endZip = st.indexOf("<br />Distance:");
				String zip = st.substring(beginZip+5, endZip);
				zip = zip.trim();
				//String address = st.substring(f, lf);
				finaladdress = finaladdress + addressr + city + " " + state; //+
				finaladdress = finaladdress.trim();
				
				Log.d("BUSINESSRATE:", finaladdress);
				break;

			}

		}
		return finaladdress;
	}

	public ArrayList<String> getClosestBusinessLocation(String query){
		
		ArrayList<String> locations = new ArrayList<String>();
		
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = mLocationManager.getBestProvider(c, true); 
		LocationProvider locationProvider = mLocationManager
				.getProvider(provider);
		Location l = mLocationManager
				.getLastKnownLocation(locationProvider.getName());
		
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
			+ formattedQuery + "&sll=" + l.getLatitude() + "," + l.getLongitude() + 
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

	public double[] getCoordsFromAddress(String addr){
		double [] coords = new double[2];
		String [] components = addr.split(" ");
		String query = "http://geocoder.us/demo_mobile.cgi?address="; //1822+Nestorita+Way%2C+San+Jose%2C+CA
		for (int x = 0; x < components.length; x++){
			String piece = components[x];
			if (piece.charAt(piece.length() - 1) == ','){
				piece = piece.replace(',', ' ');
				piece = piece.trim();
				piece = piece + "%2C";
			}
			if (x == 0){
				query = query + piece;
			}else{
				query = query + "+" + piece;
			}
		}
		try{
			URL url = new URL(query);
			URLConnection urlc = url.openConnection();
			BufferedReader buffr = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
			String in;
			while ((in = buffr.readLine()) != null){
				if (in.contains("Latitude") == true){
					in = buffr.readLine();
					String [] cmp = in.split(" ");
					coords[1] = Double.parseDouble(cmp[0]);

				}else if (in.contains("Longitude") == true){
					in = buffr.readLine();
					String [] cmp = in.split(" ");
					coords[0] = Double.parseDouble(cmp[0]);
				}
			}
		}catch(Exception e){

		}
		return coords;
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
	
	public static double [] getCoordsFromAddress1 (String add) { //formatted (Lat, Long)
		Log.d ("GETCOORDSFROMADDRESS!ADDR", add);
		double [] coords = new double [2];
		String reqStr = "";
		String [] spaceSplit = add.split (" ");
		for (int x = 0; x < spaceSplit.length; x++) {
			if (spaceSplit[x].contains ("#")) {
				continue;
			}
			if (x != 0) {
				reqStr = reqStr + "+" + spaceSplit [x];
			} else {
				reqStr = reqStr + spaceSplit [x];
			}
		}
		try {
			String httpRequest = "http://local.yahooapis.com/MapsService/V1/geocode?appid=YD-DBL8NgY_JX1msZSpomTydTXh3d6kLZLhBjtl.Q--&location=" + reqStr;
			Log.d ("PAGEADDRESSYAHOOAPI", httpRequest);
			URL geocoderPage = new URL (httpRequest);
			URLConnection geocoderConn = geocoderPage.openConnection ();
			BufferedReader buffr = new BufferedReader(new InputStreamReader(geocoderConn.getInputStream()));
			String in;
			while ((in = buffr.readLine ()) != null) {
				int i;
				if ((i = in.indexOf("<Latitude>")) != -1) {
					String tempSub = in.substring(i, in.indexOf("</Latitude>"));
					String lat = tempSub.substring(tempSub.indexOf (">") + 1);
					coords [0] = Double.parseDouble (lat);
					
				} 
				if ((i = in.indexOf("<Longitude>")) != -1) {
					String tempSub = in.substring(i, in.indexOf("</Longitude>"));
					String lon = tempSub.substring(tempSub.indexOf (">") + 1);
					coords [1] = Double.parseDouble (lon);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
		Log.d ("COORDS!", coords[0] + "");
		Log.d ("COORDS!", coords[1] + "");
		return coords;
	}

	public static double distBetweenCoords(double [] coords1, double [] coords2){
		return ((double)(Math.sqrt ((Math.pow ((coords2[1] - coords1[1]), 2) + Math.pow ((coords2[0] - coords1[0]), 2)))));
	}

	public static String stripAddressFromBusinessName (String bizname){
		String [] components = bizname.split("at");
		return (components[1].trim ());
	}
	
	public boolean onCreateOptionsMenu (Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Search for Business");
		//menu.add(0, 1, "Exit");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		Log.d ("DEBUG_NOTIFICATION", "ONMENITEMSELECTED");
	    switch (item.getItemId()) {
	    case 0:
	        Intent i = new Intent (this, BusinessSearch.class);
	        startActivityForResult (i, ACTIVITY_SEARCH); break;
	        
	    case 1:
	    	Log.d ("DEBUG_NOTIFICATION", "CASE1");
	        //setResult (RESULT_CANCELED, null, null); 
	        finish ();
	        break;
	    }
	    return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d ("DEBUG_NOTIFICATION", "ONACTRESULT+CALLED");
		if (resultCode == RESULT_CANCELED) {
			setResult (RESULT_CANCELED);
			finish ();
		}
	}

	@Override
    public void onNewIntent(final Intent newIntent)
	{
		super.onNewIntent(newIntent);
	}

}
