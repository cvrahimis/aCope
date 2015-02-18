package com.cvrahimis.costasv.icope;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.MainActivity;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    int screenWidth;
    int screenHeight;

    private GestureDetector gestureDetector;
    private AbsoluteLayout.LayoutParams thermometerLayoutParams;
    private AbsoluteLayout.LayoutParams mesurmentViewLayoutParams;
    private ImageView mesurmentView;
    private ImageView thermometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(getApplicationContext(), new SwipeGestureDetector());

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.ratingScreenMainLayout);
        final AbsoluteLayout absLayout = (AbsoluteLayout) mainLayout.findViewById(R.id.urgeFrameView);

        mesurmentView = (ImageView) absLayout.findViewById(R.id.mesurmentView);
        mesurmentView.bringToFront();

        thermometer = (ImageView) absLayout.findViewById(R.id.thermometer);
        thermometerLayoutParams = (AbsoluteLayout.LayoutParams)thermometer.getLayoutParams();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        Toast.makeText(getApplicationContext(), String.valueOf((int)Math.floor(screenWidth)), Toast.LENGTH_SHORT).show();

        mesurmentViewLayoutParams = (AbsoluteLayout.LayoutParams)mesurmentView.getLayoutParams();
        mesurmentViewLayoutParams.x = (int)Math.floor(screenWidth * .14);
        mesurmentViewLayoutParams.y = ((int)Math.floor(screenHeight * .01)) + 3;
        mesurmentViewLayoutParams.height = (int)Math.floor(screenHeight * .05) - 1;

        //mesurmentView.setLayoutParams(absParams);

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
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rating_screen, menu);

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
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void activityPress(View view) {

        switch (view.getId()) {
            case R.id.done: {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe(double d) {
        int diff = (int)Math.floor(d);
        if(mesurmentViewLayoutParams.width - diff < 0)
            mesurmentViewLayoutParams.width = 1;
        else
            mesurmentViewLayoutParams.width = mesurmentViewLayoutParams.width - diff;

        mesurmentView.setLayoutParams(mesurmentViewLayoutParams);
        Toast.makeText(getApplicationContext(), String.valueOf(mesurmentViewLayoutParams.width), Toast.LENGTH_SHORT).show();
    }

    private void onRightSwipe(double d) {
        int diff = (int)Math.floor(d);
        if(mesurmentViewLayoutParams.width + diff > screenWidth * .738)
            mesurmentViewLayoutParams.width = (int)Math.floor(screenWidth * .738);
        else
            mesurmentViewLayoutParams.width = mesurmentViewLayoutParams.width + diff;

        mesurmentView.setLayoutParams(mesurmentViewLayoutParams);
        Toast.makeText(getApplicationContext(), String.valueOf(mesurmentViewLayoutParams.width), Toast.LENGTH_SHORT).show();
    }


    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private double SWIPE_MIN_DISTANCE;
        private double SWIPE_MAX_OFF_PATH;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            //SWIPE_MIN_DISTANCE = screenWidth * .25;
            SWIPE_MAX_OFF_PATH = screenHeight * .2;
            try {
                float heightDiff = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                /*if (heightDiff > SWIPE_MAX_OFF_PATH)
                {
                    Toast.makeText(getApplicationContext(),"height: " + SWIPE_MAX_OFF_PATH, Toast.LENGTH_LONG).show();
                    return false;
                }*/

                // Left swipe
                if (diff > 0) {
                    //Toast.makeText(getApplicationContext(),"diff: " + diff, Toast.LENGTH_LONG).show();
                    MainActivity.this.onLeftSwipe(diff);
                }
                // Right swipe
                else {
                    //Toast.makeText(getApplicationContext(),"diff: " + diff, Toast.LENGTH_LONG).show();
                    MainActivity.this.onRightSwipe(-diff);
                }

            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}
