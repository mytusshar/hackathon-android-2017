package com.example.mytusshar.biotechv2.profile_like_playlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.ActivityMain;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.adapters.CustomListAdapter2;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.video_model.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mytusshar on 17-Mar-17.
 */
public class ActivityLiked extends Activity {

    private RecyclerView listView;
    private RecyclerView.Adapter adapter;
    private LinearLayout likedLayout;
    FloatingActionButton fab;

    private Toolbar toolbar;
    private ProgressDialog pDialog;
    private List<VideoModel> videoModelList = new ArrayList<VideoModel>();

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Liked Videos");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        likedLayout = (LinearLayout)findViewById(R.id.liked_layout);
        listView = (RecyclerView)findViewById(R.id.liked_list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomListAdapter2(this, videoModelList);
        listView.setAdapter(adapter);

        setListeners();
    }


////////////////////////////////////////////////////////////////////////////////////////////////////

    void setListeners(){


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
    @Override
    public void onStart() {
        super.onStart();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // Showing progress dialog before making http request

        if(!haveNetworkConnection()){
            Snackbar snackbar = Snackbar.make(likedLayout, "No Internet Connection Available....", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            getJSONData();
        }

    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    void getJSONData(){

        showDialog();

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        // Creating volley request obj
        JsonArrayRequest jsonRequest = new JsonArrayRequest(AppConfig.URL_LIKED_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON DATA RESPONSE", response.toString());
                        hidePDialog();

                        //clearing previous data
                        videoModelList.clear();
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                VideoModel videoModel = new VideoModel();
                                videoModel.setVideoID(obj.getInt("videoId"));
                                videoModel.setVideoDesc(obj.getString("description"));
                                videoModel.setThumbnailUrl(obj.getString("preview"));
                                videoModel.setVideoTitle(obj.getString("title"));
                                videoModel.setVideoURL(obj.getString("url"));
                                videoModel.setVideoDate(obj.getString("date"));
                                videoModel.setVideoLength(obj.getString("length"));
                                videoModel.setVideoViews(obj.getInt("views"));
                                videoModel.setVideoLikes(obj.getInt("likes"));

                                // adding videoModel to movies array
                                videoModelList.add(videoModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                hidePDialog();

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
        Volley.newRequestQueue(this).add(jsonRequest);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onStop() {
        super.onStop();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume() {
        super.onResume();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////



}
