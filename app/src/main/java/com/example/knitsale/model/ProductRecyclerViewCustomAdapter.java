package com.example.knitsale.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.knitsale.R;
import com.example.knitsale.SellerProductList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRecyclerViewCustomAdapter extends RecyclerView.Adapter<ProductRecyclerViewCustomAdapter.MyVIewHolder> {
    private ArrayList<ProductData>storeProductData ;
    private Context mContex;

    public ProductRecyclerViewCustomAdapter(Context mContex, ArrayList<ProductData>storeProductData) {
        this.mContex = mContex;
        this.storeProductData = storeProductData;

    }


    @NonNull
    @Override
    public MyVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContex).inflate(R.layout.seller_product_list_design,parent,false);
        return new MyVIewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVIewHolder holder, int position) {
        final ProductData productData = storeProductData.get(position);

        holder.productTitle.setText(productData.getTitle());
        if(productData.getPrice().isEmpty()){
            holder.productPrice.setText("Rs "+"0");
        }else {
            holder.productPrice.setText("Rs " + productData.getPrice());
        }
        if(productData.getDescription().isEmpty()){
            holder.productDescription.setVisibility(View.INVISIBLE);
        }else{
            holder.productDescription.setText(productData.getDescription());
        }
        Picasso.with(mContex).load(productData.getUrlproductimg1()).fit().into(holder.imgViewProduct1);
        Picasso.with(mContex).load(productData.getUrlproductimg2()).fit().into(holder.imgViewProduct2);
        Picasso.with(mContex).load(productData.getUrlproductimg3()).fit().into(holder.imgViewProduct3);
        Picasso.with(mContex).load(productData.getUrlproductimg4()).fit().into(holder.imgViewProduct4);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
                builder.setTitle(Html.fromHtml("Do you want to delete the product?")).setCancelable(true)
                        .setPositiveButton(Html.fromHtml("Delete"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String productId = productData.getProductid();
                                String userId = SellerProductList.user.getUid();
                                Task<Void> remove= util.removeProduct(userId,productId);
                                remove.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContex,"product delete",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContex,e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeProductData.size();
    }

    public class MyVIewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, productDescription;
        ImageView imgViewProduct1, imgViewProduct2, imgViewProduct3, imgViewProduct4;
        CardView cardView;
        public MyVIewHolder(View itemVIew){
            super(itemVIew);
            productTitle = itemVIew.findViewById(R.id.txtViewSellerProductListTitle);
            productPrice = itemVIew.findViewById(R.id.txtViewSellerProductListPrice);
            productDescription = itemVIew.findViewById(R.id.txtViewSellerProductDescription);
            imgViewProduct1 = itemVIew.findViewById(R.id.imgViewSellerProductListDesign1);
            imgViewProduct2 = itemVIew.findViewById(R.id.imgViewSellerProductListDesign2);
            imgViewProduct3 = itemVIew.findViewById(R.id.imgViewSellerProductListDesign3);
            imgViewProduct4 = itemVIew.findViewById(R.id.imgViewSellerProductListDesign4);
            cardView = itemVIew.findViewById(R.id.cardViewSellerProductList);
        }
    }
}
