package com.doublesunflower.core.ui.search;

import com.doublesunflower.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


public class BusinessTypeChooser extends Activity {
	
	RadioGroup rg_type;
	EditText additional_input;
	Context outerRef;
	String addr_text;
	public static final int ACTIVITY_RATE = 1;
	
	public void onCreate (Bundle b) {
		super.onCreate (b);
		Log.d ("DEBUG_NOTIFICATION", "ENTERED_ON_CREATE_+__________----____-----");
		setContentView (R.layout.typechooser_layout);
		rg_type = (RadioGroup) findViewById (R.id.type_radio_group);
		additional_input = (EditText) findViewById (R.id.additional_tags);
		outerRef = this;
		addr_text = getIntent ().getStringExtra ("chosen_biz");
		TextView tv = (TextView) findViewById (R.id.address_text_tc);
		tv.setText (addr_text);
		
		Button deselect_all = (Button) findViewById (R.id.deselect_all);
		deselect_all.setOnClickListener (new View.OnClickListener () {
			public void onClick (View v) {
				rg_type.clearCheck ();
			}
		});
		
		Button continueButton = (Button) findViewById (R.id.cont_to_rate_picker);
		continueButton.setOnClickListener (new View.OnClickListener () {
			public void onClick (View v) {
				Intent intentToStartRater = new Intent (outerRef, RateWindow.class);
				String addtl = additional_input.getText ().toString ();
				String finaladdtl = "";
				String [] indiv = addtl.split (",");
				for (int x = 0; x < indiv.length; x++) {
					if (x == (indiv.length - 1)) {
						finaladdtl = finaladdtl + indiv[x] + "s" + ", " + indiv[x] + "es";
					} else {
						finaladdtl = finaladdtl + indiv[x] + "s" + ", " + indiv[x] + "es" + ", ";
					}
				}
				intentToStartRater.putExtra ("additional_tags", finaladdtl);
				intentToStartRater.putExtra ("chosen_biz", addr_text);
				intentToStartRater.putExtra ("coords", (double []) getIntent ().getSerializableExtra ("coords"));
				if (additional_input.getText ().toString ().equals ("") && rg_type.getCheckedRadioButtonId() == -1) {
					
				}
				if (rg_type.getCheckedRadioButtonId () != -1) {
					if (rg_type.getCheckedRadioButtonId () == R.id.hotel) {
						intentToStartRater.putExtra ("given_choice", "hotels");
						startActivityForResult (intentToStartRater, ACTIVITY_RATE);
					} else {
						intentToStartRater.putExtra ("given_choice", "restaurants");
						startActivityForResult (intentToStartRater, ACTIVITY_RATE);
					}
				} else {
					if (additional_input.getText ().toString ().equals ("") == false){
						intentToStartRater.putExtra ("given_choice", "null");
						startActivityForResult (intentToStartRater, ACTIVITY_RATE);
					}
				}
			}
		});
		
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			setResult (RESULT_CANCELED);
			finish ();
		}
	}
}
