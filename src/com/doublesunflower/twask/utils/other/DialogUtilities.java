package com.doublesunflower.twask.utils.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import com.doublesunflower.R;
import com.doublesunflower.twask.view.widget.NNumberPickerDialog;
import com.doublesunflower.twask.view.widget.NNumberPickerDialog.OnNNumberPickedListener;

public class DialogUtilities {

    /**
     * Displays a dialog box with an OK button
     *
     * @param context
     * @param text
     * @param okListener
     */
    public static void okDialog(Context context, String text,
            DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
        .setTitle(R.string.information_title)
        .setMessage(text)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.ok, okListener)
        .show();
    }

    /**
     * Displays a dialog box with OK and Cancel buttons
     *
     * @param context
     * @param text
     * @param okListener
     * @param cancelListener
     */
    public static void okCancelDialog(Context context, String text,
            DialogInterface.OnClickListener okListener,
            DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
        .setTitle(R.string.information_title)
        .setMessage(text)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.ok, okListener)
        .setNegativeButton(android.R.string.cancel, cancelListener)
        .show();
    }

    /**
     * Displays a dialog box that lets users pick a day & hour value
     *
     * @param context
     * @param title title of the dialog box
     * @param listener what happens when users click ok
     */
    public static void dayHourPicker(Context context, String title,
            OnNNumberPickedListener listener) {
        Resources r = context.getResources();
        new NNumberPickerDialog(context, listener, title,
        new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 0},
        new int[] {31, 23}, new String[] {
                r.getString(R.string.daysVertical),
                r.getString(R.string.hoursVertical)
            }).show();
    }

    /**
     * Displays a dialog box that lets users pick an hour & minute value.
     *
     * @param context
     * @param title title of the dialog box
     * @param listener what happens when users click ok
     */
    public static void hourMinutePicker(Context context, String title,
            OnNNumberPickedListener listener) {
        new NNumberPickerDialog(context, listener, title,
        new int[] {0, 0}, new int[] {1, 5}, new int[] {0, 0},
        new int[] {99, 59}, new String[] {":", null}).show();
    }
}
