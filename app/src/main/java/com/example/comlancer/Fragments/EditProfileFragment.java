package com.example.comlancer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileInterface} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    public static final String KEY_STORAGE = "images/";
    public static final String KEY_USER = "images/";
    private static final int PICK_IMAGE = 100;
    private static final String TAG = "add-post";
    ImageButton ibAddImg;
    EditText etImgUrl;
    Uri mImageUri;
    DatabaseReference mRef;
    EditText etName;
    EditText etEmail;
    EditText etTag;
    EditText etInfo;
    private User mUser;



    // TODO: Rename and change types of parameters

    private Context mContext;
    private EditProfileInterface mListener;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * 2.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(User user) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditProfileFragment newInstance(int index) {
        EditProfileFragment f = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mUser = (User) getArguments().getSerializable(KEY_USER);

        }
    }





/*
    public void EditProfileFirebase() {


        etName.setText(mUser.getName());
        etTag.setText(mUser.getTag());
        etInfo.setText(mUser.getInfo());
        etEmail.setText(mUser.getEmail());
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View parentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference(MyConstants.FB_KEY_CF).child(currentUser.getUid());


        ibAddImg = parentView.findViewById(R.id.ib_img_view);
        etName = parentView.findViewById(R.id.et_name);
        etTag = parentView.findViewById(R.id.et_tag);
        etEmail = parentView.findViewById(R.id.et_email);

        etInfo = parentView.findViewById(R.id.et_title);


        etImgUrl = parentView.findViewById(R.id.et_profile_img);


        writeData();


        Button btnEditProfile = parentView.findViewById(R.id.btn_add);
        btnEditProfile.setText("edit");
        Button btnCancel = parentView.findViewById(R.id.btn_cancel);

        //EditProfileFirebase();
        ibAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeImage();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed();
            }
        });


        btnEditProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = mUser;
                        //this is to add child in database
                        user.setFirebaseUserId(currentUser.getUid());

                        user.setName(etName.getText().toString());
                        user.setImageUrl(etImgUrl.getText().toString());
                        user.setInfo(etInfo.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        user.setTag(etTag.getText().toString());


                        uploadToStorage(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });


        return parentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCancelPressed() {
        if (mListener != null) {
            mListener.onCancelClick();
        }
    }


    public void onEditPressed(User user) {
        if (mListener != null) {
            mListener.onClickEditProfileFormEditFragment(user);
        }
    }


    public void changeImage() {
        Intent intentObj = new Intent();
        intentObj.setType("image/*");
        intentObj.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentObj, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            //TODO: action
            try {

                mImageUri = data.getData();
                Log.d("img-uri", mImageUri.toString());
                Log.d("img-uri-path", mImageUri.getPath());

                InputStream imageStream = mContext.getContentResolver().openInputStream(mImageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ibAddImg.setImageBitmap(selectedImage);


            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void uploadToStorage(final User u) {

        final StorageReference imgRef = mStorageRef.child(KEY_STORAGE + u.getEmail());

        if (mImageUri != null) {
            imgRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content


                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri imageFirebaseUrl) {

                                    u.setImageUrl(imageFirebaseUrl.toString());
                                    onEditPressed(u);

                                    Log.d("imageUrl", imageFirebaseUrl.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Log.d("failToUpload", exception.toString());
                        }
                    });

        } else {
            onEditPressed(u);
        }
    }


    private void writeData() {

        etName.setText(mUser.getName());
        etEmail.setText(mUser.getEmail());
        etTag.setText(mUser.getTag());
        etInfo.setText(mUser.getInfo());
        Glide.with(mContext).load(mUser.getImageUrl()).placeholder(R.drawable.ic_add_a_photo).into(ibAddImg);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof EditProfileInterface) {
            mListener = (EditProfileInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement profileInterface");
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
    public interface EditProfileInterface {
        // TODO: Update argument type and name
        void onClickEditProfileFormEditFragment(User user);

        void onCancelClick();
    }
}
