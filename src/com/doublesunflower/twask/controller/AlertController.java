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
package com.doublesunflower.twask.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.doublesunflower.twask.identifier.TaskIdentifier;
import com.doublesunflower.twask.model.AlertModel;
import com.doublesunflower.twask.model.AlertModel.AlertDatabaseHelper;

/** Controller for Tag-related operations */
public class AlertController extends AbstractController {

    private SQLiteDatabase alertDatabase;

    /** Get a cursor to task identifiers */
    public Cursor getTaskAlertsCursor(TaskIdentifier taskId) throws SQLException {
        Cursor cursor = alertDatabase.query(ALERT_TABLE_NAME,
                AlertModel.FIELD_LIST, AlertModel.TASK + " = ?",
                new String[] { taskId.idAsString() }, null, null, null);
        return cursor;
    }

    /** Get a list of alerts for the given task */
    public List<Date> getTaskAlerts(TaskIdentifier
            taskId) throws SQLException {
        List<Date> list = new LinkedList<Date>();
        Cursor cursor = alertDatabase.query(ALERT_TABLE_NAME,
                AlertModel.FIELD_LIST, AlertModel.TASK + " = ?",
                new String[] { taskId.idAsString() }, null, null, null);

        try {
            if(cursor.getCount() == 0)
                return list;
            do {
                cursor.moveToNext();
                list.add(new AlertModel(cursor).getDate());
            } while(!cursor.isLast());

            return list;
        } finally {
            cursor.close();
        }
    }


    /** Get a list of alerts that are set for the future */
    public Set<TaskIdentifier> getTasksWithActiveAlerts() throws SQLException {
        Set<TaskIdentifier> list = new HashSet<TaskIdentifier>();
        Cursor cursor = alertDatabase.query(ALERT_TABLE_NAME,
                AlertModel.FIELD_LIST, AlertModel.DATE + " > ?",
                new String[] { Long.toString(System.currentTimeMillis()) }, null, null, null);

        try {
            if(cursor.getCount() == 0)
                return list;
            do {
                cursor.moveToNext();
                list.add(new AlertModel(cursor).getTask());
            } while(!cursor.isLast());

            return list;
        } finally {
            cursor.close();
        }
    }

    /** Remove all alerts from the task */
    public boolean removeAlerts(TaskIdentifier taskId)
            throws SQLException{
        return alertDatabase.delete(ALERT_TABLE_NAME,
                String.format("%s = ?",
                        AlertModel.TASK),
                new String[] { taskId.idAsString() }) > 0;
    }

    /** Add the given tag to the task */
    public boolean addAlert(TaskIdentifier taskId, Date date)
            throws SQLException {
        ContentValues values = new ContentValues();
        values.put(AlertModel.DATE, date.getTime());
        values.put(AlertModel.TASK, taskId.getId());
        return alertDatabase.insert(ALERT_TABLE_NAME, AlertModel.TASK,
                values) >= 0;
    }

    // --- boilerplate
    public AlertController(Context context) {
        this.context = context;
    }
    
    @Override
    public void open() throws SQLException {
        alertDatabase = new AlertDatabaseHelper(context,
                ALERT_TABLE_NAME, ALERT_TABLE_NAME).getWritableDatabase();
    }

    @Override
    public void close() {
        alertDatabase.close();
    }
}
