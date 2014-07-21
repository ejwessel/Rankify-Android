package com.ewit.rankify;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
				startActivity(new Intent(MainActivity.this, CalculateActivity.class));
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
