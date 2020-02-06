package com.example.comlancer.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.comlancer.Activitys.LoginRegistrationActivity;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterListenerInterface} interface
 * to handle interaction events.
 */
public class RegistrationFragment extends Fragment {

    private RegisterListenerInterface mListener;
    RadioGroup radioGroup;
    private Context mContext;


    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View parentView = inflater.inflate(R.layout.fragment_registration, container, false);
        final EditText etFullName = parentView.findViewById(R.id.et_full_name);
        final EditText etEmail = parentView.findViewById(R.id.et_title);
        final EditText etPass = parentView.findViewById(R.id.et_pass);
        final EditText etRePass = parentView.findViewById(R.id.et_rePass);


        radioGroup = parentView.findViewById(R.id.radioGroup);

        Button btnRegister = parentView.findViewById(R.id.btn_register);

        Button btnBackLogin = parentView.findViewById(R.id.btn_back);


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


                writeSharedPref(user);

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


    private void writeSharedPref(User user) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(LoginRegistrationActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(LoginRegistrationActivity.KEY_USER_NAME, user.getName());
        editor.putString(LoginRegistrationActivity.KEY_PASSWORD, user.getPassword());
        editor.putString(LoginRegistrationActivity.KEY_EMAILE, user.getEmail());

        String role;

        if (user.getRole().equalsIgnoreCase("User")) {
            role = (MyConstants.FB_KEY_USERS);
        } else {
            role = (MyConstants.FB_KEY_CF);
        }
        editor.putString(LoginRegistrationActivity.KEY_ROLE, role);
        editor.apply();
        onRegisterPressed(user);
    }

    public void onLoginPressed() {
        if (mListener != null) {
            mListener.onLoginClick();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
