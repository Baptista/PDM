package isel.pdm.whereat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DetailsInviteActivity extends Activity {

	private Intent my;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailsinvite);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		my = this.getIntent();
		Log.d("cenas" , my.getExtras().getString("user"));
		TextView tv = (TextView)this.findViewById(R.id.textViewdetails);
		tv.setText(my.getExtras().getString("user"));

		Button accept = (Button) this.findViewById(R.id.buttondetailsaccept);
		accept.setClickable(true);
		accept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery(my.getExtras().getString("user"));
				query.whereEqualTo("user" , ParseUser.getCurrentUser().getUsername());
				query.getFirstInBackground(	new GetCallback<ParseObject>() {
							public void done(ParseObject gameScore,
									com.parse.ParseException e) {
								if (e == null) {
									//gameScore.put("user", ParseUser.getCurrentUser().getUsername());
									gameScore.put("caniuse", true);
									gameScore.saveEventually();
								}
							}
						});
			}
		});

	}

	
}
