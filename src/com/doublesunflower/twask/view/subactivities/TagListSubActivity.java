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

//changed from Tasklist to Twask
//planning to add location-based search to Twask

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.doublesunflower.R;
import com.doublesunflower.twask.identifier.TagIdentifier;
import com.doublesunflower.twask.identifier.TaskIdentifier;
import com.doublesunflower.twask.model.TagModelForView;
import com.doublesunflower.twask.model.TaskModelForList;
import com.doublesunflower.twask.view.activities.TwaskEdit;
import com.doublesunflower.twask.view.activities.TwaskFrontDoor;
import com.doublesunflower.twask.view.activities.TwaskViewFlipper;
import com.doublesunflower.twask.view.activities.TwaskViewFlipper.ActivityCode;


/** 
 * List all tags and allows a user to see all tasks for a given tag
 *
 * @author Tim Su (timsu@stanfordalumni.org)
 *
 */
public class TagListSubActivity extends SubActivity {
    private static final int ACTIVITY_CREATE       = 0;

    private static final int MENU_SORT_ALPHA_ID    = Menu.FIRST;
    private static final int MENU_SORT_SIZE_ID     = Menu.FIRST + 1;
    private static final int CONTEXT_CREATE_ID     = Menu.FIRST + 10;
    private static final int CONTEXT_DELETE_ID     = Menu.FIRST + 11;
    private static final int CONTEXT_SHOWHIDE_ID   = Menu.FIRST + 12;

    private ListView listView;
    private List<TagModelForView> tagArray;
    private Map<Long, TaskModelForList> taskMap;
    Map<TagModelForView, Integer> tagToTaskCount;

    private static SortMode sortMode = SortMode.SIZE;
    private static boolean sortReverse = false;
    
    public TagListSubActivity(TwaskViewFlipper parent, ActivityCode code, View view) {
    	super(parent, code, view);
    }

    public TagListSubActivity(
			TwaskFrontDoor parent,
			com.doublesunflower.twask.view.activities.TwaskFrontDoor.ActivityCode code,
			View view) {
    	super(parent, code, view);
	}

	@Override
    public void onDisplay(Bundle variables) {
        listView = (ListView)findViewById(R.id.taglist);
        fillData();
    }

    // --- stuff for sorting

    private enum SortMode {
        ALPHA {
            @Override
            int compareTo(TagListSubActivity self, TagModelForView arg0, TagModelForView arg1) {
                return arg0.getName().compareTo(arg1.getName());
            }
        },
        SIZE {
            @Override
            int compareTo(TagListSubActivity self, TagModelForView arg0, TagModelForView arg1) {
                return self.tagToTaskCount.get(arg1) - self.tagToTaskCount.get(arg0);
            }
        };

        abstract int compareTo(TagListSubActivity self, TagModelForView arg0, TagModelForView arg1);
    };

    private void sortTagArray() {
        // get all tasks
        Cursor taskCursor = getTaskController().getActiveTaskListCursor();
        startManagingCursor(taskCursor);
        List<TaskModelForList> taskArray =
        	getTaskController().createTaskListFromCursor(taskCursor);
        taskMap = new HashMap<Long, TaskModelForList>();
        for(TaskModelForList task : taskArray) {
            if(task.isHidden())
                continue;
            taskMap.put(task.getTaskIdentifier().getId(), task);
        }

        // get accurate task count for each tag
        tagToTaskCount = new HashMap<TagModelForView, Integer>();
        for(TagModelForView tag : tagArray) {
            int count = 0;
            List<TaskIdentifier> tasks = getTagController().getTaggedTasks(
            		getParent(), tag.getTagIdentifier());

            for(TaskIdentifier taskId : tasks)
                if(taskMap.containsKey(taskId.getId()))
                    count++;
            tagToTaskCount.put(tag, count);
        }

        // do sort
        Collections.sort(tagArray, new Comparator<TagModelForView>() {
            public int compare(TagModelForView arg0, TagModelForView arg1) {
                return sortMode.compareTo(TagListSubActivity.this, arg0, arg1);
            }
        });
        if(sortReverse)
            Collections.reverse(tagArray);
    }

    // --- fill data

