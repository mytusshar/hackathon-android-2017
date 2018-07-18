package com.example.mytusshar.biotechv2.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.video_players.ActivityYoutubePlayer;
import com.example.mytusshar.biotechv2.video_model.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<VideoModel> videoModelItems;


	public CustomListAdapter(Activity activity, List<VideoModel> videoModelItems) {
		this.activity = activity;
		this.videoModelItems = videoModelItems;
	}

	@Override
	public int getCount() {
		return videoModelItems.size();
	}

	@Override
	public Object getItem(int location) {
		return videoModelItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.custom_list);
		ImageView thumbNail = (ImageView) convertView.findViewById(R.id.list_thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.list_title);
		TextView views = (TextView) convertView.findViewById(R.id.list_views);
		TextView likes = (TextView) convertView.findViewById(R.id.list_likes);

		final TextView popup_menu = (TextView) convertView.findViewById(R.id.list_popup_menu);

		// getting movie data for the row
		final VideoModel m = videoModelItems.get(position);

		// thumbnail image
		Picasso.with(activity).load("http://img.youtube.com/vi/"+getVideo_ID(m.getVideoURL())+"/0.jpg").into(thumbNail);
		// title
		title.setText(m.getVideoTitle());
		// views
		views.setText(m.getVideoViews() + " Views");
		//likes
		likes.setText(m.getVideoLikes() + " Likes");

		//popmenu
		popup_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//creating a popup menu
				PopupMenu popup = new PopupMenu(activity, popup_menu);
				//inflating menu from xml resource
				popup.inflate(R.menu.options_menu);
				//adding click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
							case R.id.watch_later:
								Toast.makeText(activity, "watch later clicked", Toast.LENGTH_SHORT).show();
								//handle menu1 click
								break;

						}
						return false;
					}
				});
			}
		});

		//relativeLayout listener to play video
		relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(activity,ActivityYoutubePlayer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putString("video_id", ""+m.getVideoID());
				bundle.putString("title", m.getVideoTitle());
				bundle.putString("video_url", m.getVideoURL());
				bundle.putString("desc", m.getVideoDesc());
				bundle.putString("likes", "" + m.getVideoLikes());
				bundle.putString("views", "" + m.getVideoViews());
				intent.putExtras(bundle);

				activity.startActivity(intent);

			}
		});



		return convertView;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	String getVideo_ID(String url){
		return url.substring(url.lastIndexOf("=") + 1);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////


}