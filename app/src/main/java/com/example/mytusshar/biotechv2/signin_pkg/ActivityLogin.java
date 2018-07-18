package com.example.mytusshar.biotechv2.signin_pkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.ActivityMain;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.application_config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ActivityLogin extends Activity {

    private Button btnLogin;
    private TextView btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private LinearLayout loginLayout;

    private SessionManager session;
    private SQLiteHandler db;

    String EMAIL = null;
    String PASSWORD = null;

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = (LinearLayout)findViewById(R.id.login_layout);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (TextView) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // verify login user to server
                    if(!haveNetworkConnection()){
                        Snackbar snackbar = Snackbar.make(loginLayout, "No Internet Connection Available....", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else{
                        checkLogin2(email, password);
                    }

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityRegister.class);
                startActivity(i);
                finish();
            }

        });

    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    void checkLogin2(final String email, final String password){

        EMAIL = email;
        PASSWORD = password;
        Log.d("*******************", "email: " + email + " p:" +password);
        showDialog();

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_LOGIN, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.d("LoginActivity Message", response.toString());

                        //setting session login_status = true
                        //starting session of user
                        session.setLogin(true);

                        //getting user data from server
                        getUserDataFromServer();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

                //dialog showing successful registration
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityLogin.this).create();
                alertDialog.setTitle("Error in Signing in...");
                alertDialog.setMessage("Type check your Credentials ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                //Toast.makeText(getApplicationContext(), "Invalid signing in", Toast.LENGTH_LONG).show();
                VolleyLog.e("LoginError Message ", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = email + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq2);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getUserDataFromServer(){

        pDialog.setMessage("getting user data...");
        showDialog();

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_USER_DATA, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        Log.d("USER DATA", response.toString());

                        //getting user data from response
                        try{
                            long userID = response.getLong("userId");
                            String userName = response.getString("userName");
                            String email = response.getString("email");
                            String password = response.getString("password");
                            //id== 1 then procced login
                            //otherwise show dialog box "activate your email account"
                            long statusID = response.getLong("statusId");

                            Log.d("******************", userName+" *********");

                            if(statusID == 1){
                                //starting ActivityMain
                                Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                startActivity(intent);
                                finish();
                                //adding data to database
                                db.addUser(""+userID, userName, email, password);
                            }else{
                                //dialog showing successful registration
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityLogin.this).create();
                                alertDialog.setTitle("Error in Signing in...");
                                alertDialog.setMessage("Activate Your Email Account First ");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "Invalid signing in", Toast.LENGTH_LONG).show();
                VolleyLog.e("USER DATA GETTING ERROR ", error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = EMAIL + ":" + PASSWORD;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq2);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}
