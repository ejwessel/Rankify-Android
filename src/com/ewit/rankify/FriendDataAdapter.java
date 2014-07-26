package com.ewit.rankify;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;

public class FriendDataAdapter extends ArrayAdapter<String> {
	// declaring our ArrayList of items
	private ArrayList<String> objects;
	private ImageLoader imageLoader;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public FriendDataAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
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
			v = inflater.inflate(R.layout.list_row, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		String individual = objects.get(position);
		JSONObject userData = null;
		try {
			JSONObject data = new JSONObject(individual);
			String userDataString = data.getString("User");
			userData = new JSONObject(userDataString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (userData != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			ImageView userImage = (ImageView) v.findViewById(R.id.user_image);
			TextView userName = (TextView) v.findViewById(R.id.user_name);
			TextView userRank = (TextView) v.findViewById(R.id.user_rank);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (userImage == null) {
				userImage.setBackground(null);				
			} else {
				// show The Image
				try {
					String profilePictureSmall = userData.getString("profilePictureSmall");
//					new ImageDownloader(userImage).execute(profilePictureSmall);
					imageLoader.DisplayImage(profilePictureSmall, userImage);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (userName == null) {
				userName.setText("NO NAME");
			} else {
				try {
					userName.setText(userData.getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (userRank == null) {
				userRank.setText("NO RANK");
			} else {
				try {
					int totalLikes = userData.getInt("totalLikes");
					int totalComments = userData.getInt("totalComments");
					userRank.setText(String.valueOf(totalLikes + totalComments));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		// the view must be returned to our activity
		return v;

	}

	class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public ImageDownloader(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String url = urls[0];
			Bitmap mIcon = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				mIcon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon;
		}

		protected void onPostExecute(Bitmap result) {
			
			System.out.println("downloaded image" + result);
			
			bmImage.setImageBitmap(result);
		}
	}

}
