package com.cvrahimis.costasv.icope.WritingCode;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.DBAdapter;
import com.cvrahimis.costasv.icope.MusicCode.Song;
import com.cvrahimis.costasv.icope.MusicCode.SongAdapter;
import com.cvrahimis.costasv.icope.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class OpenWritingActivity extends ActionBarActivity {

    private DBAdapter db;
    private ListView journalView;
    private ArrayList<Journal> journalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_writing);


        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.openWritingBackground);
        Drawable d;

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());


        int hour = Integer.parseInt(str);
        if(hour >= 12 && hour < 18)
        {
            d = getResources().getDrawable(R.drawable.afternoon);
            mainLayout.setBackground(d);
            //background.setImageDrawable(d);
        }
        else if (hour > 0 && hour <= 24)
        {
            d = getResources().getDrawable(R.drawable.evening);
            mainLayout.setBackground(d);
            //background.setImageDrawable(d);
        }
        else
        {
            d = getResources().getDrawable(R.drawable.morning);
            mainLayout.setBackground(d);
            //background.setImageDrawable(d);
        }


        db = new DBAdapter(this);
        db.open();

        //Cursor cur = db.getAllItems();

        journalView = (ListView)findViewById(R.id.journalList);

        journalList = new ArrayList<Journal>();

        getJournalList();

        Collections.sort(journalList, new Comparator<Journal>() {
            public int compare(Journal a, Journal b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });


        JournalAdapter journalAdt = new JournalAdapter(this, journalList);
        journalView.setAdapter(journalAdt);

    }

    public void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

    public void getJournalList()
    {
        Cursor cur = db.getAllJournals();
        if (cur.moveToFirst())
        {
            do
            {
                journalList.add(new Journal((long) cur.getLong(0), (String) cur.getString(1), (String) cur.getString(2)));
            }
            while (cur.moveToNext());
        }
    }

    //user song select
    public void journalPicked(View view) {
        //if (null != view.getTag().toString())
        //{
            Journal j = journalList.get(Integer.parseInt(view.getTag().toString()));
            Intent i = new Intent();
            i.putExtra("id", String.valueOf(j.getID()));
            i.putExtra("title", j.getTitle());
            i.putExtra("entry", j.getEntry());
            setResult(RESULT_OK, i);
            finish();
        //}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_writing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
