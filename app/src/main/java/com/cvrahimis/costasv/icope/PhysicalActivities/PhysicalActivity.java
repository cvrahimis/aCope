package com.cvrahimis.costasv.icope.PhysicalActivities;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cvrahimis.costasv.icope.R;

public class PhysicalActivity extends ActionBarActivity {

    String[] exName;
    String[] exDescriptions;
    int[] images = {R.drawable.backlifts1, R.drawable.leglifts1, R.drawable.crunches1, R.drawable.plank};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        Resources res = getResources();
        exName = res.getStringArray(R.array.exerciseNames);
        exDescriptions = res.getStringArray(R.array.exerciseDescriptions);

        listView = (ListView)findViewById(R.id.listView);
        ExerciseAdapter adapter = new ExerciseAdapter(this, exName, images, exDescriptions);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_physical, menu);
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

class ExerciseAdapter extends ArrayAdapter<String>
{
    Context context;
    int[] imgs;
    String[] exerciseNames;
    String[] exerciseDescriptions;
    ExerciseAdapter(Context c, String[] exNames, int images[],  String[] exDesc)
    {
        super(c, R.layout.physical_row, R.id.exerciseName, exNames);
        this.context = c;
        this.imgs = images;
        this.exerciseNames = exNames;
        this.exerciseDescriptions = exDesc;
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



        return row;
    }
}