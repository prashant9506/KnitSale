package com.example.knitsale;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.knitsale.model.ViewPageAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


public class SelectImageShow extends AppCompatActivity {
    private PhotoView photoViewProductImage;
    private String urlSelectProductImage,urlSellerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_show);

        photoViewProductImage = findViewById(R.id.photoViewSelectImage);
        Toast.makeText(getApplicationContext(),"you can rotate this image",Toast.LENGTH_SHORT).show();

        urlSelectProductImage= getIntent().getStringExtra(ViewPageAdapter.selectimageurl);
        urlSellerProfile = getIntent().getStringExtra(SelectProductDetails.sellerProfileImageUrl);
            Picasso.with(getApplicationContext()).load(urlSellerProfile).into(photoViewProductImage);
            Picasso.with(getApplicationContext()).load(urlSelectProductImage).into(photoViewProductImage);
    }
}
