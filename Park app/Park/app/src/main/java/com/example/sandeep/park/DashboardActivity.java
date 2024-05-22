package com.example.sandeep.park;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Sandeep on 14-01-2018.
 */

public class DashboardActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "DashboardActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    Button LogOut,searchButton, setpass, getpass;
    Context context;
    TextView EmailShow, passText;
    String EmailHolder;
    ListView listView;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult ;
    String HttpURL = "http://192.168.0.2/login/UserBook.php";
    String HttpURL_set = "http://192.168.0.2/login/setPass.php";
    String pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        listView = (ListView) findViewById(R.id.listView);
        getpass = (Button) findViewById(R.id.pass);
        setpass = (Button) findViewById(R.id.setpass);
        searchButton = (Button) findViewById(R.id.searchSlots);
       // getJSON("http://192.168.1.101/techpark/getdata.php");


        LogOut = (Button)findViewById(R.id.button);
        EmailShow = (TextView)findViewById(R.id.EmailShow);
        passText = (TextView)findViewById(R.id.passwordText);

        listView.setOnItemClickListener(this);


        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(UserLoginActivity.UserEmail);
        EmailShow.setText(EmailHolder);


        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                Intent intent = new Intent(DashboardActivity.this, UserLoginActivity.class);

                startActivity(intent);

                Toast.makeText(DashboardActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();


            }
        });

        setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = passText.getText().toString();
                setFunction(input,pos);
            }
        });

        getpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON("http://192.168.0.2/techpark/getpass.php?id="+pos);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON("http://192.168.0.2/techpark/getdata.php");
            }
        });
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    if(urlWebService=="http://192.168.0.2/techpark/getdata.php") {
                    loadIntoListView(s); }
                    else {
                        passText.setText(s);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            heroes[i] = obj.getString("id");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView temp = (TextView) view;
        position = position + 1;
        pos = String.valueOf(position);
        String data = (String) temp.getText();
        //String userName = BackgroundWorker.user_name;
        BookFunction(data,pos);
        //Toast.makeText(this,"Booking "+data+"... by "+EmailHolder,Toast.LENGTH_LONG).show();

        if(isServicesOK()){
            init();
        }

    }

    public void setFunction(final String email, final String password){

        class setClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(DashboardActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Records were updated successfully.")){

                    Toast.makeText(DashboardActivity.this,"Update: "+httpResponseMsg,Toast.LENGTH_LONG).show();


                }
                else{

                    Toast.makeText(DashboardActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL_set);

                return finalResult;
            }
        }

        setClass userLoginClass = new setClass();

        userLoginClass.execute(email,password);
    }

    public void BookFunction(final String email, final String password){

        class BookClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(DashboardActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Records were updated successfully.")){

                   Toast.makeText(DashboardActivity.this,"Booking ... by "+EmailHolder,Toast.LENGTH_LONG).show();

                    //finish();



                }
                else{

                    Toast.makeText(DashboardActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        BookClass bookClass = new BookClass();

        bookClass.execute(email,password);
    }

    private void init(){
        Intent intent = new Intent(DashboardActivity.this, MapActivity.class);
        startActivity(intent);

    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DashboardActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DashboardActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}