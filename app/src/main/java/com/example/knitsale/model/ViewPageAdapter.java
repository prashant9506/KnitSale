package com.example.knitsale.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.knitsale.SelectImageShow;
import com.example.knitsale.SelectProductDetails;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ViewPageAdapter extends PagerAdapter {

    private Context mcontext;
    private ArrayList<String> urlImage ;
    public static  String selectimageurl;

    public ViewPageAdapter(Context mcontext, ArrayList<String> urlImage) {
        this.mcontext = mcontext;
        this.urlImage = urlImage;
    }

    @Override
    public int getCount() {
        return urlImage.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(mcontext);
        Picasso.with(mcontext).load(urlImage.get(position)).fit().into(imageView);
        container.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position== 0){
                    Intent i = new Intent(mcontext, SelectImageShow.class);
                    i.putExtra(selectimageurl,urlImage.get(position));
                    mcontext.startActivity(i);
                }
                else if(position==1){
                    Intent i = new Intent(mcontext, SelectImageShow.class);
                    i.putExtra(selectimageurl,urlImage.get(position));
                    mcontext.startActivity(i);
                }
                else if(position == 2){
                    Intent i = new Intent(mcontext,SelectImageShow.class);
                    i.putExtra(selectimageurl,urlImage.get(position));
                    mcontext.startActivity(i);
                }
                else{
                    Intent i = new Intent(mcontext,SelectImageShow.class);
                    i.putExtra(selectimageurl,urlImage.get(position));
                    mcontext.startActivity(i);
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }
}
