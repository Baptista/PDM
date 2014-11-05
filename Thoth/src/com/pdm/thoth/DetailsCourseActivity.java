package com.pdm.thoth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DetailsCourseActivity extends Activity {

	private final String _tagcourse = "tagcourse";
	private Course _course;
	private ListView _list;
	private ArrayAdapter<String> _adapter;
	private ArrayList<WorkItem> _workitems;
	private Intent intent;
	
	private DbWorkItem dbworkitem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_workitem);

//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
//		
		Intent it = this.getIntent();
		_course = it.getParcelableExtra(_tagcourse);

		

		Button home = (Button) this.findViewById(R.id.button1);
		home.setClickable(true);
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://thoth.cc.e.ipl.pt/classes/"
						+ _course.getName() + "/" + _course.getSemester() + "/"
						+ _course.getClassName());
				Intent launch = new Intent(android.content.Intent.ACTION_VIEW,
						uri);
				startActivity(launch);
			}
		});

		TextView tx = (TextView) this.findViewById(R.id.textView1);
		tx.setText("WorkItems");
		_list = (ListView) this.findViewById(R.id.listView1);
		_adapter = new ArrayAdapter<String>(this, R.layout.activity_courses,
				R.id.textView1);

		_list.setClickable(true);
		_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(Intent.ACTION_INSERT);
				intent.setData(CalendarContract.Events.CONTENT_URI);
				startActivity(intent);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra(Events.TITLE, _course.getName());

				WorkItem work = _workitems.get(arg2);
				String datestring = work.getDuedate();
				String [] parts = datestring.split("-");
				GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2].substring(0 , 2)));
				intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
						calDate.getTimeInMillis());

				startActivity(intent);
			}

		});
		
		intent = new Intent(DetailsCourseActivity.this, WorkItemPull.class);
		intent.putExtra(_tagcourse, _course);
		registerReceiver(broadcastReceiver, new IntentFilter(WorkItemPull.BROADCAST_ACTION));
		
		dbworkitem = new DbWorkItem(this);
		
				
		ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		PendingIntent pending = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		// Start every new month
		final android.net.NetworkInfo wifi =
			    connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifi.isConnected()){	
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),10000*60*60*24*30, pending);
						
			
		}
	}

	
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Log.v("DISCUSSAO", "onReceiver-DetailsCourseActivity");
			_workitems = dbworkitem.getWorkItemsByCourse(_course.getId());		
			
			SetupAdapter();	
			
		}
	};
	protected void onDestroy() {
		this.unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	public void SetupAdapter() {
		for (WorkItem wi : _workitems) {
			_adapter.add(wi.toString());
		}
		_list.setAdapter(_adapter);
	}
}
