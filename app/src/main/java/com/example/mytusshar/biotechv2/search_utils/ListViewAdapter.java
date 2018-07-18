package com.example.mytusshar.biotechv2.search_utils;

/**
 * Created by mytusshar on 18-Mar-17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mytusshar.biotechv2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<SearchHistoryModel> searchHistoryModelList = null;
    private ArrayList<SearchHistoryModel> arraylist;

    public ListViewAdapter(Context context, List<SearchHistoryModel> searchHistoryModelList) {
        mContext = context;
        this.searchHistoryModelList = searchHistoryModelList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchHistoryModel>();
        this.arraylist.addAll(searchHistoryModelList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return searchHistoryModelList.size();
    }

    @Override
    public SearchHistoryModel getItem(int position) {
        return searchHistoryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_historylist, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(searchHistoryModelList.get(position).getAnimalName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchHistoryModelList.clear();
        if (charText.length() == 0) {
            searchHistoryModelList.addAll(arraylist);
        } else {
            for (SearchHistoryModel wp : arraylist) {
                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchHistoryModelList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}