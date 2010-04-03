package com.doublesunflower.core.actions.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.doublesunflower.core.common.exceptions.BusinessException;

public class GetEntityAction extends AbstractAction {
	
	private String contentUri;
	
	public GetEntityAction(String contentUri){
		this.contentUri = contentUri;
	}

	@Override
	public Object perform(Activity activity, Object params)
			throws BusinessException {
		try{
			Long entityId = (Long)params;
			Intent i = 
	        	 new Intent(Intent.ACTION_VIEW, 
	        			 Uri.parse(contentUri + "/" + entityId));
			activity.startActivity(i);
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}
