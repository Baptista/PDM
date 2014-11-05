package com.pdm.thoth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPull extends IntentService{

	private Course _course;
	static final String BROADCAST_ACTION = "BroadcastWorkItem";
	private final String _tagcourse = "tagcourse";
	private URL _url;
	private static final int NEWS_ID = 1;
	
	public NotificationPull() {
		super("NotificationPull");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		_course =arg0.getParcelableExtra(_tagcourse);
		try {
			
			_url = new URL(
					"http://thoth.cc.e.ipl.pt/api/v1/classes/"
							+ _course.getId() + "/newsitems");
		} catch (MalformedURLException e) {
			Log.d("DEBUG", "MAILFORMEDURL");
			return;
		}
		
		HttpURLConnection urlConnection = null;
		

			try {
				urlConnection = (HttpURLConnection) _url.openConnection();
			
			if (urlConnection.getResponseCode() != 200)
					return;

			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			
			JSONObject obj = new JSONObject(sb.toString());
			JSONArray arr = obj.getJSONArray("newsItems");
			if(arr.length()>0){
				SendNotification();
			}
			
			} catch (IOException e) {
				return;
			} catch (JSONException e) {
				return;
			}finally{
				if (urlConnection != null)
					urlConnection.disconnect();
			}			

	}

	public void SendNotification(){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.ic_launcher;        
		CharSequence tickerText = "have news";
		long when = System.currentTimeMillis();         
		Context context = getApplicationContext();     
		CharSequence contentTitle = "have news";  
		CharSequence contentText = "have news";      
		Intent notificationIntent = new Intent(this, CoursesActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		mNotificationManager.notify(NEWS_ID, notification);
	}
}
