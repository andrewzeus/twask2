/**
 * 
 */
package com.doublesunflower.core.actions.contacts;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.doublesunflower.core.actions.base.AbstractAction;
import com.doublesunflower.core.common.exceptions.BusinessException;

/**
 * @author Matias
 *
 */
public class OpenContactListAction extends AbstractAction {

	@Override
	public Object perform(Activity activity, Object params) throws BusinessException {
		try{
			Intent i = new Intent();
			i.setClassName( "com.minicontacts", "com.minicontacts.core.ui.contacts.GoogleContactsList" );
		    activity.startActivity(i);
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}