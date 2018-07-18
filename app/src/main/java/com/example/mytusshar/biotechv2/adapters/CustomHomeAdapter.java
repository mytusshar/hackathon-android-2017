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
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytusshar.biotechv2.video_players.ActivityYoutubePlayer;
import com.example.mytusshar.biotechv2.R;
import com.example.mytusshar.biotechv2.video_model.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomHomeAdapter extends RecyclerView.Adapter<CustomHomeAdapter.ViewHolder> {

	private Activity activity;
	private List<VideoModel> videoModelItems;
////////////////////////////////////////////////////////////////////////////////////////////////////
	public CustomHomeAdapter(Activity activity, List<VideoModel> videoModelItems) {
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
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.home_list_items, parent, false);
		return new ViewHolder(v);
	}


////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onBindViewHolder(final CustomHomeAdapter.ViewHolder holder, int position) {
		// getting movie data for the row
		final VideoModel list = videoModelItems.get(position);

		// thumbnail image
		Picasso.with(activity).load("http://img.youtube.com/vi/"+getVideo_ID(list.getVideoURL())+"/0.jpg").into(holder.thumbNail);
		// title
		holder.title.setText(list.getVideoTitle());
		holder.likes.setText(list.getVideoLikes() + " Likes");
		holder.views.setText(list.getVideoViews() + " Views");

		//popupButton listener
		holder.popupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//creating a popup menu
				PopupMenu popup = new PopupMenu(activity, holder.popupButton);
				//inflating menu from xml resource
				popup.inflate(R.menu.options_menu);
				//adding click listener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
							case R.id.watch_later:
								//handle menu1 click
								Toast.makeText(activity, "watch later clicked", Toast.LENGTH_SHORT).show();


								////////////////////////////////
								// Add To Watch Later Field
								addToWatchLater("" + list.getVideoID());
								////////////
								/////////////////////////////////
								break;
						}
						return false;
					}
				});
				//displaying the popup
				popup.show();
			}
		});


		//thumbnail image listener
		holder.thumbNail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Toast.makeText(activity, list.getTitle(), Toast.LENGTH_SHORT).show();

				//starting video player
				Intent intent=new Intent(activity,ActivityYoutubePlayer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				Bundle bundle = new Bundle();
				bundle.putString("video_id", ""+list.getVideoID());
				bundle.putString("title",list.getVideoTitle() );
				bundle.putString("video_url", list.getVideoURL());
				bundle.putString("desc", list.getVideoDesc());
				bundle.putString("likes", ""+list.getVideoLikes());
				bundle.putString("views", ""+list.getVideoViews());
				intent.putExtras(bundle);

				activity.startActivity(intent);

			}
		});

	}

////////////////////////////////////////////////////////////////////////////////////////////////////

	String getVideo_ID(String url){
		return url.substring(url.lastIndexOf("=") + 1);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////
	public class ViewHolder extends RecyclerView.ViewHolder {

	//initiating videoList views
	ImageView thumbNail;
	TextView title;
	TextView popupButton;
	TextView likes;
	TextView views;

		public ViewHolder(View convertView) {
			super(convertView);

			likes = (TextView) convertView.findViewById(R.id.home_likes);
			views = (TextView) convertView.findViewById(R.id.home_views);
			thumbNail = (ImageView) convertView.findViewById(R.id.home_thumbnail);
			title = (TextView) convertView.findViewById(R.id.home_title);
			popupButton = (TextView)convertView.findViewById(R.id.popup_menu);


		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////

	private void addToWatchLater(String video_id){



	}
////////////////////////////////////////////////////////////////////////////////////////////////////
}