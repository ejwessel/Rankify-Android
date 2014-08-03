package com.ewit.rankify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;

public class FriendList extends CustomActivity {

	private ArrayList<String> friendListData = new ArrayList<String>();
	private ActionBar actionBar;
	private ListView friendList;
	private Button shareButton;

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist_activity);

		friendList = (ListView) findViewById(R.id.friendList);

		Bundle passedValues = getIntent().getExtras();
		String stringArray = passedValues.getString("jsonArray");

		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.custom_action_bar_friendlist);
		actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
		shareButton = (Button) actionBar.getCustomView().findViewById(R.id.shareButton);

		try {
			JSONArray friendData = new JSONArray(stringArray);
			//place friend data into an array list; every index has a friend with corresponding data
			for (int i = 0; i < friendData.length(); i++) {
				friendListData.add(friendData.get(i).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Share Button Was Clicked");
				publishStory();
			}
		});

		friendList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

				String individual = friendListData.get(position);
				JSONObject userData = null;
				try {
					JSONObject data = new JSONObject(individual);
					String userDataString = data.getString("User");
					userData = new JSONObject(userDataString);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (userData != null) {
					Intent calculateIntent = new Intent(FriendList.this, SpecificsActivity.class);
					try {
						calculateIntent.putExtra("user_id", userData.getString("user_id"));
						calculateIntent.putExtra("user_name", userData.getString("name"));
						calculateIntent.putExtra("userPicture", userData.getString("profilePictureLarge"));
						calculateIntent.putExtra("rankPosition", userData.getString("rank"));
						calculateIntent.putExtra("totalLikes", userData.getInt("totalLikes"));
						calculateIntent.putExtra("albumLikes", userData.getInt("albumLikes"));
						calculateIntent.putExtra("photoLikes", userData.getInt("photoLikes"));
						calculateIntent.putExtra("videoLikes", userData.getInt("videoLikes"));
						calculateIntent.putExtra("statusLikes", userData.getInt("statusLikes"));
						calculateIntent.putExtra("totalComments", userData.getInt("totalComments"));
						calculateIntent.putExtra("albumComments", userData.getInt("albumComments"));
						calculateIntent.putExtra("photoComments", userData.getInt("photoComments"));
						calculateIntent.putExtra("videoComments", userData.getInt("videoComments"));
						calculateIntent.putExtra("statusComments", userData.getInt("statusComments"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

					startActivity(calculateIntent);
					overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
				}

			}
		});
		//An adapter acts as a bridge between the view and the data for a view.
		//The adapter provides access to the data items(friendListData)
		//An adapter is also responsible for making a view for each item in the data set...
		//(context, resource, data)
		//context = this
		//resource will be the XML file you design
		//data will be the friendListData passed in
		FriendDataAdapter adapter = new FriendDataAdapter(this, android.R.layout.simple_list_item_1, friendListData);
		friendList.setAdapter(adapter); //populates list
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			shareButton.setVisibility(View.VISIBLE);
		} else if (state.isClosed()) {
			shareButton.setVisibility(View.INVISIBLE);
		}
	}

	private void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions    
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			StringBuilder postString = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				try {
					JSONObject data = new JSONObject(friendListData.get(i));
					String userDataString = data.getString("User");
					JSONObject user = new JSONObject(userDataString);
					postString.append(user.get("name") + "\n");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			Bundle postParams = new Bundle();
						postParams.putString("name", "Facebook SDK for Android");
						postParams.putString("caption", "Build great social apps and get more installs.");
			//			postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
						postParams.putString("link", "https://developers.facebook.com/android");
//						postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
			postParams.putString("description", postString.toString());

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i("TAG", "JSON error " + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "Friend List Posted to Facebook", Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
