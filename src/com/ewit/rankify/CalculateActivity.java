package com.ewit.rankify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class CalculateActivity extends CustomActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_activity);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		super.onOptionsItemSelected(menuItem);
        startActivity(new Intent(CalculateActivity.this, MainActivity.class));
        return true;
    }
}
