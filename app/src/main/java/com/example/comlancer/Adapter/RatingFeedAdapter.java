package com.example.comlancer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.comlancer.Models.RatingFeedback;
import com.example.comlancer.R;

import java.util.ArrayList;



public class RatingFeedAdapter extends BaseAdapter {


    private final Context mContext;
    private ArrayList<RatingFeedback> mItems;

    public RatingFeedAdapter(Context context, ArrayList<RatingFeedback> feedbackList) {
        mContext = context;
        mItems = feedbackList;
    }

    public void updateFeedbackArrayList(ArrayList<RatingFeedback> feedbackList) {
        mItems = feedbackList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {

        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_feedback, viewGroup, false);
        }

        TextView tvName = itemView.findViewById(R.id.tv_name);
        //TextView tvFeedback = itemView.findViewById(R.id.tv_feedback);
        RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);

        RatingFeedback f = (RatingFeedback) getItem(position);

        tvName.setText(f.getFullName());
       // tvFeedback.setText(f.getFeedback());
        ratingBar.setRating(f.getRating());


        return itemView;
    }
}
