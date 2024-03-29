package com.cvrahimis.costasv.icope.MenuActitvity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.os.Process;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.Login;
import com.cvrahimis.costasv.icope.LoginCode.LoginActivity;
import com.cvrahimis.costasv.icope.MainActivity;
import com.cvrahimis.costasv.icope.MyApplication;
import com.cvrahimis.costasv.icope.PhysicalActivities.PhysicalActivity;
import com.cvrahimis.costasv.icope.DrawingCode.DrawingPad;
import com.cvrahimis.costasv.icope.MusicCode.MusicActivity;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;
import com.cvrahimis.costasv.icope.ReadingCode.ReadingActivity;
import com.cvrahimis.costasv.icope.WritingCode.WritingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MenuActivity extends ActionBarActivity {

    private int screenWidth;
    private int screenHeight;
    ICopePatDB db;
    public int therapistid;
    //public String[] patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("iSoothe");

        final TextView greetingLbl = (TextView) findViewById(R.id.greeting);
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.menuActivityMainLayout);

        db = new ICopePatDB(this);
        db.open();

        /*Cursor cursor = db.getTherapistID();

        if (cursor.moveToFirst()) {
            therapistid = cursor.getInt(cursor.getColumnIndex("therapistId"));
            Toast.makeText(getApplicationContext(), String.valueOf(therapistid), Toast.LENGTH_LONG).show();
        }*/

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        Drawable d;

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());


        int hour = Integer.parseInt(str);
        if(hour >= 12 && hour < 18)
        {
            d = getResources().getDrawable(R.drawable.afternoon);
            mainLayout.setBackground(d);
            greetingLbl.setText(R.string.afternoonGreet);
        }
        else if (hour > 0 && hour <= 24)
        {
            d = getResources().getDrawable(R.drawable.evening);
            mainLayout.setBackground(d);
            greetingLbl.setText(R.string.eveningGreet);
        }
        else
        {
            d = getResources().getDrawable(R.drawable.morning);
            mainLayout.setBackground(d);
            greetingLbl.setText(R.string.morningGreet);
        }

        if(db.isPatientAndTherapistOnPhone())
        {
            Cursor cur = db.getPatientName();
            if (cur.moveToFirst()) {
                greetingLbl.setText(greetingLbl.getText() + " " + cur.getString(0) + " " + cur.getString(1));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void activityPress(View view) {

        switch(view.getId())
        {
            case R.id.music:
            {
                //Toast.makeText(getApplicationContext(), "Music Button Pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MusicActivity.class);
                finish();
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.reading:
            {
                //Toast.makeText(getApplicationContext(), "Reading Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ReadingActivity.class);
                finish();
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.drawing:
            {
                //Toast.makeText(getApplicationContext(), "Drawing Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DrawingPad.class);
                finish();
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.journal:
            {
                //Toast.makeText(getApplicationContext(), "Journal Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, WritingActivity.class);
                finish();
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.exercise:
            {
                //Toast.makeText(getApplicationContext(), "Exercise Button Pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, PhysicalActivity.class);
                finish();
                startActivityForResult(intent, 1);
                break;
            }
            default:
                break;
        }
    }

    /*public void onDestroy(){
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }*/

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("exit", true);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent i)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
        }
    }
}
