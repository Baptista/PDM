package isel.pdm.whereat;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	private Location currlocation;
	private TextView tv;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 

		
		Parse.initialize(this, "FfEsBy1dJIdyQqRigOWfwHVtJw185nv2eJjry1yT",
				"IzGMVtc1KuKglyYW7KWLc8bJ1ZzFTiykPv39nb1b");
//		WifiManager wifi = (WifiManager) this.getSystemService(WIFI_SERVICE);
//		WifiInfo wi = wifi.getConnectionInfo();

		final LocationManager lm = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		List<String> a = lm.getAllProviders();
		
		lm.requestLocationUpdates(a.get(1), 50, 2000, new LocationListener() {

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				Log.d("onlocationchanged", location.toString());
				currlocation = location;

			}
		});
		Log.d("service", a.toString());

		tv = (TextView) this.findViewById(R.id.textView2);
		Button reload = (Button) this.findViewById(R.id.buttonreload);

		reload.setClickable(true);
		reload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("click reload","");
				ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseUser
						.getCurrentUser().getUsername());
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(final List<ParseObject> users,
							com.parse.ParseException e) {

						if (e == null) {

							ParseQuery<ParseObject> query = ParseQuery
									.getQuery("gps");
							query.findInBackground(new FindCallback<ParseObject>() {
								public void done(List<ParseObject> objects,
										com.parse.ParseException e) {

									if (e == null) {
										String s = "";
										for (int i = objects.size() - 1, j = 0; i > 0; --i) {
											if (contains(users, objects.get(i)
													.getString("user"))) {
												s += objects.get(i).getString(
														"user")
														+ " : "
														+ objects
																.get(i)
																.getDouble(
																		"longitude")
														+ " : "
														+ objects
																.get(i)
																.getDouble(
																		"latitude")
														+ "\n";
												
												if (j == 10)
													break;
												++j;
											}
										}
										tv.setText(s);
									}
								}
							});
						}
					}
				});
			}
		});

		Button save = (Button) this.findViewById(R.id.buttonsave);
		save.setClickable(true);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(currlocation == null){
					currlocation = lm.getLastKnownLocation("gps");
				}
				ParseObject g = new ParseObject(currlocation.getProvider());
				g.put("longitude", currlocation.getLongitude());
				g.put("latitude", currlocation.getLatitude());
				g.put("user", ParseUser.getCurrentUser().getUsername());
				g.saveEventually();
			}
		});

		Button giveauth = (Button) this.findViewById(R.id.buttongiveauth);
		giveauth.setClickable(true);
		giveauth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent it = new Intent(MainActivity.this, AuthActivity.class);
				startActivity(it);
			}
		});

	
		Button receive = (Button) this.findViewById(R.id.buttonreceive);
		receive.setClickable(true);
		receive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

								
						Intent it = new Intent(MainActivity.this,
								InviteActivity.class);
						startActivity(it);
					}
				});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean contains(List<ParseObject> list, String user) {
		for (ParseObject obj : list) {
			String a = obj.getString("user");
			Boolean b = obj.getBoolean("caniuse");
			if(a!=null){
				if (obj.getString("user").equals(user)
						&& obj.getBoolean("caniuse") == true) {
					return true;
				}
			}
		}
		return false;
	}

}
