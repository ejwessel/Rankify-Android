package com.ewit.rankify;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ewit.rankify.CalculateActivity.PullAlbums;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

public class MainActivity extends Activity {

	private static final List<String> PERMISSIONS = Arrays.asList("user_videos", "user_status", "user_photos");

	private Button aboutButton;
	private Button pullDataButton;
	private LoginButton loginButton;
	private TextView usersName;
	private String userID;
	private String accessToken;
	private ProfilePictureView profilePictureView;
	private Session session;
	private String hasFriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		aboutButton = (Button) findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
			}
		});

		usersName = (TextView) findViewById(R.id.usersName);
		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);

		pullDataButton = (Button) findViewById(R.id.pullDataButton);
		pullDataButton.setEnabled(false);
		pullDataButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
		pullDataButton.setTextColor(getResources().getColor(R.color.grayButtonColor));
		pullDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//go to pull data activity
				
//				new GetFriendData().execute(userID);
				
				Intent calculateIntent = new Intent(MainActivity.this, CalculateActivity.class);
				calculateIntent.putExtra("userID", userID);
				calculateIntent.putExtra("accessToken", accessToken);
//				calculateIntent.putExtra("hasFriends", hasFriends);
				startActivity(calculateIntent);
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
			}
		});

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions(PERMISSIONS);
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {
					//gather user info after successful login
					userID = user.getId();
					usersName.setText(user.getName());
					session = Session.getActiveSession();
					accessToken = session.getAccessToken();
					pullDataButton.setEnabled(true);

					//set visual changes
					profilePictureView.setProfileId(userID);
					pullDataButton.setEnabled(true);
					pullDataButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_blue));
					pullDataButton.setTextColor(getResources().getColor(R.color.blueButtonColor));

					System.out.println("user id: " + user.getId());
					System.out.println(user);
					System.out.println("access token:" + accessToken);

				} else {
					//set visual changes
					usersName.setText(getResources().getString(R.string.pre_usersname));
					profilePictureView.setProfileId(null);
					pullDataButton.setEnabled(false);
					pullDataButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
					pullDataButton.setTextColor(getResources().getColor(R.color.grayButtonColor));
				}

			}
		});
	}
	
	class GetFriendData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("GetFriendData Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "users/doesFriendDataExist/" + userID;

			URL url;
			try {
				url = new URL(urlPath);
				HttpURLConnection urlConnection;
				try {
					urlConnection = (HttpURLConnection) url.openConnection();
					InputStream in = null;
					try {
						in = new BufferedInputStream(urlConnection.getInputStream());
						responseString = fromStream(in);

					} finally {
						urlConnection.disconnect();

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				
				hasFriends = jsonObject.getString("hasFriends");

				System.out.println("GetFriendData Finished");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String fromStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(newLine);
		}
		return out.toString();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
