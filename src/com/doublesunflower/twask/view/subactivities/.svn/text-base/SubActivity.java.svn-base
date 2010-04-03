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
package com.doublesunflower.twask.view.subactivities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.doublesunflower.twask.controller.TagController;
import com.doublesunflower.twask.controller.TaskController;
import com.doublesunflower.twask.view.activities.TwaskFrontDoor;
import com.doublesunflower.twask.view.activities.TwaskViewFlipper;
import com.doublesunflower.twask.view.activities.TwaskViewFlipper.ActivityCode;

/**
 * Interface for views that are displayed from the main view page.
 *
 * @author timsu
 */
abstract public class SubActivity {
	
	private TwaskViewFlipper parent;
	public ActivityCode code;
	private View view;

	public SubActivity(TwaskViewFlipper parent, ActivityCode code, View view) {
		this.parent = parent;
		this.code = code;
		this.view = view;
		view.setTag(this);
	}

	// --- pass-through to activity listeners

	public SubActivity(
			TwaskFrontDoor parent2,
			com.doublesunflower.twask.view.activities.TwaskFrontDoor.ActivityCode code2,
			View view2) {
		// TODO Auto-generated constructor stub
	}

	/** Called when this subactivity is displayed to the user */
	abstract public void onDisplay(Bundle variables);

	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return false;
	}


	public void onWindowFocusChanged(boolean hasFocus) {
		//
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	// --- pass-through to activity methods

	public Resources getResources() {
		return parent.getResources();
	}

	public View findViewById(int id) {
		return view.findViewById(id);
	}

	public void startManagingCursor(Cursor c) {
		parent.startManagingCursor(c);
	}

	public void setTitle(CharSequence title) {
		parent.setTitle(title);
	}

	public void closeActivity() {
		parent.finish();
	}

	public void launchActivity(Intent intent, int requestCode) {
		parent.startActivityForResult(intent, requestCode);
	}

	// --- helper methods

	public Activity getParent() {
		return parent;
	}

	public TaskController getTaskController() {
		return parent.taskController;
	}

	public TagController getTagController() {
		return parent.tagController;
	}

	public View.OnTouchListener getGestureListener() {
		return parent.gestureListener;
	}

	public void switchToActivity(ActivityCode activity, Bundle state) {
		parent.switchToActivity(activity, state);
	}

	// --- internal methods

	public ActivityCode getActivityCode() {
		return code;
	}

	protected View getView() {
		return view;
	}
}