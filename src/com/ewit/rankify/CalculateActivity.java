package com.ewit.rankify;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class CalculateActivity extends CustomActivity {

	private ProgressBar statusFriends;
	private ProgressBar statusAlbums;
	private ProgressBar statusVideos;
	private ProgressBar statusStatus;
	private ProgressBar statusPhotos;
	private ProgressBar statusRanking;
	private ImageView gatheringFriendsCheck;
	private ImageView gatheringAlbumsCheck;
	private ImageView gatheringVideosCheck;
	private ImageView gatheringStatusesCheck;
	private ImageView gatheringPhotosCheck;
	private ImageView retrievingDataCheck;
	private Button refreshButton;
	private Button continueButton;
	private String userID;
	private String accessToken;
	private JSONArray friendList;
	private Boolean hasFriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_activity);

		//obtain passed data from previous activity
		Bundle passedValues = getIntent().getExtras();
		if (passedValues != null) {
			userID = passedValues.getString("userID");
			accessToken = passedValues.getString("accessToken");
			hasFriends = passedValues.getBoolean("hasFriends");

			System.out.println("userID:" + userID + ", accessToken: " + accessToken);
		}

		statusFriends = (ProgressBar) findViewById(R.id.friendStatus);
		statusAlbums = (ProgressBar) findViewById(R.id.albumStatus);
		statusVideos = (ProgressBar) findViewById(R.id.videosStatus);
		statusStatus = (ProgressBar) findViewById(R.id.statusStatus);
		statusPhotos = (ProgressBar) findViewById(R.id.photosStatus);
		statusRanking = (ProgressBar) findViewById(R.id.retrievingData);
		gatheringFriendsCheck = (ImageView) findViewById(R.id.gatheringFriendsCheck);
		gatheringAlbumsCheck = (ImageView) findViewById(R.id.gatheringAlbumsCheck);
		gatheringVideosCheck = (ImageView) findViewById(R.id.gatheringVideosCheck);
		gatheringStatusesCheck = (ImageView) findViewById(R.id.gatheringStatusesCheck);
		gatheringPhotosCheck = (ImageView) findViewById(R.id.gatheringPhotosCheck);
		retrievingDataCheck = (ImageView) findViewById(R.id.retrievingDataCheck);

		refreshButton = (Button) findViewById(R.id.refreshDataButton);
		continueButton = (Button) findViewById(R.id.continueButton);refreshButton.setEnabled(false);
	
		refreshButton.setEnabled(false);
		refreshButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
		refreshButton.setTextColor(getResources().getColor(R.color.grayButtonColor));

		continueButton.setEnabled(false);
		continueButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
		continueButton.setTextColor(getResources().getColor(R.color.grayButtonColor));

		refreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				compute();
			}
		});

		continueButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//go to pull data activity

				Intent friendListIntent = new Intent(CalculateActivity.this, FriendList.class);
				friendListIntent.putExtra("jsonArray", friendList.toString());
				startActivity(friendListIntent);
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
			}
		});

		if (!passedValues.getString("hasFriends").equals("1")) {
			compute();
		} else {	
			statusFriends.setVisibility(View.INVISIBLE);
			statusAlbums.setVisibility(View.INVISIBLE);
			statusVideos.setVisibility(View.INVISIBLE);
			statusStatus.setVisibility(View.INVISIBLE);
			statusPhotos.setVisibility(View.INVISIBLE);
			statusRanking.setVisibility(View.INVISIBLE);

			gatheringFriendsCheck.setVisibility(View.VISIBLE);
			gatheringAlbumsCheck.setVisibility(View.VISIBLE);
			gatheringVideosCheck.setVisibility(View.VISIBLE);
			gatheringStatusesCheck.setVisibility(View.VISIBLE);
			gatheringPhotosCheck.setVisibility(View.VISIBLE);
			retrievingDataCheck.setVisibility(View.VISIBLE);
			
			new GetFriendData().execute(userID);
		}
	}

	private void compute() {
		System.out.println("Starting to Compute...");

		statusFriends.setVisibility(View.INVISIBLE);
		statusAlbums.setVisibility(View.INVISIBLE);
		statusVideos.setVisibility(View.INVISIBLE);
		statusStatus.setVisibility(View.INVISIBLE);
		statusPhotos.setVisibility(View.INVISIBLE);
		statusRanking.setVisibility(View.INVISIBLE);

		gatheringFriendsCheck.setVisibility(View.INVISIBLE);
		gatheringAlbumsCheck.setVisibility(View.INVISIBLE);
		gatheringVideosCheck.setVisibility(View.INVISIBLE);
		gatheringStatusesCheck.setVisibility(View.INVISIBLE);
		gatheringPhotosCheck.setVisibility(View.INVISIBLE);
		retrievingDataCheck.setVisibility(View.INVISIBLE);

		refreshButton.setEnabled(false);
		refreshButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
		refreshButton.setTextColor(getResources().getColor(R.color.grayButtonColor));

		continueButton.setEnabled(false);
		continueButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
		continueButton.setTextColor(getResources().getColor(R.color.grayButtonColor));

		statusFriends.setVisibility(View.VISIBLE);

		new PullFriends().execute(userID, accessToken);
	}

	class PullFriends extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("PullFriends Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "users/pullFriends/" + userID + "/" + accessToken;

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
				String status = (String) jsonObject.get("status");
				if (status.equals("success")) {
					System.out.println("Obtained friendData Successfully");
					statusFriends.setVisibility(View.INVISIBLE);
					gatheringFriendsCheck.setImageResource(R.drawable.checkmark);
					gatheringFriendsCheck.setVisibility(View.VISIBLE);
					statusAlbums.setVisibility(View.VISIBLE);

					new PullAlbums().execute(userID, accessToken);
				} else {
					System.out.println("Unable to obtain friendData Successfully");
					gatheringFriendsCheck.setVisibility(View.VISIBLE);
					gatheringFriendsCheck.setImageResource(R.drawable.xmark);
				}

				System.out.println("PullFriends Finished");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class PullAlbums extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("PullAlbums Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "albums/pullAlbums/" + userID + "/" + accessToken;

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
				String status = (String) jsonObject.get("status");
				if (status.equals("success")) {
					System.out.println("Obtained albumData Successfully");
					statusAlbums.setVisibility(View.INVISIBLE);
					gatheringAlbumsCheck.setImageResource(R.drawable.checkmark);
					gatheringAlbumsCheck.setVisibility(View.VISIBLE);
					statusVideos.setVisibility(View.VISIBLE);

					new PullVideos().execute(userID, accessToken);
				} else {
					System.out.println("Unable to obtain albumData Successfully");
					gatheringAlbumsCheck.setVisibility(View.VISIBLE);
					gatheringAlbumsCheck.setImageResource(R.drawable.xmark);
				}

				System.out.println("PullAlbums Finished");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class PullVideos extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("PullVideos Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "videos/pullVideos/" + userID + "/" + accessToken;

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
				String status = (String) jsonObject.get("status");
				if (status.equals("success")) {
					System.out.println("Obtained videoData Successfully");
					statusVideos.setVisibility(View.INVISIBLE);
					gatheringVideosCheck.setImageResource(R.drawable.checkmark);
					gatheringVideosCheck.setVisibility(View.VISIBLE);
					statusStatus.setVisibility(View.VISIBLE);

					new PullStatuses().execute(userID, accessToken);
				} else {
					System.out.println("Unable to obtain videoData Successfully");
					gatheringVideosCheck.setVisibility(View.VISIBLE);
					gatheringVideosCheck.setImageResource(R.drawable.xmark);
				}

				System.out.println("PullVideos Finished");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class PullStatuses extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("PullStatuses Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "statuses/pullStatuses/" + userID + "/" + accessToken;

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
				String status = (String) jsonObject.get("status");
				if (status.equals("success")) {
					System.out.println("Obtained videoData Successfully");
					statusStatus.setVisibility(View.INVISIBLE);
					gatheringStatusesCheck.setImageResource(R.drawable.checkmark);
					gatheringStatusesCheck.setVisibility(View.VISIBLE);
					statusPhotos.setVisibility(View.VISIBLE);

					new PullPhotos().execute(userID, accessToken);
				} else {
					System.out.println("Unable to obtain videoData Successfully");
					gatheringStatusesCheck.setVisibility(View.VISIBLE);
					gatheringStatusesCheck.setImageResource(R.drawable.xmark);
				}

				System.out.println("PullVideos Finished");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class PullPhotos extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("PullPhotos Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "photos/pullPhotos/" + userID + "/" + accessToken;

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
				String status = (String) jsonObject.get("status");
				if (status.equals("success")) {
					System.out.println("Obtained photoData Successfully");
					statusPhotos.setVisibility(View.INVISIBLE);
					gatheringPhotosCheck.setImageResource(R.drawable.checkmark);
					gatheringPhotosCheck.setVisibility(View.VISIBLE);
					statusRanking.setVisibility(View.VISIBLE);

					new GetFriendData().execute(userID);
				} else {
					System.out.println("Unable to obtain photoData Successfully");
					gatheringPhotosCheck.setVisibility(View.VISIBLE);
					gatheringPhotosCheck.setImageResource(R.drawable.xmark);
				}

				System.out.println("PullPhotos Finished");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class GetFriendData extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			System.out.println("GetFriendData Started");
			String responseString = null;
			String urlPath = getString(R.string.site_path) + "users/getFriendsData/" + userID;

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

			System.out.println(result);

			try {

				friendList = new JSONArray(result);

				if (friendList.length() != 0) {
					System.out.println("Obtained photoData Successfully");
					statusRanking.setVisibility(View.INVISIBLE);
					retrievingDataCheck.setImageResource(R.drawable.checkmark);
					retrievingDataCheck.setVisibility(View.VISIBLE);

					refreshButton.setEnabled(true);
					refreshButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_blue));
					refreshButton.setTextColor(getResources().getColor(R.color.blueButtonColor));

					continueButton.setEnabled(true);
					continueButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_blue));
					continueButton.setTextColor(getResources().getColor(R.color.blueButtonColor));

				} else {
					System.out.println("Unable to obtain photoData Successfully");
					retrievingDataCheck.setVisibility(View.VISIBLE);
					retrievingDataCheck.setImageResource(R.drawable.xmark);

					refreshButton.setEnabled(true);
					refreshButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_blue));
					refreshButton.setTextColor(getResources().getColor(R.color.blueButtonColor));
				}

				System.out.println("GetFriendData Finished");

			} catch (JSONException e) {
				e.printStackTrace();
				System.out.println(e.toString());
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
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		startActivity(new Intent(CalculateActivity.this, MainActivity.class));
		return true;
	}
}
