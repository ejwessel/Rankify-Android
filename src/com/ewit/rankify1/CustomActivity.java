package com.ewit.rankify1;

import com.ewit.rankify1.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/*
  	Custom Activity 
	extends activity by adding the back button to the
	top as well as the back animation when clicked
*/
 

public class CustomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        
        //Adds back button to menu bar
        ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);	
    }

    //animation for backbutton added here
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){       
        onBackPressed();
        return true;
    }	
    
}
