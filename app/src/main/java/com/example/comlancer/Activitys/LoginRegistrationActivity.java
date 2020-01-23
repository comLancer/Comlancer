package com.example.comlancer.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.comlancer.Adapter.TabsPagerAdapter;
import com.example.comlancer.Fragments.LoginFragment;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.R;
import com.example.comlancer.Fragments.RegistrationFragment;
import com.example.comlancer.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginRegistrationActivity extends AppCompatActivity implements LoginFragment.onLoginInterface, RegistrationFragment.RegisterListenerInterface {
   // private static final String FB_KEY_CF = "CompanyAndFreelancer";
   // private static final String FB_KEY_USERS = "users";
    private DatabaseReference mRef;
    private static final String TAG = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        mAuth = FirebaseAuth.getInstance();

        changeFragmentTo(new LoginFragment(), LoginFragment.class.getSimpleName());


    }

    private void changeFragmentTo(Fragment fragmentToDisplay, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fl_container, fragmentToDisplay, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void registerFormFierbaseCreate(final User user) {

        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            user.setFirebaseUserId(firebaseUser.getUid());
                            addUserToFirebaseWrite(user);
                            verfiyEmail(firebaseUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginRegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    public void verfiyEmail(FirebaseUser firebaseUser) {
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginRegistrationActivity.this, "verfiy Email was send to your email",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("sendEmailVerification", "failed: " + task.getException());
                    Toast.makeText(LoginRegistrationActivity.this, "Error can not send Email",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkIfEmailIsVerified(final FirebaseUser firebaseUser) {
        return firebaseUser.isEmailVerified();
    }


    public void onSignInClick() {
        changeFragmentTo(new LoginFragment(), LoginFragment.class.getSimpleName());
    }


    private void addUserToFirebaseWrite(User user) {


        String myFirebaseRef;

        if (user.getRole().equalsIgnoreCase("User")) {
         //   myFirebaseRef = FB_KEY_USERS;
            myFirebaseRef = (MyConstants.FB_KEY_USERS);
        }else{
          //  myFirebaseRef =FB_KEY_CF;
            myFirebaseRef =(MyConstants.FB_KEY_CF);
        }

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(myFirebaseRef);

        myRef.child(user.getFirebaseUserId()).setValue(user);

        changeFragmentTo(new LoginFragment(), LoginFragment.class.getSimpleName());


    }


    @Override
    public void onLoginClick(User user) {
        changTwoActivity();
    }

    @Override
    public void onRegisterClick() {
        changeFragmentTo(new RegistrationFragment(), RegistrationFragment.class.getSimpleName());
    }


    @Override
    public void onRegisterClick(User user) {
        registerFormFierbaseCreate(user);

    }

    @Override
    public void onLoginClick() {
        changeFragmentTo(new RegistrationFragment(), RegistrationFragment.class.getSimpleName());

    }

    public void changTwoActivity() {

        Intent myIntent = new Intent(this, HomeActivity.class);
        startActivity(myIntent);
    }


}
