package com.pdm.thoth;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbWorkItem {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "workitem.db";
	public static final String TABLE = "workitem";
	public static final String C_ID = "cid";
	public static final String W_ID = "_id";
	public static final String W_ACRONYM = "acronym";
	public static final String W_TITLE = "title";
	public static final String W_SUBMIT = "submit";
	public static final String W_STARTDATE = "startdate";
	public static final String W_DUEDATE = "duedate";
	
		
	public class WorkItemOpenHelper extends SQLiteOpenHelper {
		
		
		private static final String WORKITEM_TABEL_CREATE = "CREATE TABLE " + TABLE
				+ "(" + W_ID + " integer primary key autoincrement," +
				C_ID + " text not null,"+
				W_ACRONYM + " text not null," + 
				W_TITLE + " text not null," + 
				W_SUBMIT + " text not null, " +
				W_STARTDATE + " text not null," + 
				W_DUEDATE + " text not null" +
				
				");";
	
		public WorkItemOpenHelper(Context context) {
	
			super(context, DB_NAME, null, DATABASE_VERSION);
	
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(WORKITEM_TABEL_CREATE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE);
			onCreate(db);
	
		}
	}
	
	
	private final WorkItemOpenHelper workitemopenhelper;
	
	public DbWorkItem(Context context){
		workitemopenhelper = new WorkItemOpenHelper(context);
	}
	
	public WorkItemOpenHelper getDbworkitem() {
		return workitemopenhelper;
	}
	
	public void close(){
		workitemopenhelper.close();
	}
	
	public void InsertOrIgnoreCourse(ContentValues values){
		SQLiteDatabase db = workitemopenhelper.getWritableDatabase();
		
		db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	
	public ArrayList<WorkItem> getWorkItemsByCourse(String id){
		SQLiteDatabase db = this.workitemopenhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from workitem where cid=? ", new String[]{id});
		
		ArrayList<WorkItem> list = new ArrayList<WorkItem>();
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			WorkItem c = new WorkItem();
			c.setId(cursor.getInt(cursor.getColumnIndex(W_ID)));
			c.setAcronym(cursor.getString(cursor.getColumnIndex(W_ACRONYM)));
			c.setTitle(cursor.getString(cursor.getColumnIndex(W_TITLE)));
			c.setSubmit(cursor.getString(cursor.getColumnIndex(W_SUBMIT)));
			c.setStartdate(cursor.getString(cursor.getColumnIndex(W_STARTDATE)));
			c.setDuedate(cursor.getString(cursor.getColumnIndex(W_DUEDATE)));
			list.add(c);
			
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	
	
	

}
