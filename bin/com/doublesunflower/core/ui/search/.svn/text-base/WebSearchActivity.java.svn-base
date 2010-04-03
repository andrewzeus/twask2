package com.doublesunflower.core.ui.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.doublesunflower.R;
import com.doublesunflower.core.ui.util.Util;

public class WebSearchActivity extends Activity {
	
	  private static final String TAG = "WebSearchActivity";
	
	private static String GOOGLE_URL = "http://www.google.com";
	
	private static WebView webview;
	
	protected ImageButton btnBack;
    protected ImageButton btnNew;
    protected ImageButton btnSearch;
    protected ImageButton btnSynchronize;
    
    protected String query = "qualcomm";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {

    	super.onCreate(icicle);	
        setContentView(R.layout.web);
        
        webview = (WebView)findViewById(R.id.webview);
        doSearch();
    }
    
    protected void doSearch(){
		try{
			webview.loadUrl(GOOGLE_URL + "/search?q=" + query);
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
	}
    
    protected void initializeUI(){
		TableRow trToolbar = (TableRow) findViewById(R.id.trToolbar);
		//trToolbar.setBackgroundDrawable(getWallpaper());
		
		TableRow trTitle = (TableRow) findViewById(R.id.trTitle);
		//trTitle.setBackgroundDrawable(getWallpaper());
		
		btnBack = (ImageButton)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
            	onClickBtnBack();
            }
		});      
		
		btnSearch = (ImageButton)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
            	onClickBtnSearch();
            }
		});       
   
		
		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(getResources().getString(R.string.menu_maps_item_web));
		
		ImageView iconTitle = (ImageView) findViewById(R.id.iconTitle);
		iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.world));
    }
    
    protected void onClickBtnBack(){
		finish();
	}
    
    protected void onClickBtnSearch(){
    		doSearch();
	}
    
    @Override
    public void onNewIntent(final Intent newIntent)
	{
		super.onNewIntent(newIntent);
		if (Intent.ACTION_SEARCH.equals(newIntent.getAction()))
		{
			query = newIntent.getStringExtra(SearchManager.QUERY);
			doSearch();
		}
	}

}
