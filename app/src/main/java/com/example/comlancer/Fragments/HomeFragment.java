package com.example.comlancer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.comlancer.Adapter.UserAdapter;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCategoryButttonClick} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ListView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String KEY_USER = "user";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference mRef;
    private OnCategoryButttonClick mListener;
    UserAdapter mAdapter;
    User mUser;
    private Context mContext;
    private ArrayList<User> items;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(User user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(KEY_USER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference(MyConstants.FB_KEY_CF);




        Button btnPrograming = parentView.findViewById(R.id.btn_Programing);
        btnPrograming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("Programmer");
            }
        });

        Button btnArt = parentView.findViewById(R.id.btn_art);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("Art");
            }
        });

        Button btnPhotography = parentView.findViewById(R.id.btn_Photography);
        btnPhotography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("Photography");
            }
        });

        Button btnDesign = parentView.findViewById(R.id.btn_design);
        btnDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("Design");
            }
        });


        ListView listView = parentView.findViewById(R.id.list_view);


        mAdapter = new UserAdapter(mContext);
        readCompaniesFromFirebase();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        // listView.setOnItemClickListener(this);


        return parentView;
    }


    public void readCompaniesFromFirebase() {

        Query query = FirebaseDatabase.getInstance().getReference(MyConstants.FB_KEY_CF).orderByChild("averageRating").limitToFirst(3);

        // Read from the database
        query.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                items = new ArrayList<>();
                items.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User value = d.getValue(User.class);


                    //////////////////////


                    ///////////////////
                    //  if (value.getRole().equalsIgnoreCase("Company")) {
                    items.add(value);
                    //}
                    // }
                    Collections.reverse(items);
                    mAdapter.updateFreelancerCompaniesArrayList(items);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String category) {
        if (mListener != null) {
            mListener.OnClick(category);
        }
    }

    public void onItemPressed(User user) {
        if (mListener != null) {
            mListener.onItemClickItem(user);
        }
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
        if (context instanceof OnCategoryButttonClick) {
            mListener = (OnCategoryButttonClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCategoryButttonClick");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemPressed((User) parent.getAdapter().getItem(position));

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
    public interface OnCategoryButttonClick {
        // TODO: Update argument type and name
        void OnClick(String catagory);

        void onItemClickItem(User user);
    }
}
