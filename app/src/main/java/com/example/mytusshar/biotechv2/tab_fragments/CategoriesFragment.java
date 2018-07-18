package com.example.mytusshar.biotechv2.tab_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.adapters.CustomGridAdapter;
import com.example.mytusshar.biotechv2.adapters.CustomMainGridAdapter2;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.categories.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mytusshar on 20-Mar-17.
 */
public class CategoriesFragment extends Fragment {


    private RecyclerView gridView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        gridView = (RecyclerView) rootView.findViewById(R.id.category_recycler_view);

        recyclerViewLayoutManager = new GridLayoutManager(getActivity(), 3);

        gridView.setLayoutManager(recyclerViewLayoutManager);

        adapter = new CustomMainGridAdapter2(getActivity(), categoryModelList);
        gridView.setAdapter(adapter);

        //getting initial data from server for particular category
        getJSONData();

        return rootView;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    void getJSONData(){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(getActivity());

        // Creating volley request obj
        JsonArrayRequest jsonRequest = new JsonArrayRequest(AppConfig.URL_MAIN_CATEGORY_LIST,
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
                                categoryModel.setCategoryId(""+obj.getInt("categoryId"));
                                categoryModel.setCategoryName(obj.getString("category"));
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
        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////




////////////////////////////////////////////////////////////////////////////////////////////////////
}
