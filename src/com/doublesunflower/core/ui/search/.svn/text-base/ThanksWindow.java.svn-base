package com.doublesunflower.core.ui.search;

import com.doublesunflower.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

public class ThanksWindow extends Activity {
	
	Context outerRef;
	public static final int ACTIVITY_SEARCH = 1;
	public static final int ACTIVITY_RATE = 2;
	
	public void onCreate (Bundle b) {
		super.onCreate (b);
		setContentView (R.layout.thanks_layout);
		outerRef = this;
		RadioGroup rg_choose = (RadioGroup) findViewById (R.id.proceed_choices_radio_group);
		rg_choose.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener () {
			public void onCheckedChanged (RadioGroup rg, int i) {
				switch (i) {
				case R.id.close_app: setResult (RESULT_CANCELED); finish (); break;
				case R.id.search_for_top_rated: Intent in = new Intent (outerRef, BusinessSearch.class); startActivityForResult (in, ACTIVITY_SEARCH); break;
				case R.id.rate_again: Intent intent = new Intent (outerRef, LocalSearchActivity.class); startActivityForResult (intent, ACTIVITY_RATE); break;
				default: return;
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
