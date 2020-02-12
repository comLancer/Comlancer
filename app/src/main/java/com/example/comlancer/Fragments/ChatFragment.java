package com.example.comlancer.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comlancer.Adapter.ChatRecyclerViewAdapter;
import com.example.comlancer.Models.Message;
import com.example.comlancer.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;



public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String MESSAGE_SENDER = "safa";
    private ArrayList<Message> mMessages;
    private ChatRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context mContext;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View perantView = inflater.inflate(R.layout.fragment_chat, container, false);


        //TODO : this is just to remember importing my_message drawable
        ImageView my_message = new ImageView(mContext);
        my_message.setBackgroundResource(R.drawable.my_message);

        //TODO : this is just to remember importing my_message drawable
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundResource(R.drawable.my_message);


        recyclerView = perantView.findViewById(R.id.rv_messages_view);
        setupRecyclerView();

        final TextInputEditText tietWrittenMessage = perantView.findViewById(R.id.tiet_writen_message);
        ImageButton ibSend = perantView.findViewById(R.id.ibSend);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(tietWrittenMessage.getText().toString())) {
                    addMessage(tietWrittenMessage.getText().toString());
                }

            }
        });

        return perantView;

    }


    private void addMessage(String messageContent) {
        Message m = new Message(MESSAGE_SENDER, messageContent, true);
        mMessages.add(m);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        mAdapter.notifyItemChanged(mMessages.size() - 1, m);
    }

    private void setupRecyclerView() {

        mAdapter = new ChatRecyclerViewAdapter(mContext, createDemoChat());

        // you need these codes < Start >
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
       /*
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);*/

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        scrollToLast();
    }

    private void scrollToLast() {
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(
                                    recyclerView.getAdapter().getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });
    }

    private ArrayList<Message> createDemoChat() {

        mMessages = new ArrayList<>();

        mMessages.add(new Message(MESSAGE_SENDER, "Hi!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "Hello!", true));
        mMessages.add(new Message(MESSAGE_SENDER, "I want to request for develop app!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "which kind of app u want?", true));
        mMessages.add(new Message(MESSAGE_SENDER, "Game!!, and i will send details  ", false));
        mMessages.add(new Message(MESSAGE_SENDER, "okey, I am glad to do for u..", true));
        mMessages.add(new Message(MESSAGE_SENDER, "man you having a bad day ?!", false));

/*

        mMessages.add(new Message(MESSAGE_SENDER, "2Hi!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "2Hello!", true));
        mMessages.add(new Message(MESSAGE_SENDER, "2how are you!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "2are you doing well!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "2are you doing well!are you doing well!are you doing well!are you doing well!are you doing well!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "2What is that huh!", true));
        mMessages.add(new Message(MESSAGE_SENDER, "2man you having a bad day ?!", true));

        mMessages.add(new Message(MESSAGE_SENDER, "3Hi!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "3Hello!", true));
        mMessages.add(new Message(MESSAGE_SENDER, "3how are you!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "3are you doing well!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "3are you doing well!are you doing well!are you doing well!are you doing well!are you doing well!", false));
        mMessages.add(new Message(MESSAGE_SENDER, "3What is that huh!", true));
        mMessages.add(new Message(MESSAGE_SENDER, "3man you having a bad day ?!", true));
*/


        return mMessages;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditProfileInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     **/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
