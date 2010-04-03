/*
 * Copyright (C) 2009 Double Sunflower Holdings Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.doublesunflower.core.ui.maps;

import java.text.DecimalFormat;

import com.doublesunflower.twask.distribution.AboutActivity;
import com.doublesunflower.twask.distribution.EulaActivity;
import com.doublesunflower.twask.distribution.UpdateMenu;
//import com.doublesunflower.android.downloader.DownloaderActivity;
import com.doublesunflower.twask.utils.image.ImageList;
import com.doublesunflower.twask.utils.image.ImageManager;
import com.doublesunflower.twask.utils.location.BreadCrumbService;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.doublesunflower.R;

//import com.admob.android.ads.AdView;
//import android.view.WindowManager;

/**
 * Activity which lets the user select a search area
 *
 */
public class LockCast extends MapActivity {
	
	
    private static final String tag = "doublesunflower";
	private static final String tripFileName = "currentTrip.txt";
	private String currentTripName = "";
	private int altitudeCorrectionMeters = 20;
	
	private static final int MENU_ABOUT = Menu.FIRST + 1;
	private static final int MENU_UPDATE = Menu.FIRST + 2;
	
	//private AdView example_adview;
	
	private String halfspan = "10000";
	
	private final DecimalFormat sevenSigDigits = new DecimalFormat("0.#######");
	
    private MapView mMapView;
    private MyLocationOverlay mMyLocationOverlay;
    private ImageManager mImageManager;
    
    
    public static final int MILLION = 1000000;
    
    private final static String CUSTOM_TEXT="1.0";
    private final static String FILE_CONFIG_URL =
        "http://example.com/download.config";
    private final static String CONFIG_VERSION="1.0";
    private final static String DATA_PATH = "/sdcard/data/downloadTest";
    private final static String USER_AGENT = "MyApp Downloader";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
    	
    	/*
    	if (! DownloaderActivity.ensureDownloaded(this, CUSTOM_TEXT, FILE_CONFIG_URL,
                CONFIG_VERSION, DATA_PATH, USER_AGENT)) {
            return;
        }
        */  
    	
    	//if (!EulaActivity.checkEula(this)) {
		//	return;
		//}
    	
    	/*
    	//full screen window and no title bar
    	final Window win = getWindow(); 
    	win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    	WindowManager.LayoutParams.FLAG_FULLSCREEN); 
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  
    	*/
    	
        setContentView(R.layout.lockcast);
        
        //example_adview = (AdView) findViewById(R.id.ad);
		//example_adview.setVisibility(AdView.VISIBLE);
        /*
        Button button = (Button)findViewById(R.id.ButtonStart);
        button.setOnClickListener(mStartListener);
        button = (Button)findViewById(R.id.ButtonStop);
        button.setOnClickListener(mStopListener);
        */
        
        //Button button = (Button)findViewById(R.id.ButtonExport);
		//button.setOnClickListener(mExportListener);

        
		
		/*
		ToggleButton toggleDebug = (ToggleButton)findViewById(R.id.ToggleButtonDebug);
		toggleDebug.setOnClickListener(mToggleDebugListener);
		toggleDebug.setChecked(BreadCrumbService.isShowingDebugToast());
		*/
        
