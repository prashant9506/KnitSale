package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knitsale.model.ProfileData;
import com.example.knitsale.model.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerProfileUpdate extends AppCompatActivity {

    //widget
    private TextInputLayout sellerName , sellerMob , sellerAlternativeMob , sellerBranchYear , sellerHostelRoom;
    private Button btnProfileUpdate;
    private ImageView imgViewProfile;
    private ProgressDialog mprogressDialog;

    //Firebase Datatype
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    private FirebaseAuth mfirebaseAuth;


    private FirebaseStorage mfirebaseStorage;
    private StorageReference mstorageReference;

    //request permission for image choose
    private static  final  int STORAGE_PERMISSION_CODE = 100;
    private boolean mGranted;

    private String name , mob , alternativemob , branchyear , hostelroom;
    private Uri uriProfileImage;
    private String urlCurrentProfileImage;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile_update);

        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg =findViewById(R.id.circularImgCustomToolbar);
        toolbarCircularImg.setVisibility(View.GONE);
        toolbar.setTitle("Knit Sale");
        setSupportActionBar(toolbar);

        initValue();

//        clicklistener on imageview for choose image
        imgViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooserAndUpload();
            }
        });

        //clicklistener on button for save all data
        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImageToFirebaseStorageAndData();      //call uploaProfileImageToFirebaseStorageAndData method which save all seller data and image save in storage,auth and database of firebase
            }
        });


    }

    //choose image and show on imageView and upload to firebse storage (start )
    public void showImageChooserAndUpload() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!mGranted){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                    return;
                }
            }
        }
        //image select through cropImage builtin ()
        CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

    }

    //request for permission to access image in external image (override method of showImageChooser in if (Build.Version) condition)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE && grantResults .length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission Granted , now choose image", Toast.LENGTH_SHORT).show();
                mGranted=true;
            }else{
                Toast.makeText(getApplicationContext(),"please allow permission",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //end override method

    //image show on imageVIew
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uriProfileImage = result.getUri();
                Picasso.with(getApplicationContext()).load(uriProfileImage).fit().into(imgViewProfile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    //upload image to firebase storage (uid/Profile/pic.jpg)
    public void uploadProfileImageToFirebaseStorageAndData(){

        if (util.checkTextInputEmpty(sellerName) == true) {
            sellerName.setError("please enter name");
        }
        if (util.checkTextInputEmpty(sellerBranchYear) == true) {
            sellerBranchYear.setError("please enter branch and year");
        }
        if (util.checkTextInputEmpty(sellerHostelRoom) == true) {
            sellerHostelRoom.setError("please enter hostel and room no");
        }
        if (!util.validMobile(sellerMob)) {
            return;
        }
        if(uriProfileImage == null){
            Toast.makeText(getApplicationContext(),"please choose profile pic",Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialogBoxShow();    //show progressDialogBox
            final FirebaseUser user = mfirebaseAuth.getCurrentUser();
            final StorageReference profileImageRef = mstorageReference.child(user.getUid()).child("profile/" + System.currentTimeMillis() + uriProfileImage.getLastPathSegment());
            if (uriProfileImage != null) {

                profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                        Toast.makeText(getApplicationContext(), "profile pic upload successfully", Toast.LENGTH_SHORT).show();
                        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlCurrentProfileImage = uri.toString();   //getDownloadURL which image saved in storage

                                saveSellerNameAndProfilePic();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mprogressDialog.dismiss();  //hide progressDialogBox
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
    //end of uploadProfileImageToFirebaseStorage

    //name and profilepic save in firebase auth
    public void saveSellerNameAndProfilePic(){
        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        if(user!=null) {
            if (uriProfileImage != null){
                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(sellerName.getEditText().getText().toString().trim()).setPhotoUri(Uri.parse(urlCurrentProfileImage)).build();
                user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(),"profile pic url save in auth",Toast.LENGTH_SHORT).show();
                        profileDataUpload();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mprogressDialog.dismiss();  //hide progressDialogBox
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    //end of saveSellerNameAndProfilePic

    //seller all information save in firebase realtimedatabase
    public void profileDataUpload(){

        name = sellerName.getEditText().getText().toString().trim();
        mob = sellerMob.getEditText().getText().toString().trim();
        alternativemob = sellerAlternativeMob.getEditText().getText().toString().trim();
        branchyear = sellerBranchYear.getEditText().getText().toString().trim();
        hostelroom = sellerHostelRoom.getEditText().getText().toString().trim();

        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        ProfileData profileData = new ProfileData(user.getEmail(),name, mob, alternativemob, branchyear, hostelroom, urlCurrentProfileImage);
        mdatabaseReference.child(user.getUid()).setValue(profileData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mprogressDialog.dismiss();  //hide progressDialogBox
                    Toast.makeText(getApplicationContext(), "Profile upload successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplication(),SellerProfileShow.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                mprogressDialog.dismiss();  //hide progressDialogBox
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //end of profileDataUpload method

    public void progressDialogBoxShow(){
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage("Profile is updating, please wait.....");
        mprogressDialog.setCancelable(false);
        mprogressDialog.show();
    }

    public void initValue(){
        sellerName = findViewById(R.id.txtInputSellerName);
        sellerMob = findViewById(R.id.txtInputSellerMobile);
        sellerAlternativeMob = findViewById(R.id.txtInputSellerAlternativeMobile);
        sellerBranchYear = findViewById(R.id.txtInputSellerBranchYear);
        sellerHostelRoom = findViewById(R.id.txtInputSellerHostelRoom);
        imgViewProfile = findViewById(R.id.imgViewSellerProfile);
        btnProfileUpdate = findViewById(R.id.btnSellerProfileUpdate);


        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Registered Seller");

        mfirebaseAuth = FirebaseAuth.getInstance();


        mfirebaseStorage = FirebaseStorage.getInstance();
        mstorageReference = mfirebaseStorage.getReference();
    }


}
