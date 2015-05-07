package com.cvrahimis.costasv.icope.LoginCode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.Login;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.MyApplication;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;

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
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends ActionBarActivity {

    TextView username;
    TextView password;
    Button login;
    Button cancel;
    RelativeLayout mainLayout;
    //ScrollView mainLayout;
    TextView connectLbl;
    TextView invalidLogin;
    ICopePatDB db;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("iSoothe");

        mainLayout = (RelativeLayout) findViewById(R.id.loginMainLayout);
        //mainLayout = (ScrollView) findViewById(R.id.scrollView);
        login = (Button) findViewById(R.id.loginDoneBtn);
        cancel = (Button) findViewById(R.id.cancelBtn);
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        connectLbl = (TextView) findViewById(R.id.connectLbl);
        invalidLogin = (TextView) findViewById(R.id.invalidLogin);
        spinner = (ProgressBar) findViewById(R.id.spinner);

        username.setHint("Username");
        password.setHint("Password");
        username.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        if(!isNetworkAvailable())
            connectLbl.setVisibility(View.VISIBLE);

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

        db = new ICopePatDB(this);
        db.open();

    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        finish();
        db.close();
        startActivity(intent);
    }*/

    public void onDestory(){
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void buttonPress(View view)
    {
        switch (view.getId())
        {
            case R.id.loginDoneBtn:
            {
                if(!username.getText().equals("") && !password.getText().equals(""))
                {
                    //Toast.makeText(getApplicationContext(), "LoginPress", Toast.LENGTH_SHORT).show();
                    if(isNetworkAvailable()) {

                        try {
                            String uname = String.valueOf(username.getText());
                            String pass = String.valueOf(password.getText());
                            String result = new Login().execute(uname, pass).get();
                            String[] patThrpData = result.split(",");
                            if(patThrpData.length == 8 && patThrpData[2].equals(uname) && patThrpData[3].equals(pass)) {
                                ((MyApplication) this.getApplication()).setpID((long) Long.parseLong(patThrpData[0]));
                                ((MyApplication) this.getApplication()).settID((long) Long.parseLong(patThrpData[1]));
                                invalidLogin.setVisibility(View.INVISIBLE);
                                if ((db.insertNewTherapist(Integer.parseInt(patThrpData[1]), patThrpData[6], patThrpData[7]) > 0 && db.insertNewPatient(Integer.parseInt(patThrpData[0]), Integer.parseInt(patThrpData[1]), patThrpData[2], patThrpData[3], patThrpData[4], patThrpData[5]) > 0)) {
                                    Intent intent = new Intent(this, RatingScreenActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                            else{
                                invalidLogin.setVisibility(View.VISIBLE);
                            }
                            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        }
                        catch(ExecutionException e){
                            Log.i("LoginActivity", "MyClass.getView() exception3" + e.toString());
                        }
                        catch(InterruptedException e){
                            Log.i("LoginActivity", "MyClass.getView() exception4" + e.toString());
                        }
                    }
                    else
                        connectLbl.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.cancelBtn:
            {
                Intent intent = new Intent(this, MenuActivity.class);
                finish();
                startActivity(intent);
                break;
            }
        }
    }

    /*private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
        }
        return total.toString();
    }*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*public class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        private ProgressDialog Dialog;
        //private Context context;
        public RetrieveFeedTask(LoginActivity activity){
            Dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Loading");
            Dialog.show();
        }

        protected String doInBackground(String... urls) {
            Looper.prepare();
            //String uname = urls[0];
            // Create a new HttpClient and Post Header
            String line = "";

            //HttpPost httppost = new HttpPost("http://10.0.2.2:8888/ICopeDBInserts/Login.php");
            //HttpPost httppost = new HttpPost("http://isoothe.cs.iona.edu/login.php");
            HttpPost httppost = new HttpPost("http://192.168.1.10:8888/iSoothe/iSootheMobile/login.php");
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
            Dialog.dismiss();
        }
    }*/

}
