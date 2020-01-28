package com.example.comlancer.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.comlancer.DialogFragments.ForgotPasswordDialogFragment;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
\
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private onLoginInterface mListener;
    private Context mcontext;
    ForgotPasswordDialogFragment mDialog;

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
        final    EditText etpass =perantView.findViewById(R.id.et_pass);
        final TextView tvForgetPassword = perantView.findViewById(R.id.tv_forget_pass);
        Button btnLogin=perantView.findViewById(R.id.btn_login);
        Button btnRegister=perantView.findViewById(R.id.btn_registration);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToFirebase(etEmail.getText().toString(),etpass.getText().toString());
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
        mcontext=context;
        super.onAttach(context);
        if (context instanceof onLoginInterface) {
            mListener = (onLoginInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterListenerInterface");
        }
    }





    void  loginToFirebase(final String email, final String password) {



        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task< AuthResult > task) {
                        FirebaseUser firebaseUser = null;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();



                            /////
                            User userobj = new User();
                            userobj.setEmail(email);
                            userobj.setPassword(password);
                            userobj.setFirebaseUserId(firebaseUser.getUid());
                            onButtonLogin(userobj);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mcontext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });}




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
