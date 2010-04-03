/*
 * Copyright (C) 2009 Double Sunflower Holdings Corp.
 * @author Andrew Xu
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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.doublesunflower.R;
import com.doublesunflower.twask.controller.AlertController;
import com.doublesunflower.twask.controller.TagController;
import com.doublesunflower.twask.identifier.TagIdentifier;
import com.doublesunflower.twask.identifier.TaskIdentifier;
import com.doublesunflower.twask.model.TagModelForView;
import com.doublesunflower.twask.model.TaskModelForEdit;
import com.doublesunflower.twask.model.TaskModelForList;
import com.doublesunflower.twask.model.AbstractTaskModel.RepeatInfo;
import com.doublesunflower.twask.receiver.NotificationsReceiver;
import com.doublesunflower.twask.utils.enums.Importance;
import com.doublesunflower.twask.utils.enums.RepeatInterval;
import com.doublesunflower.twask.utils.other.Constants;
import com.doublesunflower.twask.utils.other.Preferences;
import com.doublesunflower.twask.view.widget.DateControlSet;
import com.doublesunflower.twask.view.widget.DateWithNullControlSet;
import com.doublesunflower.twask.view.widget.NumberPickerControlSet;
import com.doublesunflower.twask.view.widget.NumberPickerDialog;
import com.doublesunflower.twask.view.widget.TimeDurationControlSet;
import com.doublesunflower.twask.view.widget.NumberPickerDialog.OnNumberPickedListener;
import com.doublesunflower.twask.view.widget.TimeDurationControlSet.TimeDurationType;

/**
 * This activity is responsible for creating new tasks and editing existing
 * ones. It saves a task when it is paused (screen rotated, back button
 * pressed) as long as the task has a title.
 *
 * @author timsu
 *
 */
public class TwaskEdit extends TwaskEditTabActivity<TaskModelForEdit> {

    // bundle arguments
    public static final String     TAG_NAME_TOKEN   = "t";
    public static final String     START_CHAR_TOKEN = "s";

    // menu items
    private static final int       SAVE_ID          = Menu.FIRST;
    private static final int       DISCARD_ID       = Menu.FIRST + 1;
    private static final int       DELETE_ID        = Menu.FIRST + 2;

    // other constants
    private static final int       MAX_TAGS         = 5;
    private static final int       MAX_ALERTS       = 5;
    private static final String    TAB_BASIC        = "basic";
    private static final String    TAB_DATES        = "dates";
    private static final String    TAB_ALERTS       = "alerts";
    private static final int       DEFAULT_CAL_TIME = 3600;

    // UI components
    private EditText               name;
    private ImportanceControlSet   importance;
    private TimeDurationControlSet estimatedDuration;
    private TimeDurationControlSet elapsedDuration;
    private TimeDurationControlSet notification;
    private DateControlSet         definiteDueDate;
    private DateControlSet         preferredDueDate;
    private DateControlSet         hiddenUntil;
    private EditText               notes;
    private LinearLayout           tagsContainer;
    private NotifyFlagControlSet   flags;
    private LinearLayout           alertsContainer;
    private Button                 repeatValue;
    private Spinner                repeatInterval;
    private CheckBox               addToCalendar;

    // other instance variables
    private boolean                shouldSaveState = true;
    private boolean                repeatHelpShown = false;
    private TagController          tagController;
    private AlertController        alertController;
    private List<TagModelForView>  tags;
    private List<TagIdentifier>    taskTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tagController = new TagController(this);
        tagController.open();
        alertController = new AlertController(this);
        alertController.open();

        TabHost tabHost = getTabHost();
        tabHost.setPadding(0, 4, 0, 0);
        Resources r = getResources();

        LayoutInflater.from(this).inflate(R.layout.task_edit,
                tabHost.getTabContentView(), true);

        tabHost.addTab(tabHost.newTabSpec(TAB_BASIC)
                .setIndicator("Basic",
                        r.getDrawable(R.drawable.ic_dialog_info_c))
                .setContent(R.id.tab_basic));
        tabHost.addTab(tabHost.newTabSpec(TAB_DATES)
                .setIndicator("Dates",
                        r.getDrawable(R.drawable.ic_dialog_time_c))
                .setContent(R.id.tab_dates));
        tabHost.addTab(tabHost.newTabSpec(TAB_ALERTS)
                .setIndicator("Alerts",
                        r.getDrawable(R.drawable.ic_dialog_alert_c))
                .setContent(R.id.tab_notification));

