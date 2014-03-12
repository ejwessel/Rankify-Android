package com.ewit.rankify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends CustomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		super.onOptionsItemSelected(menuItem);
        startActivity(new Intent(AboutActivity.this, MainActivity.class));
        return true;
    }
}
