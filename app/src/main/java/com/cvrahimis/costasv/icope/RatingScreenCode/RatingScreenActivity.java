package com.cvrahimis.costasv.icope.RatingScreenCode;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Looper;
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
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.ICopeActivity;
import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.MyApplication;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.SendActivities;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;


public class RatingScreenActivity extends ActionBarActivity {

    int screenWidth;
    int screenHeight;
    private GestureDetector gestureDetector;
    private ViewGroup.LayoutParams thermometerLayoutParams;
    private AbsoluteLayout.LayoutParams mesurmentViewLayoutParams;
    private ViewGroup.LayoutParams absLayoutParams;
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
    double section;
    int u;
    private String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_screen);

        db = new ICopePatDB(this);
        db.open();

        activity = "";

        /*if(db.isPatientOnPhone())
        {
            Cursor cursor = db.getAllPatients();
        }
        else
        {

        }*/

        feelingBtns = new int[]{R.id.lonely, R.id.ashamed, R.id.guilty, R.id.disgusted, R.id.angry, R.id.anxious, R.id.afraid, R.id.sad, R.id.depressed, R.id.okay, R.id.happy};
        //gestureDetector = new GestureDetector(getApplicationContext(), new SwipeGestureDetector());

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.ratingScreenMainLayout);
        final AbsoluteLayout absLayout = (AbsoluteLayout) mainLayout.findViewById(R.id.urgeFrameView);
        //absLayoutParams = absLayout.getLayoutParams();

        mesurmentView = (ImageView) absLayout.findViewById(R.id.mesurmentView);

        thermometer = (ImageView) absLayout.findViewById(R.id.thermometer);
        thermometerLayoutParams = thermometer.getLayoutParams();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //Toast.makeText(getApplicationContext(), "width: " + String.valueOf(thermometerLayoutParams.width) + " height: " + String.valueOf(thermometerLayoutParams.height), Toast.LENGTH_SHORT).show();

        mesurmentViewLayoutParams = (AbsoluteLayout.LayoutParams)mesurmentView.getLayoutParams();
        mesurmentViewLayoutParams.x = (int)Math.floor(screenWidth * .14);
        mesurmentViewLayoutParams.y = ((int)Math.floor(screenHeight * .01));
        mesurmentViewLayoutParams.height = (int)Math.floor(screenHeight * .055);
        urge = mesurmentViewLayoutParams.width;

        section = (screenWidth * .738) / 10;
        u = (int)Math.floor(mesurmentViewLayoutParams.width / section) + 1;

        thermometer.setOnTouchListener(new View.OnTouchListener() {

            float startx = 0.0f;
            float starty = 0.0f;
            float currentx = 0.0f;
            float currenty = 0.0f;
            int count = 0;
            Timer T = new Timer();

            @Override
            public boolean onTouch(View view, MotionEvent event){

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        startx = event.getX();
                        starty = event.getY();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                    {
                        currentx = event.getRawX();
                        currenty = event.getY();

                        float diffx = startx - currentx;
                        float diffy = starty - currenty;

                        if(Math.abs(diffy) < Math.abs(diffx))
                        {
                            if (startx < currentx) {
                                if(mesurmentViewLayoutParams.width + 10 > screenWidth * .738)
                                    mesurmentViewLayoutParams.width = (int)Math.floor(screenWidth * .738);
                                else
                                    mesurmentViewLayoutParams.width = mesurmentViewLayoutParams.width + 10;


                                mesurmentView.setLayoutParams(mesurmentViewLayoutParams);
                                didSwipe = true;
                            }
                            if (startx > currentx) {
                                if(mesurmentViewLayoutParams.width - 10 < 0)
                                    mesurmentViewLayoutParams.width = 1;
                                else
                                    mesurmentViewLayoutParams.width = mesurmentViewLayoutParams.width - 10;

                                mesurmentView.setLayoutParams(mesurmentViewLayoutParams);
                                didSwipe = true;
                            }
                            startx = currentx;
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
                        u = (int)Math.floor(mesurmentViewLayoutParams.width / section) + 1;
                        Toast.makeText(getApplicationContext(), String.valueOf(u), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });

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

    public void onActivityResult(int requestCode, int resultCode, Intent i)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            exit = (boolean) i.getBooleanExtra("exit", true);
            activity = (String) i.getStringExtra("activity");
        }

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
        Intent intent = new Intent(this, MenuActivity.class);
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
                insertData();
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
                        //insertData();
                        //exitAndGoHome();
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
                        insertData();
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
            {
                insertData();
                goToMenuActivity();
            }
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
                        //insertData();
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

        ICopeActivity activity = ((MyApplication) this.getApplication()).pop();
        long pID = 0;
        long tID = 0;
        //long aID = 0;

        Cursor cur = db.getPatientID();
        if (cur.moveToFirst()) {
            pID = Integer.parseInt(cur.getString(0));
        }
        cur = db.getTherapistID();
        if(cur.moveToFirst()){
            tID = Integer.parseInt(cur.getString(0));
        }

        if(pID > 0 && tID > 0 && activity == null)
        {

            SimpleDateFormat sdf = new SimpleDateFormat("H");
            int hour = Integer.parseInt(sdf.format(new Date()));

            sdf = new SimpleDateFormat("MM/d/yyyy h:m");
            String str = sdf.format(new Date());

            if(hour > 12)
                str = str +"pm";
            else
                str = str + "am";

            if(exit)
            {
                db.insertNewActivity(pID, tID, "Exit Rating", mood, u, str, "0 hours 0 minuets 0 seconds");
                new SendActivities().execute(this);
            }
            else
                db.insertNewActivity(pID, tID, "Entrance Rating", mood, u, str, "0 hours 0 minuets 0 seconds");

        }
        else if(pID > 0 && tID > 0 && activity != null)
        {
            db.insertNewActivity(pID, tID, activity.getActivityName(), mood, u, activity.getActivityTime(), activity.getActivityDuration());
        }


    }
}
