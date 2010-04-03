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

package com.doublesunflower.extensions.sales;

import com.doublesunflower.R;
import com.doublesunflower.core.MiniContacts;
import com.doublesunflower.core.ui.base.RowItem;
import com.doublesunflower.core.ui.util.Util;

/**
 * @author Matias
 *
 */
public class Sales extends MiniContacts {
	
	protected void initializeUI() {
		
		super.initializeUI();
		
		/*

		contactsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_contacts_item_customers),
				getResources().getString(R.string.msg_under_construction),
				getResources().getDrawable(R.drawable.system_users)));
		contactsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_contacts_item_suppliers),
				getResources().getString(R.string.msg_under_construction),
				getResources().getDrawable(R.drawable.system_users)));
		contactsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_contacts_item_employees),
				getResources().getString(R.string.msg_under_construction),
				getResources().getDrawable(R.drawable.system_users)));

		
		
		applicationMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_application_item_products),
				getResources().getString(R.string.msg_under_construction),
				getResources().getDrawable(R.drawable.package_generic)));
		applicationMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_application_item_orders),
				getResources().getString(R.string.msg_under_construction),
				getResources().getDrawable(R.drawable.accessories_text_editor)));

		
		
		mapsMenuItems.add(new RowItem(
				getResources().getString(R.string.menu_maps_item_routes),
				getResources().getString(R.string.msg_under_construction),
				getResources().getDrawable(R.drawable.world)));
		
		*/		

	}
	
	protected void selectMenuItem(int menuItem){
		super.selectMenuItem(menuItem);
		
		/*
		try {
			switch (selectedMenu) {
				case (CONTACTS_MENU): {
					switch (menuItem) {
						case 1: {//Customers
							showUnderConstructionActivity();
							break;
						}
						case 2: {//Supliers
							showUnderConstructionActivity();
							break;
						}
						case 3: {//Employees
							showUnderConstructionActivity();
							break;
						}
					}
					break;
				}
				case (TASK_MENU): {
					switch (menuItem) {
						case 2: {//Products
							showUnderConstructionActivity();
							break;
						}
						case 3: {//Orders
							showUnderConstructionActivity();
							break;
						}
					}
					break;
				}
				case (MAPS_MENU): {
					switch (menuItem) {
						case 1: {//Routes
							showUnderConstructionActivity();
							break;
						}
					}
					break;
				}
				case (EXIT_MENU): {
					
					break;
				}
				
			}
		
		}
		catch (Exception ex) {
			Util.showErrorDialog(this, ex);
		}
		
		
		*/
	}

}
