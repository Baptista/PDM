package deetc.pdm.yamba;


import deetc.pdm.yamba.UserInfoPull.AccountBinder;
import android.util.Log;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class UserInfoActivity extends BaseActivity implements OnSharedPreferenceChangeListener
{
	//private final String APP_TAG = "Yamba";	
	SharedPreferences prefs;
	UserInfoPull userInfo;
	boolean isBound = false;
	TextView textname;
	TextView textFollowers;
	TextView textFollowing;
	TextView textTotalStatus;
	
	//Inicialização ecrã e componentes necessárias
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_activity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		Intent intent = new Intent(this, UserInfoPull.class);
        bindService(intent, userInfoConnection, Context.BIND_AUTO_CREATE);
        
        textname = (TextView)findViewById(R.id.editText1);
        textFollowers = (TextView)findViewById(R.id.editText2);
        textFollowing = (TextView)findViewById(R.id.editText3);
        textTotalStatus = (TextView)findViewById(R.id.editText4);
        
    }
    
	//Menu	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.itemUserInfo).setEnabled(false);		
		return super.onPrepareOptionsMenu(menu);
	}
	
	//Preferências
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		Log.d("DEBUG", "onSharedPreferenceChanged");
		Log.d("DEBUG", "args1 " + arg1);
		this.prefs = arg0;
	}
	
	//UserInfoPull bound service
	private ServiceConnection userInfoConnection = new ServiceConnection()
	{
		//Realiza o bind
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d("DEBUG", "USER INFO ACTIVITY ONSERVICECONNECTION THERAD:" + Thread.currentThread().getName());
			AccountBinder binder = (AccountBinder) service;
			userInfo = binder.getService();
			textname.setText(userInfo.getName());
			textTotalStatus.setText(userInfo.getTotalStatusMessages().toString());
			textFollowers.setText(userInfo.getTotalSubscriptions().toString());
			textFollowing.setText(userInfo.getTotalSubscribers().toString());
			
	        isBound = true;
	    }
	    
		//Termina o bind
	    public void onServiceDisconnected(ComponentName arg0)
	    {
	        isBound = false;
	    }
    };
}
