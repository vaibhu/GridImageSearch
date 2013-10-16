package com.vsharma.gridimagesearch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	Spinner spinnerSize;
	Spinner spinnerType;
	Spinner spinnerSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		spinnerSize = (Spinner) findViewById(R.id.spinnerSize);
		spinnerType = (Spinner) findViewById(R.id.spinnerType);
		spinnerSearch = (Spinner) findViewById(R.id.spinnerSearch);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void save(View v) {
		String size = spinnerSize.getSelectedItem().toString();
		String type = spinnerType.getSelectedItem().toString();
		String engine = spinnerSearch.getSelectedItem().toString();
		Intent intent = new Intent();
		intent.putExtra("size", size);
		intent.putExtra("type", type);
		intent.putExtra("engine", engine);
		setResult(RESULT_OK, intent);
		finish();
	}

}
