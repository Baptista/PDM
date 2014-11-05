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

public class CoursePull extends IntentService {

	private static ArrayList<Course> _allcourses;
	private static String pref_semester;
	private URL _url;
	private DbCourse dbcourse;

	static final String BROADCAST_ACTION = "BroadcastCourse";

	public CoursePull() {
		super("CoursePull");
	}

	@Override
	public void onCreate() {
		super.onCreate();

		dbcourse = new DbCourse(this);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		try {
			_url = new URL("http://thoth.cc.e.ipl.pt/api/v1/classes");
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
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

	}

	private void PutOnContentValues(StringBuilder sb) throws JSONException,
			IOException {
		_allcourses = (ArrayList<Course>) ReaderJson.ReadJsonObject(sb,
				pref_semester, "classes");

		ContentValues cv = new ContentValues();
		for (Course c : _allcourses) {
			cv.put(DbCourse.C_ID, c.getId());
			cv.put(DbCourse.C_NAME, c.getName());
			cv.put(DbCourse.C_SEMESTER, c.getSemester());
			cv.put(DbCourse.C_CLASSNAME, c.getClassName());
			this.getdbCourse().InsertOrIgnoreCourse(cv);
		}

		Intent intent = new Intent(BROADCAST_ACTION);

		sendBroadcast(intent);
	}

	public DbCourse getdbCourse() { 
		return dbcourse;
	}

}
