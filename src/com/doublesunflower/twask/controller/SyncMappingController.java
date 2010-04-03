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

import java.util.HashSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.doublesunflower.twask.identifier.TaskIdentifier;
import com.doublesunflower.twask.model.SyncMappingModel;
import com.doublesunflower.twask.model.SyncMappingModel.SyncMappingDatabaseHelper;

/** Controller for Sync-related operations */
public class SyncMappingController extends AbstractController {

    private SQLiteDatabase syncDatabase;


    // --- updated tasks list

    /** Mark all updated tasks as finished synchronizing */
    public boolean clearUpdatedTaskList(int syncServiceId) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(SyncMappingModel.UPDATED, 0);
        return syncDatabase.update(SYNC_TABLE_NAME, values,
                SyncMappingModel.SYNC_SERVICE + " = " + syncServiceId, null) > 0;
    }

    /** Indicate that this task's properties were updated */
    public boolean addToUpdatedList(TaskIdentifier taskId) throws SQLException {
        ContentValues values = new ContentValues();
        values.put(SyncMappingModel.UPDATED, 1);
        return syncDatabase.update(SYNC_TABLE_NAME, values,
                SyncMappingModel.TASK + " = " + taskId.getId(), null) > 0;
    }

    // --- sync mapping

    /** Get all mappings for the given synchronization service */
    public HashSet<SyncMappingModel> getSyncMapping(int syncServiceId) throws SQLException {
        HashSet<SyncMappingModel> list = new HashSet<SyncMappingModel>();
        Cursor cursor = syncDatabase.query(SYNC_TABLE_NAME,
                SyncMappingModel.FIELD_LIST,
                SyncMappingModel.SYNC_SERVICE + " = " + syncServiceId,
                null, null, null, null);

        try {
            if(cursor.getCount() == 0)
                return list;
            do {
                cursor.moveToNext();
                list.add(new SyncMappingModel(cursor));
            } while(!cursor.isLast());

            return list;
        } finally {
            cursor.close();
        }
    }

    /** Saves the given task to the database. Returns true on success. */
    public boolean saveSyncMapping(SyncMappingModel mapping) {
        long newRow = syncDatabase.insert(SYNC_TABLE_NAME, SyncMappingModel.TASK,
                mapping.getMergedValues());

        return newRow >= 0;
    }

    /** Deletes the given mapping. Returns true on success */
    public boolean deleteSyncMapping(SyncMappingModel mapping) {
        return syncDatabase.delete(SYNC_TABLE_NAME, KEY_ROWID + "=" +
                mapping.getId(), null) > 0;
    }

    /** Deletes the given mapping. Returns true on success */
    public boolean deleteAllMappings(int syncServiceId) {
        return syncDatabase.delete(SYNC_TABLE_NAME, SyncMappingModel.SYNC_SERVICE +
                "=" + syncServiceId, null) > 0;
    }

    // --- boilerplate

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     */
    public SyncMappingController(Context context) {
        this.context = context;
    }

    /**
     * Open the Sync data database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    @Override
    public void open() throws SQLException {
        SQLiteOpenHelper helper = new SyncMappingDatabaseHelper(context,
                SYNC_TABLE_NAME, SYNC_TABLE_NAME);
        syncDatabase = helper.getWritableDatabase();
    }

    /** Closes database resource */
    @Override
    public void close() {
        syncDatabase.close();
    }
}
