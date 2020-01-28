package com.example.comlancer.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.comlancer.Adapter.UserAdapter;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompaniesFragment extends Fragment implements ListView.OnItemClickListener  {

    private ComapaniesListenerInerface mListener;

    UserAdapter mAdapter;
    private Context mContext;
    DatabaseReference mRef;
    private static final String TAG = "company-fragment";


    public CompaniesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.test, container, false);



        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mRef = database.getReference(MyConstants.FB_KEY_CF);

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
                ArrayList<User> items = new ArrayList<>();
                items.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User value = d.getValue(User.class);
                    if (value.getRole().equalsIgnoreCase("Comapanies")){
                        items.add(value);
                    }


                }

                mAdapter.updateFreelancerArrayList(items);



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
            mListener.onItemClick(user);
        }
    }

/*
  public void dismissDialog() {
        mDialogAddImage.dismiss();
    }*/
public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;

    if (context instanceof ComapaniesListenerInerface) {
        mListener = (ComapaniesListenerInerface) context;
    } else {
        throw new RuntimeException(context.toString()
                + " must implement CompaniesInterface");


    }
}


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Toast.makeText(mContext, "Hello", Toast.LENGTH_SHORT).show();
        onItemPressed((User) adapterView.getAdapter().getItem(i));
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
        void onItemClick(User user);
    }
}




