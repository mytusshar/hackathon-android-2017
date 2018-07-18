package com.example.mytusshar.biotechv2.video_players;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mytusshar on 27-Mar-17.
 */
public class ActivityVideoPlayer extends Activity {

    VideoView youTubeView;
    TextView playerTitle;
    TextView playerPopupMenu;
    ImageView playerLikeButton;
    TextView playerLikesText;
    TextView playerViewsText;
    TextView playerDescText;
    LinearLayout youtubeplayerLayout;

    String URL = null;
    String video_id = null;
    boolean is_video_liked = false;
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        youTubeView = (VideoView) findViewById(R.id.youtube_view);
        //initPlayerViews();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    void initPlayerViews(){
        playerTitle = (TextView)findViewById(R.id.player_title);
        playerPopupMenu = (TextView)findViewById(R.id.player_pop_menu);
        playerLikeButton = (ImageView) findViewById(R.id.player_likeButton);
        playerLikesText = (TextView)findViewById(R.id.player_likes_text);
        playerViewsText = (TextView)findViewById(R.id.no_of_views_text);
        playerDescText = (TextView)findViewById(R.id.player_desc_text);

        Bundle bundle = getIntent().getExtras();

        //getting video URL
        video_id = bundle.getString("video_id");
        URL = bundle.getString("video_url");
        playerTitle.setText(bundle.getString("title") + "\n");
        playerDescText.setText(bundle.getString("desc"));
        playerLikesText.setText(bundle.getString("likes"));
        playerViewsText.setText(bundle.getString("views"));
        playerDescText.setMovementMethod(new ScrollingMovementMethod());


        //getIsVideoLiked(video_id);
        if(!haveNetworkConnection()){
            Snackbar snackbar = Snackbar.make(youtubeplayerLayout, "No Internet Connection Available....", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else{
            increaseViews(video_id);
        }


        playerLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!haveNetworkConnection()){
                    Snackbar snackbar = Snackbar.make(youtubeplayerLayout, "No Internet Connection Available....", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    increaseLikes(video_id);
                }

            }
        });


    }

    void increaseViews(final String video_id){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_VIEW_UPDATE + video_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("IncreaseViews Data:", response.toString());

                        //////////////////////////////////
                        //updating likes and views
                        getUpdatedLikesAndViews(video_id);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Views error Message ", error.getMessage());
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

    void increaseLikes(final String video_id){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_LIKE_UPDATE + video_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("IncreaseLikes Data", response.toString());

                        //////////////////////////////////
                        //updating likes and views
                        getUpdatedLikesAndViews(video_id);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog showing successful registration

                //Toast.makeText(getApplicationContext(), "Invalid signing in", Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(this).add(jsonObjReq2);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getUpdatedLikesAndViews(final String video_id){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_GET_VIDEO_DATA + video_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("UPDATEDLIKESViews data:", response.toString());

                        //getting likes and videos
                        try{

                            playerLikesText.setText("" + response.getLong("likes"));
                            playerViewsText.setText("" + response.getLong("views"));
                            getIsVideoLiked(video_id);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("UPDATED LIKESViews ERROR ", error.getMessage());
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

    private void getIsVideoLiked(final String video_id){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);



        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_IS_LIKED + video_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("IS videoLiked Data", response.toString());

                        //getting likes and videos
                        try{

                            is_video_liked = response.getBoolean("success");
                            if(is_video_liked){
                                playerLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.like_true));
                            }
                            else{
                                playerLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.like_false));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("IS videoLIKED ERROR: ", error.getMessage());
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
}
