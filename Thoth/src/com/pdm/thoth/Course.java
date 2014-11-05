package com.pdm.thoth;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {

	private String _id;
	private String _name;
	private String _semester;
	private String _classname;

	public Course() {

	}

	public Course(Parcel in) {
		String[] data = new String[4];

		in.readStringArray(data);
		this._id = data[0];
		this._name = data[1];
		this._semester = data[2];
		this._classname = data[3];
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setSemester(String semester) {
		_semester = semester;
	}

	public void setClassName(String classname) {
		_classname = classname;
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getSemester() {
		return _semester;
	}

	public String getClassName() {
		return _classname;
	}

	@Override
	public String toString() {
		return _name + " " + _classname;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		dest.writeStringArray(new String[] { this._id, this._name,
				this._semester, this._classname });

	}

	public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
		public Course createFromParcel(Parcel in) {
			return new Course(in);
		}

		public Course[] newArray(int size) {
			return new Course[size];
		}
	};

}
