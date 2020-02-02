package com.example.comlancer.DialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

import com.example.comlancer.Models.RatingFeedBackContainer;
import com.example.comlancer.Models.RatingFeedback;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.comlancer.Activitys.LoginRegistrationActivity.KEY_USER_NAME;
import static com.example.comlancer.Activitys.LoginRegistrationActivity.MY_PREFS_NAME;


public class AddFeedbackDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_USER = "param1";


    // TODO: Rename and change types of parameters
    private User mUser;
    private static final String TAG = "addFeedback";
    private OnAddFeedback mListener;
    private Context mContext;

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
        View perantView = inflater.inflate(R.layout.fragment_add_feedback, container, false);

        final RatingBar ratingBar = perantView.findViewById(R.id.ratingbar);

        final Button btnSubmit = perantView.findViewById(R.id.btn_submit);
        final Button btnCancel = perantView.findViewById(R.id.btn_cancel);
        final EditText etnameFeedBack = perantView.findViewById(R.id.et_name_feedback);
        final EditText etFeedback = perantView.findViewById(R.id.et_feedback);

        getCurrentUserName(etnameFeedBack);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                RatingFeedback rf = new RatingFeedback();
                rf.setRating(ratingBar.getRating());
                rf.setFeedback(etFeedback.getText().toString());
                rf.setFullName(etnameFeedBack.getText().toString());

                rf.setFullName(getUserFullNameFromSharedPref());
                rf.setFirebaseId(currentUser.getUid());

                if (mUser.getMyRatingFeedback() != null) {
                    mUser.getMyRatingFeedback().getFeedbackList().add(rf);
                } else {
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
                dismiss();


            }
        });


        return perantView;
    }

    private void getCurrentUserName(final EditText etnameFeedBack) {
        //TODO : GET getName FromSharePref

        etnameFeedBack.setText(getUserFullNameFromSharedPref());

    }

    private String getUserFullNameFromSharedPref() {

        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString(KEY_USER_NAME, "No pass defined");//"No name defined" is the default value.
        Log.d("myUser-info", "Register // name: " + name);
        return name;

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
        mContext = context;
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
