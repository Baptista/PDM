package com.pdm.thoth;

import java.util.ArrayList;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	private final String _tagcourse = "tagcourse";

	private ArrayList<Course> _allcourses;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		this.getResources().newTheme();
		_allcourses = this.getIntent().getParcelableArrayListExtra(_tagcourse);

		updateCourses(_allcourses);

	}

	public void updateCourses(ArrayList<Course> allcourses) {
		if(allcourses==null) return;
		
		@SuppressWarnings("deprecation")
		android.preference.MultiSelectListPreference s = (MultiSelectListPreference) this
				.findPreference("MultipleChoose");

		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(this);

		CharSequence[] cs = new CharSequence[allcourses.size()];
		for (int i = 0; i < allcourses.size(); ++i) {
			cs[i] = allcourses.get(i).getName() + " - "
					+ allcourses.get(i).getClassName();
		}
		s.setEntries(cs);
		s.setEntryValues(cs);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		this.getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences shared, String key) {

		if (key.equals("Prefs_Semester")) {
			DbCourse dbcourse = new DbCourse(this);
			ArrayList<Course> a = dbcourse.getAllCoursesBySemester(shared.getString(key, ""));
			updateCourses(a);

			this.findPreference(key).setSummary(shared.getString(key, ""));
			return;
		} else {
			Set<String> multichoose = shared.getStringSet("MultipleChoose",
					null);
			this.getIntent().putExtra(_tagcourse, multichoose.toString());
			this.setResult(1, this.getIntent());
		}
	}

}
