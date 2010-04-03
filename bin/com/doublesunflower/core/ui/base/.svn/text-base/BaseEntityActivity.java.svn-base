/*
 * miniContacts
 * Copyright (C) 2007-2008 Matías Molinas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package com.doublesunflower.core.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.doublesunflower.R;
import com.doublesunflower.core.ui.util.Util;

/**
 * @author Matias
 *
 */
public class BaseEntityActivity extends Activity {
	
	protected TextView txtTitle;
	protected ImageView iconTitle;
	
	@Override
    public void onCreate(Bundle icicle) {
		try{
			super.onCreate(icicle);
			
			setContentView(R.layout.entity);
			
			initializeUI();
		        
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
    }
	
	protected void initializeUI(){
		TableRow trToolbar = (TableRow) findViewById(R.id.trToolbar);
		//trToolbar.setBackground(android.R.drawable.statusbar_background);
		
		TableRow trTitle = (TableRow) findViewById(R.id.trTitle);
		//trTitle.setBackground(android.R.drawable.list_selector_background);
		
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(getResources().getString(R.string.menu_util_item_under_construction));
		
		iconTitle = (ImageView) findViewById(R.id.iconTitle);
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
