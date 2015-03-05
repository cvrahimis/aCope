package com.cvrahimis.costasv.icope;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cvrahimis.costasv.icope.LoginCode.LoginActivity;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;


public class MainActivity extends Activity {

    ICopePatDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new ICopePatDB(this);
        db.open();
        Intent intent;

        if (db.isPatientAndTherapistOnPhone()) {
            intent = new Intent(this, MenuActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();

    }
}
