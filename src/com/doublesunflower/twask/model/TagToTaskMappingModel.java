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
import com.doublesunflower.twask.identifier.TagIdentifier;
import com.doublesunflower.twask.identifier.TaskIdentifier;


/** A single tag on a task */
public class TagToTaskMappingModel extends AbstractModel {

    /** Version number of this model */
    static final int                   VERSION             = 2;

    // field names

    public static final String                TASK                = "task";
    public static final String                TAG                 = "tag";

    /** Default values container */
    private static final ContentValues defaultValues       = new ContentValues();

    @Override
    public ContentValues getDefaultValues() {
        return defaultValues;
    }

    public static String[] FIELD_LIST = new String[] {
        AbstractController.KEY_ROWID,
        TASK,
        TAG,
    };

    // --- database helper

    /** Database Helper manages creating new tables and updating old ones */
    public static class TagToTaskMappingDatabaseHelper extends SQLiteOpenHelper {
        String tableName;

        public TagToTaskMappingDatabaseHelper(Context context, String databaseName, String tableName) {
            super(context, databaseName, null, VERSION);
            this.tableName = tableName;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = new StringBuilder().
            append("CREATE TABLE ").append(tableName).append(" (").
                append(AbstractController.KEY_ROWID).append(" integer primary key autoincrement, ").
                append(TASK).append(" integer not null,").
                append(TAG).append(" integer not null,").
                append("unique (").append(TASK).append(",").append(TAG).append(")").
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


    // --- constructor pass-through

    TagToTaskMappingModel(TaskIdentifier task, TagIdentifier tag) {
        super();
        setTask(task);
        setTag(tag);
    }

    public TagToTaskMappingModel(Cursor cursor) {
        super(cursor);
    }

    // --- getters and setters: expose them as you see fit

    public boolean isNew() {
        return getCursor() == null;
    }

    public TaskIdentifier getTask() {
        return new TaskIdentifier(retrieveInteger(TASK));
    }

    public TagIdentifier getTag() {
        return new TagIdentifier(retrieveInteger(TAG));
    }

    private void setTask(TaskIdentifier task) {
        setValues.put(TASK, task.getId());
    }

    private void setTag(TagIdentifier tag) {
        setValues.put(TAG, tag.getId());
    }
}
