package com.cvrahimis.costasv.icope;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.ReadingCode.Quote;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Costasv on 3/21/15.
 */
public class SendActivities extends AsyncTask<Context, Void, Void> {

    private Exception exception;
    private ICopePatDB db;

    protected Void doInBackground(Context... params) {
        //Looper.prepare();
        // Create a new HttpClient and Post Header
        String line = "";
        db = new ICopePatDB(params[0]);
        db.open();
        //HttpPost httppost = new HttpPost("http://10.0.2.2:8888/ICopeDBInserts/AddActivity.php");
        HttpPost httppost = new HttpPost("http://isoothe.cs.iona.edu/AddActivity.php");
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 5000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);


        try {

            StringBuilder xml = new StringBuilder();
            Cursor cur = db.getAllActivities();
            if (cur.getColumnCount() > 0 && cur.moveToFirst())
            {
                xml.append("<?xml version='1.0' encoding='UTF-8'?><activities>");
                do
                {
                    xml.append("<activity>");
                    xml.append("<therapistId>");
                    xml.append((String) cur.getString(0));
                    xml.append("</therapistId>");
                    xml.append("<patientId>");
                    xml.append((String) cur.getString(1));
                    xml.append("</patientId>");
                    xml.append("<time>");
                    xml.append((String) cur.getString(2));
                    xml.append("</time>");
                    xml.append("<act>");
                    xml.append((String) cur.getString(3));
                    xml.append("</act>");
                    xml.append("<duration>");
                    xml.append((String) cur.getString(4));
                    xml.append("</duration>");
                    xml.append("<mood>");
                    xml.append((String) cur.getString(5));
                    xml.append("</mood>");
                    xml.append("<urge>");
                    xml.append((String) cur.getString(6));
                    xml.append("</urge>");
                    xml.append("</activity>");
                }
                while (cur.moveToNext());
                xml.append("</activities>");
            }


            // Add your data
            List nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("activityXML", xml.toString()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);


            if (response != null) {
                InputStream inputstream = response.getEntity().getContent();
                line = convertStreamToString(inputstream);
                if(line.equals("yes"))
                {
                    db.clearActivitiesTable();
                }
            }

        } catch (ClientProtocolException e) {
            Log.i("LoginActivity", "MyClass.getView() exception" + e.toString());
            //Looper.loop();
        } catch (IOException e) {
            Log.i("LoginActivity", "MyClass.getView() exception2" + e.toString());
            //Looper.loop();
        }

        return null;
    }

    private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            //Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
        }
        return total.toString();
    }


}