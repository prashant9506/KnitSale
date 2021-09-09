package com.example.knitsale;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.knitsale.model.ProductData;
import com.example.knitsale.model.ProductNotification;
import com.example.knitsale.model.util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerProductUpload extends AppCompatActivity {

    private TextInputLayout productTitle , productPrice, productDescription;
    private Button btnProductUpload , btnChooseImage;
    private ProgressDialog mprogressDialog;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    private FirebaseStorage mfirebaseStorage;
    private StorageReference mstorageReference;

    private String title , price, description, productimage1, productimage2 , productimage3 ,productimage4;
    private FirebaseUser user;

    private static final int PICK_IMAGE = 1;
    private ArrayList<Uri> imageListUri = new ArrayList<Uri>();
    private Uri uriImageProduct;
    private ArrayList<String>UrlCurrentProductImage = new ArrayList<String>();
    private int upload_count = 0;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_upload);

        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg =findViewById(R.id.circularImgCustomToolbar);
        toolbarCircularImg.setVisibility(View.GONE);
        toolbar.setTitle("Knit Sale");
        setSupportActionBar(toolbar);

        initValue();

        user = FirebaseAuth.getInstance().getCurrentUser();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProductImage();
            }
        });

        btnProductUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productImageSaveInStorageAndData();
            }
        });

    }

    public void chooseProductImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getClipData() != null){
            int countClipData = data.getClipData().getItemCount();
            int currentImageSelect = 0 ;
            while(currentImageSelect < countClipData){
                if(currentImageSelect <= 4) {
                    uriImageProduct = data.getClipData().getItemAt(currentImageSelect).getUri();
                    imageListUri.add(uriImageProduct);
                    currentImageSelect = currentImageSelect + 1;
                }
            }
            btnChooseImage.setText("you have selected "+currentImageSelect+" images");
            btnChooseImage.setTextColor(Color.BLUE);
        }
        else{

            Toast.makeText(getApplicationContext(),"please select multiple image",Toast.LENGTH_LONG).show();
        }
    }

    public void productImageSaveInStorageAndData() {

        if(imageListUri.isEmpty()){
            Toast.makeText(getApplicationContext(),"please choose product image",Toast.LENGTH_SHORT).show();
        }
        if (util.checkTextInputEmpty(productTitle) == true) {
            productTitle.setError("please enter product title");
        }

        else if(!imageListUri.isEmpty()){
                progressDialogBoxShow();
            for (upload_count = 0; upload_count < imageListUri.size(); upload_count++) {
                Uri individualImageUri = imageListUri.get(upload_count);
                final StorageReference productImage = mstorageReference.child(user.getUid()).child("product/" + System.currentTimeMillis() + individualImageUri.getLastPathSegment());
                productImage.putFile(individualImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        productImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               UrlCurrentProductImage.add(uri.toString());
                                if(UrlCurrentProductImage.size() == imageListUri.size())
                                    productDataUpload();    //call productDataUpload method when chooseimageuri and storeimageurl both size are same
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }


        }
    }

    public void productDataUpload(){

        title = productTitle.getEditText().getText().toString().trim();
        price = productPrice.getEditText().getText().toString().trim();
        description = productDescription.getEditText().getText().toString().trim();

        for(upload_count =0 ; upload_count < UrlCurrentProductImage.size(); upload_count++){
            if(upload_count == 0){
                productimage1 = UrlCurrentProductImage.get(upload_count);
            }
            else if(upload_count == 1){
                productimage2 = UrlCurrentProductImage.get(upload_count);
            }
            else if(upload_count == 2){
                productimage3 = UrlCurrentProductImage.get(upload_count);
            }
            else{
                productimage4 = UrlCurrentProductImage.get(upload_count);
            }
        }
        final ProductData productData = new ProductData(title, price, description, productimage1, productimage2, productimage3, productimage4);

        mdatabaseReference.child(user.getUid()).push().setValue(productData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    mprogressDialog.dismiss();
                    imageListUri.clear();
                    UrlCurrentProductImage.clear();//new add
                    btnChooseImage.setText("choose multiple image");
                    btnChooseImage.setTextColor(Color.BLACK);
                    util.clearField(productTitle);
                    util.clearField(productPrice);
                    Toast.makeText(getApplicationContext(),"product upload successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),SellerProductList.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void progressDialogBoxShow(){
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage("Product is uploading, please wait.....");
        mprogressDialog.setCancelable(false);
        mprogressDialog.show();
    }

    //get reference id of all widget
    public void initValue(){
        productTitle = findViewById(R.id.txtInputProductTitle);
        productPrice = findViewById(R.id.txtInputProductPrice);
        productDescription = findViewById(R.id.txtInputProductDescription);
        btnChooseImage = findViewById(R.id.btnChooseProductImage);
        btnProductUpload = findViewById(R.id.btnSellerProductUpload);

        mfirebaseStorage = FirebaseStorage.getInstance();
        mstorageReference = mfirebaseStorage.getReference();

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Product List");
    }
}
