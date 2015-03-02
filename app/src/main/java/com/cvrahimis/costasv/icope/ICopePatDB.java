package com.cvrahimis.costasv.icope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ICopePatDB {
    static final int DATABASE_VERSION = 1;
    static final String TAG = "ICopeDB";
    static final String DATABASE_NAME = "ICopePatDataDB";

    static final String therapist_Table = "therapist";
    static final String therapist_therapistId = "therapistId";
    static final String therapist_therapistFirstName = "therapistFirstName";
    static final String therapist_therapistLastName = "therapistLastName";

    static final String patient_Table = "patient";
    static final String patient_patientId = "patientId";
    static final String patient_therapistId = "therapistId";
    static final String patient_patientLogin = "patientLogin";
    static final String patient_patientPassword = "patientPassword";
    static final String patient_patientFirstName = "patientFirstName";
    static final String patient_patientLastName = "patientLastName";

    static final String activities_Table = "activities";
    static final String activities_activityId = "activityId";
    static final String activities_therapistId = "therapistId";
    static final String activities_patientId = "patientId";
    static final String activities_time = "time";
    static final String activities_activity = "activity";
    static final String activities_duration = "duration";

    static final String ratingScreen_Table = "RatingScreen";
    static final String ratingScreen_patientID = "patientID";
    static final String ratingScreen_activityId = "activityId";
    static final String ratingScreen_mood = "mood";
    static final String ratingScreen_urge = "urge";
    static final String ratingScreen_time = "time";

    /*static final String buttonActivations_Table = "buttonActivations";
    static final String buttonActivations_buttonId = "duration";
    static final String buttonActivations_buttonName = "buttonName";
    static final String buttonActivations_therapistId = "therapistId";
    static final String buttonActivations_patientId = "patientId";
    static final String buttonActivations_time = "time";*/

    static final String CREATE_Table_therapist = "CREATE TABLE therapist( therapistId integer primary key, therapistFirstName text, therapistLastName text);";
    static final String CREATE_Table_patient = "CREATE TABLE patient(patientId integer primary key, therapistId integer, patientLogin text, patientPassword text, patientFirstName text, patientLastName text, FOREIGN KEY (therapistId) REFERENCES therapist(therapistId));";
    static final String CREATE_Table_activities = "CREATE TABLE activities( activityId integer primary Key, therapistId integer, patientId integer, time numeric, activity text, duration numeric, FOREIGN KEY (therapistId) REFERENCES therapist(therapistId), FOREIGN KEY (patientId) REFERENCES patient(patientId));";
    static final String CREATE_Table_RatingScreen = "CREATE Table RatingScreen(ratingId integer primary key, patientId integer, activityId integer, mood text, urge integer, time numeric, FOREIGN KEY (activityId) REFERENCES activities(activityId));";

    //static final String CREATE_Table_buttonActivations = "CREATE TABLE buttonActivations(buttonId integer primary key, buttonName text, therapistId integer, patientId integer, time numeric, FOREIGN KEY (therapistId) REFERENCES therapist(therapistId), FOREIGN KEY (patientId) REFERENCES patient(patientId));";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public ICopePatDB(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_Table_therapist);
                db.execSQL(CREATE_Table_patient);
                db.execSQL(CREATE_Table_activities);
                db.execSQL(CREATE_Table_RatingScreen);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE IF EXISTS RatingScreen");
            //db.execSQL("DROP TABLE IF EXISTS buttonActivations");
            db.execSQL("DROP TABLE IF EXISTS activities");
            db.execSQL("DROP TABLE IF EXISTS patient");
            db.execSQL("DROP TABLE IF EXISTS therapist");
            onCreate(db);
        }
    }

    public ICopePatDB open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertNewTherapist(int tID, String tFName, String tLName)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(therapist_therapistId, tID);
        initialValues.put(therapist_therapistFirstName, tFName);
        initialValues.put(therapist_therapistLastName, tLName);

        return db.insert(therapist_Table,  null,  initialValues);
    }

    public long insertNewPatient(int pID, int tID, String pLogin, String pPass, String pFName, String pLName)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(patient_patientId, pID);
        initialValues.put(patient_therapistId, tID);
        initialValues.put(patient_patientLogin, pLogin);
        initialValues.put(patient_patientPassword, pPass);
        initialValues.put(patient_patientFirstName, pFName);
        initialValues.put(patient_patientLastName, pLName);

        return db.insert(patient_Table,  null,  initialValues);
    }

    public long insertNewActivity(int aID, int tID, int pID, String pEmail, String activity, int duration)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(activities_activityId, aID);
        initialValues.put(activities_therapistId, tID);
        initialValues.put(activities_patientId, pID);
        SimpleDateFormat sdf = new SimpleDateFormat("dMMyyyyHm");
        String str = sdf.format(new Date());
        initialValues.put(activities_time, Integer.parseInt(str));
        initialValues.put(activities_activity, activity);
        initialValues.put(activities_duration, duration);

        return db.insert(activities_Table,  null,  initialValues);
    }

    public long insertNewRatingScreen(int pID, int aID, String mood, int urge)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ratingScreen_patientID, pID);
        initialValues.put(ratingScreen_activityId, aID);
        initialValues.put(ratingScreen_mood, mood);
        initialValues.put(ratingScreen_urge, urge);
        SimpleDateFormat sdf = new SimpleDateFormat("dMMyyyyHm");
        String str = sdf.format(new Date());
        initialValues.put(ratingScreen_time, Integer.parseInt(str));

        return db.insert(ratingScreen_Table,  null,  initialValues);
    }

    public Cursor getAllActivities()
    {
        return db.query(activities_Table, new String [] {activities_activityId, activities_therapistId, activities_patientId, activities_time, activities_activity, activities_duration}, null, null, null, null, null);
    }

    public Cursor getAllRatings()
    {
        return db.query(ratingScreen_Table, new String [] {ratingScreen_patientID, ratingScreen_activityId, ratingScreen_mood, ratingScreen_urge, ratingScreen_time}, null, null, null, null, null);
    }

    public Cursor getAllPatients()
    {
        return db.query(patient_Table, new String [] {patient_patientId, patient_patientFirstName, patient_patientLastName}, null, null, null, null, null);
    }

    public Cursor getAllTherapists()
    {
        return db.query(therapist_Table, new String [] {therapist_therapistId, therapist_therapistFirstName, therapist_therapistLastName}, null, null, null, null, null);
    }

    public boolean isPatientAndTherapistOnPhone()
    {
        if(getAllPatients().getCount() > 0 && getAllTherapists().getCount() > 0)
            return true;
        else
            return false;
    }

    public Cursor getPatientName()
    {
        return db.query(patient_Table, new String [] {patient_patientFirstName, patient_patientLastName}, "1", null, null, null, null);
    }

}
