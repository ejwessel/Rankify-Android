package com.ewit.rankify;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class MainActivity extends Activity {

	private static final List<String> PERMISSIONS = Arrays.asList("user_videos", "user_status", "user_photos");
	
	private Button aboutButton;
	private Button pullDataButton;
	private LoginButton loginButton;
	private TextView usersName;
	private String userID;
	private String accessToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		aboutButton = (Button)findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, AboutActivity.class));
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
			}
		});
		

		usersName = (TextView)findViewById(R.id.usersName);
		
		pullDataButton = (Button)findViewById(R.id.pullDataButton);
		pullDataButton.setEnabled(false);
		pullDataButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.textlines_gray));
		pullDataButton.setTextColor(getResources().getColor(R.color.grayButtonColor));
		pullDataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("pull data button does nothing for now");
			}
		});
		
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(PERMISSIONS);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {        	
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	if(user != null){
            		userID = user.getId();
            		System.out.println("user id: " + user.getId());
            		usersName.setText(user.getName());
            		System.out.println(user);
            		
            	}
            	else{
            		usersName.setText(getResources().getString(R.string.pre_usersname));
            	}
//                MainActivity.this.user = user;
//                updateUI();
//                // It's possible that we were waiting for this.user to be populated in order to post a
//                // status update.
//                handlePendingAction();
            }
        });
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
