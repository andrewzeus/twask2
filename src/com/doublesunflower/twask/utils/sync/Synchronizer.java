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
package com.doublesunflower.twask.utils.sync;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.doublesunflower.twask.controller.AbstractController;
import com.doublesunflower.twask.controller.AlertController;
import com.doublesunflower.twask.controller.SyncMappingController;
import com.doublesunflower.twask.controller.TagController;
import com.doublesunflower.twask.controller.TaskController;
import com.doublesunflower.twask.utils.other.Preferences;

public class Synchronizer {

    private static final int SYNC_ID_RTM = 1;

    // --- public interface

    public interface SynchronizerListener {
        void onSynchronizerFinished(int numServicesSynced);
    }

    /** Synchronize all activated sync services */
    public static void synchronize(Activity activity, boolean isAutoSync,
            SynchronizerListener listener) {
        currentStep = ServiceWrapper._FIRST_SERVICE.ordinal();
        servicesSynced = 0;
        autoSync = isAutoSync;
        callback = listener;
        continueSynchronization(activity);
    }


    /** Clears tokens if services are disabled */
    public static void clearUserData(Activity activity) {
        for(ServiceWrapper serviceWrapper : ServiceWrapper.values()) {
            if(serviceWrapper.isActivated(activity)) {
                serviceWrapper.service.clearPersonalData(activity);
            }
        }
    }

    // --- internal synchronization logic

    /** Synchronization Services enumeration
     *    note that id must be kept constant!
     * @author timsu
     *
     */
    private enum ServiceWrapper {
        _FIRST_SERVICE(null) { // must be first entry
            @Override
            boolean isActivated(Context arg0) {
                return false;
            }
        },

        RTM(new RTMSyncService(SYNC_ID_RTM)) {
            @Override
            boolean isActivated(Context context) {
                return Preferences.shouldSyncRTM(context);
            }
        },

        _LAST_SERVICE(null) { // must be last entry
            @Override
            boolean isActivated(Context arg0) {
                return false;
            }
        };

        private SyncService service;

        private ServiceWrapper(SyncService service) {
            this.service = service;
        }

        abstract boolean isActivated(Context context);
    }

    // Internal state for the synchronization process

    /** Current step in the sync process */
    private static int currentStep;

    /** # of services synchronized */
    private static int servicesSynced;

    /** On finished callback */
    private static SynchronizerListener callback;

    /** If this synchronization was automatically initiated */
    private static boolean autoSync;


    /** Called to do the next step of synchronization. Run me on the UI thread! */
    static void continueSynchronization(Activity activity) {
        ServiceWrapper serviceWrapper =
            ServiceWrapper.values()[currentStep];
        currentStep++;
        switch(serviceWrapper) {
        case _FIRST_SERVICE:
            continueSynchronization(activity);
            break;
        case RTM:
            if(Preferences.shouldSyncRTM(activity)) {
                servicesSynced++;
                serviceWrapper.service.synchronizeService(activity);
            } else {
                continueSynchronization(activity);
            }
            break;
        case _LAST_SERVICE:
            finishSynchronization(activity);
        }
    }

    /** Called at the end of sync. */
    private static void finishSynchronization(final Activity activity) {
        closeControllers();
        Preferences.setSyncLastSync(activity, new Date());
        if(callback != null)
            callback.onSynchronizerFinished(servicesSynced);
    }

    /** Was this sync automatically initiated? */
    static boolean isAutoSync() {
        return autoSync;
    }

    // --- controller stuff

    private static class ControllerWrapper<TYPE extends AbstractController> {
        TYPE controller;
        Class<TYPE> typeClass;
        boolean override;

        public ControllerWrapper(Class<TYPE> cls) {
            override = false;
            controller = null;
            typeClass = cls;
        }

        public TYPE get(Activity activity) {
            if(controller == null) {
                try {
                    controller = (TYPE) typeClass.getConstructors()[0].newInstance(
                            activity);
                } catch (IllegalArgumentException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                } catch (SecurityException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                } catch (InstantiationException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                } catch (IllegalAccessException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                } catch (InvocationTargetException e) {
                    Log.e(getClass().getSimpleName(), e.toString());
                }
                controller.open();
            }
            return controller;
        }

        public void set(TYPE newController) {
            close();

            override = newController != null;
            controller = newController;
        }

        public void close() {
            if(controller != null && !override) {
                controller.close();
                controller = null;
            }
        }
    }

   private static ControllerWrapper<SyncMappingController> syncController =
        new ControllerWrapper<SyncMappingController>(SyncMappingController.class);
    private static ControllerWrapper<TaskController> taskController =
        new ControllerWrapper<TaskController>(TaskController.class);
    private static ControllerWrapper<TagController> tagController =
        new ControllerWrapper<TagController>(TagController.class);
    private static ControllerWrapper<AlertController> alertController =
        new ControllerWrapper<AlertController>(AlertController.class);

    static SyncMappingController getSyncController(Activity activity) {
        return syncController.get(activity);
    }

    static TaskController getTaskController(Activity activity) {
        return taskController.get(activity);
    }

    static TagController getTagController(Activity activity) {
        return tagController.get(activity);
    }

    static AlertController getAlertController(Activity activity) {
        return alertController.get(activity);
    }

    public static void setTaskController(TaskController taskController) {
        Synchronizer.taskController.set(taskController);
    }

    public static void setTagController(TagController tagController) {
        Synchronizer.tagController.set(tagController);
    }

    private static void closeControllers() {
        syncController.close();
        taskController.close();
        tagController.close();
        alertController.close();
    }
}
