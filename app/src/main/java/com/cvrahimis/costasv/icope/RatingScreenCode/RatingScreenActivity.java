package com.cvrahimis.costasv.icope.RatingScreenCode;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;


public class RatingScreenActivity extends ActionBarActivity {

    int screenWidth;
    int screenHeight;
    private GestureDetector gestureDetector;
    private AbsoluteLayout.LayoutParams thermometerLayoutParams;
    private AbsoluteLayout.LayoutParams mesurmentViewLayoutParams;
    private ImageView mesurmentView;
    private ImageView thermometer;
    public final static int RESULT_CLOSE_ALL = 0;
    public boolean didFeelingPressed = false;
    public boolean didSwipe = false;
    public boolean exit = false;
    public int[] feelingBtns;
    public String mood = "";
    private ICopePatDB db;
    public int urge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new ICopePatDB(this);
        db.open();

        /*if(db.isPatientOnPhone())
        {
            Cursor cursor = db.getAllPatients();
        }
        else
        {

        }*/

        feelingBtns = new int[]{R.id.lonely, R.id.ashamed, R.id.guilty, R.id.disgusted, R.id.angry, R.id.anxious, R.id.afraid, R.id.sad, R.id.depressed, R.id.okay, R.id.happy};
        gestureDetector = new GestureDetector(getApplicationContext(), new SwipeGestureDetector());

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.ratingScreenMainLayout);
        final AbsoluteLayout absLayout = (AbsoluteLayout) mainLayout.findViewById(R.id.urgeFrameView);

        mesurmentView = (ImageView) absLayout.findViewById(R.id.mesurmentView);
        //mesurmentView.bringToFront();

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
        urge = mesurmentViewLayoutParams.width;

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
    public void onResume(){
        super.onResume();
        didFeelingPressed = false;
        didSwipe = false;
        defaultColors();
    }

    @Override
    public void onBackPressed() {
        exitLogic();
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
                exitLogic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy(){
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    public void activityPress(View view) {
        switch (view.getId()) {
            case R.id.done: {
                exitLogic();
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
        didSwipe = true;

        double section = (screenWidth * .738) / 10;
        int u = (int)Math.floor(mesurmentViewLayoutParams.width / section);
        Toast.makeText(getApplicationContext(), String.valueOf(u), Toast.LENGTH_SHORT).show();
    }

    private void onRightSwipe(double d) {
        int diff = (int)Math.floor(d);
        if(mesurmentViewLayoutParams.width + diff > screenWidth * .738)
            mesurmentViewLayoutParams.width = (int)Math.floor(screenWidth * .738);
        else
            mesurmentViewLayoutParams.width = mesurmentViewLayoutParams.width + diff;


        mesurmentView.setLayoutParams(mesurmentViewLayoutParams);
        didSwipe = true;

        double section = (screenWidth * .738) / 10;
        int u = (int)Math.floor(mesurmentViewLayoutParams.width / section);
        Toast.makeText(getApplicationContext(), String.valueOf(u), Toast.LENGTH_SHORT).show();
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

                if (heightDiff > SWIPE_MAX_OFF_PATH)
                {
                    Toast.makeText(getApplicationContext(),"height: " + SWIPE_MAX_OFF_PATH, Toast.LENGTH_LONG).show();
                    return false;
                }

                // Left swipe
                if (diff > 0) {
                    //Toast.makeText(getApplicationContext(),"diff: " + diff, Toast.LENGTH_LONG).show();
                    RatingScreenActivity.this.onLeftSwipe(diff);
                }
                // Right swipe
                else {
                    //Toast.makeText(getApplicationContext(),"diff: " + diff, Toast.LENGTH_LONG).show();
                    RatingScreenActivity.this.onRightSwipe(-diff);
                }

            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent i)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
            exit = (boolean) i.getBooleanExtra("exit", true);
    }

    public void feelingClick(View view){
        didFeelingPressed = true;
        switch (view.getId())
        {
            case R.id.lonely:
            {
                changeFeelingPressColors(R.id.lonely);
                break;
            }
            case R.id.ashamed:
            {
                changeFeelingPressColors(R.id.ashamed);
                break;
            }
            case R.id.guilty:
            {
                changeFeelingPressColors(R.id.guilty);
                break;
            }
            case R.id.disgusted:
            {
                changeFeelingPressColors(R.id.disgusted);
                break;
            }
            case R.id.angry:
            {
                changeFeelingPressColors(R.id.angry);
                break;
            }
            case R.id.anxious:
            {
                changeFeelingPressColors(R.id.anxious);
                break;
            }
            case R.id.afraid:
            {
                changeFeelingPressColors(R.id.afraid);
                break;
            }
            case R.id.sad:
            {
                changeFeelingPressColors(R.id.sad);
                break;
            }
            case R.id.depressed:
            {
                changeFeelingPressColors(R.id.depressed);
                break;
            }
            case R.id.okay:
            {
                changeFeelingPressColors(R.id.okay);
                break;
            }
            case R.id.happy:
            {
                changeFeelingPressColors(R.id.happy);
                break;
            }
            default:
                break;
        }
    }
    public void changeFeelingPressColors(int btnID)
    {
        Button tempFeelingBtn;
        Button moodBtn = (Button) findViewById(btnID);
        mood = moodBtn.getText().toString();
        moodBtn.setBackgroundColor(Color.parseColor("#0000FF"));
        moodBtn.setTextColor(Color.parseColor("#FFFFFF"));
        for(int i = 0; i < feelingBtns.length; i++)
        {
            if(feelingBtns[i] != btnID)
            {
                tempFeelingBtn = (Button) findViewById(feelingBtns[i]);
                tempFeelingBtn.setBackgroundColor(Color.parseColor("#00FF33"));
                tempFeelingBtn.setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    public void defaultColors(){
        Button tempFeelingBtn;
        for(int i = 0; i < feelingBtns.length; i++)
        {
            tempFeelingBtn = (Button) findViewById(feelingBtns[i]);
            tempFeelingBtn.setBackgroundColor(Color.parseColor("#00FF33"));
            tempFeelingBtn.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void goToMenuActivity(){
        Intent intent = new Intent(this, com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity.class);
        startActivityForResult(intent, 1);
    }

    public void exitAndGoHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }

    public void exitLogic(){
        if(exit)
        {
            if(didSwipe && didFeelingPressed)
            {
                exitAndGoHome();
            }
            else if(didSwipe && !didFeelingPressed)
            {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("You did not select a feeling");
                newDialog.setMessage("Please select one");
                newDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        insertData();
                        exitAndGoHome();
                    }
                });
                newDialog.show();
            }
            else if(!didSwipe && didFeelingPressed)
            {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("You did not select an urge");
                newDialog.setMessage("Do you like to keep it the same?");
                newDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exitAndGoHome();
                    }
                });
                newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();
            }
            else
            {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("Rating Screen Not complete");
                newDialog.setMessage("You must select a feeling and rate urge.");
                newDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                newDialog.show();
            }
        }
        else
        {
            if(didSwipe && didFeelingPressed)
                goToMenuActivity();

            else if(didSwipe && !didFeelingPressed)
            {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("You did not select a feeling");
                newDialog.setMessage("Please select one");
                newDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                newDialog.show();
            }
            else if(!didSwipe && didFeelingPressed)
            {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("You did not select an urge");
                newDialog.setMessage("Do you like to keep it the same?");
                newDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToMenuActivity();
                    }
                });
                newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();
            }
            else
            {
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("Rating Screen Not complete");
                newDialog.setMessage("You must select a feeling and rate urge.");
                newDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                newDialog.show();
            }
        }
    }

    public void insertData(){
        /*double section = (screenWidth * .738) / 10;
        int u = (int)Math.floor(mesurmentViewLayoutParams.width / section);
        db.insertNewRatingScreen()*/
    }
}