package com.example.comlancer.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.comlancer.DialogFragments.AddFeedbackDialogFragment;
import com.example.comlancer.Fragments.FreelancerFragment;
import com.example.comlancer.Fragments.LoginFragment;
import com.example.comlancer.Fragments.ProfileFragment;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements ProfileFragment.profileInterface ,AddFeedbackDialogFragment.OnAddFeedback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button profile = findViewById(R.id.btn_profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentTo(new ProfileFragment(), ProfileFragment.class.getSimpleName());
            }
        });
    }

/*        BottomNavigationView bottomNav=findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemReselectedListener((BottomNavigationView.OnNavigationItemReselectedListener) navListener);


    }

/// this is for  BottomNavigationView
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;
                    switch (menuItem.getItemId()){
                    case R.id.item_nav_tab:
                            selectedFragment=new FreelancerFragment();
                            break;
                        case R.id.item_nav_tab:
                            selectedFragment=new FreelancerFragment();
                            break;


                    }
                }
            };*/


    private void changeFragmentTo(Fragment fragmentToDisplay, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_container, fragmentToDisplay, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }



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

    }

    @Override
    public void onFeedbackSubmit(User user) {
        addFeedbackToProfileUser(user);

    }
}