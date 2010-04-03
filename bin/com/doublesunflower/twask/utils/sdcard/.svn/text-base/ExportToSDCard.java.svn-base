package com.doublesunflower.twask.utils.sdcard;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.doublesunflower.R;
import com.doublesunflower.twask.utils.location.BreadCrumbService;

public class ExportToSDCard {
	

	 private static final String tag ="TWASK";
	 private static final String tripFileName = "currentTrip.txt";
	 private static String currentTripName = "";
	 private static int altitudeCorrectionMeters = 20;
	 
	 private static String halfspan = "10000";
	 private static final DecimalFormat sevenSigDigits = new DecimalFormat("0.#######");
    
    
    public static void initTripName(Context ctx) {
		// see if there's currently a trip in the trip file
    	String tripName = "new";
    	try {
	        FileInputStream fIn = ctx.openFileInput(tripFileName);
	        InputStreamReader isr = new InputStreamReader(fIn);
	        char[] inputBuffer = new char[1024];
	        isr.read(inputBuffer);
	        isr.close();
	        fIn.close();
	        tripName = new String(inputBuffer).trim();
	        Log.i(tag,"loaded trip name: "+tripName);
    	} catch (FileNotFoundException fnfe) {
    		Log.i(tag,"first run, no "+tripFileName);
    		try {
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd");
	    		tripName = sdf.format(new Date());
	    		saveTripName(ctx, tripName);
    		} catch (Exception e) {
    			Log.e(tag, e.toString());
    		}
    	} catch (IOException ioe) {
    		Log.e(tag, ioe.toString());
    	}
    	//EditText tripNameEditor = (EditText)findViewById(R.id.EditTextTripName);
    	//tripNameEditor.setText(tripName);
    	currentTripName = tripName;
	}
    
    public static void saveTripName(Context ctx, String tripName) throws FileNotFoundException, IOException {
        FileOutputStream fOut = ctx.openFileOutput(tripFileName,
                Context.MODE_PRIVATE);
        OutputStreamWriter osw = new OutputStreamWriter(fOut); 
        osw.write(tripName);
        osw.flush();
        osw.close();
        fOut.close();
    }
    
