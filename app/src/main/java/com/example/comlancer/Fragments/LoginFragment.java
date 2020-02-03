package com.example.comlancer.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.comlancer.Activitys.LoginRegistrationActivity;
import com.example.comlancer.DialogFragments.ForgotPasswordDialogFragment;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;
import static com.example.comlancer.Models.MyConstants.FB_KEY_CF;
import static com.example.comlancer.Models.MyConstants.FB_KEY_USERS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * \
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private onLoginInterface mListener;
    private Context mcontext;
    ForgotPasswordDialogFragment mDialog;
    User mUser;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment


        View perantView = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText etEmail = perantView.findViewById(R.id.et_title);
        final EditText etpass = perantView.findViewById(R.id.et_pass);
        final TextView tvForgetPassword = perantView.findViewById(R.id.tv_forget_pass);
        final CheckBox cbStayLoggedIn = perantView.findViewById(R.id.cb_stay_logged_in);
        Button btnLogin = perantView.findViewById(R.id.btn_login);
        Button btnRegister = perantView.findViewById(R.id.btn_registration);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginToFirebase(etEmail.getText().toString(), etpass.getText().toString(), cbStayLoggedIn.isChecked());

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonRegister();
            }
        });


        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new ForgotPasswordDialogFragment();
                mDialog.show(getChildFragmentManager(), ForgotPasswordDialogFragment.class.getSimpleName());

            }
        });


        return perantView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonLogin(User user) {
        if (mListener != null) {
            mListener.onLoginClick(user);
        }
    }

    public void onButtonRegister() {
        if (mListener != null) {
            mListener.onRegisterClick();
        }
    }


    @Override
    public void onAttach(Context context) {
        mcontext = context;
        super.onAttach(context);
        if (context instanceof onLoginInterface) {
            mListener = (onLoginInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterListenerInterface");
        }
    }


    private void writeSharedPref(final User user, final boolean shouldStayLogin) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FB_KEY_CF).child(mAuth.getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User value = dataSnapshot.getValue(User.class);
                String role;
                if (value != null && value.getFirebaseUserId() != null) {
                    role = FB_KEY_CF;
                } else {
                    role = FB_KEY_USERS;
                }
                SharedPreferences.Editor editor = mcontext.getSharedPreferences(LoginRegistrationActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(LoginRegistrationActivity.KEY_PASSWORD, user.getPassword());
                editor.putString(LoginRegistrationActivity.KEY_EMAILE, user.getEmail());
                editor.putBoolean(LoginRegistrationActivity.KEY_STAY_LOGIN, shouldStayLogin);
                editor.putString(LoginRegistrationActivity.KEY_ROLE, role);

                Log.d("myUser-info", "login // name: " + user.getName() + "/" + role);
                editor.apply();

                onButtonLogin(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("value", "Failed to read value.", error.toException());
            }
        });


    }


    void loginToFirebase(final String email, final String password, final boolean shouldStayLogin) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser firebaseUser = null;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signin", "signInWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();


                            if (checkIfEmailIsVerified(firebaseUser)) {
                                /////
                                User userobj = new User();
                                userobj.setEmail(email);
                                userobj.setPassword(password);
                                userobj.setFirebaseUserId(firebaseUser.getUid());

                                writeSharedPref(userobj, shouldStayLogin);

                            } else {
                                Toast.makeText(mcontext, "Please Verfiy your email", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signin", "signInWithEmail:failure", task.getException());
                            Toast.makeText(mcontext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    private boolean checkIfEmailIsVerified(final FirebaseUser firebaseUser) {
        return firebaseUser.isEmailVerified();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

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
    public interface onLoginInterface {
        // TODO: Update argument type and name
        void onLoginClick(User user);

        void onRegisterClick();
    }
}
