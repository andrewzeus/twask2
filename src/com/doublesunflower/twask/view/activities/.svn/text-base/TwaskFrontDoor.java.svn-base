/*
 * Copyright (C) 2009 Double Sunflower Holdings Corp.
 * Author: Andrew Xu
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


package com.doublesunflower.twask.view.activities;

import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.doublesunflower.twask.distribution.EulaActivity;
import com.doublesunflower.R;
import com.doublesunflower.twask.controller.TagController;
import com.doublesunflower.twask.controller.TaskController;
import com.doublesunflower.twask.receiver.StartupReceiver;
import com.doublesunflower.twask.utils.other.Constants;
import com.doublesunflower.twask.utils.other.Preferences;
import com.doublesunflower.twask.utils.sync.Synchronizer;
import com.doublesunflower.twask.view.subactivities.SubActivity;
import com.doublesunflower.twask.view.subactivities.TagListSubActivity;
import com.doublesunflower.twask.view.subactivities.TwaskListSubActivity;

public class TwaskFrontDoor extends Activity {

    /** Enum for the different pages that we can display */
    public enum ActivityCode {
    	TASK_LIST,
    	TAG_LIST,
    	TASK_LIST_W_TAG
    };

    /** Bundle Key: activity code id of current activity */
    private static final String LAST_ACTIVITY_TAG = "l";

    /** Bundle Key: variables of current activity */
    private static final String LAST_BUNDLE_TAG = "b";

    /** Bundle Key: variables to pass to the subactivity */
    public  static final String VARIABLES_TAG = "v";
	
	private SubActivity taskList;
	private SubActivity tagList;
	private SubActivity taskListWTag;
	
	private Bundle lastActivityBundle;

	// data controllers
	public TaskController taskController;
	public TagController tagController;

	// static variables
	/** If set, the application will close when this activity gets focus */
	public static boolean shouldCloseInstance = false;

    @Override
    /** Called when loading up the activity for the first time */
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);	
    	
    	if (!EulaActivity.checkEula(this)) {
			//return;
		}
    	
    	Bundle variables = null;
    	
        /*display subactivity view*/
        if(savedInstanceState != null && savedInstanceState.containsKey(LAST_ACTIVITY_TAG)) {
        	//viewFlipper.setDisplayedChild(savedInstanceState.getInt(LAST_ACTIVITY_TAG));
        	variables = savedInstanceState.getBundle(LAST_BUNDLE_TAG);
        	//getCurrentSubActivity().onDisplay(variables);
        } else {
         
            if(getIntent().hasExtra(VARIABLES_TAG))
                variables = getIntent().getBundleExtra(VARIABLES_TAG);
        	//getCurrentSubActivity().onDisplay(variables);
        }
    	
    	//if(savedInstanceState != null) {
    	Boolean which_subactivity = true;
    	   
    		if (which_subactivity = getIntent().getExtras().get("meMap").equals(true))
            	taskList.onDisplay(variables);
            else
            	tagList.onDisplay(variables);          
        //}
	
    	
       // setContentView(R.layout.viewflipper_twask);

        // open controllers & perform application startup rituals
        StartupReceiver.onStartupApplication(this);
        shouldCloseInstance = false;
        
        taskController = new TaskController(this);
        taskController.open();
        tagController = new TagController(this);
        tagController.open();
        
        Synchronizer.setTagController(tagController);
        Synchronizer.setTaskController(taskController);

        setupSubActivities();
        
      
        
        // auto sync if requested
        Integer autoSyncHours = Preferences.autoSyncFrequency(this);
        if(autoSyncHours != null) {
            final Date lastSync = Preferences.getSyncLastSync(this);

            if(lastSync == null || lastSync.getTime() +
                    1000L*3600*autoSyncHours < System.currentTimeMillis()) {
                Synchronizer.synchronize(this, true, null);
            }
        }
    }

    /** Set up user interface components */
    private void setupSubActivities() {
    	
        taskList = new TwaskListSubActivity(this, ActivityCode.TASK_LIST,
        		findViewById(R.id.tasklist_layout));
        tagList = new TagListSubActivity(this, ActivityCode.TAG_LIST,
        		findViewById(R.id.taglist_layout));
        taskListWTag = new TwaskListSubActivity(this, ActivityCode.TASK_LIST_W_TAG,
        		findViewById(R.id.tasklistwtag_layout));
       
    }
   
    /* ======================================================================
     * ======================================================= event handling
     * ====================================================================== */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	taskController.close();
        tagController.close();
        Synchronizer.setTagController(null);
        Synchronizer.setTaskController(null);
    }
}