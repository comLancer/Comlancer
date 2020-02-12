package com.example.comlancer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;

import java.util.ArrayList;


public class UserAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<User> mItemsArrayList;


    public UserAdapter(Context context) {
        mContext = context;
        mItemsArrayList = new ArrayList<>();
    }

    public void updateFreelancerCompaniesArrayList(ArrayList<User> newItems) {
        mItemsArrayList = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItemsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {

        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_company_freelancer, viewGroup, false);


        }

        TextView tvName = itemView.findViewById(R.id.tv_name);
        ImageView ivImg = itemView.findViewById(R.id.iv_img_profile);
        RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);

        User u = (User) getItem(position);
        tvName.setText(u.getName());
        ratingBar.setRating(u.getAverageRating());

        Glide.with(mContext).load(u.getImageUrl()).placeholder(R.drawable.ic_profile).into(ivImg);


        return itemView;
    }
}
