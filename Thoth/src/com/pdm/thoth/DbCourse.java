package com.pdm.thoth;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbCourse{

	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "course.db";
	public static final String TABLE = "course";
	public static final String C_ID = "_id";
	public static final String C_NAME = "name";
	public static final String C_SEMESTER = "semester";
	public static final String C_CLASSNAME = "classname";
	
	private static final String[] PROJECTION = {C_ID ,C_NAME,C_SEMESTER,C_CLASSNAME};
	
	
	public class CourseOpenHelper extends SQLiteOpenHelper {
			
	
		private static final String COURSE_TABEL_CREATE = "CREATE TABLE " + TABLE
				+ "(" + C_ID + " integer primary key autoincrement," + C_NAME
				+ " text not null," + C_SEMESTER + " text not null," + C_CLASSNAME
				+ " text not null);";
	
		public CourseOpenHelper(Context context) {
	
			super(context, DB_NAME, null, DATABASE_VERSION);
	
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(COURSE_TABEL_CREATE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE);
			onCreate(db);
	
		}
	}

	
	private final CourseOpenHelper courseopenhelper;
	
	public DbCourse(Context context){
		courseopenhelper = new CourseOpenHelper(context);
	}
	
	public CourseOpenHelper getDbcourse() {
		return courseopenhelper;
	}
	
	public void close(){
		courseopenhelper.close();
	}
	
	public void InsertOrIgnoreCourse(ContentValues values){
		SQLiteDatabase db = courseopenhelper.getWritableDatabase();
		db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	
	public ArrayList<Course> getAllCourses(){
		SQLiteDatabase db = this.courseopenhelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE,PROJECTION, null, null, null, null,null);
		ArrayList<Course> list = new ArrayList<Course>();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Course c = new Course();
			
			c.setId(cursor.getString(cursor.getColumnIndex(C_ID)));
			c.setName(cursor.getString(cursor.getColumnIndex(C_NAME)));
			c.setSemester(cursor.getString(cursor.getColumnIndex(C_SEMESTER)));
			c.setClassName(cursor.getString(cursor.getColumnIndex(C_CLASSNAME)));
			
			list.add(c);
		}
		cursor.close();
		return list;
	}
	
	
	public ArrayList<Course> getAllCoursesBySemester(String semester){
		SQLiteDatabase db = this.courseopenhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id,name,semester,classname from course where semester=? " , new String[]{semester});
		ArrayList<Course> list = new ArrayList<Course>();
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Course c = new Course();
			
			c.setId(cursor.getString(cursor.getColumnIndex(C_ID)));
			c.setName(cursor.getString(cursor.getColumnIndex(C_NAME)));
			c.setSemester(cursor.getString(cursor.getColumnIndex(C_SEMESTER)));
			c.setClassName(cursor.getString(cursor.getColumnIndex(C_CLASSNAME)));
			
			list.add(c);
			cursor.moveToNext();
			
		}
		cursor.close();
		return list;
	}
	
	
	
	
}
