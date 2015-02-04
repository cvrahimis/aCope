package com.cvrahimis.costasv.icope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBAdapter 
{
	static final String ROWID = "_id";
	static final String TITLE = "title";
	static final String ENTRY = "entry";
    static final String DATE = "date";
	static final String TAG = "DBAdapter";
	static final String DATABASE_NAME = "ICopeDB";
	static final String DATABASE_TABLE = "journalEntries";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE = "create table journalEntries (_id integer primary key autoincrement, title text not null, entry text not null, date text not null);";
	
	final Context context;
	
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
		
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try
			{
				db.execSQL(DATABASE_CREATE);
			}
			catch (SQLException e) { e.printStackTrace(); }
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
		{
			db.execSQL("DROP TABLE IF EXISTS journalEntries");
			onCreate(db);
		}
	}
	
	
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	
	public void close()
	{
		DBHelper.close();
	}

	
	public long insertNewItem(String t,  String e, String d)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(TITLE, t);
		initialValues.put(ENTRY, e);
		initialValues.put(DATE, d);
		return db.insert(DATABASE_TABLE,  null,  initialValues);
	}

	
	public boolean deleteItem(long rowId)
	{
		return db.delete(DATABASE_TABLE, ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteItemByTitle(String t)
	{
		return db.delete(DATABASE_TABLE, TITLE + "='" + t + "'", null) > 0;
	}
	
	
	public Cursor getAllItems()
	{
		return db.query(DATABASE_TABLE, new String [] {ROWID, TITLE, ENTRY, DATE}, null, null, null, null, null);
	}
	
	
	public Cursor getItem(long rowId) throws SQLException
	{
		Cursor cur = null;
		
		try
		{
			cur = db.query(true, DATABASE_TABLE,  new String [] {ROWID, TITLE, ENTRY, DATE},  ROWID + "=" + rowId, null, null, null, null, null);
		}
		catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}
		
		if (cur != null) cur.moveToFirst();
		return cur;
	}
	
	
	public Cursor getItemByTitle(String title) throws SQLException
	{
		Cursor cur = null;
		try
		{
			cur = db.query(false, DATABASE_TABLE,  new String [] {ROWID, TITLE, ENTRY, DATE},  TITLE + "='" + title + "'", null, null, null, null, null);
		}
		catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}
	
		if (cur != null) cur.moveToFirst();
		return cur;
	}

    public Cursor getItemByDate(String date) throws SQLException
    {
        Cursor cur = null;
        try
        {
            cur = db.query(false, DATABASE_TABLE,  new String [] {ROWID, TITLE, ENTRY, DATE},  DATE + "='" + date + "'", null, null, null, null, null);
        }
        catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}

        if (cur != null) cur.moveToFirst();
        return cur;
    }
	
	
	public int getItemId(String title)
	{
		Cursor cur = getItemByTitle(title);

		return cur.getInt(0);
	}
	
	
	public Cursor getAllIds()
	{		
		Cursor cur = db.query(true, DATABASE_TABLE,  new String [] {ROWID},  null, null, null, null, null, null);
		if (cur != null) cur.moveToFirst();
		return cur;
	}
	
	public boolean updateItem(long rowId, String title, String entry)
	{
		ContentValues args = new ContentValues();
		args.put(TITLE, title);
		args.put(ENTRY, entry);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
        String str = sdf.format(new Date());

        args.put(DATE, str);

		return db.update(DATABASE_TABLE, args, ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateItemEntry(long rowId, String entry)
	{
		ContentValues args = new ContentValues();
		args.put(ENTRY, entry);
		return db.update(DATABASE_TABLE, args, ROWID + "=" + rowId, null) > 0;
	}
	public boolean hasEntry(long rowId)
	{
		Cursor cur = null;
		
		try
		{
			cur = db.query(true, DATABASE_TABLE,  new String [] {ENTRY},  ROWID + "=" + rowId, null, null, null, null, null);
		}
		catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}
		
		if (cur != null) 
		{
			cur.moveToFirst();
			if(cur.getString(0).equals(""))
			{
				return false;
			}
		}
		return true;		
	}
}


