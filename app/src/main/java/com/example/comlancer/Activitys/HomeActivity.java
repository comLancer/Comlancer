package com.example.comlancer.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.comlancer.Adapter.MyRecyclerViewAdapter;
import com.example.comlancer.DialogFragments.AddFeedbackDialogFragment;
import com.example.comlancer.DialogFragments.AddImageDialogFragment;
import com.example.comlancer.Fragments.ChatFragment;
import com.example.comlancer.Fragments.EditProfileFragment;
import com.example.comlancer.Fragments.ProfileFragment;
import com.example.comlancer.Models.ComlancerImages;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeActivity extends AppCompatActivity implements ProfileFragment.profileInterface, AddFeedbackDialogFragment.OnAddFeedback,
        ChatFragment.OnFragmentInteractionListener, EditProfileFragment.EditProfileInterface, MyRecyclerViewAdapter.OnItemClickListener, AddImageDialogFragment.AddImgeToRecycleViewlInterface {
    /// this is for  BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.item_nav_home:
                            Intent goHomeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                            startActivity(goHomeIntent);

                            break;
                        case R.id.item_nav_search:
                            Intent goTabIntent = new Intent(HomeActivity.this, TabFreelancerCompanyActivity.class);
                            startActivity(goTabIntent);

                            break;
                        case R.id.item_nav_profile:
                            changeFragmentTo(new ProfileFragment(), ProfileFragment.class.getSimpleName());
                            break;

                        case R.id.item_nav_chat:
                            changeFragmentTo(new ChatFragment(), ChatFragment.class.getSimpleName());
                            break;
                    }


                    return true;
                }

            };

    //this is for RecycleView
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button profile = findViewById(R.id.btn_profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo();
            }
        });


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) navListener);

    }

    private void getUserInfo() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference myRef = database.getReference(MyConstants.FB_KEY_CF).child(currentUser.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);

                changeFragmentTo(ProfileFragment.newInstance(value), ProfileFragment.class.getSimpleName());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    // To chang Fragment .. we use  if and else .. 1.upload fragment and display it in activity
    private void changeFragmentTo(Fragment fragmentToLoad, String fragmentTag) {
        if (getSupportFragmentManager().findFragmentByTag(fragmentTag) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.frame_container, fragmentToLoad, fragmentTag)
                    .addToBackStack(fragmentTag)
                    .commit();

        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.frame_container, fragmentToLoad, fragmentTag)
                    .commit();
        }
    }

    //that function allow the user to write the feedback and then upload it into the database in firebase...it also will display it in freelancer/company profile
    private void addFeedbackToProfileUser(User user) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(MyConstants.FB_KEY_CF)
                .child(user.getName());

        myRef.setValue(user);


        ProfileFragment fragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            fragment.dismissDialog();
            fragment.readUsersFromFirebase();
            fragment.updateRating(user.getAverageRating());
        }

    }


    @Override
    public void onClick(User user) {
        // changeFragmentTo(new RegisterFragment(), RegisterFragment.class.getSimpleName());
        changeFragmentTo(EditProfileFragment.newInstance(user), EditProfileFragment.class.getSimpleName());

    }

    @Override
    public void onFeedbackSubmit(User user) {
        addFeedbackToProfileUser(user);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClickEditProfileFormEditFragment(User user) {
        editUserInfo(user);
    }


    // this function will take the data from the edit tezxt and save in the darabase then it will return to profile fragmnet
    private void editUserInfo(User user) {

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

        goBackToProfileFragment(user);


    }

    //this same change fragment().   yousef add this function becouse the image was uploading repeatly
    private void goBackToProfileFragment(User user) {
        onBackPressed();

        ProfileFragment fragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());

        if (fragment != null && fragment.isVisible()) {

            fragment.updateUI(user);
        }

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

        onBackPressed();

        ProfileFragment fragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ProfileFragment.class.getSimpleName());

        if (fragment != null && fragment.isVisible()) {
            fragment.updateUI(user);
        }
    }

    @Override
    public void onCancelClick() {


    }


    @Override
    public void onItemClicked(ComlancerImages u) {

    }
}