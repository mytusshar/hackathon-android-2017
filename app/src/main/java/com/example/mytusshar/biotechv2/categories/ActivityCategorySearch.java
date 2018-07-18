package com.example.mytusshar.biotechv2.categories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.ActivityMain;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.adapters.CustomListAdapter;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.search_utils.ListViewAdapter;
import com.example.mytusshar.biotechv2.video_model.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mytusshar on 28-Mar-17.
 */
public class ActivityCategorySearch extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    ListViewAdapter adapter;
    SearchView editsearch;
    LinearLayout searchLayout;

    FloatingActionButton fab;
    private ProgressDialog pDialog;
    private List<VideoModel> videoModelList = new ArrayList<VideoModel>();
    private ListView listView;
    private CustomListAdapter video_adapter;

    String areaId; //also know as categoryId
    String areaName; //also know as categoryName
    String preview;

    Toolbar toolbar;
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchLayout = (LinearLayout)findViewById(R.id.search_layout);

        toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //video_list related
        //listview and adapter for searched video
        listView = (ListView) findViewById(R.id.searched_list);
        video_adapter = new CustomListAdapter(this, videoModelList);
        listView.setAdapter(video_adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        setListeners();

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search_view);
        editsearch.setOnQueryTextListener(this);


        Bundle bundle = getIntent().getExtras();
        areaId = bundle.getString("area_id");
        areaName = bundle.getString("area_name");
        preview = bundle.getString("preview");
        //getting all videos from this category

        if(preview.contains("-8a3a-"))
            getNonSearchDataCategory();
        if(preview.contains("-8a2e-"))
            getNonSearchDataAreas();

    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStart() {
        super.onStart();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onQueryTextSubmit(String query) {

        if(!haveNetworkConnection()){
            Snackbar snackbar = Snackbar.make(searchLayout, "No Internet Connection Available....", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            //getting search result
            getSearchData(query);
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
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


///////////////////////////////////////////////////////////////////////////////////////////////////

    void getSearchData(final String query){

        showDialog();

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        ///////////////////////////
        String ch = "c";
        if(preview.contains("-8a3a-"))
            ch = "c";
        if(preview.contains("-8a2e-"))
            ch = "a";
        ///////////////////////////
        Toast.makeText(this, "char : " + ch, Toast.LENGTH_SHORT).show();
        String jsonString =
                        " {\n" +
                        "  \"searchKey\"" + ":" + "\"" + query + "\"" + ",\n" +
                        "  \"fieldId\"" + ":" + "\"" + areaId + "\"" + ",\n" +
                        "  \"type\"" + ":" + "\"" + ch +"\"" + "\n" +
                        " }\n";

        Volley.newRequestQueue(this).add(
                new JsonRequest<JSONArray>(Request.Method.POST, AppConfig.URL_SEARCH, jsonString,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                Log.d("JSON DATA RESPONSE", response.toString());
                                hidePDialog();


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

                                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("SearchError: ", error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        try {
                            params.put("key", query);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return params;
                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = userDetailsModel.getEMAIL() + ":" + userDetailsModel.getPASSWORD();
                        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", auth);
                        return headers;
                    }

                });
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


    void getNonSearchDataAreas(){

        showDialog();
        //Toast.makeText(this, "non search data ", Toast.LENGTH_LONG).show();

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        // Creating volley request obj
        JsonArrayRequest jsonRequest = new JsonArrayRequest(AppConfig.URL_AREAS_VIDEO_LIST + areaId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("CATEGORY RESPONSE", response.toString());
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


                        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
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


    void getNonSearchDataCategory(){

        showDialog();
        //Toast.makeText(this, "non search data ", Toast.LENGTH_LONG).show();

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        // Creating volley request obj
        JsonArrayRequest jsonRequest = new JsonArrayRequest(AppConfig.URL_CATEGORY_VIDEO_LIST + areaId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("CATEGORY RESPONSE", response.toString());
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


                        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
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
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void showDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onStop() {
        super.onStop();

    }


////////////////////////////////////////////////////////////////////////////////////////////////////
}