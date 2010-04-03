/**
 * 
 */
package com.doublesunflower.core.ui.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.doublesunflower.R;
import com.doublesunflower.core.actions.contacts.GetContactListAction;
import com.doublesunflower.core.common.Action;
import com.doublesunflower.core.ui.base.BaseListActivity;

/**
 * @author Matias
 *
 */
public class GoogleContactsList extends BaseListActivity {

	protected Action buildGetEntityListAction(){
		return new GetContactListAction();
	}
	
	protected void initializeUI(){
		super.initializeUI();
		
		contentUri = "content://contacts/people";
		
		btnNew.setImageDrawable(getResources().getDrawable(R.drawable.contact_new));

		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setText(getResources().getString(R.string.menu_contacts_item_contacts));
		
		ImageView iconTitle = (ImageView) findViewById(R.id.iconTitle);
		iconTitle.setImageDrawable(getResources().getDrawable(R.drawable.system_users));
	}
	
	protected void fillList(Cursor managedCursor){
		//Now create a new list adapter bound to the cursor. 
        // SimpleListAdapter is designed for binding to a Cursor.
        listAdapter = new SimpleCursorAdapter(
                this, // Context.
                R.layout.row,  // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
                managedCursor, // Pass in the cursor to bind to.
                /*new String[] {People.PHOTO, People.NAME, People.NOTES}, // Array of cursor columns to bind to.
                new int[]{ R.id.iconRow, R.id.txtRow1, R.id.txtRow2}); // Parallel array of which template objects to bind to those columns.
                */
                new String[] {People.NAME, People.NOTES}, // Array of cursor columns to bind to.
                new int[]{ R.id.txtRow1, R.id.txtRow2});  

        // Bind to our new adapter.
        setListAdapter(listAdapter);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode,
                                       Intent extras) {
         super.onActivityResult(requestCode, resultCode, extras);
    } 
	

}
