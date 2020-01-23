package com.example.comlancer.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.comlancer.R;
import com.example.comlancer.Models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterListenerInterface} interface
 * to handle interaction events.
 */
public class RegistrationFragment extends Fragment {

    private RegisterListenerInterface mListener;
    RadioGroup radioGroup;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View parentView = inflater.inflate(R.layout.fragment_registration, container, false);
        final EditText etFullName = parentView.findViewById(R.id.et_full_name);
        final EditText etEmail = parentView.findViewById(R.id.et_email);
        final EditText etPass = parentView.findViewById(R.id.et_pass);
        final EditText etRePass = parentView.findViewById(R.id.et_rePass);


        radioGroup = parentView.findViewById(R.id.radioGroup);

        Button btnRegister = parentView.findViewById(R.id.btn_register);
        //Button btnLogin = parentView.findViewById(R.id.btn_login);
        Button btnBackLogin = parentView.findViewById(R.id.btn_back_login);




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = getRole(parentView);
                User user = new User();
                user.setName(etFullName.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setPassword(etPass.getText().toString());
                user.setPassword(etRePass.getText().toString());
                user.setRole(role);

                onRegisterPressed(user);


            }
        });

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginPressed();

            }
        });

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginPressed();
            }
        });


        return parentView;
    }

    private String getRole(View parentView) {

        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioButton = parentView.findViewById(selectedId);

        return (String) radioButton.getText();

    }



    public void onRegisterPressed(User user) {
        if (mListener != null) {
            mListener.onRegisterClick(user);
        }
    }

    public void onLoginPressed() {
        if (mListener != null) {
            mListener.onLoginClick();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterListenerInterface) {
            mListener = (RegisterListenerInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterListenerInterface");
        }
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
    public interface RegisterListenerInterface {
        // TODO: Update argument type and name
        void onRegisterClick(User user);

        void onLoginClick();
    }
}
