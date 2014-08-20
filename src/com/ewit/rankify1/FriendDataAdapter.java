package com.ewit.rankify1;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewit.rankify1.R;
import com.fedorvlasov.lazylist.ImageLoader;

public class FriendDataAdapter extends ArrayAdapter<JSONObject> {
	// declaring our ArrayList of items
	private ArrayList<JSONObject> objects;
	private ArrayList<JSONObject> objectsSearched;
	private ImageLoader imageLoader;
	private Context context;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public FriendDataAdapter(Context context, int textViewResourceId, ArrayList<JSONObject> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
		this.context = context;
		this.objectsSearched = new ArrayList<JSONObject>();
		this.objectsSearched.addAll(this.objects);
		imageLoader = new ImageLoader(context);
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		View listViewItem = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (listViewItem == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listViewItem = inflater.inflate(R.layout.list_row, parent, false);
		}

		final JSONObject userData = objectsSearched.get(position);

		if (userData != null) {

			ImageView userImage = (ImageView) listViewItem.findViewById(R.id.user_image);
			TextView userName = (TextView) listViewItem.findViewById(R.id.user_name);
			TextView userRank = (TextView) listViewItem.findViewById(R.id.user_rank);

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

				listViewItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							System.out.println("object at position: " + position + "was clicked");
							System.out.println("person " + userData.getString("name") + " was clicked");

							Intent calculateIntent = new Intent(context, SpecificsActivity.class);
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

							context.startActivity(calculateIntent);
							((Activity) context).overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
		
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// the view must be returned to our activity
		return listViewItem;
	}

	@Override
	public int getCount() {
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
