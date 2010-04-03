/**
 * 
 */
package com.doublesunflower.extensions.sales.actions.customer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.doublesunflower.core.actions.base.AbstractAction;
import com.doublesunflower.core.common.exceptions.BusinessException;
import com.doublesunflower.extensions.sales.ui.customer.CustomerListActivity;

/**
 * @author Matias
 *
 */
public class OpenCustomerListAction extends AbstractAction {
	
	protected static final int SUB_ACTIVITY_REQUEST_CODE = 1001;

	@Override
	public Object perform(Activity activity, Object params)
			throws BusinessException {
		try{
			Intent intent = new Intent(activity, CustomerListActivity.class);
		    activity.startActivityForResult(intent, SUB_ACTIVITY_REQUEST_CODE);
		   
		}catch(Exception ex){
			Log.e("Error", ex.getMessage());
			throw new BusinessException(ex);
		}
		return null;
	}

}