    public static void doExport(Context ctx) {
		// export the db contents to a kml file
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			//EditText editAlt = (EditText)findViewById(R.id.EditTextAltitudeCorrection);
			//altitudeCorrectionMeters = Integer.parseInt(editAlt.getText().toString());
			
			altitudeCorrectionMeters = 20;
			Log.i(tag, "altitude Correction updated to "+altitudeCorrectionMeters);
			
			db = ctx.openOrCreateDatabase(BreadCrumbService.DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
			cursor = db.rawQuery("SELECT * " +
                    " FROM " + BreadCrumbService.POINTS_TABLE_NAME +
                    " ORDER BY GMTTIMESTAMP ASC",
                    null);
            
			int gmtTimestampColumnIndex = cursor.getColumnIndexOrThrow("GMTTIMESTAMP");
            int latitudeColumnIndex = cursor.getColumnIndexOrThrow("LATITUDE");
            int longitudeColumnIndex = cursor.getColumnIndexOrThrow("LONGITUDE");
            int altitudeColumnIndex = cursor.getColumnIndexOrThrow("ALTITUDE");
            int accuracyColumnIndex = cursor.getColumnIndexOrThrow("ACCURACY");
			
            if (cursor.moveToFirst()) {
				StringBuffer fileBuf = new StringBuffer();
				String beginTimestamp = null;
				String endTimestamp = null;
				String gmtTimestamp = null;
				initFileBuf(fileBuf, initValuesMap());
				do {
					gmtTimestamp = cursor.getString(gmtTimestampColumnIndex);
					if (beginTimestamp == null) {
						beginTimestamp = gmtTimestamp;
					}
					double latitude = cursor.getDouble(latitudeColumnIndex);
					double longitude = cursor.getDouble(longitudeColumnIndex);
					double altitude = cursor.getDouble(altitudeColumnIndex) + altitudeCorrectionMeters;
					double accuracy = cursor.getDouble(accuracyColumnIndex);
					fileBuf.append(sevenSigDigits.format(longitude)+","+sevenSigDigits.format(latitude)+","+altitude+"\n");
				} while (cursor.moveToNext());
				endTimestamp = gmtTimestamp;
				closeFileBuf(fileBuf, beginTimestamp, endTimestamp);
				String fileContents = fileBuf.toString();
				Log.d(tag, fileContents);
				File sdDir = new File("/sdcard/GPSLogger");
				sdDir.mkdirs();
				File file = new File("/sdcard/GPSLogger/"+currentTripName+".kml");
				FileWriter sdWriter = new FileWriter(file, false);
				sdWriter.write(fileContents);
				sdWriter.close();
    			Toast.makeText(ctx,
    					"Export completed!",
    					Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(ctx,
						"I didn't find any location points in the database, so no KML file was exported.",
						Toast.LENGTH_LONG).show();
			}
		} catch (FileNotFoundException fnfe) {
			Toast.makeText(ctx,
					"Error trying access the SD card.  Make sure your handset is not connected to a computer and the SD card is properly installed",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(ctx,
					"Error trying to export: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	private static HashMap <String, String> initValuesMap() {
		HashMap <String, String> valuesMap = new HashMap <String, String>();
		valuesMap.put("FILENAME", currentTripName);
		
		//RadioButton airButton = (RadioButton)findViewById(R.id.RadioAir);
		//if (airButton.isChecked()) {
			// use air settings
			//valuesMap.put("EXTRUDE", "1");
			//valuesMap.put("TESSELLATE", "0");
			//valuesMap.put("ALTITUDEMODE", "absolute");
		//} else {
			// use ground settings for the export
			valuesMap.put("EXTRUDE", "0");
			valuesMap.put("TESSELLATE", "1");
			valuesMap.put("ALTITUDEMODE", "clampToGround");
		//}
		
		return valuesMap;
	}

	private static void initFileBuf(StringBuffer fileBuf, HashMap<String, String> valuesMap) {
		fileBuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		fileBuf.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
		fileBuf.append("  <Document>\n");
		fileBuf.append("    <name>"+valuesMap.get("FILENAME")+"</name>\n");
		fileBuf.append("    <description>GPSLogger KML export</description>\n");
		fileBuf.append("    <Style id=\"yellowLineGreenPoly\">\n");
		fileBuf.append("      <LineStyle>\n");
		fileBuf.append("        <color>7f00ffff</color>\n");
		fileBuf.append("        <width>4</width>\n");
		fileBuf.append("      </LineStyle>\n");
		fileBuf.append("      <PolyStyle>\n");
		fileBuf.append("        <color>7f00ff00</color>\n");
		fileBuf.append("      </PolyStyle>\n");
		fileBuf.append("    </Style>\n");
		fileBuf.append("    <Placemark>\n");
		fileBuf.append("      <name>Absolute Extruded</name>\n");
		fileBuf.append("      <description>Transparent green wall with yellow points</description>\n");
		fileBuf.append("      <styleUrl>#yellowLineGreenPoly</styleUrl>\n");
		fileBuf.append("      <LineString>\n");
		fileBuf.append("        <extrude>"+valuesMap.get("EXTRUDE")+"</extrude>\n");
		fileBuf.append("        <tessellate>"+valuesMap.get("TESSELLATE")+"</tessellate>\n");
		fileBuf.append("        <altitudeMode>"+valuesMap.get("ALTITUDEMODE")+"</altitudeMode>\n");
		fileBuf.append("        <coordinates>\n");
	}
	
	private static void closeFileBuf(StringBuffer fileBuf, String beginTimestamp, String endTimestamp) {
		fileBuf.append("        </coordinates>\n");
		fileBuf.append("     </LineString>\n");
		fileBuf.append("	 <TimeSpan>\n");
		String formattedBeginTimestamp = zuluFormat(beginTimestamp);
		fileBuf.append("		<begin>"+formattedBeginTimestamp+"</begin>\n");
		String formattedEndTimestamp = zuluFormat(endTimestamp);
		fileBuf.append("		<end>"+formattedEndTimestamp+"</end>\n");
		fileBuf.append("	 </TimeSpan>\n");
		fileBuf.append("    </Placemark>\n");
		fileBuf.append("  </Document>\n");
		fileBuf.append("</kml>");
	}

	private static String zuluFormat(String beginTimestamp) {
		// turn 20081215135500 into 2008-12-15T13:55:00Z
		StringBuffer buf = new StringBuffer(beginTimestamp);
		buf.insert(4, '-');
		buf.insert(7, '-');
		buf.insert(10, 'T');
		buf.insert(13, ':');
		buf.insert(16, ':');
		buf.append('Z');
		return buf.toString();
	}
	
	
	
	
	
}