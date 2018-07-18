package com.example.mytusshar.biotechv2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mytusshar.biotechv2.video_players.ActivityYoutubePlayer;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.video_model.VideoModel;
import com.squareup.picasso.Picasso;
import java.util.List;


public class CustomListAdapter2 extends RecyclerView.Adapter<CustomListAdapter2.ViewHolder> {

	private Activity activity;
	private List<VideoModel> videoModelItems;
////////////////////////////////////////////////////////////////////////////////////////////////////
	public CustomListAdapter2(Activity activity, List<VideoModel> videoModelItems) {
		this.videoModelItems = videoModelItems;
		this.activity = activity;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int getItemCount() {
		return videoModelItems.size();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
		return new ViewHolder(v);
	}


////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onBindViewHolder(final CustomListAdapter2.ViewHolder holder, int position) {

		// getting movie data for the row
		final VideoModel list = videoModelItems.get(position);

		// thumbnail image
		Picasso.with(activity).load("http://img.youtube.com/vi/"+getVideo_ID(list.getVideoURL())+"/0.jpg").into(holder.thumbNail);

		// title
		holder.title.setText(list.getVideoTitle());
		//views
		holder.views.setText(list.getVideoViews() + " Views");
		//likes
		holder.likes.setText(list.getVideoLikes() + " Likes");

		//popupmenu
		holder.popup_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//creating a popup menu
				PopupMenu popup = new PopupMenu(activity, holder.popup_menu);
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
		holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent=new Intent(activity,ActivityYoutubePlayer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Bundle bundle = new Bundle();
				bundle.putString("video_id", ""+list.getVideoID());
				bundle.putString("title", list.getVideoTitle());
				bundle.putString("video_url", list.getVideoURL());
				bundle.putString("desc", list.getVideoDesc());
				bundle.putString("likes", "" + list.getVideoLikes());
				bundle.putString("views", "" + list.getVideoViews());
				intent.putExtras(bundle);

				activity.startActivity(intent);

			}
		});


	}

////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////

	String getVideo_ID(String url){
		return url.substring(url.lastIndexOf("=") + 1);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
	public class ViewHolder extends RecyclerView.ViewHolder {

		//initiating videoList views
		RelativeLayout relativeLayout;
		ImageView thumbNail;
		TextView title;
		TextView views;
		TextView likes;
		TextView popup_menu;

		public ViewHolder(View convertView) {
			super(convertView);

			relativeLayout = (RelativeLayout)convertView.findViewById(R.id.custom_list);
			thumbNail = (ImageView) convertView.findViewById(R.id.list_thumbnail);
			title = (TextView) convertView.findViewById(R.id.list_title);
			views = (TextView) convertView.findViewById(R.id.list_views);
			likes = (TextView) convertView.findViewById(R.id.list_likes);
			popup_menu = (TextView) convertView.findViewById(R.id.list_popup_menu);

		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
}