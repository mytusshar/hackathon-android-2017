package com.example.mytusshar.biotechv2.application_config;


public class AppConfig {

    public static String LOCAL_HOST = "http://192.168.43.213:8090";

    public static String LOCAL_PREVIEW = "http://192.168.43.213/";

    public static String URL_GET_VIDEO_DATA = LOCAL_HOST + "/video/";

    // Server user login url
    public static String URL_LOGIN = LOCAL_HOST + "/login";

    // Server user register url
    public static String URL_REGISTER = LOCAL_HOST + "/user";  //POST JSON OBJECT

    // Server getting user data
    public static String URL_USER_DATA = LOCAL_HOST + "/user";  //GET JSON DATA

    //server forgot password link and send email with link in jason object
    public static String URL_FORGOT_PASSWORD = LOCAL_HOST + "/reset";

    // Server sending like update with video ID and will add that video to liked_list
    public static String URL_LIKE_UPDATE = LOCAL_HOST + "/like/";

    // Server sending view update with video ID
    public static String URL_IS_LIKED = LOCAL_HOST + "/isliked/";

    // Server is liked liked or not
    public static String URL_VIEW_UPDATE = LOCAL_HOST + "/view/";

    // Server sending search query
    public static String URL_SEARCH = LOCAL_HOST + "/video";

    // Server getting liked list
    public static String URL_LIKED_LIST = LOCAL_HOST + "/likes";

    //server getting watch later list
    public static String URL_WATCH_LATER = LOCAL_HOST + "/later";

    //server adding video to watch later
    public static String URL_ADD_WATCH_LATER = LOCAL_HOST + "/later/";

    //remove from watch later
    public static String URL_REMOVE_ONE_WATCH_LATER = LOCAL_HOST + "/later/remove/";

    //clear all watch later list
    public static String URL_REMOVE_ALL_WATCH_LATER = LOCAL_HOST + "/later/clear";


    public static String URL_SHARE_VIDEO = LOCAL_HOST + "/share/";
    public static String URL_SHARE_VIDEO_LIST = LOCAL_HOST + "/share";


    //////////////////////////////
    // new urls

    public static String URL_AREAS_LIST = LOCAL_HOST + "/area";
    public static String URL_AREAS_VIDEO_LIST = LOCAL_HOST + "/areavideos/";

    public static String URL_MAIN_CATEGORY_LIST = LOCAL_HOST + "/category";
    public static String URL_SUB_CATEGORY_LIST = LOCAL_HOST + "/sub/";
    public static String URL_CATEGORY_VIDEO_LIST = LOCAL_HOST + "/subvideos/";


    public static String URL_NO_OF_SUBCATEGORY= LOCAL_HOST + "/noofsub/";
    public static String URL_NO_OF_SUBCAT_VIDEO = LOCAL_HOST + "/noofvid/";
    public static String URL_NO_OF_AREA_VIDEO = LOCAL_HOST + "/noofvidar/";

    public static String URL_YEAR_FILTER = LOCAL_HOST + "/year/";

    ////////////////////////////
}
