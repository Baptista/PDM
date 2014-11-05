package com.pdm.thoth;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReaderJson {

	public static ArrayList<?> ReadJsonObject(StringBuilder is,
			String semester, String tag) throws JSONException, IOException {
		JSONObject obj = new JSONObject(is.toString());
		if (tag.equals("classes")) {
			return readJsonArrayForCourse(obj, semester, tag);
		} else {
			return readJsonArrayForWorkItem(obj, semester, tag);
		}

	}

	private static ArrayList<WorkItem> readJsonArrayForWorkItem(
			JSONObject jsonobj, String semester, String tag)
			throws JSONException {

		ArrayList<WorkItem> workitems = new ArrayList<WorkItem>();
		JSONArray arr = jsonobj.getJSONArray(tag);

		for (int i = 0; i < arr.length(); ++i) {
			JSONObject mobj = arr.getJSONObject(i);
			WorkItem wi = readJsonObjectForWorkItem(mobj);
			if (wi != null) {
				workitems.add(wi);
			}
		}
		return workitems;
	}

	private static ArrayList<Course> readJsonArrayForCourse(JSONObject jsonobj,
			String semester, String tag) throws IOException, JSONException {

		ArrayList<Course> allcourse = new ArrayList<Course>();

		JSONArray arr = jsonobj.getJSONArray(tag);
		for (int i = 0; i < arr.length(); ++i) {

			JSONObject mobj = arr.getJSONObject(i);

			Course c = readJsonObjectForCourse(mobj);
			if (c != null)
				allcourse.add(c);

		}
		return allcourse;
	}

	private static Course readJsonObjectForCourse(JSONObject jsonobj)
			throws JSONException {

		Course c = new Course();
		c.setId(jsonobj.getString("id"));
		c.setName(jsonobj.getString("courseUnitShortName"));
		c.setSemester(jsonobj.getString("lectiveSemesterShortName"));
		c.setClassName(jsonobj.getString("className"));

		return c;
	}

	private static WorkItem readJsonObjectForWorkItem(JSONObject jsonobj)
			throws JSONException {
		WorkItem work = new WorkItem();
		work.setId(jsonobj.getInt("id"));
		work.setAcronym(jsonobj.getString("acronym"));
		work.setTitle(jsonobj.getString("title"));
		work.setSubmit(jsonobj.getString("requiresGroupSubmission"));
		work.setStartdate(jsonobj.getString("startDate"));
		work.setDuedate(jsonobj.getString("dueDate"));

		return work;
	}
}
