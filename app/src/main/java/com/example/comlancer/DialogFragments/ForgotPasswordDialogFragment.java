package com.example.comlancer.DialogFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comlancer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class ForgotPasswordDialogFragment extends DialogFragment {

   // private profileInterface mListener;

    Context mContext;

    public ForgotPasswordDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_forgot_password_dialog, container, false);


        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final EditText etEmail = parentView.findViewById(R.id.et_email_login);

        Button btnSend = parentView.findViewById(R.id.btn_send_login);
        Button btnCancel = parentView.findViewById(R.id.btn_cancel);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = etEmail.getText().toString();
                if(TextUtils.isEmpty(userEmail)){
                    etEmail.setError("Can't be empty..");
                    return;
                }else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                dismiss();
                            }
                            Toast.makeText(mContext, "Email Not Found", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        return parentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onClick(uri);
        }
    }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
   /*     if (context instanceof profileInterface) {
            mListener = (profileInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement profileInterface");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
/*      public interface profileInterface {
        // TODO: Update argument type and name
        void onClick(Uri uri);
    }*/
}
