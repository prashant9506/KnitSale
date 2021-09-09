package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knitsale.model.ProductData;
import com.example.knitsale.model.ProductGridViewCustomAdapter;
import com.example.knitsale.model.util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ProductData> storeProductData = new ArrayList<>();

    private CircleImageView imgsellerpic;         //toolbar
    private LinearLayout linerLayoutSellerProfileBox;
    private TextView txtViewLoginSellerName;      //toolbar
    private ProgressBar mprogressBar;
    private GridView griViewAllProduct;
    private ProductGridViewCustomAdapter mproductGridViewCustomAdapter;


    private FirebaseAuth mfirebaseAuth;
    private FirebaseUser user;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;

    private ChildEventListener mchildEventListener;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkInternetConnection();

        toolbar = findViewById(R.id.customToolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        user = mfirebaseAuth.getCurrentUser();

        //clicklistener on circularImg
       linerLayoutSellerProfileBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(user == null){
                   Toast.makeText(getApplicationContext(),"you are not login",Toast.LENGTH_SHORT).show();
               }
               else{
                   startActivity(new Intent(getApplicationContext(),SellerDashboard.class));
               }
           }
       });

        util.circleShowProgressBar(mprogressBar);
        showAllProduct();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeProductData.clear();
                showAllProduct();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    public void showAllProduct(){
        mchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                util.circleHideProgressBar(mprogressBar);

                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    ProductData productData = snapshot.getValue(ProductData.class);
                    storeProductData.add(productData);
                    productData.setSellerid(dataSnapshot.getKey());
                    productData.setProductid(snapshot.getKey());
                }

                mproductGridViewCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    ProductData productData = snapshot.getValue(ProductData.class);
                    productData.setProductid(snapshot.getKey());
                    storeProductData.remove(productData);
                }

                mproductGridViewCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        };
        mdatabaseReference.addChildEventListener(mchildEventListener);
        mproductGridViewCustomAdapter = new ProductGridViewCustomAdapter(this,storeProductData);
        griViewAllProduct.setAdapter(mproductGridViewCustomAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseReference.removeEventListener(mchildEventListener);
    }

    //create a menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemForgotPassword: {
                startActivity(new Intent(getApplicationContext(), SellerForgotPassword.class));
                return true;
            }
            case R.id.itemRegistrationLogin:{
                user = mfirebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Toast.makeText(getApplicationContext(),"already login",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), SellerRegistrationSign.class));
                }
                return true;
            }
            case R.id.itemContact:{
                AlertDialog.Builder builder = new  AlertDialog.Builder(MainActivity.this);
                builder.setMessage(Html.fromHtml("If any query or suggestion, please contact us"+"<font color='#D81B60'> parashant.188606@knit.ac.in</font>")).setCancelable(true)
                        .setPositiveButton(Html.fromHtml("open gmail"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.gmail.com"));
                                startActivity(intent);
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //end of menu bar and its option


    @Override
    protected void onStart() {
        super.onStart();
        if(user == null){
            Picasso.with(getApplicationContext()).load("drawable/profile_pic").fit().into(imgsellerpic);
            txtViewLoginSellerName.setText("Buy and fill your needs");

        }else {
            if(user.getPhotoUrl() == null) {
                Picasso.with(getApplicationContext()).load("drawable/profile_pic").fit().into(imgsellerpic);
                txtViewLoginSellerName.setText("Buy and fill your needs");
            }else {
                Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).fit().into(imgsellerpic);
                txtViewLoginSellerName.setText(user.getDisplayName().toUpperCase());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user= mfirebaseAuth.getCurrentUser();
        if(user == null){
            Picasso.with(getApplicationContext()).load("drawable/profile_pic").fit().into(imgsellerpic);
            txtViewLoginSellerName.setText("Buy and fill your needs");
        }else {
            if(user.getPhotoUrl() == null) {
                Picasso.with(getApplicationContext()).load("drawable/profile_pic").fit().into(imgsellerpic);
                txtViewLoginSellerName.setText("Buy and fill your needs");
            }else {
                Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).fit().into(imgsellerpic);
                txtViewLoginSellerName.setText(user.getDisplayName().toUpperCase());
            }
        }

    }

    //check internet connection method
    public void checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Check internet connection!!!!").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    //end of checkInternetConnection method

    public void initView(){
        linerLayoutSellerProfileBox = findViewById(R.id.linearLayoutBoxCurrentUserLoginMainActivity);
        imgsellerpic = findViewById(R.id.circularImgCustomToolbar);
        txtViewLoginSellerName = findViewById(R.id.txtViewCustomToolbar);
        griViewAllProduct = findViewById(R.id.gridViewAllProductList);
        mprogressBar = findViewById(R.id.progressBarMainActivity);
        swipeRefreshLayout = findViewById(R.id.swipRefreshLayoutMainActivity);

        mfirebaseAuth = FirebaseAuth.getInstance();

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Product List");

    }
}
