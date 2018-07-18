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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.application_config.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityRegister extends Activity {

    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputRetypePassword;
    private LinearLayout registerLayout;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    int count;

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerLayout = (LinearLayout)findViewById(R.id.register_layout);
        inputName = (EditText) findViewById(R.id.firstname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputRetypePassword = (EditText) findViewById(R.id.confirm_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Registering ...");

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputRetypePassword.getText().toString().trim();

                if(password.equals(confirmPassword)){
                    if (!name.isEmpty() &&  !email.isEmpty() && !password.isEmpty()) {

                        //adding user details to database on server
                        //Toast.makeText(getApplicationContext(), "register clicked" + (++count), Toast.LENGTH_LONG).show();
                        if(!haveNetworkConnection()){
                            Snackbar snackbar = Snackbar.make(registerLayout, "No Internet Connection Available....", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{
                            registerUser2(name, email, password);
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Confirm Password wrong..." + (++count), Toast.LENGTH_LONG).show();

                }

            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(i);
                finish();
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        count = 0;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registerUser2(final String name, String email, final String password) {

        showDialog();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userName", name);
        params.put("email", email);
        params.put("password", password);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_REGISTER, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideDialog();
                        Log.d("RegisterActivity: ", response.toString());

                        try {

                            boolean success = response.getBoolean("success");
                            if (success) {
                                // User successfully stored in MySQL server
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), message + " Check Your Email!", Toast.LENGTH_LONG).show();

                                //dialog showing successful registration
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityRegister.this).create();
                                alertDialog.setTitle("Registration Successful");
                                alertDialog.setMessage("Activation link sent to your account... ");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                // Launch login activity
                                                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                alertDialog.show();




                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = response.getString("message");
                                //dialog showing successful registration
                                //dialog showing successful registration
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityRegister.this).create();
                                alertDialog.setTitle("Registration Error");
                                alertDialog.setMessage("try registration again... ");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();


                                Toast.makeText(getApplicationContext(), errorMsg , Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                VolleyLog.e("********Registration Error: ", error.getMessage());
            }
        });

        //prevent volley from ending same request twice
        jsonObjReq2.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq2);
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
