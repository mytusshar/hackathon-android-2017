package com.example.mytusshar.biotechv2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.categories.ActivityCategorySearch;
import com.example.mytusshar.biotechv2.categories.CategoryModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mytusshar on 28-Mar-17.
 */
public class CustomGridAdapter extends RecyclerView.Adapter<CustomGridAdapter.ViewHolder> {

    private Activity activity;
    private List<CategoryModel> categoryModelItems;
////////////////////////////////////////////////////////////////////////////////////////////////////
    public CustomGridAdapter(Activity activity, List<CategoryModel> categoryModelItems) {

        this.categoryModelItems = categoryModelItems;
        this.activity = activity;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;
        TextView total;
        LinearLayout grid_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            total = (TextView) itemView.findViewById(R.id.total);
            grid_layout = (LinearLayout) itemView.findViewById(R.id.grid_linear_layout);
            tv = (TextView) itemView.findViewById(R.id.textView1);
            img = (ImageView) itemView.findViewById(R.id.imageView1);
        }

    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public CustomGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid_items, parent,false);
        return new ViewHolder(rowView);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       final CategoryModel model = categoryModelItems.get(position);

        holder.tv.setText(model.getCategoryName());

        String path = model.getCategoryImage();
        Picasso.with(activity).load(path).into(holder.img);
        Picasso.with(activity).setLoggingEnabled(true);
        Log.d(">>>>>>>>>A-URL_PREV: ", path);

        //setting no of categories

        if(model.getCategoryImage().contains("sub"))
            getNoOfCatVideo(model.getCategoryId(), holder.total);
        if(model.getCategoryImage().contains("area"))
            getNoOfAreaVideo(model.getCategoryId(), holder.total);


        holder.grid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting video player
                Intent intent = new Intent(activity,ActivityCategorySearch.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("area_name", "" + model.getCategoryName());
                bundle.putString("area_id", model.getCategoryId());
                bundle.putString("preview", model.getCategoryImage());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });


    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public int getItemCount() {
        return categoryModelItems.size();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getNoOfCatVideo(String cat_id, final TextView total){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(activity);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_NO_OF_SUBCAT_VIDEO+ cat_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Cat Video DATA: ", response.toString());

                        try{

                            total.setText("" + response.getLong("value") + " Videos" );

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Cat Video error: ", error.getMessage());
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
        Volley.newRequestQueue(activity).add(jsonObjReq2);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getNoOfAreaVideo(String cat_id, final TextView total){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(activity);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_NO_OF_AREA_VIDEO + cat_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Area Video Data: ", response.toString());

                        try{

                            total.setText("" + response.getLong("value") + " Videos" );

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Area Video DATA error: ", error.getMessage());
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
        Volley.newRequestQueue(activity).add(jsonObjReq2);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
}