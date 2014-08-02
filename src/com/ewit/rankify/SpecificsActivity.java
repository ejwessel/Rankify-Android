package com.ewit.rankify;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
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
	private TextView rank;
	private TextView total;
	private TextView likes;
	private TextView comments;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specifics_activity);

		Typeface type = Typeface.createFromAsset(getAssets(), "fonts/futura-condensed-extrabold.ttf");

		rank = (TextView) findViewById(R.id.rank);
		total = (TextView) findViewById(R.id.total);
		likes = (TextView) findViewById(R.id.likes);
		comments = (TextView) findViewById(R.id.comments);
		userImage = (ImageView) findViewById(R.id.userPicture);
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
		
		userImage.setImageResource(R.drawable.user_icon);
		new ImageLoadTask(getIntent().getStringExtra("userPicture"), userImage).execute();

		rank.setTypeface(type);
		total.setTypeface(type);
		likes.setTypeface(type);
		comments.setTypeface(type);
		rankPosition.setTypeface(type);
		rankScore.setTypeface(type);
		totalLikes.setTypeface(type);
		totalComments.setTypeface(type);
		albumLikes.setTypeface(type);
		albumComments.setTypeface(type);
		photoLikes.setTypeface(type);
		photoComments.setTypeface(type);
		videoLikes.setTypeface(type);
		videoComments.setTypeface(type);
		statusLikes.setTypeface(type);
		statusComments.setTypeface(type);

		setTitle(getIntent().getStringExtra("user_name"));
		rankPosition.setText("#" + getIntent().getStringExtra("rankPosition"));
		rankScore.setText(String.valueOf(getIntent().getIntExtra("totalLikes", 0) + getIntent().getIntExtra("totalComments", 0)));
		totalLikes.setText(String.valueOf(getIntent().getIntExtra("totalLikes", 0)));
		albumLikes.setText(String.valueOf(getIntent().getIntExtra("albumLikes", 0)));
		photoLikes.setText(String.valueOf(getIntent().getIntExtra("photoLikes", 0)));
		videoLikes.setText(String.valueOf(getIntent().getIntExtra("videoLikes", 0)));
		statusLikes.setText(String.valueOf(getIntent().getIntExtra("statusLikes", 0)));
		totalComments.setText(String.valueOf(getIntent().getIntExtra("totalComments", 0)));
		albumComments.setText(String.valueOf(getIntent().getIntExtra("albumComments", 0)));
		photoComments.setText(String.valueOf(getIntent().getIntExtra("photoComments", 0)));
		videoComments.setText(String.valueOf(getIntent().getIntExtra("videoComments", 0)));
		statusComments.setText(String.valueOf(getIntent().getIntExtra("statusComments", 0)));

		actionBar = getActionBar();
		actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
		Button button = new  Button(actionBar.getThemedContext());
		ImageView imageView = new ImageView(actionBar.getThemedContext());
		imageView.setScaleType(ImageView.ScaleType.CENTER);
		imageView.setImageResource(R.drawable.checkmark);
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
				Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		layoutParams.rightMargin = 40;
		imageView.setLayoutParams(layoutParams);
		actionBar.setCustomView(imageView);
	}

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		// Inflate the menu; this adds items to the action bar if it is present.
	//		getMenuInflater().inflate(R.menu.specifics, menu);
	//		return true;
	//	}

	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		private ImageView imageView;

		public ImageLoadTask(String url, ImageView imageView) {
			this.url = url;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				System.out.println("LOADING USER IMAGE");
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
			System.out.println("FINISHED LOADING USER IMAGE");
		}

	}

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
