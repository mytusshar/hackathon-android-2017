package com.example.mytusshar.biotechv2.video_players;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.application_config.AppConfig;
import com.example.mytusshar.biotechv2.application_config.UserDetailsModel;
import com.example.mytusshar.biotechv2.signin_pkg.ActivityLogin;
import com.example.mytusshar.biotechv2.signin_pkg.SQLiteHandler;
import com.example.mytusshar.biotechv2.signin_pkg.SessionManager;
import com.example.mytusshar.biotechv2.video_model.VideoModel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ActivityYoutubePlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String YOUTUBE_API_KEY = "AIzaSyCG25IwSEZcJuF5Te7kko9XawkHaEJ48Ws";
    public String URL = "https://www.youtube.com/watch?v=fhWaJi1Hsfo";

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;

    TextView playerTitle;
    TextView playerPopupMenu;
    ImageView playerLikeButton;
    TextView playerLikesText;
    TextView playerViewsText;
    TextView playerDescText;
    ImageView shareImage;
    LinearLayout youtubeplayerLayout;

    String video_id = null;
    boolean is_video_liked = false;

////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player);

        youtubeplayerLayout = (LinearLayout)findViewById(R.id.youtubeplayer_layout);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YOUTUBE_API_KEY, this);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();

        initPlayerViews();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    void initPlayerViews(){
        playerTitle = (TextView)findViewById(R.id.player_title);
        playerPopupMenu = (TextView)findViewById(R.id.player_pop_menu);
        playerLikeButton = (ImageView) findViewById(R.id.player_likeButton);
        playerLikesText = (TextView)findViewById(R.id.player_likes_text);
        playerViewsText = (TextView)findViewById(R.id.no_of_views_text);
        playerDescText = (TextView)findViewById(R.id.player_desc_text);
        shareImage = (ImageView) findViewById(R.id.share_image);


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

        playerPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopUp();

            }
        });

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWindow();
            }
        });


    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    void showPopUp(){
        //creating a popup menu
        android.widget.PopupMenu popup = new android.widget.PopupMenu(getApplicationContext(), playerPopupMenu);
        //inflating menu from xml resource
        popup.getMenuInflater().inflate(R.menu.options_menu, popup.getMenu());
        //popup.inflate(R.menu.profile_options_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.watch_later:{
                        //perform logout action here
                        addToWatchLater(video_id);
                        Toast.makeText(ActivityYoutubePlayer.this, "watch later", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        //player.setPlayerStateChangeListener(playerStateChangeListener);
        //player.setPlaybackEventListener(playbackEventListener);

        if (!wasRestored) {
            //video url for playing
            String video_id = getVideo_ID(URL);
            //Toast.makeText(this, video_id, Toast.LENGTH_SHORT).show();
            player.cueVideo(video_id);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

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

                VolleyLog.e("UPDATED LIKES Views ERROR ", error.getMessage());
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

    private void addToWatchLater(String video_id){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_ADD_WATCH_LATER + video_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("IncreaseLikes Data", response.toString());

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
        Volley.newRequestQueue(this).add(jsonObjReq2);

    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    void shareVideo(String video_id, String share_id){

        final UserDetailsModel userDetailsModel = new UserDetailsModel(this);

        JSONObject obj = new JSONObject();

        try{
            obj.put("email", share_id);
        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(AppConfig.URL_SHARE_VIDEO + video_id, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Shared Data", response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("SHARED ERROR: ", error.getMessage());
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

    void shareWindow(){

        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView, AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(shareImage);

        final EditText receiverEmail = (EditText) popupView.findViewById(R.id.share_id);
        Button btnDismiss = (Button)popupView.findViewById(R.id.button_cancel);
        Button btnShare = (Button)popupView.findViewById(R.id.button_share);

        receiverEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(receiverEmail, InputMethodManager.SHOW_FORCED);
            }
        });
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String share_id = receiverEmail.getText().toString().trim();
                shareVideo(video_id, share_id);
                popupWindow.dismiss();
            }
        });



    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    String getVideo_ID(String url){
        return url.substring(url.lastIndexOf("=") + 1);
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

////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for  reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }
}
