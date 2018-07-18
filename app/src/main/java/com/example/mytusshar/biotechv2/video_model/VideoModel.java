package com.example.mytusshar.biotechv2.video_model;

import java.util.ArrayList;

public class VideoModel {
	private String title, thumbnailUrl;
	private int year;
	private double rating;
	private ArrayList<String> genre;


	private int video_id;
	private String video_url;
	private String thumbnail_url;
	private String video_title;
	private String video_length;
	private int video_views;
	private int video_likes;
	private String video_desc;
	private String video_date;
	private ArrayList<String> comments;


	public VideoModel() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}




	////////////////////////////////////////////////////////////////////////////////////////
	//video model methods

	//video_id
	public void setVideoID(int id){
		this.video_id = id;
	}
	public int getVideoID(){
		return this.video_id;
	}

	//thumbnail_url
	public void setThumbnailURL(String url){
		this.thumbnail_url = url;
	}
	public String getThumbnailURL(){
		return this.thumbnail_url;
	}

	//video_url
	public void setVideoURL(String url){
		this.video_url = url;
	}
	public String getVideoURL(){
		return this.video_url;
	}

	//video_title
	public void setVideoTitle(String title){
		this.video_title = title;
	}
	public String getVideoTitle(){
		return this.video_title;
	}

	//video_length
	public void setVideoLength(String length){
		this.video_length =length;
	}
	public String getVideoLength(){
		return this.video_length;
	}

	//video_views
	public void setVideoViews(int views){
		this.video_views = views;
	}
	public int getVideoViews(){
		return this.video_views;
	}

	//video_likes
	public void setVideoLikes(int likes){
		this.video_likes = likes;
	}
	public int getVideoLikes(){
		return this.video_likes;
	}

	//video_desc
	public void setVideoDesc(String desc){
		this.video_desc = desc;
	}
	public String getVideoDesc(){
		return this.video_desc;
	}

	//video_date
	public void setVideoDate(String date){
		this.video_date = date;
	}
	public String getVideoDate(){
		return this.video_date;
	}




}
