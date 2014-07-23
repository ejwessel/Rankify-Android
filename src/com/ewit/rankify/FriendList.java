package com.ewit.rankify;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendList extends Activity {

	ListView friendList;
	ArrayList<String> nameList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist_activity);

		ListView friendList = (ListView) findViewById(R.id.friendList);
		Bundle passedValues = getIntent().getExtras();
		Boolean pass = passedValues.getBoolean("pass");
		String stringArray = passedValues.getString("jsonArray");

		try {
			JSONArray friendData = new JSONArray(stringArray);
			for (int i = 0; i < friendData.length(); i++) {
				nameList.add(friendData.get(i).toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//				nameList = new ArrayList<String>(Arrays.asList(jsonArray));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
		friendList.setAdapter(adapter); //populates list
		//		optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		//			     public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
		//			         TextView clickedView = (TextView) view;
		//			         String textClicked = clickedView.getText().toString();
		//			         
		//			         switch(position){
		//			         	case 0: startActivity(new Intent(MainActivity.this, FrequenciesActivity.class));
		//				         		overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		//				         		break;
		//			         	case 1: startActivity(new Intent(MainActivity.this, LengthsActivity.class));
		//		         				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		//		         				break;
		//			         	case 2: startActivity(new Intent(MainActivity.this, VelocitiesActivity.class));
		//		         				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		//		         				break;
		//			         	case 3: startActivity(new Intent(MainActivity.this, DimensionlessActivity.class));
		//		         				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		//		         				break;
		//			         	case 4: startActivity(new Intent(MainActivity.this, MiscellaneousActivity.class));
		//		         				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		//		         				break;
		//			         	case 5: startActivity(new Intent(MainActivity.this, AboutActivity.class));
		//		         				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
		//		         				break;
		//			         }
		//			     }
		//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
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
