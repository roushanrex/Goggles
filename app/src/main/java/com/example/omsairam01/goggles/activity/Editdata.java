package com.example.omsairam01.goggles.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.config.Constant;
import com.example.omsairam01.goggles.config.VolleyMultipartRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Editdata extends AppCompatActivity {

    TextView pdtxt,invalid;
    EditText pdedt;
    Button updatebtn;
    String id2;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdata);
        pdtxt=(TextView)findViewById(R.id.textView3);
        pdedt=(EditText) findViewById(R.id.editText3);
        updatebtn=(Button)findViewById(R.id.button);
        invalid= (TextView) findViewById(R.id.textView11);



        Intent intent = getIntent();
        id2 = intent.getStringExtra("id");
//        String pd = intent.getStringExtra("paid2");
        String prc = intent.getStringExtra("price2");

        if (prc.equals("0"))
        {

            updatebtn.setVisibility(View.GONE);
            pdtxt.setVisibility(View.GONE);
            pdedt.setVisibility(View.GONE);
            invalid.setVisibility(View.VISIBLE);

        }
        else
        {
            updatebtn.setVisibility(View.VISIBLE);
            invalid.setVisibility(View.GONE);
            pdtxt.setVisibility(View.VISIBLE);
            pdedt.setVisibility(View.VISIBLE);
        }


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                hitUrlForUploadImage();

            }
        });


    }



        private void hitUrlForUploadImage() {
            {
                final ProgressDialog dialog= ProgressDialog.show(Editdata.this,"","Loading.....",false);
                VolleyMultipartRequest request= new VolleyMultipartRequest(Request.Method.POST, Constant.UPDATEPRICE, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        dialog.dismiss();
                        String parsed= "";
                        try {
                            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                            Log.d("Responseforglass",parsed);
                            Intent intent= new Intent(Editdata.this,MainActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } catch (UnsupportedEncodingException e) {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params=new HashMap<>();
                        params.put("key",Constant.KEY);

                        params.put("item_id",id2);
                        params.put("unpaid_amount",pdedt.getText().toString());

                        return params;
                    }



                };
                RequestQueue requestQueue = Volley.newRequestQueue(Editdata.this);
                requestQueue.getCache().clear();
                requestQueue.add(request);
            }

        }

    }