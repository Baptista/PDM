package com.pdm.thoth;

import java.util.ArrayList;
import java.util.Set;

public class Utils {

	public static ArrayList<Course> filterbyCourses(
			ArrayList<Course> _allcourses, Set<String> _courseschoosen,
			String semester) {
		
				
		ArrayList<Course> courses = new ArrayList<Course>();
		for (String cc : _courseschoosen) {

			for (Course course : _allcourses) {

				if (cc.contains(course.getClassName())
						&& cc.contains(course.getName())
						)
					courses.add(course);

			}
		}
		return courses;
	}
	public static ArrayList<Course> filterbysemester(ArrayList<Course> _allcourses , String semester){
		ArrayList<Course> courses = new ArrayList<Course>();
		for(Course c : _allcourses){
			if(c.getSemester().equals(semester))
				courses.add(c);
		}
		return courses;
	}
}
