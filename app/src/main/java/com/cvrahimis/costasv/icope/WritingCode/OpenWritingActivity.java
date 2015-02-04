package com.cvrahimis.costasv.icope.WritingCode;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.DBAdapter;
import com.cvrahimis.costasv.icope.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class OpenWritingActivity extends ActionBarActivity {

    private DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_writing);

        db = new DBAdapter(this);
        db.open();




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

    public static class WritingActivity extends ActionBarActivity {
        DBAdapter db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_writing);

            final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.background);
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

            final EditText title = (EditText) findViewById(R.id.title);
            title.setHint(R.string.titleHint);

            final EditText entry = (EditText) findViewById(R.id.entry);
            entry.setHint(R.string.entryHint);
        }

        public void activityPress(View view) {

            switch(view.getId())
            {
                case R.id.saveBtn:
                {
                    Toast.makeText(getApplicationContext(), "Save Button Pressed", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(this, MusicActivity.class);
                    //startActivityForResult(intent, 1);
                    break;
                }
                case R.id.openBtn:
                {
                    Toast.makeText(getApplicationContext(), "Open Button Pressed", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.deleteBtn:
                {
                    Toast.makeText(getApplicationContext(), "Delete Button Pressed", Toast.LENGTH_SHORT).show();

                    //Intent intent = new Intent(this, DrawingPad.class);
                    //startActivityForResult(intent, 1);

                    break;
                }
                default:
                    break;
            }
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_writing, menu);
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
}
