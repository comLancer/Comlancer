package com.example.comlancer.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.comlancer.Adapter.UserAdapter;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.SearchListener;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.comlancer.Models.MyConstants.KEY_ALL_ITEMS;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompaniesFragment extends Fragment implements ListView.OnItemClickListener, SearchListener {

    private ComapaniesListenerInerface mListener;

    UserAdapter mAdapter;
    private Context mContext;
    DatabaseReference mRef;
    private static final String TAG = "company-fragment";
    private ArrayList<User> items;


    public CompaniesFragment() {
        // Required empty public constructor
    }

    public void setListener(CompaniesFragment.ComapaniesListenerInerface listener) {
        mListener = listener;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.test, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mRef = database.getReference(MyConstants.FB_ALL_USERS);

        ListView listView = parentView.findViewById(R.id.list_view);


        mAdapter = new UserAdapter(mContext);
        readCompaniesFromFirebase();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        return parentView;


    }


    public void readCompaniesFromFirebase() {
        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                items = new ArrayList<>();
                items.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User value = d.getValue(User.class);
                    if (value.getRole().equalsIgnoreCase("Company")) {
                        items.add(value);
                    }
                }
                mAdapter.updateFreelancerCompaniesArrayList(items);

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


/*
    private void displayAddCompanyDialog() {
        mDialogAddImage = new AddCompanyDialogFragment(mContext);

        mDialogAddImage.show(getChildFragmentManager(), AddCompanyDialogFragment.class.getSimpleName());
    }
*/


    public void onItemPressed(User user) {
        if (mListener != null) {
            mListener.onItemClickCompany(user);
        }
    }

    /*
      public void dismissFeedbackDialog() {
            mDialogAddImage.dismiss();
        }*/
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        onItemPressed((User) adapterView.getAdapter().getItem(i));

    }

    @Override
    public void onTextChanged(String searchKey) {

        if (items != null && !searchKey.equals(KEY_ALL_ITEMS)) {
            search(searchKey);
        } else {
            readCompaniesFromFirebase();
        }

    }

    private void search(String searchKey) {

        ArrayList<User> temp = new ArrayList<>();

        for (User u : items) {
            boolean isName = u.getName().toLowerCase().contains(searchKey.toLowerCase());
            boolean isTag = u.getTag().toLowerCase().contains(searchKey.toLowerCase());
            if (isName || isTag) {
                temp.add(u);
            }
        }
        mAdapter.updateFreelancerCompaniesArrayList(temp);

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
    public interface ComapaniesListenerInerface {
        // TODO: Update argument type and name
        void onItemClickCompany(User user);
    }
}




