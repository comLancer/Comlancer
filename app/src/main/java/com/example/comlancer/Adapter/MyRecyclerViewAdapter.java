package com.example.comlancer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comlancer.Models.ComlancerImages;
import com.example.comlancer.R;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends
        RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<ComlancerImages> mItemsArrayList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    //the constructor of the LastRecordsRecyclerViewAdapter
    public MyRecyclerViewAdapter(Context context) {
        this.mContext = context;

        setOnItemClickListener(context);
    }


    //onCreateViewHolder allows you to inflate the "List items view"
//also allows to make some action during once the list created
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recycler_view, parent, false);
        return new MyViewHolder(itemView);
    }

    //onBindViewHolder , allows you to write the data into the fields
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ComlancerImages u = mItemsArrayList.get(position);
        holder.tvTitle.setText(u.getTitle());
        Glide.with(mContext).load(u.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.ivImg);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemPressed(u);

            }
        });

    }


    public void updateList(ArrayList<ComlancerImages> items) {
        mItemsArrayList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemsArrayList.size();
    }

    private void onItemPressed(ComlancerImages u) {
        if (mListener != null) {
            mListener.onItemClicked(u);
        }
    }

    private void setOnItemClickListener(Context context) {
        mListener = (OnItemClickListener) context;
    }

    public interface OnItemClickListener {

        void onItemClicked(ComlancerImages u);
    }

    //here we define all views we will use
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final ImageView ivImg;


        public MyViewHolder(View view) {
            super(view);

            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImg = itemView.findViewById(R.id.iv_icon);

        }
    }


}