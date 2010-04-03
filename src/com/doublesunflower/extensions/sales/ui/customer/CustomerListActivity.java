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


package com.doublesunflower.extensions.sales.ui.customer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.doublesunflower.R;
import com.doublesunflower.core.common.Action;
import com.doublesunflower.core.ui.base.BaseListActivity;
import com.doublesunflower.extensions.sales.actions.customer.GetCustomerListAction;
import com.doublesunflower.extensions.sales.actions.customer.OpenCustomerAction;

/**
 * @author Matias
 *
 */
public class CustomerListActivity extends BaseListActivity {
	
	protected Action getListAction(){
		return new GetCustomerListAction();
	}
	
	protected Action getViewEntityAction(){
		return new OpenCustomerAction();
	}
	
	protected void initializeUI(){
		super.initializeUI();
		
		btnNew.setImageDrawable(getResources().getDrawable(R.drawable.contact_new));

		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(getResources().getString(R.string.menu_contacts_item_customers));
		
		ImageView iconTitle = (ImageView) findViewById(R.id.iconTitle);
		iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.system_users));
	}
	
	protected void fillList(Cursor managedCursor){
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode,
                                       Intent extras) {
         super.onActivityResult(requestCode, resultCode, extras);
         
    } 
	
	
}
