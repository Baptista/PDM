package com.pdm.thoth;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Set;


import android.net.ConnectivityManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CoursesActivity extends Activity {

	private String pref_semester;
	private ArrayList<Course> _allcourses;
	private ArrayList<Course> _onList;
	private Set<String> _courseschoosen;
	private final String _tagcourse = "tagcourse";
	private ListView _list;
	private ArrayAdapter<String> _adapter;
	private Intent intent;
	private DbCourse dbcourse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_courses);

//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
		
		_list = (ListView) this.findViewById(R.id.listView1);
		_adapter = new ArrayAdapter<String>(this, R.layout.activity_courses,
				R.id.textView1);

		_list.setClickable(true);
		_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(CoursesActivity.this,
						DetailsCourseActivity.class);
				intent.putExtra(_tagcourse, _onList.get(arg2));
				startActivity(intent);

			}

		});
		
		intent = new Intent(CoursesActivity.this, CoursePull.class);
		registerReceiver(broadcastReceiver, new IntentFilter(CoursePull.BROADCAST_ACTION));
		
		
		ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		PendingIntent pending = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		final android.net.NetworkInfo wifi =
			    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifi.isConnected()){	
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),5000, pending);
			
		}
		dbcourse = new DbCourse(this);				
		loadPreference();
		
	}

	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Log.v("BroadcastReceive", "onReceive");
			if(pref_semester != null){
				_allcourses = dbcourse.getAllCoursesBySemester(pref_semester);		
				
				SetupAdapter();	
			}
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();				
	}

	@Override
	protected void onPause() {
				
		super.onPause();
		
	}
	
	@Override
	protected void onDestroy() {
		this.unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.pref:
			Intent it = new Intent(this, PrefActivity.class);
			it.putParcelableArrayListExtra(_tagcourse, dbcourse.getAllCoursesBySemester(pref_semester));
			_adapter.clear();
			startActivityForResult(it, 0);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		loadPreference();
		SetupAdapter();
	}

	private void loadPreference() {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(this);
		pref_semester = shared.getString("Prefs_Semester", "");
		if(pref_semester == ""){
			Intent it = new Intent(this, PrefActivity.class);
			it.putParcelableArrayListExtra(_tagcourse, _allcourses);
			startActivityForResult(it, 0);

		}else{		
			_courseschoosen = shared.getStringSet("MultipleChoose", null);
		}
	}
	public void SetupAdapter() {
		if (pref_semester.equals(""))
			return;
			
		else if(_courseschoosen.size() == 0){
			_onList = dbcourse.getAllCoursesBySemester(pref_semester);//Utils.filterbysemester(_allcourses, pref_semester);
		}
		else {
			_onList = Utils.filterbyCourses(dbcourse.getAllCoursesBySemester(pref_semester), _courseschoosen,
					pref_semester);
		}
		_adapter.clear();
		for (Course c : _onList)
			_adapter.add(c.toString());
		_list.setAdapter(_adapter);
		
	}

}
