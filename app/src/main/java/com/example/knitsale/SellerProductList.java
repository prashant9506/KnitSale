package com.example.knitsale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knitsale.model.ProductData;
import com.example.knitsale.model.ProductGridViewCustomAdapter;
import com.example.knitsale.model.ProductRecyclerViewCustomAdapter;
import com.example.knitsale.model.util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerProductList extends AppCompatActivity {

    private ArrayList<ProductData> storeProductData = new ArrayList<>();
    private TextView txtViewShowMessage;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;
    public static FirebaseUser user ;

    private ProgressBar mprogressBar;
    private RecyclerView productListRecyclerView;
    private ProductRecyclerViewCustomAdapter productCustomAdapter;

    private ChildEventListener mchildEventListener;

    private Toolbar toolbar;
    private CircleImageView toolbarCircularImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_list);

        toolbar = findViewById(R.id.customToolbar);
        toolbarCircularImg =findViewById(R.id.circularImgCustomToolbar);
        toolbarCircularImg.setVisibility(View.GONE);
        toolbar.setTitle("Knit Sale");
        setSupportActionBar(toolbar);

        txtViewShowMessage = findViewById(R.id.txtViewdatanotfound);
        productListRecyclerView = findViewById(R.id.recyclerViewSellerProduct);
        mprogressBar = findViewById(R.id.progressBarProductList);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("Product List");
        user = FirebaseAuth.getInstance().getCurrentUser();

        util.circleShowProgressBar(mprogressBar);

            productShow();
    }


    public void productShow(){

        mchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                util.circleHideProgressBar(mprogressBar);
                txtViewShowMessage.setVisibility(View.GONE);

                ProductData productData = dataSnapshot.getValue(ProductData.class);
                storeProductData.add(productData);
                productData.setProductid(dataSnapshot.getKey()); //for get onlyproductId in sellerProductList for delete product (use in ProductRecyclerViewCustomAdapter)

                productListRecyclerView.smoothScrollToPosition(productCustomAdapter.getItemCount());
                productCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                ProductData productData = dataSnapshot.getValue(ProductData.class);
                productData.setProductid(dataSnapshot.getKey());
                storeProductData.remove(productData);
                productCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mdatabaseReference.child(user.getUid()).addChildEventListener(mchildEventListener);
        productCustomAdapter = new ProductRecyclerViewCustomAdapter(this,storeProductData);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListRecyclerView.setAdapter(productCustomAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mdatabaseReference.child(user.getUid()).removeEventListener(mchildEventListener);
    }
}