        setUpUIComponents();
		setUpListeners();
    }

    @Override
    protected TaskModelForEdit getModel(TaskIdentifier identifier) {
        if (identifier != null)
            return controller.fetchTaskForEdit(this, identifier);
        else
            return controller.createNewTaskForEdit();
    }

    /* ======================================================================
     * =============================================== model reading / saving
     * ====================================================================== */

    /** Populate UI component values from the model */
    private void populateFields() {
        Resources r = getResources();

        // set UI components based on model variables
        if(model.getCursor() != null)
            startManagingCursor(model.getCursor());
        
        if(model.getTaskIdentifier() == null) {
        	Bundle extras = getIntent().getExtras();
        	if(extras != null && extras.containsKey(START_CHAR_TOKEN))
        		name.setText("" + extras.getChar(START_CHAR_TOKEN));
        } else
        	name.setText(model.getName());
        
        if(model.getName().length() > 0)
            setTitle(new StringBuilder().
                append(r.getString(R.string.taskEdit_titlePrefix)).
                append(" ").
                append(model.getName()));
        
        estimatedDuration.setTimeDuration(model.getEstimatedSeconds());
        elapsedDuration.setTimeDuration(model.getElapsedSeconds());
        importance.setImportance(model.getImportance());
        definiteDueDate.setDate(model.getDefiniteDueDate());
        preferredDueDate.setDate(model.getPreferredDueDate());
        hiddenUntil.setDate(model.getHiddenUntil());
        notification.setTimeDuration(model.getNotificationIntervalSeconds());
        flags.setValue(model.getNotificationFlags());
        notes.setText(model.getNotes());
        
        if(model.getTaskIdentifier() == null) {
            Integer reminder = Preferences.getDefaultReminder(this);
            if(reminder != null)
                notification.setTimeDuration(24*3600*reminder);
        }
        
        if(model.getCalendarUri() != null)
            addToCalendar.setText(r.getString(R.string.showCalendar_label));

        // tags
        tags = tagController.getAllTags(this);
        if(model.getTaskIdentifier() != null) {
            taskTags = tagController.getTaskTags(this, model.getTaskIdentifier());
            if(taskTags.size() > 0) {
                Map<TagIdentifier, TagModelForView> tagsMap =
                    new HashMap<TagIdentifier, TagModelForView>();
                for(TagModelForView tag : tags)
                    tagsMap.put(tag.getTagIdentifier(), tag);
                for(TagIdentifier id : taskTags) {
                    if(!tagsMap.containsKey(id))
                        continue;

                    TagModelForView tag = tagsMap.get(id);
                    addTag(tag.getName());
                }
            }
        } else {
            taskTags = new LinkedList<TagIdentifier>();

            Bundle extras = getIntent().getExtras();
            if(extras != null && extras.containsKey(TAG_NAME_TOKEN)) {
                addTag(extras.getString(TAG_NAME_TOKEN));
            }
        }
        addTag("");

        // alerts
        if(model.getTaskIdentifier() != null) {
            List<Date> alerts = alertController.getTaskAlerts(model.getTaskIdentifier());
            for(Date alert : alerts) {
                addAlert(alert);
            }
        }

        // repeats
        RepeatInfo repeatInfo = model.getRepeat();
        if(repeatInfo != null) {
            setRepeatValue(repeatInfo.getValue());
            repeatInterval.setSelection(repeatInfo.getInterval().ordinal());
        } else
            setRepeatValue(0);
    }

    /** Save task model from values in UI components */
    private void save() {
        // don't save if user accidentally created a new task
        if(name.getText().length() == 0)
            return;

        model.setName(name.getText().toString());
        model.setEstimatedSeconds(estimatedDuration.getTimeDurationInSeconds());
        model.setElapsedSeconds(elapsedDuration.getTimeDurationInSeconds());
        model.setImportance(importance.getImportance());
        model.setDefiniteDueDate(definiteDueDate.getDate());
        model.setPreferredDueDate(preferredDueDate.getDate());
        model.setHiddenUntil(hiddenUntil.getDate());
        model.setNotificationFlags(flags.getValue());
        model.setNotes(notes.getText().toString());
        model.setNotificationIntervalSeconds(notification.getTimeDurationInSeconds());
        model.setRepeat(getRepeatValue());

        try {
            // write out to database
            controller.saveTask(model);
            saveTags();
            saveAlerts();
            NotificationsReceiver.updateAlarm(this, controller, alertController, model);
        } catch (Exception e) {
            Log.e("astrid", "Error saving", e);
        }
    }

    /** Save task tags. Must be called after task already has an ID */
    private void saveTags() {
        Set<TagIdentifier> tagsToDelete;
        Set<TagIdentifier> tagsToAdd;

        HashSet<String> tagNames = new HashSet<String>();
        for(int i = 0; i < tagsContainer.getChildCount(); i++) {
            TextView tagName = (TextView)tagsContainer.getChildAt(i).findViewById(R.id.text1);
            if(tagName.getText().length() == 0)
                continue;
            tagNames.add(tagName.getText().toString());
        }

        // map names to tag identifiers, creating them if necessary
        HashSet<TagIdentifier> tagIds = new HashSet<TagIdentifier>();
        HashMap<String, TagIdentifier> tagsByName = new HashMap<String, TagIdentifier>();
        for(TagModelForView tag : tags)
            tagsByName.put(tag.getName(), tag.getTagIdentifier());
        for(String tagName : tagNames) {
            if(tagsByName.containsKey(tagName))
                tagIds.add(tagsByName.get(tagName));
            else {
                TagIdentifier newTagId = tagController.createTag(tagName);
                tagIds.add(newTagId);
            }
        }

        // intersect tags to figure out which we need to add / remove
        tagsToDelete = new HashSet<TagIdentifier>(taskTags);
        tagsToDelete.removeAll(tagIds);
        tagsToAdd = tagIds;
        tagsToAdd.removeAll(taskTags);

        // perform the database updates
        for(TagIdentifier tagId : tagsToDelete)
            tagController.removeTag(model.getTaskIdentifier(), tagId);
        for(TagIdentifier tagId : tagsToAdd)
            tagController.addTag(model.getTaskIdentifier(), tagId);
    }

    /** Helper method to save alerts for this task */
    private void saveAlerts() {
        alertController.removeAlerts(model.getTaskIdentifier());

        for(int i = 0; i < alertsContainer.getChildCount(); i++) {
            DateControlSet dateControlSet = (DateControlSet)alertsContainer.
                getChildAt(i).getTag();
            Date date = dateControlSet.getDate();
            alertController.addAlert(model.getTaskIdentifier(), date);
        }
    }

    /* ======================================================================
     * ==================================================== UI initialization
     * ====================================================================== */

    /** Initialize UI components */
    private void setUpUIComponents() {
        
    	Resources r = getResources();
        
        setTitle(new StringBuilder()
            .append(r.getString(R.string.taskEdit_titleGeneric)));

        // populate Basic Tab instance variables
        name = (EditText)findViewById(R.id.name);
        importance = new ImportanceControlSet(R.id.importance_container);
        tagsContainer = (LinearLayout)findViewById(R.id.tags_container);
        estimatedDuration = new TimeDurationControlSet(this,
                R.id.estimatedDuration, 0, R.string.hour_minutes_dialog,
                TimeDurationType.HOURS_MINUTES);
        elapsedDuration = new TimeDurationControlSet(this, R.id.elapsedDuration,
                0, R.string.hour_minutes_dialog,
                TimeDurationType.HOURS_MINUTES);   
        notes = (EditText)findViewById(R.id.notes);
        
     // populate Date Tab instance variables
        definiteDueDate = new DateWithNullControlSet(this, R.id.definiteDueDate_notnull,
                R.id.definiteDueDate_date, R.id.definiteDueDate_time);
        preferredDueDate = new DateWithNullControlSet(this, R.id.preferredDueDate_notnull,
                R.id.preferredDueDate_date, R.id.preferredDueDate_time);
        addToCalendar = (CheckBox)findViewById(R.id.add_to_calendar);
        hiddenUntil = new DateWithNullControlSet(this, R.id.hiddenUntil_notnull,
                R.id.hiddenUntil_date, R.id.hiddenUntil_time);
        repeatInterval = (Spinner)findViewById(R.id.repeat_interval);
        repeatValue = (Button)findViewById(R.id.repeat_value);
        ArrayAdapter<String> repeatAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                RepeatInterval.getLabels(getResources()));
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatInterval.setAdapter(repeatAdapter);
        
     // populate Alert Tab instance variables
        flags = new NotifyFlagControlSet(R.id.flag_before,
                R.id.flag_during, R.id.flag_after, R.id.flag_nonstop);
        
        
        alertsContainer = (LinearLayout)findViewById(R.id.alert_container);
        notification = new TimeDurationControlSet(this, R.id.notification,
                R.string.notification_prefix, R.string.notification_dialog,
                TimeDurationType.DAYS_HOURS);
       
    }

    /** Set up button listeners */
    private void setUpListeners() {
    	
        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveButtonClick();
            }
        });
        Button discardButton = (Button) findViewById(R.id.discard);
        discardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                discardButtonClick();
            }
        });
        Button deleteButton = (Button) findViewById(R.id.delete);
        if(model.getTaskIdentifier() == null)
            deleteButton.setVisibility(View.GONE);
        else {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    deleteButtonClick();
                }
            });
        }
        Button addAlertButton = (Button) findViewById(R.id.addAlert);
        addAlertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addAlert(null);
            }
        });
        repeatValue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repeatValueClick();
            }
        });
    }

    /** Set up the repeat value button */
    private void setRepeatValue(int value) {
        if(value == 0)
            repeatValue.setText(R.string.repeat_value_unset);
        else
            repeatValue.setText(Integer.toString(value));
        repeatValue.setTag(value);
    }

    private RepeatInfo getRepeatValue() {
        if(repeatValue.getTag().equals(0))
            return null;
        return new RepeatInfo(RepeatInterval.values()
                    [repeatInterval.getSelectedItemPosition()],
                (Integer)repeatValue.getTag());
    }

    /** Adds an alert to the alert field */
    private boolean addAlert(Date alert) {
        if(alertsContainer.getChildCount() >= MAX_ALERTS)
            return false;

        LayoutInflater inflater = getLayoutInflater();
        final View alertItem = inflater.inflate(R.layout.edit_alert_item, null);
        alertsContainer.addView(alertItem);

        DateControlSet dcs = new DateControlSet(this,
                (Button)alertItem.findViewById(R.id.date),
                (Button)alertItem.findViewById(R.id.time));
        dcs.setDate(alert);
        alertItem.setTag(dcs);

        ImageButton reminderRemoveButton;
        reminderRemoveButton = (ImageButton)alertItem.findViewById(R.id.button1);
        reminderRemoveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertsContainer.removeView(alertItem);
            }
        });

        return true;
    }

    /** Adds a tag to the tag field */
    private boolean addTag(String tagName) {
        if (tagsContainer.getChildCount() >= MAX_TAGS) {
            return false;
        }

        LayoutInflater inflater = getLayoutInflater();
        final View tagItem = inflater.inflate(R.layout.edit_tag_item, null);
        tagsContainer.addView(tagItem);

        AutoCompleteTextView textView = (AutoCompleteTextView)tagItem.
            findViewById(R.id.text1);
        textView.setText(tagName);
        ArrayAdapter<TagModelForView> tagsAdapter =
            new ArrayAdapter<TagModelForView>(this,
                    android.R.layout.simple_dropdown_item_1line, tags);
        textView.setAdapter(tagsAdapter);
        textView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if(start == 0 && tagsContainer.getChildAt(
                        tagsContainer.getChildCount()-1) == tagItem) {
                    addTag("");
                }
            }

            public void afterTextChanged(Editable s) {
                //
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                //
            }
        });

        ImageButton reminderRemoveButton;
        reminderRemoveButton = (ImageButton)tagItem.findViewById(R.id.button1);
        reminderRemoveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tagsContainer.removeView(tagItem);
            }
        });

        return true;
    }

    /* ======================================================================
     * ======================================================= event handlers
     * ====================================================================== */

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus && TwaskViewFlipper.shouldCloseInstance) { // user wants to quit
            finish();
        }
    }

    private void saveButtonClick() {
        setResult(RESULT_OK);
        finish();
    }

    private void discardButtonClick() {
        shouldSaveState = false;
        setResult(RESULT_CANCELED);
        finish();
    }

    private void deleteButtonClick() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_title)
            .setMessage(R.string.delete_this_task_title)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    controller.deleteTask(model.getTaskIdentifier());
                    shouldSaveState = false;
                    setResult(Constants.RESULT_GO_HOME);
                    finish();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private void repeatValueClick() {
        final int tagValue = (Integer)repeatValue.getTag();
        if(tagValue > 0)
            repeatHelpShown = true;

        final Runnable openDialogRunnable = new Runnable() {
            public void run() {
                repeatHelpShown = true;

                int dialogValue = tagValue;
                if(dialogValue == 0)
                    dialogValue = 1;

                new NumberPickerDialog(TwaskEdit.this, new OnNumberPickedListener() {
                    public void onNumberPicked(NumberPickerControlSet view, int number) {
                        setRepeatValue(number);
                    }
                }, getResources().getString(R.string.repeat_picker_title),
                dialogValue, 1, 0, 31).show();
            }
        };

        if(repeatHelpShown || !Preferences.shouldShowRepeatHelp(this)) {
            openDialogRunnable.run();
            return;
        }

        new AlertDialog.Builder(this)
        .setTitle(R.string.repeat_help_dialog_title)
        .setMessage(R.string.repeat_help_dialog)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openDialogRunnable.run();
            }
        })
        .setNeutralButton(R.string.repeat_help_hide,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Preferences.setShowRepeatHelp(TwaskEdit.this, false);
                openDialogRunnable.run();
            }
        })
        .show();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case SAVE_ID:
            saveButtonClick();
            return true;
        case DISCARD_ID:
            discardButtonClick();
            return true;
        case DELETE_ID:
            deleteButtonClick();
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item;

        item = menu.add(Menu.NONE, SAVE_ID, 0, R.string.save_label);
        item.setIcon(android.R.drawable.ic_menu_save);
        item.setAlphabeticShortcut('s');

        item = menu.add(Menu.NONE, DISCARD_ID, 0, R.string.discard_label);
        item.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        item.setAlphabeticShortcut('c');

        if (model.getTaskIdentifier() != null) {
            item = menu.add(Menu.NONE, DELETE_ID, 0, R.string.delete_label);
            item.setIcon(android.R.drawable.ic_menu_delete);
            item.setAlphabeticShortcut('d');
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        save();
        super.onSaveInstanceState(outState);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(TAG_NAME_TOKEN))
            outState.putString(TAG_NAME_TOKEN,
                    extras.getString(TAG_NAME_TOKEN));
    }

    @Override
    protected void onPause() {
        // create calendar event
        if(addToCalendar.isChecked() && model.getCalendarUri() == null) {
            Uri uri = Uri.parse("content://calendar/events");
            ContentResolver cr = getContentResolver();

            ContentValues values = new ContentValues();
            values.put("title", name.getText().toString());
            values.put("calendar_id", 1);
            values.put("description", notes.getText().toString());
            values.put("hasAlarm", 0);
            values.put("transparency", 0);
            values.put("visibility", 0);

            Long deadlineDate = null;
            if (model.getPreferredDueDate() != null)
                deadlineDate = model.getPreferredDueDate().getTime();
            else if (model.getDefiniteDueDate() != null)
                deadlineDate = model.getDefiniteDueDate().getTime();
            else
                deadlineDate = System.currentTimeMillis() + 24*3600*1000L;

            int estimatedTime = DEFAULT_CAL_TIME;
            if(model.getEstimatedSeconds() != null &&
                    model.getEstimatedSeconds() > 0) {
                estimatedTime = model.getEstimatedSeconds();
            }
            values.put("dtstart", deadlineDate - estimatedTime * 1000L);
            values.put("dtend", deadlineDate);

            Uri result = cr.insert(uri, values);
            if(result != null)
                model.setCalendarUri(result.toString());
            else
                Log.e("twask", "Error creating calendar event!");
        }

        if(shouldSaveState)
            save();

        if(addToCalendar.isChecked() && model.getCalendarUri() != null) {
            Uri result = Uri.parse(model.getCalendarUri());
            Intent intent = new Intent(Intent.ACTION_EDIT, result);

            // recalculate dates
            Long deadlineDate = null;
            if (model.getPreferredDueDate() != null)
                deadlineDate = model.getPreferredDueDate().getTime();
            else if (model.getDefiniteDueDate() != null)
                deadlineDate = model.getDefiniteDueDate().getTime();
            else
                deadlineDate = System.currentTimeMillis() + 24*3600*1000L;

            int estimatedTime = DEFAULT_CAL_TIME;
            if(model.getEstimatedSeconds() != null &&
                    model.getEstimatedSeconds() > 0) {
                estimatedTime = model.getEstimatedSeconds();
            }
            intent.putExtra("beginTime", deadlineDate - estimatedTime * 1000L);
            intent.putExtra("endTime", deadlineDate);

            startActivity(intent);
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tagController.close();
        alertController.close();
    }

    /* ======================================================================
     * ========================================== UI component helper classes
     * ====================================================================== */

    /** Control set dealing with notification flags */
    public class NotifyFlagControlSet {
        private CheckBox before, during, after, nonstop;

        public NotifyFlagControlSet(int beforeId, int duringId,
                int afterId, int nonstopId) {
            before = (CheckBox)findViewById(beforeId);
            during = (CheckBox)findViewById(duringId);
            after = (CheckBox)findViewById(afterId);
            nonstop = (CheckBox)findViewById(nonstopId);
        }

        public void setValue(int flags) {
            before.setChecked((flags &
                    TaskModelForEdit.NOTIFY_BEFORE_DEADLINE) > 0);
            during.setChecked((flags &
                    TaskModelForEdit.NOTIFY_AT_DEADLINE) > 0);
            after.setChecked((flags &
                    TaskModelForEdit.NOTIFY_AFTER_DEADLINE) > 0);
            nonstop.setChecked((flags &
                    TaskModelForEdit.NOTIFY_NONSTOP) > 0);
        }

        public int getValue() {
            int value = 0;
            if(before.isChecked())
                value |= TaskModelForEdit.NOTIFY_BEFORE_DEADLINE;
            if(during.isChecked())
                value |= TaskModelForEdit.NOTIFY_AT_DEADLINE;
            if(after.isChecked())
                value |= TaskModelForEdit.NOTIFY_AFTER_DEADLINE;
            if(nonstop.isChecked())
                value |= TaskModelForEdit.NOTIFY_NONSTOP;
            return value;
        }
    }

    /** Control set dealing with importance */
    public class ImportanceControlSet {
        private List<CompoundButton> buttons = new LinkedList<CompoundButton>();

        public ImportanceControlSet(int containerId) {
            LinearLayout layout = (LinearLayout)findViewById(containerId);
            Resources r = getResources();

            for(Importance i : Importance.values()) {
                final ToggleButton button = new ToggleButton(TwaskEdit.this);
                button.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1));
                button.setTextColor(r.getColor(i.getColorResource()));
                button.setTextOff(r.getString(i.getLabelResource()));
                button.setTextOn(r.getString(i.getLabelResource()));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setImportance((Importance)button.getTag());
                    }
                });
                button.setTag(i);

                buttons.add(button);
                layout.addView(button);
            }
        }

        public void setImportance(Importance i) {
            for(CompoundButton b : buttons) {
                if(b.getTag() == i) {
                    b.setTextSize(24);
                    b.setChecked(true);
                } else {
                    b.setTextSize(16);
                    b.setChecked(false);
                }
            }
        }

        public Importance getImportance() {
            for(CompoundButton b : buttons)
                if(b.isChecked())
                    return (Importance)b.getTag();
            return Importance.DEFAULT;
        }
    }

    /** Control set dealing with "blocking on" */
