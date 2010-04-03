/**
 * 
 */
package com.doublesunflower.extensions.sales.actions.customer;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.doublesunflower.core.actions.base.AbstractAction;
import com.doublesunflower.core.common.exceptions.BusinessException;

/**
 * @author Matias
 *
 */
public class GetCustomerListAction extends AbstractAction {

	@Override
	public Object perform(Activity activity, Object params)
			throws BusinessException {
		Cursor managedCursor = null;
		try{
			//An array specifying which columns to return. 
			//The provider exposes a list of column names it returns for a specific
			//query, or you can get all columns and iterate through them. 
		    String[] projection = new String[] {
		         android.provider.BaseColumns._ID,
		         android.provider.Contacts.PeopleColumns.NAME,
		         android.provider.Contacts.PeopleColumns.DISPLAY_NAME
		     };
			//Querying for a cursor is like querying for any SQL-Database
			//Best way to retrieve a query; returns a managed query. 
		    managedCursor = activity.managedQuery( android.provider.Contacts.People.CONTENT_URI,
                 projection, //Which columns to return. 
                 //android.provider.Contacts.ContactMethods.TYPE + " = " + Util.CUSTOMER_CONTACT_TYPE,       // WHERE clause.
                 null, //retrieve all for testing...
                 null,
                 android.provider.Contacts.PeopleColumns.NAME + " ASC"); // Order-by clause.
		    
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return managedCursor;
	}

}
