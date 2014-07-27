package com.ewit.rankify;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecificsActivity extends CustomActivity {

	private ImageView userImage;
	private TextView rankPosition;
	private TextView rankScore;
	private TextView totalLikes;
	private TextView albumLikes;
	private TextView photoLikes;
	private TextView videoLikes;
	private TextView statusLikes;
	private TextView totalComments;
	private TextView albumComments;
	private TextView photoComments;
	private TextView videoComments;
	private TextView statusComments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specifics_activity);
		
		userImage = (ImageView) findViewById(R.id.user_image);
		rankPosition = (TextView) findViewById(R.id.rankPosition);
		rankScore = (TextView) findViewById(R.id.rankScore);
		totalLikes = (TextView) findViewById(R.id.totalLikes);
		albumLikes = (TextView) findViewById(R.id.albumLikes);
		photoLikes = (TextView) findViewById(R.id.photoLikes);
		videoLikes = (TextView) findViewById(R.id.videoLikes);
		statusLikes = (TextView) findViewById(R.id.statusLikes);
		totalComments = (TextView) findViewById(R.id.totalComments);
		albumComments = (TextView) findViewById(R.id.albumComments);
		photoComments = (TextView) findViewById(R.id.photoComments);
		videoComments = (TextView) findViewById(R.id.videoComments);
		statusComments = (TextView) findViewById(R.id.statusComments);
		
		rankPosition.setText(getIntent().getStringExtra("rankPosition"));
		rankScore.setText(String.valueOf(getIntent().getIntExtra("totalLikes", 0) + getIntent().getIntExtra("totalComments", 0)));
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.specifics, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
