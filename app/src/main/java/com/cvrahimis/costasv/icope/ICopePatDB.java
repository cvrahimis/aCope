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
    static final String activities_mood = "mood";
    static final String activities_urge = "urge";
    static final String activities_activity = "activity";
    static final String activities_duration = "duration";

    static final String buttonActivations_Table = "buttonActivations";
    static final String buttonActivations_buttonId = "buttonId";
    static final String buttonActivations_buttonName = "buttonName";
    static final String buttonActivations_therapistId = "therapistId";
    static final String buttonActivations_patientId = "patientId";
    static final String buttonActivations_time = "time";

    static final String CREATE_Table_therapist = "CREATE TABLE therapist(therapistId integer primary key, therapistLogin text, therapistPassword text, therapistFirstName text, therapistLastName text);";
    static final String CREATE_Table_patient = "CREATE TABLE patient(patientId integer primary key, therapistId integer, patientLogin text, patientPassword text, patientFirstName text, patientLastName text, FOREIGN KEY (therapistId) REFERENCES therapist(therapistId));";
    static final String CREATE_Table_activities = "CREATE TABLE activities(activityId integer primary key, therapistId integer, patientId integer, time text, mood text, urge integer, activity text, duration text, FOREIGN KEY (therapistId) REFERENCES therapist(therapistId), FOREIGN KEY (patientId) REFERENCES patient(patientId));";
    static final String CREATE_Table_buttonActivations = "CREATE TABLE buttonActivations(buttonId integer primary key, therapistId integer, patientId integer, time text, FOREIGN KEY (therapistId) REFERENCES therapist(therapistId), FOREIGN KEY (patientId) REFERENCES patient(patientId));";

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
                db.execSQL(CREATE_Table_buttonActivations);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE IF EXISTS buttonActivations");
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

    public long insertNewTherapist(long tID, String tFName, String tLName)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(therapist_therapistId, tID);
        initialValues.put(therapist_therapistFirstName, tFName);
        initialValues.put(therapist_therapistLastName, tLName);

        return db.insert(therapist_Table,  null,  initialValues);
    }

    public long insertNewPatient(long pID, long tID, String pLogin, String pPass, String pFName, String pLName)
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

    public long insertNewActivity(long tID, long pID, String activityName, String mood, int urge, String time, String duration)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(activities_therapistId, tID);
        initialValues.put(activities_patientId, pID);
        initialValues.put(activities_time, time);
        initialValues.put(activities_mood, mood);
        initialValues.put(activities_urge, urge);
        initialValues.put(activities_activity, activityName);
        initialValues.put(activities_duration, duration);

        return db.insert(activities_Table,  null,  initialValues);
    }

    public long insertNewRatingScreen(String bntName, int tID, int pID, int time)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(buttonActivations_buttonName, bntName);
        initialValues.put(buttonActivations_therapistId, tID);
        initialValues.put(buttonActivations_patientId, pID);
        initialValues.put(buttonActivations_time, time);

        return db.insert(buttonActivations_Table,  null,  initialValues);
    }

    public Cursor getAllActivities()
    {
        return db.query(activities_Table, new String [] {activities_therapistId, activities_patientId, activities_time, activities_activity, activities_duration, activities_mood, activities_urge}, null, null, null, null, null);
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

    public Cursor getPatientUsername()
    {
        return db.query(patient_Table, new String [] {patient_patientLogin}, "1", null, null, null, null);
    }

    public Cursor getPatientPassword()
    {
        return db.query(patient_Table, new String [] {patient_patientPassword}, "1", null, null, null, null);
    }

    public Cursor getPatientID()
    {
        return db.query(patient_Table, new String [] {patient_patientId}, "1", null, null, null, null);
    }

    public Cursor getTherapistID()
    {
        return db.query(therapist_Table, new String [] {therapist_therapistId}, "1", null, null, null, null);
    }

    public boolean updatePatientAndTherapist(int id){
        Cursor cur = this.getTherapistID();
        int tID = -1;
        if (cur.moveToFirst()) {
            tID = Integer.parseInt(cur.getString(0));
        }
        ContentValues cv = new ContentValues();
        cv.put("therapistId", id);

        return (db.update(patient_Table, cv, "therapistId=" + tID, null) > 0 && db.update(therapist_Table, cv, "therapistId=" + tID, null) > 0);
    }

    public void clearActivitiesTable()
    {
        db.execSQL("delete from activities");
    }
    /*public void insertActivities(){
        if(this.isPatientAndTherapistOnPhone())
        {
            if (getAllActivities().getCount() > 0)
            {

            }
        }
    }*/

}
