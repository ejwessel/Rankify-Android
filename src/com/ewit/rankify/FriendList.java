package com.ewit.rankify;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class FriendList extends CustomActivity {

	ListView friendListView;
	ArrayList<String> friendListData = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist_activity);

		ListView friendList = (ListView) findViewById(R.id.friendList);
		Bundle passedValues = getIntent().getExtras();
		String stringArray = passedValues.getString("jsonArray");

		try {
			JSONArray friendData = new JSONArray(stringArray);
			//place friend data into an array list; every index has a friend with corresponding data
			for (int i = 0; i < friendData.length(); i++) {
				friendListData.add(friendData.get(i).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

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

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
////		 Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.friend_list, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {

		super.onOptionsItemSelected(menuItem);
		Intent intent = new Intent(FriendList.this, CalculateActivity.class);
		//may need to putExtra info here so when a user goes back they dont recompute but instead keep it in a stable state
		startActivity(intent);
		
		return true;
	}

}
