/*
 * Copyright (C) 2009 Double Sunflower Holdings Corp.
 * Author: Andrew Xu
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


package com.doublesunflower.core;

import java.util.ArrayList;
//import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.doublesunflower.R;
import com.doublesunflower.core.actions.contacts.OpenContactListAction;
import com.doublesunflower.core.actions.map.OpenMapAction;
import com.doublesunflower.core.actions.search.OpenLocalSearchAction;
import com.doublesunflower.core.actions.search.OpenWebSearchAction;
import com.doublesunflower.core.actions.sync.OpenGoogleCalendarSyncAction;
import com.doublesunflower.core.actions.sync.OpenGoogleMailSyncAction;
import com.doublesunflower.core.common.Action;
import com.doublesunflower.core.common.exceptions.BusinessException;
import com.doublesunflower.core.ui.base.RowItem;
import com.doublesunflower.core.ui.util.Util;

//import com.doublesunflower.twask.view.activities.TwaskFrontDoor;
import com.doublesunflower.twask.utils.image.ImageManager;
import com.doublesunflower.twask.utils.location.BreadCrumbService;
import com.doublesunflower.twask.utils.sdcard.ExportToSDCard;
import com.doublesunflower.twask.view.activities.TwaskViewFlipper;

public class MiniContacts extends ListActivity {

	public static final int CONTACTS_MENU = 0;
	public static final int TASK_MENU = 1;
	public static final int MAPS_MENU = 2;
	public static final int EXIT_MENU = 3;
	public static final int SYNC_MENU = 4;
	public static final int SHARE_MENU = 5;

	protected int selectedMenu;
	protected ListAdapter listAdapter;
	protected ArrayList<RowItem> contactsMenuItems;
	protected ArrayList<RowItem> applicationMenuItems;
	protected ArrayList<RowItem> mapsMenuItems;
	protected ArrayList<RowItem> exitMenuItems;
	protected ArrayList<RowItem> syncMenuItems;
	protected ArrayList<RowItem> shareMenuItems;
	
	protected ImageButton btnSearch = null;
	protected ImageButton btnContacts = null;
	protected ImageButton btnTask = null;
	protected ImageButton btnMaps = null;
	protected ImageButton btnExit = null;
	protected ImageButton btnSync = null;
	protected ImageButton btnShare = null;
	
	protected ImageView iconTitle = null;
	protected TextView txtTitle = null;
	
	protected Action openContactListAction = null;
	protected Action openMapAction = null;
	protected Action openWebSearchAction = null;
	protected Action openLocalSearchAction = null;
	protected Action openGoogleCalendarSyncAction = null;
	protected Action openGoogleMailSyncAction = null;
	protected Action openShareAction = null;
	protected Action openTaskAction = null;

	@Override
	public void onCreate(Bundle icicle) {
		try {
			super.onCreate(icicle);
			setContentView(R.layout.main);
			initializeUI();
			selectMenu(TASK_MENU);
		} catch (Exception ex) {
			Util.showErrorDialog(this, ex);
		}
	}
	
	protected void selectMenu(int menu){
		selectedMenu = menu;
		switch (menu) {
			case (CONTACTS_MENU): {
				iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.system_users));
				txtTitle.setText(R.string.menu_contacts);
				break;
			}
			case (TASK_MENU): {
				iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.appointment_new));
				txtTitle.setText(R.string.menu_task);
				break;
			}
			case (MAPS_MENU): {
				iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_map));
				txtTitle.setText(R.string.menu_maps);
				break;
			}
			case (EXIT_MENU): {
				iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.system_logout));
				txtTitle.setText(R.string.menu_exit);
				break;
			}
			case (SYNC_MENU): {
				iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.rss));
				txtTitle.setText(R.string.menu_sync);
				break;
			}
			case (SHARE_MENU): {
				iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.document_new));
				txtTitle.setText(R.string.menu_share);
				break;
			}
		}
		loadMenu();
	}

	protected void initializeUI() {
		
		openContactListAction = new OpenContactListAction();
		openMapAction = new OpenMapAction();
		openWebSearchAction = new OpenWebSearchAction();
		openLocalSearchAction = new OpenLocalSearchAction();
		openGoogleCalendarSyncAction = new OpenGoogleCalendarSyncAction();
		openGoogleMailSyncAction = new OpenGoogleMailSyncAction();
		
		TableRow trToolbar = (TableRow) findViewById(R.id.trToolbar);
		//trToolbar.setBackgroundDrawable(getWallpaper());
		
		iconTitle = (ImageView) findViewById(R.id.iconTitle);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		
		btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	Search();
            }
        }); 

		btnContacts = (ImageButton) findViewById(R.id.btnContacts);
		btnContacts.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	selectMenu(CONTACTS_MENU);
            }
        }); 
		
		btnTask = (ImageButton) findViewById(R.id.btnTask);
		btnTask.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	selectMenu(TASK_MENU);
            }
        }); 
		
		btnMaps = (ImageButton) findViewById(R.id.btnMaps);
		btnMaps.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	selectMenu(MAPS_MENU);
            }
        }); 
		
		btnExit = (ImageButton) findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	selectMenu(EXIT_MENU);
            }
        }); 
		
		
		btnSync = (ImageButton) findViewById(R.id.btnSync);
		btnSync.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	selectMenu(SYNC_MENU);
            }
        }); 
		
		btnShare = (ImageButton) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	selectMenu(SHARE_MENU);
            }
        }); 

		TableRow trTitle = (TableRow) findViewById(R.id.trTitle);
		//trTitle.setBackgroundDrawable(getWallpaper());

		contactsMenuItems = new ArrayList<RowItem>();
		contactsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_contacts_item_contacts),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.system_users)));
		
		

		applicationMenuItems = new ArrayList<RowItem>();
		applicationMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_tasks_item_task),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.document_new)));
		applicationMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_tasks_item_tag),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.tag_find)));
		
		

		mapsMenuItems = new ArrayList<RowItem>();
		mapsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_maps_item_favorite_places),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.world)));
		mapsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_maps_item_start_gpslogging),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.world)));
		mapsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_maps_item_stop_gpslogging),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.world)));
		
		

		exitMenuItems = new ArrayList<RowItem>();
		exitMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_exit_item_exit),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.emblem_unreadable)));
		

		syncMenuItems = new ArrayList<RowItem>();
		syncMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_sync_item_gcalendar),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.video_x_generic)));
		syncMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_sync_item_gmail),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.video_x_generic)));
		

		shareMenuItems = new ArrayList<RowItem>();
		shareMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_share_item_share),
				getResources().getString(R.string.msg_in_operation),
				getResources().getDrawable(R.drawable.document_new)));
	}
	
	protected void Search(){
		new AlertDialog.Builder(this)            	
        .setTitle(getResources().getString(R.string.title_search_dialog))
        .setItems(R.array.items_global_search_dialog,
        	new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int selectedItem) {
            	String[] syncOptions = 
            		getResources().getStringArray(R.array.items_global_search_dialog); 
            	if (selectedItem==0){
            		GoogleSearch();
            	}else{
            		LocalSearch(syncOptions[selectedItem]);
            	}
            }
         })
       .show();
	}
	
	protected void GoogleSearch(){
		try {
			openWebSearchAction.perform(this, null);
		} catch (BusinessException be) {
			Util.showErrorDialog(this, be);
		}
	}
	
	protected void LocalSearch(String str){		   
		try {
			openLocalSearchAction.perform(this, null);
		} catch (BusinessException be) {
			Util.showErrorDialog(this, be);
		}
		
	}


	private void loadMenu() {
		switch (selectedMenu) {
			case (CONTACTS_MENU): {
				listAdapter = new SimpleAdapter(
						this,
						contactsMenuItems,
						R.layout.row,
						/*new String[] {RowItem.ICON, RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{ R.id.iconRow, R.id.txtRow1, R.id.txtRow2});*/
						new String[] {RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{R.id.txtRow1, R.id.txtRow2});
				break;
			}
			case (TASK_MENU): {
				listAdapter = new SimpleAdapter(
						this,
						applicationMenuItems,
						R.layout.row,
						new String[] {RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{ R.id.txtRow1, R.id.txtRow2});
				break;
			}
			case (MAPS_MENU): {
				listAdapter = new SimpleAdapter(
						this,
						mapsMenuItems,
						R.layout.row,
						new String[] {RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{R.id.txtRow1, R.id.txtRow2});
				break;
			}
			case (SYNC_MENU): {
				listAdapter = new SimpleAdapter(
						this,
						syncMenuItems,
						R.layout.row,
						new String[] {RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{R.id.txtRow1, R.id.txtRow2});
				break;
			}
			case (SHARE_MENU): {
				listAdapter = new SimpleAdapter(
						this,
						shareMenuItems,
						R.layout.row,
						new String[] {RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{R.id.txtRow1, R.id.txtRow2});
				break;
			}
			case (EXIT_MENU): {
				listAdapter = new SimpleAdapter(
						this,
						exitMenuItems,
						R.layout.row,
						new String[] {RowItem.ROW_TEXT_1, RowItem.ROW_TEXT_2},
		                new int[]{R.id.txtRow1, R.id.txtRow2});
				break;
			}
		}

		// Display it
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		selectMenuItem(position);
	}
	
	protected void selectMenuItem(int menuItem){
		try {
			switch (selectedMenu) {
				case (CONTACTS_MENU): {
					switch (menuItem) {
						case 0: {//System Contacts
							openContactListAction.perform(this, null);
							break;
						}
					}
					break;
				}
				case (TASK_MENU): {
					switch (menuItem) {
					case 0: {//Tasks
						//showUnderConstructionActivity();
						Intent twaskListIntent = new Intent(this, TwaskViewFlipper.class);
						
						Bundle bundle = new Bundle();
						bundle.putInt("which_subactivity", 0);
				        twaskListIntent.putExtra(TwaskViewFlipper.VARIABLES_TAG, bundle);
			
				        startActivity(twaskListIntent);
				        //finish();
						break;
					}
					case 1: {//Tags
						//showUnderConstructionActivity();
						
						Intent twaskListIntent = new Intent(this, TwaskViewFlipper.class);
											

						Bundle bundle = new Bundle();
						bundle.putInt("which_subactivity", 1);
				        twaskListIntent.putExtra(TwaskViewFlipper.VARIABLES_TAG, bundle);
				        
				        startActivity(twaskListIntent);
				        //finish();
						break;
					}
				}
				break;
				}
				case (MAPS_MENU): {
					switch (menuItem) {
						case 0: {//Favorite places. open lockcast.java
							openMapAction.perform(this, null);
							break;
						}
						case 1: {//start gps logging..
							startService(new Intent(MiniContacts.this,
				                    BreadCrumbService.class));
							
							break;
						}
						case 2: {//stop gps logging..
							stopService(new Intent(MiniContacts.this,
				                    BreadCrumbService.class));
							
							ExportToSDCard.initTripName(this);
							ExportToSDCard.doExport(this);
							
							break;
						}
						
						
					}
					break;
				}
				
				case (SYNC_MENU): {
					switch (menuItem) {
						case 0: {//Favorite places..
							openGoogleCalendarSyncAction.perform(this, null);
							break;
						}
							
						case 1: {//Favorite places..
							openGoogleMailSyncAction.perform(this, null);
							break;
						}
					}
					break;
				}
				case (SHARE_MENU): {
					switch (menuItem) {
						case 0: {//Favorite places..
							openShareAction.perform(this, null);
							break;
						}
					}
					break;
				}
				case (EXIT_MENU): {
					switch (menuItem) {
						case 0: {//Exit
							new AlertDialog.Builder(MiniContacts.this)            	
			                .setTitle(getResources().getString(R.string.dialog_title_question))
			                .setMessage(getResources().getString(R.string.dialog_exit_question))
			                .setIcon(R.drawable.help_browser)
			                .setPositiveButton(getResources().getString(R.string.dialog_button_yes)
			                		, new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int whichButton) {
			                       setResult(RESULT_OK);
			                       finish();
			                    }
			                })
			               .setNegativeButton(getResources().getString(R.string.dialog_button_no),
			            		   new DialogInterface.OnClickListener() {
			                   public void onClick(DialogInterface dialog, int whichButton) {
			                       
			                   }
			               })
			               .show();
							break;
						}
					}
					break;
				}
			}
			
		} catch (BusinessException be) {
			Util.showErrorDialog(this, be);
		}
	}
	
	protected void showUnderConstructionActivity(){
		//TODO:TEMPORAL!!!!!!!!!
		Intent i = new Intent();
		i.setClassName( "com.minicontacts", "com.minicontacts.core.ui.util.UnderConstructionActivity" );
		startActivity( i );
	}

}