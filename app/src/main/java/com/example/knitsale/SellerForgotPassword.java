package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.knitsale.model.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerForgotPassword extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private Button btnPasswordReset;
    private ProgressBar mprogressBar;

    private FirebaseAuth mfirebaseAuth;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_forgot_password);

        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg =findViewById(R.id.circularImgCustomToolbar);
        toolbarCircularImg.setVisibility(View.GONE);
        toolbar.setTitle("Knit Sale");
        setSupportActionBar(toolbar);


        textInputEmail = findViewById(R.id.txtInputSellerEmailForResetPswd);
        btnPasswordReset = findViewById(R.id.btnSellerSendPasswordLink);
        mprogressBar = findViewById(R.id.progressBarForgotPassword);

        mfirebaseAuth = FirebaseAuth.getInstance();



        btnPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  resetPassword();
            }
        });

    }

    public void resetPassword(){
        mfirebaseAuth = FirebaseAuth.getInstance();
        if(!util.validEmail(textInputEmail)){
            return;
        }
            util.circleShowProgressBar(mprogressBar);
            String email =textInputEmail.getEditText().getText().toString().trim();
            mfirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        util.circleHideProgressBar(mprogressBar);
                        Toast.makeText(getApplicationContext(),"please check your email account",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    util.circleHideProgressBar(mprogressBar);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
}
