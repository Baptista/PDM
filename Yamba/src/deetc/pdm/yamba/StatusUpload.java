package deetc.pdm.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class StatusUpload extends IntentService implements OnSharedPreferenceChangeListener{

	private Handler _uiThreadHandler;
	SharedPreferences _prefs;
	private String _username , _password, _server , _textbox;
	Twitter _twitter;
	private ConnectivityManager connMgr;
	private DBStatus dbdata;	
	
	
	public StatusUpload() {
		super("StatusUpload");
		Log.v("StatusUpload", "Construtor" + Thread.currentThread().getId());		
	}

	@Override
	public void onCreate() {
		Log.v("StatusUpload", "onCreate() on thread " + Thread.currentThread().getId());
		
		super.onCreate();
		_prefs = PreferenceManager.getDefaultSharedPreferences(this);
		_prefs.registerOnSharedPreferenceChangeListener(this);
        
		
		_uiThreadHandler = new Handler();		
		
		_username = _prefs.getString("username", "");
		_password = _prefs.getString("password", "");
		_server = _prefs.getString("server", "");
		
        _twitter = new Twitter(_username, _password);
        _twitter.setAPIRootUrl(_server);        
        
        dbdata = new DBStatus(this);
        
        connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	@Override
	public void onDestroy() {
		Log.v("StatusUpload", "onDestroy() on thread " + Thread.currentThread().getId());
		super.onDestroy();
	}
	
	public DBStatus getStatusData() { // <2>
		return dbdata;
	}
	
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.v("StatusUpload", "onHandleIntent() on thread " + Thread.currentThread().getId());
		Log.v("StatusUpload", "onHandleIntent() on thread " + arg0.toString());
		_textbox = arg0.getStringExtra("input");
		
				
		_uiThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				final android.net.NetworkInfo wifi =
					    connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				
				if(!_textbox.equals("")){
					if(wifi.isConnected()){
						
						List<StatusInfo> list = getStatusData().getStatus();
						for(StatusInfo s : list){
							_twitter.setStatus(s.getText());
						}
						_twitter.setStatus(_textbox);
						
					}
					else{
						ContentValues values = new ContentValues();
						values.put(DBStatus.U_TEXT , _textbox);
						getStatusData().Insert(values);
					}
				}
			}
		});
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		_prefs = arg0;
		_username = _prefs.getString("username", "");
		_password = _prefs.getString("password", "");
		_server = _prefs.getString("server", "");
		
        _twitter = new Twitter(_username, _password);
        _twitter.setAPIRootUrl(_server);
	}

	
}
