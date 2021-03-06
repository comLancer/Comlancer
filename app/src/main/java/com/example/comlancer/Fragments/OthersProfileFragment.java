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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comlancer.Adapter.MyRecyclerViewAdapter;
import com.example.comlancer.Adapter.RatingFeedAdapter;
import com.example.comlancer.DialogFragments.AddFeedbackDialogFragment;
import com.example.comlancer.Models.ComlancerImages;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link OthersProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OthersProfileFragment extends Fragment implements MyRecyclerViewAdapter.OnItemClickListener, AddFeedbackDialogFragment.OnAddFeedback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_Users = "user";
    private static final int NUMBER_OF_COLUMNS = 3;
    User mUser;

    RatingBar mRatingBar;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    TextView tvInfo;
    TextView tvName;
    TextView tvTag;
    ListView lvFeedback;
    RatingFeedAdapter mAdapterRating;
    CircularImageView ivProfile;
    //    AddImageDialogFragment mDialogAddImage;
//    ImageButton imgbtnEditProfile;
    ComlancerImages comlancerImages;
    private profileInterface mListener;
    private Context mContext;
    //    ImageButton imgbtnAddImg;
    private AddFeedbackDialogFragment mDialogFeedback;
    private RecyclerView rvGrid;
    private MyRecyclerViewAdapter mAdapterRecycle;

    public OthersProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * tance of fragment PersonalProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OthersProfileFragment newInstance(User user) {
        OthersProfileFragment fragment = new OthersProfileFragment();
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
        View parentView = inflater.inflate(R.layout.fragment_others_profile, container, false);

        //this is for RecycleView

        FloatingActionButton fabAdd = parentView.findViewById(R.id.fab_add);
        Button btnChat = parentView.findViewById(R.id.btn_chat);

        mAuth = FirebaseAuth.getInstance().getInstance();
        ivProfile = parentView.findViewById(R.id.iv_img_profile);
        mRatingBar = parentView.findViewById(R.id.ratingBar);
        tvName = parentView.findViewById(R.id.tv_name);
        tvInfo = parentView.findViewById(R.id.tv_info);
        tvInfo.setMovementMethod(new ScrollingMovementMethod());
        rvGrid = parentView.findViewById(R.id.recycler_view);
        ImageButton imgbtnLiner = parentView.findViewById(R.id.imgbtn_liner);
        ImageButton imgbtnGrid = parentView.findViewById(R.id.imgbtn_grid);

        mAdapterRecycle = new MyRecyclerViewAdapter(mContext);
        setupRecyclerViewGrid();

        tvTag = parentView.findViewById(R.id.tv_tag);
        //   imgbtnEditProfile = parentView.findViewById(R.id.imgbtn_edit_profile);
        // imgbtnAddImg = parentView.findViewById(R.id.imgbtn_addImage);
        lvFeedback = parentView.findViewById(R.id.lv_list_feedback);
        mAdapterRating = new RatingFeedAdapter(mContext);
        readUsersFromFirebase();
        lvFeedback.setAdapter(mAdapterRating);


        if (mUser.getImagesContainer() != null) {
            mAdapterRecycle.updateList(mUser.getImagesContainer().getImageList());
        }


        if (mAuth.getCurrentUser() == null) {

            fabAdd.hide();

        }

        if (mAuth.getCurrentUser() != null) {

            fabAdd.show();
            ForAddFeedback(fabAdd);
        }


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "chat not available..", Toast.LENGTH_SHORT).show();
            }
        });

        imgbtnLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRecyclerViewLiner();
            }
        });


        imgbtnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupRecyclerViewGrid();
            }
        });


        readUsersFromFirebase();

        return parentView;
    }

    private void ForAddFeedback(FloatingActionButton fabAdd) {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              /*  openLoginFragment();
                openDialogFeedback();*/
                mDialogFeedback = AddFeedbackDialogFragment.newInstance(mUser);
                mDialogFeedback.show(getChildFragmentManager(), AddFeedbackDialogFragment.class.getSimpleName());

            }
        });
    }



/*void openDialogFeedback() {
    mDialogFeedback = AddFeedbackDialogFragment.newInstance(mUser);
    mDialogFeedback.show(getChildFragmentManager(), AddFeedbackDialogFragment.class.getSimpleName());
}

    void openLoginFragment() {
        Intent i=new Intent(mContext, LoginRegistrationActivity.class);
        startActivity(i);


    }*/



    //this is for RecycleView....
    private void setupRecyclerViewGrid() {
        rvGrid.setLayoutManager(new GridLayoutManager(mContext, NUMBER_OF_COLUMNS));
        rvGrid.setItemAnimator(new DefaultItemAnimator());
        rvGrid.setAdapter(mAdapterRecycle);
    }

    //this is for RecycleView....
    private void setupRecyclerViewLiner() {
        rvGrid.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true));
        rvGrid.setItemAnimator(new DefaultItemAnimator());
        rvGrid.setAdapter(mAdapterRecycle);
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


    }


    public void dismissFeedbackDialog() {

        mDialogFeedback.dismiss();
    }


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

    @Override
    public void onFeedbackSubmit(User user) {

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
