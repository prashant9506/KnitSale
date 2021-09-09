package com.example.knitsale.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.knitsale.R;
import com.example.knitsale.SellerForgotPassword;
import com.example.knitsale.SellerProductUpload;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;



public class util {

    public static boolean validEmail(TextInputLayout txtEmail){
        String email =txtEmail.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            txtEmail.setError("please enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("please enter valid email");
            return false;
        }
        else
            txtEmail.setError(null);
            return true;
    }

    public static boolean validPswd(TextInputLayout txtPswd){
        String pswd = txtPswd.getEditText().getText().toString().trim();
        if(pswd.isEmpty()){
            txtPswd.setError("please enter password");
            return false;
        }
        else if(pswd.length()< 6 || pswd.length() > 8 ){
            txtPswd.setError("password length min 6 and max 8");
            return false;
        }
        else
            txtPswd.setError(null);
            return true;
    }

    public static boolean validMobile(TextInputLayout txtMob){
//        Integer mobno = Integer.valueOf(txtMob.getEditText().getText().toString().trim());
        String mobno = txtMob.getEditText().getText().toString().trim();

        if(mobno.isEmpty()){
            txtMob.setError("please enter mobile no.");
            return false;
        }
        else if(mobno.length() < 10 || mobno.length() > 10 || !Patterns.PHONE.matcher(mobno).matches()){
            txtMob.setError("please enter valid mobile no.");
            return false;
        }

        else
            txtMob.setError(null);
            return true;
    }

    public static boolean checkTextInputEmpty(TextInputLayout txtInput){
        String txt = txtInput.getEditText().getText().toString().trim();
        if(txt.isEmpty()){
            return true;
        }
        else {
            txtInput.setError(null);
            return false;
        }
    }

    public static void clearField (TextInputLayout textInput){
        textInput.getEditText().setText(null);
    }

    public static void circleShowProgressBar(ProgressBar progressBar){
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void circleHideProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(View.GONE);
    }

    public static Task<Void> removeProduct(String userId, String productId) {


        Task<Void> task = FirebaseDatabase.getInstance().getReference().child("Product List").child(userId).child(productId).setValue(null);
        return task;
    }



}
