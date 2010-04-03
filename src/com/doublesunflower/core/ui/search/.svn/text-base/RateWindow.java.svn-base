package com.doublesunflower.core.ui.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.doublesunflower.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
//import android.telephony.TelephonyProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RateWindow extends Activity {
	
	String addrTxt;
	Context outerRef;
	public static final int ACTIVITY_THANKS = 1;
	private TelephonyManager tm;
	
	
	public void onCreate (Bundle b){
		super.onCreate (b);
		setContentView (R.layout.rate_layout);
		
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		outerRef = this;
		TextView addressText = (TextView) findViewById (R.id.address_text);
		addrTxt = getIntent ().getStringExtra("chosen_biz");
		addressText.setText (addrTxt);
		Button submit = (Button) findViewById (R.id.submit_rating);
		submit.setOnClickListener (new View.OnClickListener() {
			public void onClick (View v){
				RadioGroup rg_rate = (RadioGroup) findViewById (R.id.rate_radio_group);
				int rb_id = rg_rate.getCheckedRadioButtonId();
				int rating = 0;
				switch (rb_id){
				case R.id.rating_five: rating = 5; break;
				case R.id.rating_four: rating = 4; break;
				case R.id.rating_three: rating = 3; break;
				case R.id.rating_two: rating = 2; break;
				case R.id.rating_one: rating = 1; break;
				default: rating = -1;
				}
				Log.d ("DEBUG_NOTIFICATION", "................NUM_VAL:" + rating);
				if (rating == -1){
					
					/*
					showAlert ("BusinessRate Error", 0, "Please select a rating before submitting the form.", "OK", new DialogInterface.OnClickListener (){
						public void onClick (DialogInterface di, int t){
							//exitFunction = true;
							//et1.setText("");//TODO: Complete
						}
					}, false, null);
					*/
				} else{
					
					try{
						Socket sock = new Socket ("98.207.50.162", 9379);
						PrintWriter ratingsWriter = new PrintWriter (sock.getOutputStream ());
						ratingsWriter.println ("w");
						ratingsWriter.println (addrTxt);
						ratingsWriter.println (rating);
						ratingsWriter.println (tm.getLine1Number());
						ratingsWriter.println (getIntent ().getStringExtra ("additional_tags"));
						ratingsWriter.println (getIntent ().getStringExtra ("given_choice"));
						double [] coords = (double []) getIntent ().getSerializableExtra ("coords");
						String coordsString = coords[0] + ", " + coords[1];
						ratingsWriter.println (coordsString);
						ratingsWriter.close ();
						Intent i = new Intent (outerRef, ThanksWindow.class);
						startActivityForResult (i, ACTIVITY_THANKS);
						
						//ratingsWriter.flush ();
					} catch (IOException ioe){
						Log.d ("EXCEPTION_THROWN", ioe.getMessage ());
						/*
						showAlert ("BusinessRate Error", 0, "Unable to connect to ratings database", "OK", new DialogInterface.OnClickListener (){
							public void onClick (DialogInterface di, int t){
								//exitFunction = true;
								//et1.setText("");//TODO: Complete
							}
						}, false, null);
						*/
					}
					
					
					
				}
			}

		});
		
		
	}
	
/*	public boolean onCreateOptionsMenu (Menu m) {
		super.onCreateOptionsMenu (m);
		m.add (0, 0, "Exit");
		return true;
	}
	
	public boolean onMenuItemSelected (Menu.Item item) {
		switch (item.getId()) {
		case 0 : Log.d ("DEBUG_NOTIFICATION", "FINISHING"); setResult (RESULT_CANCELED, null, null); finish (); break;
		}
		return false;
		
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			setResult (RESULT_CANCELED, null);
			finish ();
		}
	}
	
}
