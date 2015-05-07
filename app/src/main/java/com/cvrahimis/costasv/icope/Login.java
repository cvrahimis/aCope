package com.cvrahimis.costasv.icope;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Costasv on 5/6/15.
 */
public class Login extends AsyncTask<String, Void, String> {

    private Exception exception;
    //private ProgressDialog Dialog;
    //private Context context;
    //public RetrieveFeedTask(LoginActivity activity)
    // {
        // Dialog = new ProgressDialog(activity);
//        }

    @Override
    protected void onPreExecute() {
        //Dialog.setMessage("Loading");
        //Dialog.show();
    }

    protected String doInBackground(String... urls) {
        Looper.prepare();
        //String uname = urls[0];
        // Create a new HttpClient and Post Header
        String line = "";

        //HttpPost httppost = new HttpPost("http://10.0.2.2:8888/ICopeDBInserts/Login.php");
        //HttpPost httppost = new HttpPost("http://isoothe.cs.iona.edu/login.php");
        HttpPost httppost = new HttpPost("http://192.168.1.8:8888/iSoothe/iSootheMobile/login.php");
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 5000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);


        try {
            //Thread.sleep(3000);
            // Add your data
            List nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("Username", urls[0]));
            nameValuePairs.add(new BasicNameValuePair("Password", urls[1]));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);


            if (response != null) {
                InputStream inputstream = response.getEntity().getContent();
                line = convertStreamToString(inputstream);
            }

        } catch (ClientProtocolException e) {
            Log.i("LoginActivity", "MyClass.getView() exception" + e.toString());
            //Looper.loop();
        } catch (IOException e) {
            Log.i("LoginActivity", "MyClass.getView() exception2" + e.toString());
            //Looper.loop();
        }
        //catch (InterruptedException e){}


        return line;
    }

    @Override
    protected void onPostExecute(String result)
    {
        //Dialog.dismiss();
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

