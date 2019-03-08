package com.example.omsairam01.goggles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.omsairam01.goggles.activity.MainActivity;
import com.squareup.picasso.Picasso;

public class Imageactivity extends AppCompatActivity {


    ImageView imageView;
    String god;
    Bitmap  bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageactivity);
//        getSupportActionBar().hide();

     //   Image=getIntent().getStringExtra("abc");
       god=getIntent().getStringExtra("abc");

//        Log.d("ghj",getIntent().getStringExtra("som"));
   //     Log.d("MBItmap",getIntent().getStringExtra("abc"));
        imageView=findViewById(R.id.imgzom);
        Picasso.with(getApplicationContext()).load(String.valueOf(god)).into(imageView);
        //  imageView.setImageResource(Integer.parseInt(Image));
/*
        imageView.setImageResource(Integer.parseInt(Image));
*/
//        imageView.setImageBitmap(bitmap);









    }
}
