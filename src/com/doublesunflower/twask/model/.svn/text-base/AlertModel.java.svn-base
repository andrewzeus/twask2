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

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.doublesunflower.twask.controller.AbstractController;
import com.doublesunflower.twask.identifier.TaskIdentifier;


/** A single alert on a task */
public class AlertModel extends AbstractModel {

    /** Version number of this model */
    static final int                   VERSION             = 1;

    // field names

    public static final String                TASK                = "task";
    public static final String                DATE                = "date";

    /** Default values container */
    private static final ContentValues defaultValues       = new ContentValues();

    @Override
    public ContentValues getDefaultValues() {
        return defaultValues;
    }

    public static String[] FIELD_LIST = new String[] {
        AbstractController.KEY_ROWID,
        TASK,
        DATE,
    };

   
    // --- constructor pass-through

    AlertModel(TaskIdentifier task, Date date) {
        super();
        setTask(task);
        setDate(date);
    }

    public AlertModel(Cursor cursor) {
        super(cursor);
    }

    // --- getters and setters: expose them as you see fit

    public boolean isNew() {
        return getCursor() == null;
    }

    public TaskIdentifier getTask() {
        return new TaskIdentifier(retrieveLong(TASK));
    }

    public Date getDate() {
        return new Date(retrieveLong(DATE));
    }

    private void setTask(TaskIdentifier task) {
        setValues.put(TASK, task.getId());
    }

    private void setDate(Date date) {
        setValues.put(DATE, date.getTime());
    }
    
    // --- database helper

    /** Database Helper manages creating new tables and updating old ones */
    public static class AlertDatabaseHelper extends SQLiteOpenHelper {
        String tableName;

        public AlertDatabaseHelper(Context context, String databaseName, String tableName) {
            super(context, databaseName, null, VERSION);
            this.tableName = tableName;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = new StringBuilder().
            append("CREATE TABLE ").append(tableName).append(" (").
                append(AbstractController.KEY_ROWID).append(" integer primary key autoincrement, ").
                append(TASK).append(" integer not null,").
                append(DATE).append(" integer not null,").
                append("unique (").append(TASK).append(",").append(DATE).append(")").
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
