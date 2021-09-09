package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.knitsale.model.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerRegistrationSign extends AppCompatActivity {

    private TextInputLayout txtEmail , txtPassword;
    private Button btnRegistration , btnLogin;
    private ProgressDialog mprogressDialog;

    private FirebaseAuth mfirebaseAuth;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_sign);

        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg =findViewById(R.id.circularImgCustomToolbar);
        toolbarCircularImg.setVisibility(View.GONE);
        toolbar.setTitle("Knit Sale");
        setSupportActionBar(toolbar);

        initValues();           // get reference all widget,FirebaseAuth.

        sellerRegistration();   //call sellerRegistration method

        sellerLogin();          //call sellerLogin method
    }

    //seller registration method
    public void sellerRegistration(){
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnLogin.setVisibility(View.GONE);

                if(!util.validEmail(txtEmail) | !util.validPswd(txtPassword)){
                    return;
                }

                String email = txtEmail.getEditText().getText().toString().toLowerCase().trim();
                String password = txtPassword.getEditText().getText().toString().trim();

                progressDialogBoxShow("Registration, please wait......");   //show progressDialogBox like ProgressBar

                mfirebaseAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mprogressDialog.dismiss();   //hide progressDialogBOx
                            Toast.makeText(SellerRegistrationSign.this,"Registration Successfully",Toast.LENGTH_LONG).show();
                            btnLogin.setVisibility(View.VISIBLE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mprogressDialog.dismiss();
                        Toast.makeText(SellerRegistrationSign.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    //end of sellerRegistration method

    //seller login method
    public void sellerLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnRegistration.setVisibility(View.GONE);

                if(!util.validEmail(txtEmail) | !util.validPswd(txtPassword)){
                    return;
                }

                String email = txtEmail.getEditText().getText().toString().toLowerCase().trim();
                String password = txtPassword.getEditText().getText().toString().trim();

                progressDialogBoxShow("Login, please wait.....");   //show progressDialogBox

                mfirebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            mprogressDialog.dismiss();       //hide progressDialogBox
                            Toast.makeText(SellerRegistrationSign.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                            startActivity( new Intent(SellerRegistrationSign.this,SellerDashboard.class));
                            finish();   //for when return d'nt show this activity
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mprogressDialog.dismiss();
                        btnRegistration.setVisibility(View.VISIBLE);
                        Toast.makeText(SellerRegistrationSign.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    //end of sellerLogin method


    public void progressDialogBoxShow(String message){
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage(message);
        mprogressDialog.setCancelable(false);
        mprogressDialog.show();
    }
    public void initValues(){
        txtEmail = findViewById(R.id.txtInputSellerEmail);
        txtPassword = findViewById(R.id.txtInputSellerPassword);
        btnRegistration = findViewById(R.id.btnSellerRegistration);
        btnLogin = findViewById(R.id.btnSellerLogin);

        mfirebaseAuth = FirebaseAuth.getInstance();
    }
}
