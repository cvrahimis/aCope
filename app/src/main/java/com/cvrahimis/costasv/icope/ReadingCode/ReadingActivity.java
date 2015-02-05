package com.cvrahimis.costasv.icope.ReadingCode;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.DBAdapter;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.WritingCode.Journal;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadingActivity extends ActionBarActivity {
    DBAdapter db;
    private ArrayList<Quote> quotesList;
    private int count = 0;
    private TextView quote;
    private TextView author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        quote = (TextView) findViewById(R.id.quote);
        author = (TextView) findViewById(R.id.author);

        db = new DBAdapter(this);
        db.open();

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.readingBackground);
        Drawable d;

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());


        int hour = Integer.parseInt(str);
        if (hour >= 12 && hour < 18)
        {
            d = getResources().getDrawable(R.drawable.afternoon);
            mainLayout.setBackground(d);
        }
        else if (hour > 0 && hour <= 24)
        {
            d = getResources().getDrawable(R.drawable.evening);
            mainLayout.setBackground(d);
        }
        else
        {
            d = getResources().getDrawable(R.drawable.morning);
            mainLayout.setBackground(d);
        }

        if(db.quoteCount() == 0)
            setUpQuoteTable();

        quotesList = new ArrayList<Quote>();

        getQuotesList();

        if(quotesList.size() > 0)
        {
            Quote q = quotesList.get(count);
            author.setText(q.getAuthor());
            quote.setText(q.getQuote());
        }

    }

    public void setUpQuoteTable()
    {
        String[] authors = new String[]{"\"–Albert Einstein\"","\"–Wayne Gretzky\"","\"–Buddha\"", "\"–Socrates\""};
        String[] quotes = new String[]{"\"Strive not to be a success, but rather to be of value\"","\"You miss 100% of the shots you don’t take\"","\"The mind is everything. What you think you become\"", "\"An unexamined life is not worth living\""};

        for(int i = 0; i < authors.length; i++)
        {
            db.insertNewQuote(quotes[i], authors[i]);
        }
    }

    public void nextQuote(View view){
        Toast.makeText(getApplicationContext(), "Screen Tap", Toast.LENGTH_SHORT).show();
        count++;
        if(count >= quotesList.size())
            count = 0;
        quote.setText(quotesList.get(count).getQuote());
        quote.startAnimation(AnimationUtils.loadAnimation(ReadingActivity.this, android.R.anim.fade_in));
        author.setText(quotesList.get(count).getAuthor());
        author.startAnimation(AnimationUtils.loadAnimation(ReadingActivity.this, android.R.anim.fade_in));
    }

    public void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reading, menu);
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

    public void getQuotesList()
    {
        Cursor cur = db.getAllQuotes();
        if (cur.moveToFirst())
        {
            do
            {
                quotesList.add(new Quote((long) cur.getLong(0), (String) cur.getString(1), (String) cur.getString(2)));
            }
            while (cur.moveToNext());
        }
    }
}