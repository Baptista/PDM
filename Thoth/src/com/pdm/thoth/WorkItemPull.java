package com.pdm.thoth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

public class WorkItemPull extends IntentService {

	private DbWorkItem dbworkitem;
	private URL _url;
	private Course _course;
	static final String BROADCAST_ACTION = "BroadcastWorkItem";
	private final String _tagcourse = "tagcourse";
	private ArrayList<WorkItem> _workitems;
	
	public WorkItemPull() {
		super("WorkItemPull");		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		dbworkitem = new DbWorkItem(this);
	}	
	
	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.d("DISCUSSAO", "onHandleIntent");
		
		_course =arg0.getParcelableExtra(_tagcourse);
		try {
			
			_url = new URL(
					"http://thoth.cc.e.ipl.pt/api/v1/classes/"
							+ _course.getId() + "/workitems");
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
			
			PutOnContentValues(sb);
			} catch (IOException e) {
				return;
			} catch (JSONException e) {
				return;
			}finally{
				if (urlConnection != null)
					urlConnection.disconnect();
			}			

	}
	
	
	
	private void PutOnContentValues ( StringBuilder sb) throws JSONException, IOException{
		_workitems = (ArrayList<WorkItem>) ReaderJson.ReadJsonObject(sb,
				_course.getSemester(), "workItems");
		
		ContentValues cv = new ContentValues();
		for(WorkItem wi: _workitems){
			cv.put(DbWorkItem.C_ID, _course.getId());
			cv.put(DbWorkItem.W_ID, wi.getId());
			cv.put(DbWorkItem.W_ACRONYM , wi.getAcronym());
			cv.put(DbWorkItem.W_TITLE, wi.getTitle());
			cv.put(DbWorkItem.W_STARTDATE, wi.getStartdate());
			cv.put(DbWorkItem.W_DUEDATE, wi.getDuedate());
			cv.put(DbWorkItem.W_SUBMIT, wi.getSubmit());
			this.getdbworkitem().InsertOrIgnoreCourse(cv);
		}
		
		Intent intent = new Intent(BROADCAST_ACTION);
		
		sendBroadcast(intent);
		Intent intentnot = new Intent(WorkItemPull.this , NotificationPull.class);
		startService(intentnot);
	}

	public DbWorkItem getdbworkitem() { 
		return dbworkitem;
	}
}
