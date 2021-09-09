package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knitsale.model.ProductData;
import com.example.knitsale.model.ProductGridViewCustomAdapter;
import com.example.knitsale.model.ProfileData;
import com.example.knitsale.model.ViewPageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectProductDetails extends AppCompatActivity {

    private TextView productTitle, productPrice, productDescription, sellerName, sellerEmail, mob, alternativeMob, branchYear, hostelRoom;
    private ViewPager viewPagerShowSelectProductImage;
    private ViewPageAdapter mviewPageAdapter;
    private CircleImageView imgSellerProfile;
    private LinearLayout sliderDotsPannel, productDescriptionLayout;
    private int dotcount;
    private ImageView[] dots;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReferenceRegisterSeller, mdatabaseReferenceProductList;
    private ValueEventListener mvalueListenearSellerRegistered, mvalueListenearProductList;

    public static String sellerid, productid, sellerProfileImageUrl;
    private ArrayList<String>urlImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product_details);

        initValue();
        productDataShow();
        sellerDataShow();

    }

    public void productDataShow(){
        mvalueListenearProductList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ProductData productData = dataSnapshot.getValue(ProductData.class);
                productTitle.setText(productData.getTitle());
                if(productData.getPrice().isEmpty()){
                    productPrice.setText("Rs "+"0");
                }else {
                    productPrice.setText("Rs " + productData.getPrice());
                }
                if(productData.getDescription().isEmpty()){
                    productDescriptionLayout.setVisibility(View.INVISIBLE);
                }else{
                    productDescription.setText(productData.getDescription());
                }
                urlImage.add(productData.getUrlproductimg1());
                urlImage.add(productData.getUrlproductimg2());
                urlImage.add(productData.getUrlproductimg3());
                urlImage.add(productData.getUrlproductimg4());
                mviewPageAdapter = new ViewPageAdapter(SelectProductDetails.this,urlImage);
                viewPagerShowSelectProductImage.setAdapter(mviewPageAdapter);           //viewPager for sliding image
                sliderDots();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        };
        mdatabaseReferenceProductList.child(sellerid).child(productid).addValueEventListener(mvalueListenearProductList);
    }

    public void sellerDataShow(){

        mvalueListenearSellerRegistered = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ProfileData profileData = dataSnapshot.getValue(ProfileData.class);
                if(profileData == null) {
                    Picasso.with(getApplicationContext()).load("drawable/profile_pic").fit().into(imgSellerProfile);
                }
                else{
                    sellerName.setText(profileData.getName());
                    sellerEmail.setText(profileData.getEmail());
                    mob.setText(profileData.getMob());
                    alternativeMob.setText(profileData.getAlternativemob());
                    branchYear.setText(profileData.getBranchyear());
                    hostelRoom.setText(profileData.getHostelroom());
                    Picasso.with(getApplicationContext()).load(profileData.getProfileurl()).fit().into(imgSellerProfile);
                    imgSellerProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(),SelectImageShow.class);
                            i.putExtra(sellerProfileImageUrl,profileData.getProfileurl());
                            startActivity(i);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        };
        mdatabaseReferenceRegisterSeller.child(sellerid).addValueEventListener(mvalueListenearSellerRegistered);
    }

    public void sliderDots(){
        dotcount = mviewPageAdapter.getCount();
        dots = new ImageView[dotcount];

        for(int i = 0; i < dotcount; i++){
            dots[i] = new ImageView(getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsPannel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPagerShowSelectProductImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i < dotcount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseReferenceProductList.child(sellerid).child(productid).removeEventListener(mvalueListenearProductList);
        mdatabaseReferenceRegisterSeller.child(sellerid).removeEventListener(mvalueListenearSellerRegistered);
    }

    public void initValue(){
        viewPagerShowSelectProductImage = findViewById(R.id.viewPagerSelectproduct);
        productTitle = findViewById(R.id.txtViewSelectProductTitle);
        productPrice = findViewById(R.id.txtViewSelectProductPrice);
        productDescription = findViewById(R.id.txtViewSelectProductDescription);
        sellerName = findViewById(R.id.txtViewSelectProductSellerName);
        sellerEmail = findViewById(R.id.txtViewSelectProductSellerEmail);
        mob = findViewById(R.id.txtViewSelectProductSellerMob);
        alternativeMob = findViewById(R.id.txtViewSelectProductSellerAltMob);
        branchYear = findViewById(R.id.txtViewSelectProductSellerBranchYear);
        hostelRoom = findViewById(R.id.txtViewSelectProductSellerHostelRoom);
        imgSellerProfile = findViewById(R.id.imgViewSelectProductSellerProfile);
        sliderDotsPannel = findViewById(R.id.linearLayourSliderDots);
        productDescriptionLayout = findViewById(R.id.linearLayoutSelectProductDescription);

        sellerid = getIntent().getStringExtra(ProductGridViewCustomAdapter.SellerId);
        productid = getIntent().getStringExtra(ProductGridViewCustomAdapter.ProductId);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReferenceRegisterSeller = mfirebaseDatabase.getReference("Registered Seller");
        mdatabaseReferenceProductList = mfirebaseDatabase.getReference("Product List");


    }
}
