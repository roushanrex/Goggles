package com.example.omsairam01.goggles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.model.SearchModel;
import com.squareup.picasso.Picasso;

public class SearchDeatail extends AppCompatActivity {
    TextView  self1,name1,price1,mobile1,pay1,unpay1,address1;
    ImageView img;
    SearchModel searchModel;
    Button unpaidbtn,readybtn,btnupdate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_detail);
        searchModel = (SearchModel) getIntent().getExtras().getSerializable("searchData");



        //  init();
        //   }

        //   private void init() {


        self1 = findViewById(R.id.self);
        name1 = findViewById(R.id.name);
        price1 = findViewById(R.id.price);
        mobile1 = findViewById(R.id.mobile);
        pay1 = findViewById(R.id.pay);
        unpay1 = findViewById(R.id.unpay);
        address1 = findViewById(R.id.address);
        unpaidbtn = (Button) findViewById(R.id.button12);
        readybtn = findViewById(R.id.button14);
        btnupdate = findViewById(R.id.button10);
        img =(ImageView)findViewById(R.id.imageView1);


        unpaidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SearchDeatail.this, "Product is ready", Toast.LENGTH_SHORT).show();

//                 Intent intent=new Intent(SearchDeatail.this,Editdata.class);
//                 startActivity(intent);
            }
        });

        readybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchDeatail.this, "Product is ready", Toast.LENGTH_SHORT).show();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchDeatail.this, "Button Update ", Toast.LENGTH_SHORT).show();

//                Intent intent=new Intent(SearchDeatail.this,Update.class);
//                startActivity(intent);
            }
        });


        self1.setText("Sales Person : " + searchModel.getSelf());
        name1.setText("Name : " + searchModel.getName());
        price1.setText("Price: " + searchModel.getPrice());
        mobile1.setText("Mobile : " + searchModel.getMobile());
        pay1.setText("Paid: " + searchModel.getPaidPrice());
        unpay1.setText("Unpaid : " + searchModel.getUnpaidAmount());
        address1.setText("Address : " + searchModel.getAddress());

        Picasso.with(getApplicationContext()).load(searchModel.getImages()).into(img);
        //     Picasso.with(this).load(searchModel.getImages()).into(img);


    }
}
