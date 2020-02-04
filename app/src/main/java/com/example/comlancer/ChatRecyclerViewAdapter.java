package com.example.comlancer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {


    private final Context mContext;
    private ArrayList<Message> mMessages;


    //create adapter constructor
    public ChatRecyclerViewAdapter(Context context, ArrayList<Message> itemsArrayList) {
        mContext = context;
        mMessages = itemsArrayList;
    }

    @NonNull
    @Override
    //retturn list item layout
    public ChatRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_message, viewGroup, false);

        return new ChatRecyclerViewAdapter.MyViewHolder(itemView);

    }

    @Override
    //connect between viewHolder and data
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.MyViewHolder holder, int position) {

        Message message = mMessages.get(position);

        if (message.getIsBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            holder.vMyMessageView.setVisibility(View.VISIBLE);
            holder.vTheirMessageView.setVisibility(View.GONE);
            holder.tvMyMessageBody.setText(message.getContent());

        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            holder.vTheirMessageView.setVisibility(View.VISIBLE);
            holder.vMyMessageView.setVisibility(View.GONE);
            holder.name.setText(message.getSender());
            holder.tvTheirMessageBody.setText(message.getContent());

        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    public void addMessage(Message massage) {
        mMessages.add(massage);
        notifyItemChanged(mMessages.size() - 1);

    }

    //declare all the views in the list item
    class MyViewHolder extends RecyclerView.ViewHolder {


        View vMyMessageView;
        View vTheirMessageView;

        TextView tvMyMessageBody;
        TextView tvTheirMessageBody;
        TextView name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            vMyMessageView = itemView.findViewById(R.id.inc_my_message);
            vTheirMessageView = itemView.findViewById(R.id.inc_their_message);

            tvMyMessageBody = itemView.findViewById(R.id.message_body);
            tvTheirMessageBody = itemView.findViewById(R.id.their_message_body);
            name = itemView.findViewById(R.id.name);
        }
    }


}
