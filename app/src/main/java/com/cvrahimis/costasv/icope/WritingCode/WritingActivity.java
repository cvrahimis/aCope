package com.cvrahimis.costasv.icope.WritingCode;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.DrawingCode.DrawingPad;
import com.cvrahimis.costasv.icope.MusicCode.MusicActivity;
import com.cvrahimis.costasv.icope.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.background);

        Drawable d;

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());


        int hour = Integer.parseInt(str);
        if(hour >= 12 && hour < 18)
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
}
