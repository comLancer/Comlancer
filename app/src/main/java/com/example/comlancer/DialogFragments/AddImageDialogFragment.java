package com.example.comlancer.DialogFragments;

import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.comlancer.Models.ComlancerImages;
import com.example.comlancer.Models.ImagesContainer;
import com.example.comlancer.Models.MyConstants;
import com.example.comlancer.Models.User;
import com.example.comlancer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.comlancer.Fragments.EditProfileFragment.KEY_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddImgeToRecycleViewlInterface} interface
 * to handle interaction events.
 * Use the {@link AddImageDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddImageDialogFragment extends DialogFragment {
    // TODO:  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String KEY_USER = "user";     //it must be freelancer that add image
    private static final int PICK_IMAGE = 100;
    private static final String TAG = "profile image";
    User mUser;
    DatabaseReference mRef;
    Uri mImageUri;
    ImageButton imgbtnAddImgeFromGalary;
    Button btnAddImgToRecycleView;
    EditText etTitle;
    private AddImgeToRecycleViewlInterface mListener;
    private String mParam1;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Context mContext;


    public AddImageDialogFragment() {
        // Required empty public constructor
    }

    public static AddImageDialogFragment newInstance(User user) {
        AddImageDialogFragment fragment = new AddImageDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(KEY_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_to_add_imag_dialog, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference(MyConstants.FB_KEY_CF).child(currentUser.getUid());


        Button btnCancel = parentView.findViewById(R.id.btn_cancel);
        etTitle = parentView.findViewById(R.id.et_title);
        btnAddImgToRecycleView = parentView.findViewById(R.id.btn_add_img_to_recycle_view);
        imgbtnAddImgeFromGalary = parentView.findViewById(R.id.imgbtn_add_imge_recycle_view);
        imgbtnAddImgeFromGalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });


        btnAddImgToRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ComlancerImages comlancerImages = new ComlancerImages();
                //this is to add child in database
                comlancerImages.setFirebaseUserId(currentUser.getUid());
                comlancerImages.setTitle(etTitle.getText().toString());

                uploadToStorage(comlancerImages);
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed();
            }
        });


        return parentView;
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
                imgbtnAddImgeFromGalary.setImageBitmap(selectedImage);


            } catch (FileNotFoundException e) {
                Toast.makeText(mContext, "Image not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void uploadToStorage(final ComlancerImages comlancerImages) {

        final StorageReference imgRef = mStorageRef.child(KEY_STORAGE + comlancerImages.getTitle());

        if (mImageUri != null) {
            imgRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content


                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri imageFirebaseUrl) {

                                    comlancerImages.setImageUrl(imageFirebaseUrl.toString());

                                    addNewImageToUser(comlancerImages);

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
            comlancerImages.setImageUrl("https://firebasestorage.googleapis.com/v0/b/comlancer-562c5.appspot.com/o/images%2Fsafa95alshuaili%40gmail.com?alt=media&token=5d0cce31-847f-4b57-b858-ef5108df7d65");
            addNewImageToUser(comlancerImages);
        }
    }

    private void addNewImageToUser(ComlancerImages comlancerImages) {

        if (mUser.getImagesContainer() != null) {
            mUser.getImagesContainer().getImageList().add(comlancerImages);
        } else {

            ImagesContainer ic = new ImagesContainer();
            ArrayList<ComlancerImages> images = new ArrayList<>();

            images.add(comlancerImages);
            ic.setImageList(images);

            mUser.setImagesContainer(ic);

        }


        onButtonPressed(mUser);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(User user) {
        if (mListener != null) {
            mListener.onClickAddImage(user);
//           dismiss();
        }
    }

    public void onCancelPressed() {
        if (mListener != null) {
            mListener.onCancelClick();
        }
    }


    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
        if (context instanceof AddImgeToRecycleViewlInterface) {
            mListener = (AddImgeToRecycleViewlInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddImgeToRecycleViewlInterface");
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
    public interface AddImgeToRecycleViewlInterface {
        // TODO: Update argument type and name
        void onClickAddImage(User user);

        void onCancelClick();
    }
}
