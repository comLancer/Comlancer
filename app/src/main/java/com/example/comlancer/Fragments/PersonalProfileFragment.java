package com.example.comlancer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comlancer.Adapter.MyRecyclerViewAdapter;
import com.example.comlancer.Adapter.RatingFeedAdapter;
import com.example.comlancer.DialogFragments.AddImageDialogFragment;
import com.example.comlancer.Models.ComlancerImages;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link PersonalProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalProfileFragment extends Fragment implements MyRecyclerViewAdapter.OnItemClickListener, AddImageDialogFragment.AddImgeToRecycleViewlInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_Users = "user";
    private profileInterface mListener;
    User mUser;
    RatingBar mRatingBar;
    private static final int NUMBER_OF_COLUMNS = 3;
    private Context mContext;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    TextView tvInfo;
    TextView tvName;
    TextView tvTag;
    ListView lvFeedback;
    RatingFeedAdapter mAdapterRating;
    CircularImageView ivProfile;
    AddImageDialogFragment mDialogAddImage;
    ImageButton imgbtnEditProfile;
    ComlancerImages comlancerImages;
    ImageButton imgbtnAddImg;
    //  private AddFeedbackDialogFragment mDialogFeedback;
    private RecyclerView rvGrid;
    private MyRecyclerViewAdapter mAdapterRecycle;

    public PersonalProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * tance of fragment PersonalProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalProfileFragment newInstance(User user) {
        PersonalProfileFragment fragment = new PersonalProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_Users, user);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(KEY_Users);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_personal_profile, container, false);

        //this is for RecycleView


        mAuth = FirebaseAuth.getInstance().getInstance();
        Button btnChat = parentView.findViewById(R.id.btn_chat);
        ivProfile = parentView.findViewById(R.id.iv_img_profile);
        mRatingBar = parentView.findViewById(R.id.ratingBar);
        tvName = parentView.findViewById(R.id.tv_name);
        tvInfo = parentView.findViewById(R.id.tv_info);
        tvInfo.setMovementMethod(new ScrollingMovementMethod());
        rvGrid = parentView.findViewById(R.id.recycler_view);
        mAdapterRecycle = new MyRecyclerViewAdapter(mContext);
        tvTag = parentView.findViewById(R.id.tv_tag);
        imgbtnEditProfile = parentView.findViewById(R.id.imgbtn_edit_profile);
        imgbtnAddImg = parentView.findViewById(R.id.imgbtn_addImage);
        lvFeedback = parentView.findViewById(R.id.lv_list_feedback);
        mAdapterRating = new RatingFeedAdapter(mContext);

        readUsersFromFirebase();
        lvFeedback.setAdapter(mAdapterRating);
        setupRecyclerViewGrid();
        if (mUser.getImagesContainer() != null) {
            mAdapterRecycle.updateList(mUser.getImagesContainer().getImageList());
        }

        // FloatingActionButton fabAdd = parentView.findViewById(R.id.fab_add);


       /* fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogFeedback = AddFeedbackDialogFragment.newInstance(mUser);
                mDialogFeedback.show(getChildFragmentManager(), AddFeedbackDialogFragment.class.getSimpleName());
            }
        });
*/

        imgbtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditPressed(mUser);
            }
        });


        readUsersFromFirebase();

        return parentView;


    }


    //this is for RecycleView....
    private void setupRecyclerViewGrid() {
        rvGrid.setLayoutManager(new GridLayoutManager(mContext, NUMBER_OF_COLUMNS));
        rvGrid.setItemAnimator(new DefaultItemAnimator());
        rvGrid.setAdapter(mAdapterRecycle);
    }


    @Override
    public void onClickAddImage(User user) {
        String myFirebaseRef;

        if (user.getRole().equalsIgnoreCase("User")) {
            //   myFirebaseRef = FB_KEY_USERS;
            myFirebaseRef = (MyConstants.FB_KEY_USERS);
        } else {
            //  myFirebaseRef =FB_KEY_CF;
            myFirebaseRef = (MyConstants.FB_KEY_CF);
        }

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(myFirebaseRef);

        myRef.child(user.getFirebaseUserId()).setValue(user);


    }

    @Override
    public void onCancelClick() {

    }


    public void readUsersFromFirebase() {

        tvName.setText(mUser.getName());
        tvInfo.setText(mUser.getInfo());
        tvTag.setText(mUser.getTag());

        Glide.with(mContext).load(mUser.getImageUrl()).placeholder(R.mipmap.ic_launcher_round).into(ivProfile);


        mRatingBar.setRating(mUser.getAverageRating());
        mUser = mUser;


        if (mUser.getMyRatingFeedback() != null) {
            mAdapterRating.updateFeedbackArrayList(mUser.getMyRatingFeedback().getFeedbackList());
        }


        imgbtnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogAddImage = AddImageDialogFragment.newInstance(mUser);
                mDialogAddImage.show(getChildFragmentManager(), AddImageDialogFragment.class.getSimpleName());


            }
        });


    }


    public void dismissAddImageDialog() {

        mDialogAddImage.dismiss();
    }


/*
    public void dismissFeedbackDialog() {

        mDialogFeedback.dismiss();
    }
*/


    public void updateRating(Float newRating) {
        mRatingBar.setRating(newRating);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onEditPressed(User user) {
        if (mListener != null) {
            mListener.onClick(user);
        }
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
        if (context instanceof profileInterface) {
            mListener = (profileInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement profileInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    //this function will take info from edit profile .. we write User becouse it the data from it ,was update insid user
    public void updateUI(User user) {

        tvName.setText(user.getName());
        tvInfo.setText(user.getInfo());
        tvTag.setText(user.getTag());
        //upload the image from the database and display in the xml
        Glide.with(mContext).load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher_round).into(ivProfile);


        mRatingBar.setRating(user.getAverageRating());
        mUser = user;


        if (user.getMyRatingFeedback() != null) {
            mAdapterRating.updateFeedbackArrayList(user.getMyRatingFeedback().getFeedbackList());
        }


        //todo : code not opining profile after closing dialog
        if (user.getImagesContainer() != null) {
            mAdapterRecycle.updateList(user.getImagesContainer().getImageList());
        }


    }


    @Override
    public void onItemClicked(ComlancerImages user) {

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
    public interface profileInterface {
        // TODO: Update argument type and name
        void onClick(User user);
    }
}
