package com.cvrahimis.costasv.icope.MenuActitvity;

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
import com.cvrahimis.costasv.icope.ReadingCode.ReadingActivity;
import com.cvrahimis.costasv.icope.WritingCode.WritingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MenuActivity extends ActionBarActivity {

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final TextView greetingLbl = (TextView) findViewById(R.id.greeting);
        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.menuActivityMainLayout);

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
            //background.setImageDrawable(d);
            greetingLbl.setText(R.string.afternoonGreet);
        }
        else if (hour > 0 && hour <= 24)
        {
            d = getResources().getDrawable(R.drawable.evening);
            mainLayout.setBackground(d);
            //background.setImageDrawable(d);
            greetingLbl.setText(R.string.eveningGreet);
        }
        else
        {
            d = getResources().getDrawable(R.drawable.morning);
            mainLayout.setBackground(d);
            //background.setImageDrawable(d);
            greetingLbl.setText(R.string.morningGreet);
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
            case R.id.music:
            {
                Toast.makeText(getApplicationContext(), "Music Button Pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MusicActivity.class);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.reading:
            {
                Toast.makeText(getApplicationContext(), "Reading Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ReadingActivity.class);
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.drawing:
            {
                Toast.makeText(getApplicationContext(), "Drawing Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DrawingPad.class);
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.journal:
            {
                Toast.makeText(getApplicationContext(), "Journal Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, WritingActivity.class);
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.exercise:
            {
                Toast.makeText(getApplicationContext(), "Exercise Button Pressed", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent i)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
        }
    }
}
