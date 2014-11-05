package isel.pdm.whereat;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class InviteActivity extends Activity {

	
	ArrayAdapter<String> adapter = null;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		Parse.initialize(this, "FfEsBy1dJIdyQqRigOWfwHVtJw185nv2eJjry1yT",
				"IzGMVtc1KuKglyYW7KWLc8bJ1ZzFTiykPv39nb1b");

		adapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_1);

		final ListView lv = (ListView) this.findViewById(R.id.listView1);
		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(InviteActivity.this,
						DetailsInviteActivity.class);
				intent.putExtra("user", adapter.getItem(arg2));
				startActivityForResult(intent, arg2);

			}

		});

		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> users,
					com.parse.ParseException e) {
				adapter.clear();
				for (final ParseObject obj : users) {
					String a = obj.getString("user");
					if(a!=null)
					adapter.add(a);
					
				}
				lv.setAdapter(adapter);

			}
		});

	}

	protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> users,	com.parse.ParseException e) {
				for (final ParseObject obj : users) {
					String a = obj.getString("user");
					if(a!=null && a.equals(adapter.getItem(requestCode))){
						obj.deleteInBackground();
						
					}
						
				}
					
			}
		
	});
	}
}
