package com.example.comlancer.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.comlancer.Adapter.MyRecyclerViewAdapter;
import com.example.comlancer.DialogFragments.AddFeedbackDialogFragment;
import com.example.comlancer.DialogFragments.AddImageDialogFragment;
import com.example.comlancer.Fragments.ChatFragment;
import com.example.comlancer.Fragments.CompaniesFragment;
import com.example.comlancer.Fragments.EditProfileFragment;
import com.example.comlancer.Fragments.FreelancerFragment;
import com.example.comlancer.Fragments.HomeFragment;
import com.example.comlancer.Fragments.OthersProfileFragment;
import com.example.comlancer.Fragments.PersonalProfileFragment;
import com.example.comlancer.Fragments.TabFreelancerCompanyFragment;
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
import static com.example.comlancer.Activitys.LoginRegistrationActivity.KEY_EMAILE;
import static com.example.comlancer.Activitys.LoginRegistrationActivity.MY_PREFS_NAME;
import static com.example.comlancer.Models.MyConstants.FB_ALL_USERS;
import static com.example.comlancer.Models.MyConstants.KEY_SP_ERROR_NO_EMAIL;

public class ControlActivity extends AppCompatActivity implements PersonalProfileFragment.profileInterface, AddFeedbackDialogFragment.OnAddFeedback,
        ChatFragment.OnFragmentInteractionListener, EditProfileFragment.EditProfileInterface, MyRecyclerViewAdapter.OnItemClickListener, AddImageDialogFragment.AddImgeToRecycleViewlInterface,
        HomeFragment.OnCategoryButttonClick, CompaniesFragment.ComapaniesListenerInerface, FreelancerFragment.FreelancerListenerInerface, TabFreelancerCompanyFragment.TabPageListener {
    /// this is for  BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.item_nav_home:
                            changeFragmentTo(new HomeFragment(), HomeFragment.class.getSimpleName());
                            break;
                        case R.id.item_nav_search:

                            changeFragmentTo(new TabFreelancerCompanyFragment(), TabFreelancerCompanyFragment.class.getSimpleName());

                            break;
                        case R.id.item_nav_profile:
                            getUserInfo();

                            break;

                    /*    case R.id.item_nav_chat:
                            changeFragmentTo(new ChatFragment(), ChatFragment.class.getSimpleName());
                            break;*/
                    }


                    return true;
                }

            };

    //this is for RecycleView
    Context mContext;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        changeFragmentTo(new HomeFragment(), HomeFragment.class.getSimpleName());// display home fragment


        bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) navListener);

    }

    private void getUserInfo() {
        if (getEmailSharedPref().equals(KEY_SP_ERROR_NO_EMAIL)) {
            moveToLoginRegisterActivity();
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final FirebaseUser currentUser = mAuth.getCurrentUser();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (currentUser != null && currentUser.getUid() != null) {

                DatabaseReference myRef = database.getReference(FB_ALL_USERS).child(currentUser.getUid());

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        User value = dataSnapshot.getValue(User.class);

                        changeFragmentTo(PersonalProfileFragment.newInstance(value), PersonalProfileFragment.class.getSimpleName());

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            } else {
                moveToLoginRegisterActivity();
            }
        }

    }

    private void moveToLoginRegisterActivity() {
        startActivity(new Intent(ControlActivity.this, LoginRegistrationActivity.class));
    }

    private String getEmailSharedPref() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString(KEY_EMAILE, MyConstants.KEY_SP_ERROR_NO_EMAIL);//"No name defined" is the default value.
        Log.d("myUser-info", "Register // name: " + name);
        return name;


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


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FB_ALL_USERS).child(user.getFirebaseUserId());

        myRef.setValue(user);


        PersonalProfileFragment fragment = (PersonalProfileFragment) getSupportFragmentManager().findFragmentByTag(PersonalProfileFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            // fragment.dismissFeedbackDialog();
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
    public void finishActivity() {
        finish();
    }

    @Override
    public void onFeedbackSubmit(User user) {
        addFeedbackToProfileUser(user);

    }


    @Override
    public void onClickEditProfileFormEditFragment(User user) {
        editUserInfo(user);
    }


    // this function will take the data from the edit tezxt and save in the database then it will return to profile fragment
    private void editUserInfo(User user) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FB_ALL_USERS);

        myRef.child(user.getFirebaseUserId()).setValue(user);

        goBackToProfileFragment(user);


    }

    //this same change fragment().   yousef add this function becouse the image was uploading repeatly
    private void goBackToProfileFragment(User user) {
        onBackPressed();

        PersonalProfileFragment fragment = (PersonalProfileFragment) getSupportFragmentManager().findFragmentByTag(PersonalProfileFragment.class.getSimpleName());

        if (fragment != null && fragment.isVisible()) {

            fragment.updateUI(user);
        }

    }


    @Override
    public void onClickAddImage(User user) {


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FB_ALL_USERS);

        myRef.child(user.getFirebaseUserId()).setValue(user);


        PersonalProfileFragment fragment = (PersonalProfileFragment) getSupportFragmentManager().findFragmentByTag(PersonalProfileFragment.class.getSimpleName());

        if (fragment != null && fragment.isVisible()) {
            fragment.dismissAddImageDialog();
            fragment.updateUI(user);
        }
    }

    private void openLoginFragment() {
        changeFragmentTo(new OthersProfileFragment(), OthersProfileFragment.class.getSimpleName());
    }


    @Override
    public void onCancelClick() {


    }


    @Override
    public void onItemClicked(ComlancerImages u) {

    }


    @Override
    public void onItemClickTap(User user) {

        changeFragmentTo(OthersProfileFragment.newInstance(user), OthersProfileFragment.class.getSimpleName());

    }

    @Override
    public void onItemClickCompany(User user) {

    }

    @Override
    public void onItemClickFreelancer(User user) {
        openLoginFragment();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void OnClick(String catagory) {
        bottomNav.setSelectedItemId(R.id.item_nav_search);
        changeFragmentTo(TabFreelancerCompanyFragment.newInstance(catagory), TabFreelancerCompanyFragment.class.getSimpleName());
    }

    @Override
    public void onItemClickItem(User user) {
        changeFragmentTo(OthersProfileFragment.newInstance(user), OthersProfileFragment.class.getSimpleName());

    }

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);

            if (fragment instanceof HomeFragment) {
                bottomNav.setSelectedItemId(R.id.item_nav_home);
            } else if (fragment instanceof TabFreelancerCompanyFragment) {
                bottomNav.setSelectedItemId(R.id.item_nav_search);
            } else if (fragment instanceof PersonalProfileFragment) {
                bottomNav.setSelectedItemId(R.id.item_nav_profile);
            }


        } else {
            finish();
        }
    }
}