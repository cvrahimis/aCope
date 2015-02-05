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
	static final String Journal_ROWID = "_id";
	static final String Journal_TITLE = "title";
	static final String Journal_ENTRY = "entry";
    //static final String DATE = "date";
	static final String TAG = "DBAdapter";
	static final String DATABASE_NAME = "ICopeDB";
	static final String JOURNAL_TABLE = "journalEntries";
    static final String QUOTES_TABLE = "quotes";
    static final String Quotes_ROWID = "_id";
    static final String Quotes_Quote = "quote";
    static final String Quotes_Author = "author";
	static final int DATABASE_VERSION = 1;
	
	static final String CREATE_JOURNAL_TABLE = "create table journalEntries (_id integer primary key autoincrement, title text not null, entry text not null);";
	static final String CREATE_QUOTE_TABLE = "create table quotes(_id integer primary key autoincrement, quote text not null, author text not null);";

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
				db.execSQL(CREATE_JOURNAL_TABLE);
                db.execSQL(CREATE_QUOTE_TABLE);
			}
			catch (SQLException e) { e.printStackTrace(); }
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
		{
			db.execSQL("DROP TABLE IF EXISTS journalEntries");
            db.execSQL("DROP TABLE IF EXISTS quotes");
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

    public long insertNewQuote(String q, String a)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Quotes_Quote, q);
        initialValues.put(Quotes_Author, a);

        return db.insert(QUOTES_TABLE,  null,  initialValues);
    }
	
	public long insertNewJournal(String t,  String e/*, String d*/)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(Journal_TITLE, t);
		initialValues.put(Journal_ENTRY, e);
		//initialValues.put(DATE, d);
		return db.insert(JOURNAL_TABLE,  null,  initialValues);
	}

	
	public boolean deleteJournalEntry(long rowId)
	{
		return db.delete(JOURNAL_TABLE, Journal_ROWID + "=" + rowId, null) > 0;
	}

    public boolean deleteQuoteByRowID(long rowId)
    {
        return db.delete(QUOTES_TABLE, Quotes_ROWID + "=" + rowId, null) > 0;
    }
	
	public Cursor getAllJournals()
	{
		return db.query(JOURNAL_TABLE, new String [] {Journal_ROWID, Journal_TITLE, Journal_ENTRY/*, DATE*/}, null, null, null, null, null);
	}

    public Cursor getAllQuotes()
    {
        return db.query(QUOTES_TABLE, new String [] {Quotes_ROWID, Quotes_Quote, Quotes_Author}, null, null, null, null, null);
    }

	public Cursor getJournal(long rowId) throws SQLException
	{
		Cursor cur = null;
		
		try
		{
			cur = db.query(true, JOURNAL_TABLE,  new String [] {Journal_ROWID, Journal_TITLE, Journal_ENTRY/*, DATE*/},  Journal_ROWID + "=" + rowId, null, null, null, null, null);
		}
		catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}
		
		if (cur != null) cur.moveToFirst();
		return cur;
	}

    public Cursor getQuote(long rowId) throws SQLException
    {
        Cursor cur = null;

        try
        {
            cur = db.query(true, QUOTES_TABLE,  new String [] {Quotes_ROWID, Quotes_Quote, Quotes_Author},  Quotes_ROWID + "=" + rowId, null, null, null, null, null);
        }
        catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}

        if (cur != null) cur.moveToFirst();
        return cur;
    }
	
	
	public Cursor getJournalByTitle(String title) throws SQLException
	{
		Cursor cur = null;
		try
		{
			cur = db.query(false, JOURNAL_TABLE,  new String [] {Journal_ROWID, Journal_TITLE, Journal_ENTRY/*, DATE*/},  Journal_TITLE + "='" + title + "'", null, null, null, null, null);
		}
		catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}
	
		if (cur != null) cur.moveToFirst();
		return cur;
	}

    public Cursor getQuoteByQuote(String quote) throws SQLException
    {
        Cursor cur = null;
        try
        {
            cur = db.query(false, QUOTES_TABLE,  new String [] {Quotes_ROWID, Quotes_Quote, Quotes_Author},  Quotes_Quote + "='" + quote + "'", null, null, null, null, null);
        }
        catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}

        if (cur != null) cur.moveToFirst();
        return cur;
    }

    public Cursor getQuoteByAuthor(String author) throws SQLException
    {
        Cursor cur = null;
        try
        {
            cur = db.query(false, QUOTES_TABLE,  new String [] {Quotes_ROWID, Quotes_Quote, Quotes_Author},  Quotes_Author + "='" + author + "'", null, null, null, null, null);
        }
        catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}

        if (cur != null) cur.moveToFirst();
        return cur;
    }

    /*public Cursor getItemByDate(String date) throws SQLException
    {
        Cursor cur = null;
        try
        {
            cur = db.query(false, JOURNAL_TABLE,  new String [] {Journal_ROWID, Journal_TITLE, Journal_ENTRY, DATE},  DATE + "='" + date + "'", null, null, null, null, null);
        }
        catch(SQLException e) {Log.d(null, "DB EXCEPTION" + e);}

        if (cur != null) cur.moveToFirst();
        return cur;
    }*/
	
	
	public int getJournalId(String title)
	{
		Cursor cur = getJournalByTitle(title);

		return cur.getInt(0);
	}

    public int getQuoteId(String quote)
    {
        Cursor cur = getQuoteByQuote(quote);
        return cur.getInt(0);
    }
	
	public Cursor getAllJournalIds()
	{		
		Cursor cur = db.query(true, JOURNAL_TABLE,  new String [] {Journal_ROWID},  null, null, null, null, null, null);
		if (cur != null) cur.moveToFirst();
		return cur;
	}

    public Cursor getAllQuoteIds()
    {
        Cursor cur = db.query(true, QUOTES_TABLE,  new String [] {Quotes_ROWID},  null, null, null, null, null, null);
        if (cur != null) cur.moveToFirst();
        return cur;
    }
	
	public boolean updateJournal(long rowId, String title, String entry)
	{
		ContentValues args = new ContentValues();
		args.put(Journal_TITLE, title);
		args.put(Journal_ENTRY, entry);

        /*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = sdf.format(new Date());
        args.put(DATE, date);*/

		return db.update(JOURNAL_TABLE, args, Journal_ROWID + "=" + rowId, null) > 0;
	}

    public boolean updateQuotes(long rowId, String quote, String author)
    {
        ContentValues args = new ContentValues();
        args.put(Quotes_Quote, quote);
        args.put(Quotes_Author, author);

        /*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = sdf.format(new Date());
        args.put(DATE, date);*/

        return db.update(QUOTES_TABLE, args, Quotes_ROWID + "=" + rowId, null) > 0;
    }

	public boolean updateJournalEntry(long rowId, String entry)
	{
		ContentValues args = new ContentValues();
		args.put(Journal_ENTRY, entry);
		return db.update(JOURNAL_TABLE, args, Journal_ROWID + "=" + rowId, null) > 0;
	}
	public boolean hasJournalEntry(long rowId)
	{
		Cursor cur = null;
		
		try
		{
			cur = db.query(true, JOURNAL_TABLE,  new String [] {Journal_ENTRY},  Journal_ROWID + "=" + rowId, null, null, null, null, null);
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

    public int quoteCount()
    {
        return getAllQuoteIds().getCount();
    }

    public boolean hasQuote(long rowId)
    {
        Cursor cur = null;

        try
        {
            cur = db.query(true, QUOTES_TABLE,  new String [] {Quotes_Quote, Quotes_Author},  Quotes_ROWID + "=" + rowId, null, null, null, null, null);
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


