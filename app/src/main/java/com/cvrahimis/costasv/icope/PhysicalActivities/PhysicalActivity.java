package com.cvrahimis.costasv.icope.PhysicalActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.ICopeActivity;
import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PhysicalActivity extends ActionBarActivity {

    String[] exName;
    String[] exDescriptions;
    int screenWidth;
    private ICopeActivity activity;
    int screenHeight;
    int[] images = {R.drawable.backlifts1, R.drawable.leglifts1, R.drawable.crunches1, R.drawable.plank};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        SimpleDateFormat sdf = new SimpleDateFormat("H");
        int hour = Integer.parseInt(sdf.format(new Date()));

        sdf = new SimpleDateFormat("MM/d/yyyy h:m");
        String str = sdf.format(new Date());

        if(hour > 12)
            str = str +"pm";
        else
            str = str + "am";
        activity = new ICopeActivity("Drawing", str);

        Resources res = getResources();
        exName = res.getStringArray(R.array.exerciseNames);
        exDescriptions = res.getStringArray(R.array.exerciseDescriptions);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.physicalBackground);

        Drawable d;

        sdf = new SimpleDateFormat("HH");
        str = sdf.format(new Date());

        //int hour = Integer.parseInt(str);
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

        listView = (ListView)findViewById(R.id.listView);
        ExerciseAdapter adapter = new ExerciseAdapter(this, exName, images, exDescriptions, screenWidth);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        exitLogic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_physical, menu);

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

    public void exitLogic(){
        /*ICopePatDB db = new ICopePatDB(this);
        db.open();
        if(db.isPatientAndTherapistOnPhone())
        {*/
            Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RatingScreenActivity.class);
            finish();
            //db.close();
            startActivity(intent);
        /*}
        else
        {
            Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuActivity.class);
            finish();
            db.close();
            startActivity(intent);
        }*/
    }

}

class ExerciseAdapter extends ArrayAdapter<String>
{
    Context context;
    int[] imgs;
    String[] exerciseNames;
    String[] exerciseDescriptions;
    int screenWidth;
    ExerciseAdapter(Context c, String[] exNames, int images[],  String[] exDesc, int screenW)
    {
        super(c, R.layout.physical_row, R.id.exerciseName, exNames);
        this.context = c;
        this.imgs = images;
        this.exerciseNames = exNames;
        this.exerciseDescriptions = exDesc;
        this.screenWidth = screenW;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.physical_row, parent, false);

        ImageView exerciseImage = (ImageView)row.findViewById(R.id.exerciseImage);
        TextView exerciseName = (TextView) row.findViewById(R.id.exerciseName);
        TextView exerciseDescription = (TextView) row.findViewById(R.id.description);

        exerciseImage.setImageResource(imgs[position]);
        exerciseName.setText(exerciseNames[position]);
        exerciseDescription.setText(exerciseDescriptions[position]);

        ViewGroup.LayoutParams params = exerciseImage.getLayoutParams();
        params.width = (int)Math.floor(screenWidth * .2);
        params.height = (int)Math.floor(screenWidth * .2);
        exerciseImage.setLayoutParams(params);

        params = exerciseName.getLayoutParams();
        params.width = (int)Math.floor(screenWidth * .6);
        exerciseName.setLayoutParams(params);

        params = exerciseDescription.getLayoutParams();
        params.width = (int)Math.floor(screenWidth * .6);
        exerciseDescription.setLayoutParams(params);

        return row;
    }
}