package com.example.knitsale.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knitsale.R;
import com.example.knitsale.SelectProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductGridViewCustomAdapter extends BaseAdapter {

    private ArrayList<ProductData>storeProductData ;
    private Context mContex;

    public static String SellerId = "SellerId";
    public static String ProductId= "ProductId";
    public ProductGridViewCustomAdapter( Context mContex, ArrayList<ProductData>storeProductData) {
        this.mContex = mContex;
        this.storeProductData = storeProductData;


    }

    @Override
    public int getCount() {
        return storeProductData.size();
    }

    @Override
    public Object getItem(int position) {
        return storeProductData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContex).inflate(R.layout.all_product_list_design, null);

        ImageView productImage = convertView.findViewById(R.id.imgViewAllProductListImage);
        TextView productTitle = convertView.findViewById(R.id.txtViewAllProductListTitle);
        TextView productPrice = convertView.findViewById(R.id.txtViewAllProductListPrice);

        final ProductData productData = storeProductData.get(position);

        productTitle.setText(productData.getTitle());
        if(productData.getPrice().isEmpty()){
            productPrice.setText("Rs "+"0");
        }else {
            productPrice.setText("Rs " + productData.getPrice());
        }
        Picasso.with(mContex).load(productData.getUrlproductimg1()).fit().into(productImage);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String sellerId = productData.getSellerid();
                    String productId = productData.getProductid();
                    Intent i = new Intent(mContex, SelectProductDetails.class);
                    i.putExtra(SellerId, sellerId);
                    i.putExtra(ProductId, productId);
                    mContex.startActivity(i);
            }
        });
        return convertView;
    }
}
