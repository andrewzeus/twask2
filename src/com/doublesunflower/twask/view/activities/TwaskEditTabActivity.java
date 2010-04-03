/*
 * Copyright (C) 2009 Double Sunflower Holdings Corp.
 *
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
 * ASTRID: Android's Simple Task Recording Dashboard
 *
 * Copyright (c) 2009 Tim Su
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.doublesunflower.twask.view.activities;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.doublesunflower.R;
import com.doublesunflower.twask.controller.TaskController;
import com.doublesunflower.twask.identifier.TaskIdentifier;
import com.doublesunflower.twask.model.AbstractTaskModel;
import com.doublesunflower.twask.utils.other.DialogUtilities;

/** Hack hack for tabbed activity. Should provide better interaction
 *
 * @author timsu
 */
public abstract class TwaskEditTabActivity<MODEL_TYPE extends
        AbstractTaskModel> extends TabActivity {
	
    //protected static final String LOAD_INSTANCE_TOKEN = TaskModificationActivity.LOAD_INSTANCE_TOKEN;
    public static final String LOAD_INSTANCE_TOKEN = "id";
    protected TaskController controller;
    protected MODEL_TYPE model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        controller = new TaskController(this);
        controller.open();

        try {
            // check if we have a TaskIdentifier
            TaskIdentifier identifier = null;
            Bundle extras = getIntent().getExtras();
            
            if(savedInstanceState != null && savedInstanceState.containsKey(LOAD_INSTANCE_TOKEN)) {
                identifier = new TaskIdentifier(savedInstanceState.getLong(
                        LOAD_INSTANCE_TOKEN));
            } else if(extras != null && extras.containsKey(LOAD_INSTANCE_TOKEN))
                identifier = new TaskIdentifier(extras.getLong(
                        LOAD_INSTANCE_TOKEN));

            model = getModel(identifier);
            
        } catch (Exception e) {
            showErrorAndFinish(R.string.error_opening, e);
        }
    }

    protected void showErrorAndFinish(int prefix, Throwable e) {
        Resources r = getResources();
        DialogUtilities.okDialog(this,
                r.getString(prefix) + " " +
                e.getLocalizedMessage(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(model.getTaskIdentifier() != null)
            outState.putLong(LOAD_INSTANCE_TOKEN, model.getTaskIdentifier().getId());
    }
    
    abstract protected MODEL_TYPE getModel(TaskIdentifier identifier);
}
