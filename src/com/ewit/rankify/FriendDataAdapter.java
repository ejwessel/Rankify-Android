package com.ewit.rankify;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;

public class FriendDataAdapter extends ArrayAdapter<JSONObject> {
	// declaring our ArrayList of items
	private ArrayList<JSONObject> objects;
	private ArrayList<JSONObject> objectsSearched;
	private ImageLoader imageLoader;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public FriendDataAdapter(Context context, int textViewResourceId, ArrayList<JSONObject> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
		this.objectsSearched = new ArrayList<JSONObject>();
		this.objectsSearched.addAll(this.objects);
		imageLoader = new ImageLoader(context);
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_row, parent, false);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		System.out.println("Friends: " + objectsSearched.size());
		JSONObject userData = objectsSearched.get(position); //BUG IS HERE!	

		if (userData != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			ImageView userImage = (ImageView) v.findViewById(R.id.user_image);
			TextView userName = (TextView) v.findViewById(R.id.user_name);
			TextView userRank = (TextView) v.findViewById(R.id.user_rank);

			// check to see if each individual textview is null.
			// if not, assign some text!
			try {
				if (userImage == null) {
					userImage.setBackground(null);
				} else {
					String profilePictureSmall = userData.getString("profilePictureSmall");
					imageLoader.DisplayImage(profilePictureSmall, userImage);
				}
				if (userName == null) {
					userName.setText("NO NAME");
				} else {
					userName.setText(userData.getString("name"));
				}
				if (userRank == null) {
					userRank.setText("NO RANK");
				} else {
					int totalLikes = userData.getInt("totalLikes");
					int totalComments = userData.getInt("totalComments");
					userRank.setText(String.valueOf(totalLikes + totalComments));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// the view must be returned to our activity
		return v;

	}

	@Override
	public int getCount(){
		return objectsSearched.size();
	}
	
	public void filter(String charText) {

		charText = charText.toLowerCase(Locale.getDefault());
		//		objectsSearched.clear();
		objectsSearched = new ArrayList<JSONObject>();
		if (charText.length() == 0) {
			objectsSearched.addAll(objects);
		} else {
			for (JSONObject person : objects) {
				try {
					if (person.get("name").toString().toLowerCase(Locale.getDefault()).contains(charText)) {
						objectsSearched.add(person);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("filtered");
		notifyDataSetChanged();
	}
}
