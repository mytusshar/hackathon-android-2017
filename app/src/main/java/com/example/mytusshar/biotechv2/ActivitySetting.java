package com.example.mytusshar.biotechv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mytusshar on 18-Mar-17.
 */
public class ActivitySetting extends AppCompatActivity {

    TextView clear_history;
    TextView clear_watch_later;

    FloatingActionButton fab;
    private Toolbar toolbar;

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        clear_watch_later = (TextView) findViewById(R.id.clear_watched);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setListeners();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    void setListeners(){

        clear_watch_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "watch clear", Toast.LENGTH_SHORT).show();
                clearWatchLater();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getBaseContext(), ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });



    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void clearWatchLater(){
        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_REMOVE_ALL_WATCH_LATER, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Clear watch later Data:", response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("watch error Message ", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = userDetailsModel.getEMAIL() + ":" + userDetailsModel.getPASSWORD();
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
}
