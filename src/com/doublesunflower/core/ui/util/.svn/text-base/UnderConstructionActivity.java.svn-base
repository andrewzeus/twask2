/**
 * 
 */
package com.doublesunflower.core.ui.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.doublesunflower.R;

/**
 * @author Matias
 *
 */
public class UnderConstructionActivity extends Activity {
	
	@Override
    public void onCreate(Bundle icicle) {
		try{
			super.onCreate(icicle);
			
			setContentView(R.layout.under_construction);
			
			initializeUI();
		        
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
    }
	
	protected void initializeUI(){
		TableRow trToolbar = (TableRow) findViewById(R.id.trToolbar);
		//trToolbar.setBackground(android.R.drawable.statusbar_background);
		
		TableRow trTitle = (TableRow) findViewById(R.id.trTitle);
		//trTitle.setBackground(android.R.drawable.list_selector_background_focus);
		
		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(getResources().getString(R.string.menu_util_item_under_construction));
		
		ImageView iconTitle = (ImageView) findViewById(R.id.iconTitle);
		iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.empty));
		
		ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
                 finish();
            }
		});         
	}

}
