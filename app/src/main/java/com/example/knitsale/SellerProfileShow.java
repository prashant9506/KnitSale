package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knitsale.model.ProfileData;
import com.example.knitsale.model.util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Util;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerProfileShow extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView email, name , mob , alternativemob , branchyear , hostelroom;
    private ProgressBar progressBarShowProfile;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    private FirebaseAuth mfirebaseAuth;

    private ValueEventListener getProfileEventListener;

    private FirebaseUser user;
    private String Uid;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile_show);

        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg =findViewById(R.id.circularImgCustomToolbar);
        toolbarCircularImg.setVisibility(View.GONE);
        toolbar.setTitle("Knit Sale");
        setSupportActionBar(toolbar);

        initValue();

        user = mfirebaseAuth.getCurrentUser();
        Uid = user.getUid();

        profileDataShow();

    }

    public void profileDataShow(){

        util.circleShowProgressBar(progressBarShowProfile);
            getProfileEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    email.setText(user.getEmail());
                    util.circleHideProgressBar(progressBarShowProfile);
                    ProfileData profileData = dataSnapshot.getValue(ProfileData.class);
                    name.setText(profileData.getName());
                    mob.setText(profileData.getMob());
                    if (profileData.getAlternativemob().isEmpty()) {
                        alternativemob.setText("----------");
                    } else {
                        alternativemob.setText(profileData.getAlternativemob());
                    }
                    branchyear.setText(profileData.getBranchyear());
                    hostelroom.setText(profileData.getHostelroom());

                    Picasso.with(getApplicationContext()).load(profileData.getProfileurl()).fit().into(profileImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mdatabaseReference.child(Uid).addValueEventListener(getProfileEventListener);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseReference.child(Uid).removeEventListener(getProfileEventListener);
    }


    public void initValue(){
        profileImage = findViewById(R.id.imgViewSellerProfileShow);
        email = findViewById(R.id.txtViewSellerEmailShow);
        name = findViewById(R.id.txtViewSellerNameShow);
        mob = findViewById(R.id.txtViewSellerMobShow);
        alternativemob = findViewById(R.id.txtViewSellerAlternativeMobShow);
        branchyear = findViewById(R.id.txtViewSellerBranchYearShow);
        hostelroom = findViewById(R.id.txtViewSellerHostelRoomShow);
        progressBarShowProfile = findViewById(R.id.progressBarProfielShow);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Registered Seller");

        mfirebaseAuth = FirebaseAuth.getInstance();

    }
}
