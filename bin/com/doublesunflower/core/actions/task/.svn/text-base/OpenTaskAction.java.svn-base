package com.doublesunflower.core.actions.task;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.doublesunflower.core.actions.base.AbstractAction;
import com.doublesunflower.core.common.exceptions.BusinessException;

public class OpenTaskAction extends AbstractAction {

	@Override
	public Object perform(Activity activity, Object params) throws BusinessException {
		try{
			Intent i = new Intent();
			i.setClassName( "com.doublesunflower", "com.doublesunflower.core.ui.task.TaskActivity" );
		    activity.startActivity(i);
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}