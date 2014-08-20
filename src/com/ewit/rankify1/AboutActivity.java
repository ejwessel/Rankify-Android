package com.ewit.rankify1;

import com.ewit.rankify1.R;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends CustomActivity {

	private Button contactButton;
	private TextView versionNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		contactButton = (Button) findViewById(R.id.contactButton);
		versionNumber = (TextView) findViewById(R.id.versionNumber);

		contactButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(Intent.ACTION_SEND);
//				intent.setType("message/rfc822");
//				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support.rankify@e-wit.co.uk" });
//				intent.putExtra(Intent.EXTRA_SUBJECT, "Rankify Support");
//				startActivity(intent);
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri.parse("mailto:support.rankify@e-wit.co.uk?subject=Rankify Support");
				intent.setData(data);
				startActivity(intent);

			}
		});
	
		try {
			versionNumber.setText("Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
