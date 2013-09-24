package deetc.pdm.yamba;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBTimeLine {
	
	private static final String TAG = DBTimeLine.class.getSimpleName();
	
	static final int VERSION = 1;
	static final String DATABASE = "timeline.db";
	static final String TABLETIMELINE = "timeline";
	
	public static final String U_ID = "_id";
	public static final String U_CREATED_AT = "created_at";
	public static final String U_TEXT = "txt";
	
	public static final String U_NAME = "user";
	private static final String GET_ALL_ORDER_BY_DATE = U_CREATED_AT + " DESC";
	static final String[] DB_TEXT_COLUMNS = {/*U_NAME, U_CREATED_AT ,*/ U_TEXT };
	static final String[] DB_ALL_COLUMNS = { U_ID, U_NAME, U_CREATED_AT, U_TEXT };
	
	
	public class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context){
			super(context, DATABASE, null, VERSION);
			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating Database:" + DATABASE);
			db.execSQL(
					"create table " + TABLETIMELINE + "(" + 
							U_ID + " int primary key, " +
							U_CREATED_AT + " text, " + 
							U_NAME + " text, " + 
							U_TEXT + " text)"
					
					);			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("drop table " + TABLETIMELINE);
			onCreate(db);
		}
		
	}
		
	private final DbHelper dbHelper;
		
	public DBTimeLine(Context context) { //
		this.dbHelper = new DbHelper(context);
		this.dbHelper.getReadableDatabase();
		Log.i(TAG, "Initialized data");
	}
	
	public void Close(){
		this.dbHelper.close();
	}
	
	public DbHelper getDBHelper(){
		return dbHelper;
	}
	
	
	
	public void Insert(ContentValues values){
		Log.i(TAG, "Insert on "+ values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(TABLETIMELINE, null, values,
			SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close(); //
		}
	}
	
	public List<UserInfo> getStatus(){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLETIMELINE , DB_ALL_COLUMNS,null,null,null,null,GET_ALL_ORDER_BY_DATE);
		List<UserInfo> lists = new ArrayList<UserInfo>();
		cursor.moveToFirst();
		int i=0;
		while(!cursor.isAfterLast()){
			UserInfo user = new UserInfo();
			user.setId(i++);
			user.setName(cursor.getString(1));
			user.setTime(cursor.getString(2));
			user.setText(cursor.getString(3));
			lists.add(user);
			
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
	}
	
	public long isDBPopulate(){
		return this.dbHelper.getReadableDatabase().getPageSize();
	}
	
	
	public String getStatusbyId(long id){
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		try{
			Cursor cursor = db.query(TABLETIMELINE, DB_TEXT_COLUMNS, U_ID + "=" + id,null,null,null,null);
			try{
				return cursor.moveToNext()?cursor.getString(0):null;
			}finally{
				cursor.close();
			}
		}finally{
			db.close();
		}
	}	
}
