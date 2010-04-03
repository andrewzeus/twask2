package com.doublesunflower.core.actions.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.doublesunflower.core.common.exceptions.BusinessException;

public class NewEntityAction extends AbstractAction {
	
	private String contentUri;
	
	public NewEntityAction(String contentUri){
		this.contentUri = contentUri;
	}

	@Override
	public Object perform(Activity activity, Object params)
	throws BusinessException {
		try{
			Intent i = 
		    	 new Intent(Intent.ACTION_INSERT, 
		    			 Uri.parse(contentUri));
			activity.startActivity(i);
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}
