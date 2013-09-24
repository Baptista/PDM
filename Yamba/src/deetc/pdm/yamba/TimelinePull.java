package deetc.pdm.yamba;

import java.util.List;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Status;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class TimelinePull extends IntentService implements OnSharedPreferenceChangeListener{

	private Handler _uiThreadHandler;
	private SharedPreferences _prefs;
	private String _username, _password, _server;
	private Twitter _twitter;
	private List<Status> _list;
	private String _nMessages; 
	
	
	private DBTimeLine dbdata;
	
	static final String BROADCAST_ACTION = "Broadcastthis";
	public TimelinePull() {
		super("TimelinePull");
		Log.v("TimelinePull", "Construtor" + Thread.currentThread().getId());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.v("TimelinePull", "onCreate" + Thread.currentThread().getId());
		
		_prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_prefs.registerOnSharedPreferenceChangeListener(this);
        
		
		_uiThreadHandler = new Handler();		
		
		_username = _prefs.getString("username", "");
		_password = _prefs.getString("password", "");
		_server = _prefs.getString("server", "");
		
        _twitter = new Twitter(_username, _password);
        _twitter.setAPIRootUrl(_server); 
		
		_nMessages = _prefs.getString("numbermessages", "10");
        
		dbdata = new DBTimeLine(this);
	}
	
	
	public DBTimeLine getStatusData() { // <2>
		return dbdata;
	}
	
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.v("TimelinePull", "onHandleIntent" + Thread.currentThread().getId());
		_uiThreadHandler.post(new Runnable() {
						
			public void run() {
											
				try
				{
					_list = _twitter.getUserTimeline();
					
					ContentValues values = new ContentValues();
					for(Status status : _list){
						values.put(DBTimeLine.U_ID, status.getId());
						values.put(DBTimeLine.U_NAME, status.getUser().getName());
						values.put(DBTimeLine.U_CREATED_AT, status.getCreatedAt().toString());
						values.put(DBTimeLine.U_TEXT, status.getText());
						getStatusData().Insert(values);
					}
					
					Intent intent = new Intent(BROADCAST_ACTION);
					
					sendBroadcast(intent);
				}
				catch(TwitterException e)
				{
					startActivity(new Intent(deetc.pdm.yamba.TimelinePull.this , deetc.pdm.yamba.PrefsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
			}
		});
		
	}

	
	@Override
	public void onDestroy() {
		Log.v("TimelinePull", "onDestroy" + Thread.currentThread().getId());
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		Log.v("TimelinePull", "onSharedPreferenceChanged");
		
		_prefs = sharedPreferences;
		_username = _prefs.getString("username", "");
		_password = _prefs.getString("password", "");
		_server = _prefs.getString("server", "");
		
        _twitter = new Twitter(_username, _password);
        _twitter.setAPIRootUrl(_server);
		_nMessages = _prefs.getString("numbermessages", "10");
        
	}
	
}
