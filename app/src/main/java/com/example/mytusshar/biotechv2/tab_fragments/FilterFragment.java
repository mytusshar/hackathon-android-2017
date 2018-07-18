package com.example.mytusshar.biotechv2.tab_fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.adapters.CustomListAdapter;
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


public class FilterFragment extends Fragment {

    private RecyclerView listView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    // Movies json url
    private ProgressDialog pDialog;
    private List<VideoModel> videoModelList = new ArrayList<VideoModel>();

    String year = "2016";

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_filter,container, false);

        Button filter = (Button)rootView.findViewById(R.id.filter);
        final EditText year = (EditText)rootView.findViewById(R.id.year);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query_year = year.getText().toString().trim();
                getJSONData(query_year);
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swifeRefresh);
        listView = (RecyclerView) rootView.findViewById(R.id.trending_list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CustomListAdapter2(getActivity(), videoModelList);
        listView.setAdapter(adapter);

        return rootView;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request

        //on refress it loads data again
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJSONData("2017");
            }
        });

        pDialog.setMessage("Loading...");
        pDialog.show();
        getJSONData("2017");
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    void getJSONData(String year){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(getActivity());


        // Creating volley request obj
        JsonArrayRequest jsonRequest = new JsonArrayRequest(AppConfig.URL_YEAR_FILTER + year,
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

                            //dismissing loading spinner from view after loading data
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
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
        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

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