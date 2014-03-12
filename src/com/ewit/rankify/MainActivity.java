package com.ewit.rankify;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button aboutButton;
	private Button pullDataButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		aboutButton = (Button)findViewById(R.id.aboutButton);
		pullDataButton = (Button)findViewById(R.id.pullDataButton);
		pullDataButton.setEnabled(false);
		
		aboutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
			}
		});
	}
}