        mImageManager = ImageManager.getInstance(this);		
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        // Add the map view to the frame
        //This one has the real signing certitifcate on /andrewxuheng/Downloads/my_real_signing.keystore
        //mMapView = new MapView(this, "0FkguPkCBiXn2YM_l1XKzIiO4hpEDZKu7AAMRYg");
        //This has the map certificate for ~/.android/debug.keystore only on macbook
        mMapView = new MapView(this, "0FkguPkCBiXlP5aBOebQ0RfniajHj-5Q35ETYdw");
        frame.addView(mMapView, 
                new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
        mMyLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
            mMapView.getController().animateTo(mMyLocationOverlay.getMyLocation());
        }});
        
        mMapView.getOverlays().add(mMyLocationOverlay);
        mMapView.getController().setZoom(15);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setSatellite(false);      
        addZoomControls(frame);
        
        Button goButton = (Button) findViewById(R.id.go);
        goButton.setOnClickListener(mSearchListener);
		
        
       // initTripName();
        
        //EditText editAltitudeCorrection = (EditText)findViewById(R.id.EditTextHalfSpan);
		//editAltitudeCorrection.setText(String.valueOf(halfspan));
           
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onStop() {
        mMyLocationOverlay.disableMyLocation();
        super.onStop();
    }
    
    
   
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		/*
		menu.add(0, MENU_ABOUT, 0, R.string.about)
		  .setIcon(android.R.drawable.ic_menu_info_details) .setShortcut('0', 'a');
		
		UpdateMenu.addUpdateMenu(this, menu, 0, MENU_UPDATE, 0, R.string.update_menu);
        */
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ABOUT:
			//showAboutBox();
			return true;
			
		case MENU_UPDATE:
			//UpdateMenu.showUpdateBox(this);
			return true;
		}
		return super.onOptionsItemSelected(item);

	}
	
	private void showAboutBox() {
		startActivity(new Intent(this, AboutActivity.class));
	}
	
	/*

    private OnClickListener mStartListener = new OnClickListener() {
        public void onClick(View v)
        {
        	EditText editHalfSpan = (EditText)findViewById(R.id.EditTextHalfSpan);
			//halfspan = Integer.parseInt(editHalfSpan.getText().toString());
			halfspan = editHalfSpan.getText().toString();
        	
			Intent i = new Intent(LockCast.this,
                    BreadCrumbService.class);
	        i.putExtra(ImageManager.halfspan, halfspan);
	        
	        ImageManager.halfspan = halfspan;
        	
        	startService(i);       
            
        }
    };

    private OnClickListener mStopListener = new OnClickListener() {
        public void onClick(View v)
        {
            stopService(new Intent(LockCast.this,
                    BreadCrumbService.class));
        }
    };
    
    
    private OnClickListener mExportListener = new OnClickListener() {
    	public void onClick(View v) {
    		doExport();
    	}
    };
   
    
    private OnClickListener mToggleDebugListener = new OnClickListener() {
		public void onClick(View v) {
			boolean currentDebugState = BreadCrumbService.isShowingDebugToast();
			BreadCrumbService.setShowingDebugToast(!currentDebugState);
			ToggleButton toggleButton = (ToggleButton)findViewById(R.id.ToggleButtonDebug);
			toggleButton.setChecked(!currentDebugState);
		}
    };
    */
    
    /**
     * Starts a new search when the user clicks the search button.
     */
    private OnClickListener mSearchListener = new OnClickListener() {
    	public void onClick(View view) {
	        // Get the search area
	        int latHalfSpan = mMapView.getLatitudeSpan() >> 1;
	        int longHalfSpan = mMapView.getLongitudeSpan() >> 1;
	        
	        // Remember how the map was displayed so we can show it the same way later
	        GeoPoint center = mMapView.getMapCenter();
	        int zoom = mMapView.getZoomLevel();
	        int latitudeE6 = center.getLatitudeE6();
	        int longitudeE6 = center.getLongitudeE6();
	
	        Intent i = new Intent(LockCast.this, ImageList.class);
	        i.putExtra(ImageManager.ZOOM_EXTRA, zoom);
	        i.putExtra(ImageManager.LATITUDE_E6_EXTRA, latitudeE6);
	        i.putExtra(ImageManager.LONGITUDE_E6_EXTRA, longitudeE6);        
	
	        float minLong = ((float) (longitudeE6 - longHalfSpan)) / MILLION;
	        float maxLong = ((float) (longitudeE6 + longHalfSpan)) / MILLION;
	        float minLat = ((float) (latitudeE6 - latHalfSpan)) / MILLION;
	        float maxLat = ((float) (latitudeE6 + latHalfSpan)) / MILLION;
	
	        mImageManager.clear();
	        mImageManager.load(minLong, maxLong, minLat, maxLat);/*separate thread loading pics*/
	        startActivity(i);
    	}
    };
    

    /**
     * Add zoom controls to our frame layout
     */
    private void addZoomControls(FrameLayout frame) {
        // Get the zoom controls and add them to the bottom of the map
        View zoomControls = mMapView.getZoomControls();

        FrameLayout.LayoutParams p = 
            new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);
        frame.addView(zoomControls, p);
    }
    
    
    
    
    
	
	

	public void setAltitudeCorrectionMeters(int altitudeCorrectionMeters) {
		this.altitudeCorrectionMeters = altitudeCorrectionMeters;
	}

	public int getAltitudeCorrectionMeters() {
		return altitudeCorrectionMeters;
	}
    
	 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
