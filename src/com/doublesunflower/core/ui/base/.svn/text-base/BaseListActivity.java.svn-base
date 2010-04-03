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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;

import com.doublesunflower.R;
import com.doublesunflower.core.actions.base.GetEntityAction;
import com.doublesunflower.core.actions.base.NewEntityAction;
import com.doublesunflower.core.common.Action;
import com.doublesunflower.core.common.exceptions.BusinessException;
import com.doublesunflower.core.ui.util.Util;

/**
 * @author Matias
 *
 * Abstract Activity to list entities. By default. This activity 
 * can handle requests to either show all entities, or the results
 * of a standard, tagcloud or websom search performed by relevant 
 * content provider.
 */

public abstract class BaseListActivity extends ListActivity {
	/**
     * A key to store/retrieve the search criteria in a bundle
     */
    public static final String SEARCH_CRITERIA_KEY = "SearchCriteria";
    
    protected Cursor mCursor;
    
    protected ImageButton btnBack;
    protected ImageButton btnNew;
    protected ImageButton btnSearch;
    protected ImageButton btnSynchronize;
    
    protected Action getEntityListAction;
    protected Action getEntityAction;
    protected Action newEntityAction;
    
    protected ListAdapter listAdapter;
    
    protected String contentUri;
    
    protected String query = "";
    
	
	@Override
    public void onCreate(Bundle icicle) {
		try{
			super.onCreate(icicle);
			setContentView(R.layout.list);
			
			initializeUI();
			
			getEntityListAction = buildGetEntityListAction();
			getEntityAction = buildGetEntityAction();
			newEntityAction = buildNewEntityAction();
			
			doSearch();
			
			//setDefaultKeyMode(SEARCH_DEFAULT_KEYS);
		        
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
    }
	
	protected void doSearch(){
		try{
			mCursor = (Cursor)getEntityListAction.perform(this,query);
			fillList(mCursor);
		}catch (Exception ex){
			Util.showErrorDialog(this, ex);
		}
	}
	
	protected void initializeUI(){
		TableRow trToolbar = (TableRow) findViewById(R.id.trToolbar);
		//trToolbar.setBackground(android.R.drawable.statusbar_background);
		
		TableRow trTitle = (TableRow) findViewById(R.id.trTitle);
		//trTitle.setBackground(android.R.drawable.list_selector_background_focus);
		
		btnBack = (ImageButton)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
            	onClickBtnBack();
            }
		});     
		
		btnNew = (ImageButton)findViewById(R.id.btnNew);
		btnNew.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
            	onClickBtnNew();
            }
		});    
		
		btnSearch = (ImageButton)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
            	onClickBtnSearch();
            }
		});       
		
		btnSynchronize = (ImageButton)findViewById(R.id.btnSynchronize);
		btnSynchronize.setOnClickListener(new OnClickListener(){
            // @Override
            public void onClick(View arg) {
            	onClickBtnSynchronize();
            }
		});    
	}
	
	protected void onClickBtnBack(){
		finish();
	}
	
	protected void onClickBtnNew(){
		if (newEntityAction!=null){
			try{
				newEntityAction.perform(this, null);
			}catch(BusinessException be){
				Util.showErrorDialog(this, be);
			}
		}
	}
	
	protected void onClickBtnSearch(){
		new AlertDialog.Builder(this)            	
        .setTitle(getResources().getString(R.string.title_search_dialog))
        .setItems(R.array.items_search_dialog,
        	new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedItem) {
            	String[] syncOptions = 
            		getResources().getStringArray(R.array.items_search_dialog); 
            	if (selectedItem==0){
            		onSearchRequested();
            	}else{
            		new AlertDialog.Builder(BaseListActivity.this)                        
                    .setMessage(
                    		getResources().getString(R.string.msg_search_option)
                    		+ " " + syncOptions[selectedItem]
                    		+ " " + getResources().getString(R.string.msg_not_available))                       
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            
                        }
                     })
                    .show();
            	}
            }
         })
       .show();
	}
	
	
	protected void onClickBtnSynchronize(){
		new AlertDialog.Builder(this)            	
        .setTitle(getResources().getString(R.string.title_synchronize_dialog))
        .setItems(R.array.items_synchronize_dialog,
        	new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedItem) {
            	String[] syncOptions = 
            		getResources().getStringArray(R.array.items_synchronize_dialog);                    	                    	
                new AlertDialog.Builder(BaseListActivity.this)                        
                .setMessage(
                		getResources().getString(R.string.msg_sync_option)
                		+ " " + syncOptions[selectedItem]
                		+ " " + getResources().getString(R.string.msg_not_available))                       
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        
                    }
                 })
                .show();
            }
         })
       .show();
	}
	
	protected abstract void fillList(Cursor managedCursor);
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id){
		try{
		     super.onListItemClick(l, v, position, id);
	         Cursor c = (Cursor) listAdapter.getItem(position);
	         long entityId = c.getLong(
	        		 c.getColumnIndex(android.provider.BaseColumns._ID));
	         getEntityAction.perform(this, new Long(entityId));
		}catch(BusinessException be){
			Util.showErrorDialog(this, be);
		}
    } 
	
	protected Action buildGetEntityListAction(){
		return null;
	}
	
	protected Action buildGetEntityAction(){
		GetEntityAction a = new GetEntityAction(contentUri);
		return a;
	}
	
	protected Action buildNewEntityAction(){
		NewEntityAction a = new NewEntityAction(contentUri);
		return a;
	}
	
	public void onNewIntent(final Intent newIntent)
	{
		super.onNewIntent(newIntent);
		if (Intent.ACTION_SEARCH.equals(newIntent.getAction()))
		{
			query = newIntent.getStringExtra(SearchManager.QUERY);
			doSearch();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
                                       Intent extras) {
         super.onActivityResult(requestCode, resultCode, extras);
         
    } 



}