/*
    public class BlockingOnControlSet  {

        private CheckBox activatedCheckBox;
        private Spinner taskBox;

        public BlockingOnControlSet(int checkBoxId, int taskBoxId) {
            activatedCheckBox = (CheckBox)findViewById(checkBoxId);
            taskBox = (Spinner)findViewById(taskBoxId);

            Cursor tasks = controller.getActiveTaskListCursor();
            startManagingCursor(tasks);
            SimpleCursorAdapter tasksAdapter = new SimpleCursorAdapter(TwaskEdit.this,
                    android.R.layout.simple_list_item_1, tasks,
                    new String[] { TaskModelForList.getNameField() },
                    new int[] { android.R.id.text1 });
            taskBox.setAdapter(tasksAdapter);

            activatedCheckBox.setOnCheckedChangeListener(
                    new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView,
                        boolean isChecked) {
                    taskBox.setEnabled(isChecked);
                }
            });

        }

        public void setBlockingOn(TaskIdentifier value) {
            activatedCheckBox.setChecked(value != null);
            if(value == null) {
                return;
            }

            for(int i = 0; i < taskBox.getCount(); i++)
                if(taskBox.getItemIdAtPosition(i) == value.getId()) {
                    taskBox.setSelection(i);
                    return;
                }

            // not found
            activatedCheckBox.setChecked(false);
        }

        public TaskIdentifier getBlockingOn() {
            if(!activatedCheckBox.isChecked())
                return null;

            return new TaskIdentifier(taskBox.getSelectedItemId());
        }
    }
    */
}
