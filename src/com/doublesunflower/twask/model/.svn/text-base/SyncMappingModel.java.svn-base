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
package com.doublesunflower.twask.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.doublesunflower.twask.controller.AbstractController;
import com.doublesunflower.twask.identifier.TaskIdentifier;
import com.doublesunflower.twask.utils.sync.TaskProxy;


/** A single tag on a task */
public class SyncMappingModel extends AbstractModel {


    /** Version number of this model */
    static final int                   VERSION             = 1;

    // field names

    public static final String                TASK          = "task";
    public static final String                SYNC_SERVICE  = "service";
    static final String                		  REMOTE_ID     = "remoteId";
    public static final String                UPDATED       = "updated";

    /** Default values container */
    private static final ContentValues defaultValues = new ContentValues();
    static {
        defaultValues.put(UPDATED, 0);
    }

    @Override
    public ContentValues getDefaultValues() {
        return defaultValues;
    }

    public static String[] FIELD_LIST = new String[] {
        AbstractController.KEY_ROWID,
        TASK,
        SYNC_SERVICE,
        REMOTE_ID,
        UPDATED,
    };

   

    // --- constructor pass-through

    public SyncMappingModel(TaskIdentifier task, TaskProxy taskProxy) {
        this(task, taskProxy.getSyncServiceId(), taskProxy.getRemoteId());
    }

    public SyncMappingModel(TaskIdentifier task, int syncServiceId, String remoteId) {
        super();
        setTask(task);
        setSyncServiceId(syncServiceId);
        setRemoteId(remoteId);
    }

    public SyncMappingModel(Cursor cursor) {
        super(cursor);
        getId();
        getTask();
        getSyncServiceId();
        getRemoteId();
        isUpdated();
    }

    // --- getters and setters

    public long getId() {
        return retrieveLong(AbstractController.KEY_ROWID);
    }

    public TaskIdentifier getTask() {
        return new TaskIdentifier(retrieveLong(TASK));
    }

    public int getSyncServiceId() {
        return retrieveInteger(SYNC_SERVICE);
    }

    public String getRemoteId() {
        return retrieveString(REMOTE_ID);
    }

    public boolean isUpdated() {
        return retrieveInteger(UPDATED) == 1;
    }

    private void setTask(TaskIdentifier task) {
        setValues.put(TASK, task.getId());
    }

    private void setSyncServiceId(int id) {
        setValues.put(SYNC_SERVICE, id);
    }

    private void setRemoteId(String remoteId) {
        setValues.put(REMOTE_ID, remoteId);
    }
    
    // --- database helper

    /** Database Helper manages creating new tables and updating old ones */
    public static class SyncMappingDatabaseHelper extends SQLiteOpenHelper {
        String tableName;

        public SyncMappingDatabaseHelper(Context context, String databaseName, String tableName) {
            super(context, databaseName, null, VERSION);
            this.tableName = tableName;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = new StringBuilder().
            append("CREATE TABLE ").append(tableName).append(" (").
                append(AbstractController.KEY_ROWID).append(" integer primary key autoincrement, ").
                append(TASK).append(" integer not null,").
                append(SYNC_SERVICE).append(" integer not null,").
                append(REMOTE_ID).append(" text not null,").
                append(UPDATED).append(" integer not null,").
                append("unique (").append(TASK).append(",").append(SYNC_SERVICE).append(")").
            append(");").toString();
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(getClass().getSimpleName(), "Upgrading database from version " +
                    oldVersion + " to " + newVersion + ".");

            switch(oldVersion) {
            default:
                // we don't know how to handle it... do the unfortunate thing
                Log.e(getClass().getSimpleName(), "Unsupported migration, table dropped!");
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
                onCreate(db);
            }
        }
    }

    
}
