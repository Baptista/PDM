package deetc.pdm.yamba;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBStatus {

private static final String TAG = DBStatus.class.getSimpleName();
	
	static final int VERSION = 1;
	static final String DATABASE = "status.db";
	static final String TABLESTATUS = "status";
	
	public static final String U_ID = "_id";
	public static final String U_TEXT = "txt";
		
	static final String[] DB_TEXT_COLUMNS = {U_TEXT };
	static final String[] DB_ALL_COLUMNS = { U_ID, U_TEXT };
	
	public class DbStatusHelper extends SQLiteOpenHelper{

		public DbStatusHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating Database:" + DATABASE);
			db.execSQL(
					"create table " + TABLESTATUS + "(" + 
							U_ID + " integer primary key autoincrement, " +
							U_TEXT + " text)"
					
					);		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("drop table " + TABLESTATUS);
			onCreate(db);
		}
		
	}

	private final DbStatusHelper dbHelper;
	
	public DBStatus(Context context) { //
		this.dbHelper = new DbStatusHelper(context);
		this.dbHelper.getReadableDatabase();
		Log.i(TAG, "Initialized data");
	}
	
	public void Close(){
		this.dbHelper.close();
	}
	
	public DbStatusHelper getDBHelper(){
		return dbHelper;
	}
	
	public void Insert(ContentValues values){
		Log.i(TAG, "Insert on "+ values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLESTATUS, null, values,
			SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close(); //
		}
	}
	
	
	
	public List<StatusInfo> getStatus(){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLESTATUS , DB_ALL_COLUMNS,null,null,null,null,null);
		List<StatusInfo> lists = new ArrayList<StatusInfo>();
		cursor.moveToFirst();
		int i=0;
		while(!cursor.isAfterLast()){
			StatusInfo user = new StatusInfo();
			user.setId(i++);
			user.setText(cursor.getString(3));
			lists.add(user);
			
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
	}
	
	public void Remove(long id){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		db.execSQL("delete from " + TABLESTATUS + " where "+ U_ID+"="+id);
	}
	
}
