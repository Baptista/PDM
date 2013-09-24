package deetc.pdm.yamba;

//import java.net.URI;
import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter.User;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


public class UserInfoPull extends Service implements OnSharedPreferenceChangeListener
{
	
	private final IBinder accountInfo = new AccountBinder(); 
	SharedPreferences prefs;
	Twitter twitter;
	String username;
	String password;
	String server;
	List<Status> list;
	
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		Log.d("DEBUG", "USER INFO PULL ONBIND THERAD:" + Thread.currentThread().getName());
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
		
		username = prefs.getString("username", "");
		password = prefs.getString("password", "");
		server = prefs.getString("server", "");
		
		twitter = new Twitter(username, password);
		twitter.setAPIRootUrl(server);
	
		return accountInfo;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String arg1) {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs = prefs;		
		username = prefs.getString("username", "");		
		password = prefs.getString("password", "");
		server = prefs.getString("server", "");
		
		twitter = new Twitter(username, password);
		twitter.setAPIRootUrl(server);
	}
	
	public class AccountBinder extends Binder{
		
		UserInfoPull getService(){
			Log.d("DEBUG", "USER INFO PULL GETSERVICE THERAD:" + Thread.currentThread().getName());
			return UserInfoPull.this;
		}		
	}	

	//Nome
	public String getName()
	{
		Log.d("DEBUG", "USER INFO PULL GETNAME THERAD:" + Thread.currentThread().getName());
		return twitter.getScreenName();		
	}
	
	
	

	//Imagem de perfil
	/*
	Bitmap img;
	public Bitmap getAvatar()
	{
		DownloadImageTask task = new DownloadImageTask();
	    task.onPostExecute(task.doInBackground("http://184.72.255.216/avatar/2-96-20111226041201.png"));
		return img;
	}
	
	private class DownloadImageTask extends AsyncTask <String, Void, Bitmap> {

	    protected Bitmap doInBackground(String... urls)
	    {
	    	String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try
	        {
	        	InputStream in = new java.net.URL(urldisplay).openStream();
	        	mIcon11 = BitmapFactory.decodeStream(in);
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
		    }
		    return mIcon11;
		}        
	
		protected void onPostExecute(Bitmap result)
		{
			img = result;
		}
	}
	*/
	
	
	
	
	//Número de mensagens de status	
	public Integer getTotalStatusMessages()
	{				
		User s = twitter.getUser(username);
		return s.getStatusesCount();
	}
	
	//Número de subscrições
	public Integer getTotalSubscriptions()
	{
		User s = twitter.getUser(username);
		return s.getFollowersCount();		
	}
	
	//Número de subscritores
	public Integer getTotalSubscribers()
	{				
		int total = 0;
		list = twitter.getUserTimeline();
		for(int i = 0 ; i < list.size() ; i++){
			if(!list.get(i).user.equals(username))
				++total;
		}
		return total;
	}

}