package com.ewit.rankify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;

public class FriendList extends CustomActivity implements SearchView.OnQueryTextListener {

	private ArrayList<JSONObject> friendListData = new ArrayList<JSONObject>();
	private ActionBar actionBar;
	private ListView friendList;
	private Button shareButton;
	private SearchView searchView;
	private FriendDataAdapter adapter;
	private String oldPost = "";

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

		searchView = (SearchView) actionBar.getCustomView().findViewById(R.id.searchView);
		setupSearchView();

		try {
			JSONArray friendData = new JSONArray(stringArray);
			JSONObject friend = null;
			//place friend data into an array list; every index has a friend with corresponding data
			for (int i = 0; i < friendData.length(); i++) {

				friend = new JSONObject(friendData.get(i).toString());
				friend = new JSONObject(friend.get("User").toString());
				friendListData.add(friend);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Share Button Was Clicked");

				AlertDialog.Builder builder = new AlertDialog.Builder(FriendList.this);
				builder.setCancelable(true);
				builder.setTitle("Post to Facebook");
				builder.setMessage("This will post the top 10 people to your facebook. Click \"Post\" below to post to your wall!");
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						publishStory();
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		//An adapter acts as a bridge between the view and the data for a view.
		//The adapter provides access to the data items(friendListData)
		//An adapter is also responsible for making a view for each item in the data set...
		//(context, resource, data)
		//context = this
		//resource will be the XML file you design
		//data will be the friendListData passed in
		adapter = new FriendDataAdapter(this, android.R.layout.simple_list_item_1, friendListData);
		friendList.setAdapter(adapter); //populates list
	}

	//Shows the publish button only when the user is authenticated:
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
			for (int i = 0; i < 9; i++) {
				String name;
				try {
					name = friendListData.get(i).getString("name");
					postString.append((i + 1) + ". " + name + ", ");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//append the last person
			String name;
			try {
				name = friendListData.get(9).getString("name");
				postString.append(10 + ". " + name);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			Bundle postParams = new Bundle();
			postParams.putString("name", "Rankify");
			postParams.putString("caption", "Top 10 Friends:");
			postParams.putString("link", "http://e-wit.co.uk/rankifyapp/index.php");
			postParams.putString("picture", null);
			postParams.putString("description", postString.toString());

			if (oldPost.equals(postString.toString())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(FriendList.this);
				builder.setCancelable(true);
				builder.setTitle("Unable to Post to Facebook");
				builder.setMessage("Facebook disallows posting of redundant content");
				builder.setInverseBackgroundForced(true);
				builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				oldPost = postString.toString(); //set the old post to what we're posting

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
							Toast toast = Toast.makeText(getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						} else {
							Toast toast = Toast.makeText(getApplicationContext(), "Top 10 Friends published", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					}
				};

				Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			}
		}
	}

	//Used to determine whether or not the user has granted the necessary permissions to publish the story.
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

	@Override
	public boolean onQueryTextChange(String newText) {
		System.out.println("Search Text Changed");
		adapter.filter(newText);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}

	private void setupSearchView() {
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);
	}
}
