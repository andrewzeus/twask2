/**
 * 
 */
package com.doublesunflower.core.actions.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.doublesunflower.core.common.exceptions.BusinessException;

/**
 * @author Matias
 *
 */
public class AbstractOpenEntityAction extends AbstractAction {
	
	protected Uri contentUri;

	@Override
	public Object perform(Activity activity, Object params)
			throws BusinessException {
		try{
			String id = (String)params;
		    //Create the URI of the entity we want to view based on the id
	        Uri uri = Uri.withAppendedPath(contentUri, id);
	        Intent i = new Intent(Intent.ACTION_VIEW, uri);
	        activity.startActivity(i);
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}
