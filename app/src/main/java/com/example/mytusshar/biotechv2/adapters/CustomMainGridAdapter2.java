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
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.categories.ActivityCategorySearch;
import com.example.mytusshar.biotechv2.categories.ActivitySubCategory;
import com.example.mytusshar.biotechv2.categories.CategoryModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mytusshar on 28-Mar-17.
 */
public class CustomMainGridAdapter2 extends RecyclerView.Adapter<CustomMainGridAdapter2.ViewHolder> {

    private Activity activity;
    private List<CategoryModel> categoryModelItems;
////////////////////////////////////////////////////////////////////////////////////////////////////
    public CustomMainGridAdapter2(Activity activity, List<CategoryModel> categoryModelItems) {

        this.categoryModelItems = categoryModelItems;
        this.activity = activity;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView total;
        ImageView img;
        LinearLayout grid_layout;

    public ViewHolder(View itemView) {
        super(itemView);
        grid_layout = (LinearLayout) itemView.findViewById(R.id.grid_linear_layout);
        total = (TextView) itemView.findViewById(R.id.total);
        tv = (TextView) itemView.findViewById(R.id.textView1);
        img = (ImageView) itemView.findViewById(R.id.imageView1);
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public CustomMainGridAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid_items, parent,false);
        return new ViewHolder(rowView);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       final CategoryModel model = categoryModelItems.get(position);
        holder.tv.setText(model.getCategoryName());

        //setting no of categories
        getNoOfSubCat(model.getCategoryId(), holder.total);

        String path = model.getCategoryImage();
        Picasso.with(activity).load(path).into(holder.img);
        Picasso.with(activity).setLoggingEnabled(true);
        Log.d(">>>>>>>>>C-URL_PREV: ", path);

        holder.grid_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting video player
                Intent intent = new Intent(activity, ActivitySubCategory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("category_name", "" + model.getCategoryName());
                bundle.putString("category_id", model.getCategoryId());

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

    private void getNoOfSubCat(String cat_id, final TextView total){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(activity);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_NO_OF_SUBCATEGORY + cat_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("No OF SUBCAT Data: ", response.toString());

                        try{

                            total.setText("" + response.getLong("value") + " Categories" );

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("IncreaseLikes error: ", error.getMessage());
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