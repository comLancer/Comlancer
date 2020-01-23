package com.example.comlancer.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.RatingFeedBackContainer;
import com.example.comlancer.Models.RatingFeedback;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddFeedbackDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_USER = "param1";


    // TODO: Rename and change types of parameters
    private User mUser;
    private static final String TAG = "addFeedback";
    private OnAddFeedback mListener;

    public AddFeedbackDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }

    }

    public static AddFeedbackDialogFragment newInstance(User user) {
        AddFeedbackDialogFragment fragment = new AddFeedbackDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(KEY_USER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_add_feedback, container, false);

        final RatingBar ratingBar = itemView.findViewById(R.id.ratingbar);
        final EditText etFeedback = itemView.findViewById(R.id.et_feedback);
        final Button btnSubmit = itemView.findViewById(R.id.btn_submit);
        final Button btnCancel = itemView.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser currentUser = mAuth.getCurrentUser();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(MyConstants.FB_KEY_USERS).child(currentUser.getUid());

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User value = dataSnapshot.getValue(User.class);

                        RatingFeedback rf = new RatingFeedback();
                        rf.setRating(ratingBar.getRating());
                        rf.setFeedback(etFeedback.getText().toString());

                        rf.setFullName(value.getName());
                        rf.setFirebaseId(value.getFirebaseUserId());
                        if (mUser.getMyRatingFeedback() != null) {
                            mUser.getMyRatingFeedback().getFeedbackList().add(rf);
                        }
                        else {
                            RatingFeedBackContainer rfc = new RatingFeedBackContainer();
                            ArrayList<RatingFeedback> ratingFeedbackArrayList = new ArrayList<>();
                            ratingFeedbackArrayList.add(rf);
                            rfc.setFeedbackList(ratingFeedbackArrayList);

                            mUser.setMyRatingFeedback(rfc);

                        }

                        //update averageRating
                        int numberOfFeedbacks = mUser.getNumberOfFeedbacks() + 1;
                        float sumOfFeedbacks = mUser.getRatingSum() + ratingBar.getRating();
                        float averageRating = sumOfFeedbacks / numberOfFeedbacks;

                        mUser.setNumberOfFeedbacks(numberOfFeedbacks);
                        mUser.setRatingSum(sumOfFeedbacks);
                        mUser.setAverageRating(averageRating);

                        onSubmitPressed(mUser);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });


        return itemView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSubmitPressed(User user) {
        if (mListener != null) {
            mListener.onFeedbackSubmit(user);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddFeedback) {
            mListener = (OnAddFeedback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginListener");
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
     */
    public interface OnAddFeedback {
        // TODO: Update argument type and name
        void onFeedbackSubmit(User user);
    }
}
