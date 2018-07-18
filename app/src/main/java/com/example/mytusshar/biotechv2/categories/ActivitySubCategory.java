package com.example.mytusshar.biotechv2.categories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.mytusshar.biotechv2.adapters.CustomGridAdapter;
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
public class ActivitySubCategory extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView gridView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

    String categoryId;
    String categoryName;
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);



        gridView = (RecyclerView) findViewById(R.id.sub_category_recycler_view);
        recyclerViewLayoutManager = new GridLayoutManager(this, 3);
        gridView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new CustomGridAdapter(this, categoryModelList);
        gridView.setAdapter(adapter);


        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getString("category_id");
        categoryName = bundle.getString("category_name");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(categoryName);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //getting initial data from server for particular category
        getJSONData();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    void getJSONData(){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        // Creating volley request obj
        JsonArrayRequest jsonRequest = new JsonArrayRequest(AppConfig.URL_SUB_CATEGORY_LIST + categoryId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON DATA RESPONSE", response.toString());


                        //clearing previous data
                        categoryModelList.clear();
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                CategoryModel categoryModel = new CategoryModel();
                                categoryModel.setCategoryId("" + obj.getInt("subCategoryId"));
                                categoryModel.setCategoryName(obj.getString("subCategory"));
                                categoryModel.setCategoryImage(obj.getString("preview"));

                                // adding videoModel to movies array
                                categoryModelList.add(categoryModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());


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
}