    /** Fill in the Tag List with our tags */
    private void fillData() {
    	
        Resources r = getResources();

        tagArray = getTagController().getAllTags(getParent());

        // perform sort
        sortTagArray();

        // set up the title
        StringBuilder title = new StringBuilder().
            append(r.getString(R.string.tagList_titlePrefix)).
            append(" ").append(r.getQuantityString(R.plurals.Ntags,
                tagArray.size(), tagArray.size()));
        setTitle(title);

        // set up our adapter
        TagListAdapter tagAdapter = new TagListAdapter(getParent(),
                android.R.layout.simple_list_item_1, tagArray);
        
        listView.setAdapter(tagAdapter);

        // list view listener
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                TagModelForView tag = (TagModelForView)view.getTag();

                Bundle bundle = new Bundle();
                bundle.putLong(TwaskListSubActivity.TAG_TOKEN, tag.getTagIdentifier().getId());
                switchToActivity(ActivityCode.TASK_LIST_W_TAG, bundle);
            }
        });

        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                    ContextMenuInfo menuInfo) {
                AdapterContextMenuInfo adapterMenuInfo =
                    (AdapterContextMenuInfo)menuInfo;
                int position = adapterMenuInfo.position;

                menu.add(position, CONTEXT_CREATE_ID, Menu.NONE,
                        R.string.tagList_context_create);
                menu.add(position, CONTEXT_DELETE_ID, Menu.NONE,
                        R.string.tagList_context_delete);

                int showHideLabel = R.string.tagList_context_hideTag;
                if(tagArray.get(position).shouldHideFromMainList())
                    showHideLabel = R.string.tagList_context_showTag;
                menu.add(position, CONTEXT_SHOWHIDE_ID, Menu.NONE,
                        showHideLabel);

                menu.setHeaderTitle(tagArray.get(position).getName());
            }
        });


        listView.setOnTouchListener(getGestureListener());
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        fillData();
    }

    // --- list adapter

    private void createTask(TagModelForView tag) {
        Intent intent = new Intent(getParent(), TwaskEdit.class);
        intent.putExtra(TwaskEdit.TAG_NAME_TOKEN, tag.getName());
        launchActivity(intent, ACTIVITY_CREATE);
    }

    private void deleteTag(final TagIdentifier tagId) {
        new AlertDialog.Builder(getParent())
            .setTitle(R.string.delete_title)
            .setMessage(R.string.delete_this_tag_title)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    getTagController().deleteTag(tagId);
                    fillData();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		switchToActivity(ActivityCode.TASK_LIST, null);
    		return true;
    	}
   	
    	return false;
    }

    @Override
    /** Picked item in the options list */
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case MENU_SORT_ALPHA_ID:
            if(sortMode == SortMode.ALPHA)
                sortReverse = !sortReverse;
            else {
                sortMode = SortMode.ALPHA;
                sortReverse = false;
            }
            fillData();
            return true;
        case MENU_SORT_SIZE_ID:
            if(sortMode == SortMode.SIZE)
                sortReverse = !sortReverse;
            else {
                sortMode = SortMode.SIZE;
                sortReverse = false;
            }
            fillData();
            return true;
        case CONTEXT_CREATE_ID:
            TagModelForView tag = tagArray.get(item.getGroupId());
            createTask(tag);
            return true;
        case CONTEXT_DELETE_ID:
            tag = tagArray.get(item.getGroupId());
            deleteTag(tag.getTagIdentifier());
            return true;
        case CONTEXT_SHOWHIDE_ID:
            tag = tagArray.get(item.getGroupId());
            tag.toggleHideFromMainList();
            getTagController().saveTag(tag);
            fillData();
            return true;
        }

        return false;
    }

    // --- creating stuff

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item;

        item = menu.add(Menu.NONE, MENU_SORT_ALPHA_ID, Menu.NONE,
                R.string.tagList_menu_sortAlpha);
        item.setIcon(android.R.drawable.ic_menu_sort_alphabetically);
        item.setAlphabeticShortcut('a');

        item = menu.add(Menu.NONE, MENU_SORT_SIZE_ID, Menu.NONE,
                R.string.tagList_menu_sortSize);
        item.setIcon(android.R.drawable.ic_menu_sort_by_size);
        item.setAlphabeticShortcut('s');

        return true;
    }
    
    private class TagListAdapter extends ArrayAdapter<TagModelForView> {

    	private List<TagModelForView> objects;
        private int resource;
        private Context context;
        private LayoutInflater inflater;

        public TagListAdapter(Context context, int resource,
                List<TagModelForView> objects) {
        	
            super(context, resource, objects);

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.objects = objects;
            this.resource = resource;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	View view = convertView;

            if(view == null) {
                view = inflater.inflate(resource, parent, false);
            }
            setupView(view, objects.get(position));

            return view;
        }

        public void setupView(View view, final TagModelForView tag) {
            
			Resources r = context.getResources();
            view.setTag(tag);

            final TextView name = ((TextView)view.findViewById(android.R.id.text1));
            
            name.setText(new StringBuilder(tag.getName()).
                    append(" (").append(tagToTaskCount.get(tag)).append(")"));

            if(tagToTaskCount.get(tag) == 0)
                name.setTextColor(r.getColor(R.color.task_list_done));
            else
            	name.setTextColor(Color.BLACK);            	
        }
    }

}