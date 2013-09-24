package deetc.pdm.yamba;


import java.util.Calendar;
import java.util.List;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
     
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TimeLineActivity extends BaseActivity implements OnSharedPreferenceChangeListener {

	private ListView scroll;
	private String nChars;
	private String nMessages; 
	private SharedPreferences prefs;
	private Intent intent;
	private DBTimeLine dbData;
	private SQLiteDatabase db;
	private Cursor cursor;
	ArrayAdapter<UserInfo> adapter;
	private ConnectivityManager connMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_activity_main);				
	
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
				
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
		
		
		nChars = prefs.getString("NumberCharacteres"  , "140");
		nMessages = prefs.getString("numbermessages", "10");
			
				
		scroll = (ListView) findViewById(R.id.listView1);
			
		scroll.setClickable(true);
		scroll.setOnItemClickListener( new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					Intent intent = new Intent(TimeLineActivity.this , deetc.pdm.yamba.DetailsActivity.class);
					intent.putExtra("Parametro", adapter.getItem(arg2));
					startActivity(intent);
					
				}
				
			}
		);
		
				
		intent = new Intent(TimeLineActivity.this, deetc.pdm.yamba.TimelinePull.class);
		registerReceiver(broadcastReceiver, new IntentFilter(TimelinePull.BROADCAST_ACTION));
		
		connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		
		if(prefs.getBoolean("Key_CheckBoxAuto", false)){
			
			PendingIntent pending = PendingIntent.getService(this, 0, intent, 0);
			AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			final android.net.NetworkInfo wifi =
				    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(wifi.isConnected())	
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), Integer.parseInt(prefs.getString("Key_TimeUpdate", "30000")), pending);
		}
		
		dbData = new DBTimeLine(this);
		
	}
	@Override
	protected void onDestroy() {
		Log.d("DEBUG", "TIMELINE ACTIVITY ONDESTROY THERAD:" + Thread.currentThread().getName());
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		Log.d("DEBUG", "ONRESUME");
		
		if(prefs.getBoolean("Key_CheckBoxAuto", false)){
			PendingIntent pending = PendingIntent.getService(this, 0, intent, 0);
			AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			final android.net.NetworkInfo wifi =
				    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(wifi.isConnected())
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), Integer.parseInt(prefs.getString("Key_TimeUpdate", "30000")), pending);
			
		}
		startService(intent);
	
		List<UserInfo> lists = dbData.getStatus();		
		
		adapter = new ArrayAdapter<UserInfo>(this , android.R.layout.simple_list_item_1, lists);
			
		scroll.setAdapter(adapter);		
		
		super.onResume();
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		menu.findItem(R.id.itemTimeLine).setEnabled(false);		
		return super.onPrepareOptionsMenu(menu);
	}	
	
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Log.d("BroadcastReceive", "onReceive thread: "+Thread.currentThread().getName());
			List<UserInfo> lists = dbData.getStatus();		
			
			adapter.clear();
			adapter.addAll(lists);
			scroll.setAdapter(adapter);	
			
		}
	
	};
	

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String arg1) {
		Log.d("DEBUG", "ONSHAREFCHANGER");
		this.prefs = prefs;		
		nChars = prefs.getString("NumberCharacteres"  , "140");
		nMessages = prefs.getString("numbermessages", "10");
		
	}
}