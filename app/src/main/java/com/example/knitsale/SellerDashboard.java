package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerDashboard extends AppCompatActivity {

    private CardView cardViewProductUpload, cardViewProductShow, cardViewProfileUpdate, cardViewProfileShow;
    private FirebaseUser user;
    private FirebaseAuth mfirebaseAuth;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);


        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg = findViewById(R.id.circularImgCustomToolbar);
        toolbar.setTitle("Welcome Seller");
        toolbarCircularImg.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        initValue();
        user = FirebaseAuth.getInstance().getCurrentUser();

        cardViewProductUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getPhotoUrl() == null) {
                    checkProfileUpdateDialog();

                } else {

                    startActivity(new Intent(getApplicationContext(), SellerProductUpload.class));
                }
            }
        });

        cardViewProductShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerProductList.class);
                startActivity(i);
            }
        });

        cardViewProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SellerProfileUpdate.class));
            }
        });

        cardViewProfileShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getPhotoUrl() == null) {
                    checkProfileUpdateDialog();

                } else {

                    startActivity(new Intent(getApplicationContext(), SellerProfileShow.class));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sign_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSignout: {
                mfirebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            }
            case R.id.itemPolicy:{
                policyAlertDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkProfileUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SellerDashboard.this);
        builder.setTitle(Html.fromHtml("<font color='#00bfff'>Firstly, please update your profile</font>")).setCancelable(true)
                // builder.setMessage("Are you sure want to exit").setCancelable(false)
                .setPositiveButton(Html.fromHtml("<font color='#00bfff'>Profile Update</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(SellerDashboard.this, SellerProfileUpdate.class));

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void policyAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SellerDashboard.this);
        builder.setTitle(Html.fromHtml("<font color='#00bfff'>Protocol and Policy, please follow it</font>"))
                .setMessage(Html.fromHtml("1. If you are not a student of Knit Sultanpur College and you are registered then in this condition, your account will be deleted permanently.")
                +"\n\n"+ Html.fromHtml("2. If you post (product upload) wrong and dirty then your account will be disable for sometime and you will get warning through KnitSale, even if you post wrong then your account will be deleted permanently.")
                +"\n\n"+Html.fromHtml("3. Once the product are sold, remove the product."))
                .setCancelable(false)
                .setPositiveButton(Html.fromHtml("Read"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void initValue() {
        cardViewProfileUpdate = findViewById(R.id.cardViewProfileUpdateActivity);
        cardViewProfileShow = findViewById(R.id.cardVIewProfileShowActivity);
        cardViewProductUpload = findViewById(R.id.cardViewProductUploadActivity);
        cardViewProductShow = findViewById(R.id.cardViewProductListActivity);

        mfirebaseAuth = FirebaseAuth.getInstance();
    }


}
