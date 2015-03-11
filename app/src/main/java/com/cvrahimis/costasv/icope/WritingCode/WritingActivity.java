package com.cvrahimis.costasv.icope.WritingCode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.DBAdapter;
import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritingActivity extends ActionBarActivity {

    private DBAdapter db;
    private EditText title;
    private EditText entry;
    private long rowID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        db = new DBAdapter(this);
        db.open();

        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.background);

        Drawable d;

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());

        title = (EditText) findViewById(R.id.title);
        title.setHint(R.string.titleHint);

        entry = (EditText) findViewById(R.id.entry);
        entry.setHint(R.string.entryHint);

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
        getMenuInflater().inflate(R.menu.menu_writing, menu);

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

    public void activityPress(View view) {

        switch(view.getId())
        {
            case R.id.saveBtn:
            {
                if(rowID == 0)
                {
                    Toast.makeText(getApplicationContext(), "Save Button Pressed", Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getApplicationContext(), "Title: " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Entry: " + entry.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (!entry.getText().toString().equals("") && !title.getText().toString().equals("")) {
                        //Toast.makeText(getApplicationContext(), "Title: " + title.getText().toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Entry: " + entry.getText().toString(), Toast.LENGTH_SHORT).show();
                        /*
                        PROBABLY DONT NEED DATE
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        String date = sdf.format(new Date());
                        */
                        //Toast.makeText(getApplicationContext(), "DateFormat: " + date, Toast.LENGTH_SHORT).show();

                        rowID = db.insertNewJournal(title.getText().toString(), entry.getText().toString()/*, date*/);
                        //Toast.makeText(getApplicationContext(), "Saved " + title.getText().toString() + ": " + rowID, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    db.updateJournal(rowID,title.getText().toString(), entry.getText().toString());
                }
                break;
            }
            case R.id.openBtn:
            {
                Toast.makeText(getApplicationContext(), "Open Button Pressed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, OpenWritingActivity.class);
                //finish();
                startActivityForResult(intent, 1);

                break;
            }
            case R.id.deleteBtn:
            {
                Toast.makeText(getApplicationContext(), "Delete Button Pressed", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("Delete Journal");
                newDialog.setMessage("Are you sure you want to delete the current journal?");
                newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(rowID != 0)
                        {
                            if(db.deleteJournalEntry(rowID))
                            {
                                Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                                entry.setText("");
                                title.setText("");
                                rowID = 0;
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Delete Not Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();

                break;
            }
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent i)
    {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            rowID = Integer.parseInt(i.getStringExtra("id"));
            title.setText((String) i.getStringExtra("title"));
            entry.setText((String) i.getStringExtra("entry"));
        }
    }

    public void exitLogic(){
        /*ICopePatDB idb = new ICopePatDB(this);
        idb.open();
        if(idb.isPatientAndTherapistOnPhone())
        {*/
            Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();
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
}
