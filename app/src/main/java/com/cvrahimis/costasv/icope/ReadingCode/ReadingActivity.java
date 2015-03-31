package com.cvrahimis.costasv.icope.ReadingCode;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.DBAdapter;
import com.cvrahimis.costasv.icope.ICopeActivity;
import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.MyApplication;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ReadingActivity extends ActionBarActivity {
    DBAdapter db;
    private ArrayList<Quote> quotesList;
    private int count = 0;
    private TextView quote;
    private TextView author;
    private ICopeActivity activity;
    private TimerTask mTimerTask;
    private int seconds = 0;
    ICopePatDB patDB;
    final Handler handler = new Handler();
    Timer t = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        startTimerTask();

        SimpleDateFormat sdf = new SimpleDateFormat("H");
        int hour = Integer.parseInt(sdf.format(new Date()));

        sdf = new SimpleDateFormat("MM/d/yyyy h:m");
        String str = sdf.format(new Date());

        if(hour > 12)
            str = str +"pm";
        else
            str = str + "am";
        activity = new ICopeActivity("Reading", str);

        quote = (TextView) findViewById(R.id.quote);
        author = (TextView) findViewById(R.id.author);

        db = new DBAdapter(this);
        db.open();

        patDB = new ICopePatDB(this);
        patDB.open();

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.readingBackground);
        Drawable d;

        sdf = new SimpleDateFormat("HH");
        str = sdf.format(new Date());


        hour = Integer.parseInt(str);
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

        quotesList = new ArrayList<>();

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
        String[] authors = new String[]{"–Dr. Seuss","–Walt Disney","–Buddha", "–Buddha", "–Henry David Thoreau",
                                        "-Buddha","–Theodore Roosevelt","–Oscar Wilde", "–Wes Fessler", "–Ralph Waldo Emerson",
                                        "–Karen Ravn","–Lady Gage","–Buddha", "–Carl Jung", "–Buddha",
                                        "-Michael Jordan","–Mother Teresa","–Helen Keller", "–Audrey Hepburn", "–Ralph Waldo Emerson",
                                        "–Finding Nemo","–Eckhart Tolle", "–Thich Nhat Hanh", "–Dalai Lama"
        };
        String[] quotes = new String[]{"\"You have a brain in your head. You have feet in your shoes.  You can steer yourself any direction you choose!\"",
                                       "\"If you can dream it, you can do it\"",
                                       "\"You can search throughout the entire universe for someone who is more deserving of your love and affection than you are yourself, and that person is not to be found anywhere.  You yourself, as much as anybody in the entire universe deserve your love and affection.\"",
                                       "\"Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment.\"",
                                       "\"Go confidently in the direction of your dreams. Live the life you have imagined.\"",
                                       "\"What you think you become. What you feel you attract. What you imagine you create.\"",
                                       "\"Believe you can and you’re halfway there.\"",
                                       "\"To love oneself is the beginning of a lifelong romance.\"",
                                       "\"Look for goodness in others, for beauty in the world, and for possibilities in yourself.\"",
                                       "\"What lies behind us and what lies before us are tiny matters compared to what lies within us.\"",
                                       "\"Only as high as I reach can I grow, only as far as I seek can I go, only as deep as I look can I see, only as much as I dream can I be.\"",
                                       "\"You have to be unique, and different, and shine in your own way.\"",
                                       "\"The mind is everything. What you think you become.\"",
                                       "\"Who looks outside, dreams. Who looks inside, awakes.\"",
                                       "\"The mind is everything.  What you think you become.\"",
                                       "\"You must expect great things of yourself before you can do them.\"",
                                       "\"Be happy in the moment, that’s enough. Each moment is all we need, not more.\"",
                                       "\"The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart.\"",
                                       "\"Nothing is impossible, the word itself says 'I'm possible'!\"",
                                       "\"There are far, far better things ahead than any we leave behind.\"",
                                       "\"Just keep swimming\"",
                                       "\"The power for creating a better future is contained in the present moment; you create a good future by creating a good present.\"",
                                       "\"There is no way to happiness, happiness is the way.\"",
                                       "\"If you want others to be happy, practice compassion.  If you want to be happy, practice compassion.\""
        };

        for(int i = 0; i < authors.length; i++)
        {
            db.insertNewQuote(quotes[i], authors[i]);
        }
    }

    public void nextQuote(View view){
        //Toast.makeText(getApplicationContext(), "Screen Tap", Toast.LENGTH_SHORT).show();
        count++;
        if(count >= quotesList.size())
            count = 0;

        //quote.startAnimation(AnimationUtils.loadAnimation(ReadingActivity.this, android.R.anim.slide_out_right));
        quote.setText(quotesList.get(count).getQuote());
        quote.startAnimation(AnimationUtils.loadAnimation(ReadingActivity.this, android.R.anim.slide_in_left));
        //author.startAnimation(AnimationUtils.loadAnimation(ReadingActivity.this, android.R.anim.fade_out));
        author.setText(quotesList.get(count).getAuthor());
        author.startAnimation(AnimationUtils.loadAnimation(ReadingActivity.this, android.R.anim.fade_in));
    }

    public void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        exitLogic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reading, menu);

        MenuItem itm1 = menu.add(0, 0, 0, "ADD");
        {
            itm1.setTitle("Done");
            itm1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

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
        switch (item.getItemId()) {
            case 0:
                exitLogic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void exitLogic(){
        /*ICopePatDB idb = new ICopePatDB(this);
        idb.open();
        if(idb.isPatientAndTherapistOnPhone())
        {*/
            stopTask();
            int hours = seconds / 3600;
            seconds = seconds - (3600 * hours);
            int min = seconds / 60;
            seconds = seconds - (60 * min);
            activity.setActivityDuration(hours + " hours " + min + " minuets " + seconds + " seconds");
            ((MyApplication) this.getApplication()).push(activity);
            Intent intent = new Intent(this, RatingScreenActivity.class);
            finish();
            //idb.close();
            startActivity(intent);
        /*}
        else
        {
            Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuActivity.class);
            finish();
            idb.close();
            startActivity(intent);
        }*/
    }

    public void startTimerTask(){

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        seconds++;
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 0, 1000);  //

    }

    public void stopTask(){

        if(mTimerTask!=null){
            mTimerTask.cancel();
        }

    }
    //public int getSeconds(){ return seconds; }


}
