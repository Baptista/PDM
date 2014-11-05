package com.pdm.thoth;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CourseProvider extends ContentProvider{

	private static final String TAG = CourseProvider.class.getSimpleName();
	
	public static final Uri CONTENT_URI = Uri
			.parse("content://thoth.courseprovider");
	
	DbCourse dbcourse;
	
	@Override
	public boolean onCreate() {
		dbcourse = new DbCourse(this.getContext());
		return true;
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		long id = this.getId(uri);
	    int count;
	    SQLiteDatabase db = dbcourse.getDbcourse().getWritableDatabase();
	    try {
	      if (id < 0) {
	        count = db.delete(DbCourse.TABLE, selection, selectionArgs);
	      } else {
	        count = db.delete(DbCourse.TABLE, DbCourse.C_ID + "=" + id, null);
	      }
	    } finally {
	      db.close();
	    }

	    // Notify the Context's ContentResolver of the change
	    getContext().getContentResolver().notifyChange(uri, null);

	    return count;

	}

	@Override
	public String getType(Uri arg0) {
		return this.getType(arg0);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbcourse.getDbcourse().getWritableDatabase();
		try{
			long id = db.insertOrThrow(DbCourse.TABLE, null, values);
			if (id == -1) {
		        throw new RuntimeException(String.format(
		            "%s: Failed to insert [%s] to [%s] for unknown reasons.", TAG,
		            values, uri));
			}else{
				Uri newUri = ContentUris.withAppendedId(uri, id);
		        // Notify the Context's ContentResolver of the change
		        getContext().getContentResolver().notifyChange(newUri, null);
		        return newUri;
			}
		}finally{
			db.close();
		}
	}

	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {

		
			long id = this.getId(uri);
		    SQLiteDatabase db = dbcourse.getDbcourse().getReadableDatabase();
		    Log.d(TAG, "querying");

		    Cursor c;

		    if (id < 0) {
		      c = db.query(DbCourse.TABLE, projection, selection, selectionArgs,
		          null, null, sortOrder);
		    } else {
		      c = db.query(DbCourse.TABLE, projection, DbCourse.C_ID + "=" + id,
		          null, null, null, null);
		    }

		    // Notify the context's ContentResolver if the cursor result set changes
		    c.setNotificationUri(getContext().getContentResolver(), uri);

		    return c;
		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		long id = this.getId(uri);
	    int count;
	    SQLiteDatabase db = dbcourse.getDbcourse().getWritableDatabase();
	    try {
	      if (id < 0) {
	        count = db.update(DbCourse.TABLE, values, selection, selectionArgs);
	      } else {
	        count = db.update(DbCourse.TABLE, values, DbCourse.C_ID + "=" + id,
	            null);
	      }
	    } finally {
	      db.close();
	    }

	    // Notify the Context's ContentResolver of the change
	    getContext().getContentResolver().notifyChange(uri, null);

	    return count;
	}

	private long getId(Uri uri) {
	    String lastPathSegment = uri.getLastPathSegment();
	    if (lastPathSegment != null) {
	      try {
	        return Long.parseLong(lastPathSegment);
	      } catch (NumberFormatException e) {
	        // at least we tried
	      }
	    }
	    return -1;
	  }
}
