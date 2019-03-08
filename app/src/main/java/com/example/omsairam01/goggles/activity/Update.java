package com.example.omsairam01.goggles.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.omsairam01.goggles.Imageactivity;
import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.config.Constant;
import com.example.omsairam01.goggles.config.VolleyMultipartRequest;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Update extends AppCompatActivity {


    EditText sale1, name1, mobile1, address1,price1,paid1;
    TextView unpaid1;

    int paid;
    int unPaid = 0;
    Bitmap mBitmap;
    Button button;

    private static final int CAMERA_REQUEST = 1;
    Button updatebtn;
    String id1;

    CircleImageView image;

    public Update() {
        // Required empty public constructor
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        sale1 = findViewById(R.id.editText4);
        name1 =  findViewById(R.id.editText5);
        mobile1 = findViewById(R.id.editText18);
        address1 =  findViewById(R.id.editText20);
        price1 =  findViewById(R.id.editText6);
        paid1 =  findViewById(R.id.editText22);
        unpaid1 =  findViewById(R.id.textView8);
        button =  findViewById(R.id.button6);
        updatebtn =  findViewById(R.id.updatebtn1);
        image=(CircleImageView) findViewById(R.id.profile_image);
        button=(Button)findViewById(R.id.button6);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Toast.makeText(Update.this,"Plz Choose image",Toast.LENGTH_SHORT).show();



            }
        });





        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (sale1.getText().toString().length() == 0) {
                    sale1.setError("please enter Sale");
                } else if (name1.getText().toString().length() == 0) {
                    name1.setError("please enter paid name");
                } else if (mobile1.getText().toString().length() < 10) {
                        mobile1.setError("please enter paid mobile");

                } else if (address1.getText().toString().length() == 0) {
                    address1.setError("please enter paid address");

                } else if (price1.getText().toString().length() == 0) {
                    price1.setError("please enter paid price");
                } else if (paid1.getText().toString().length() == 0) {
                    paid1.setError("please enter paid ");
                    
             }else if (mBitmap != null) {

                    hitUrlForUploadImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Intent intent = getIntent();
         id1 = intent.getStringExtra("id");
        String sl = intent.getStringExtra("sale1");
        String nm = intent.getStringExtra("name1");
        String mb = intent.getStringExtra("mobile1");
        String add = intent.getStringExtra("address1");
        String pr = intent.getStringExtra("price1");
        String pd = intent.getStringExtra("paid1");
        String unp=intent.getStringExtra("unpaid1");

        sale1.setText(sl);
        name1.setText(nm);
        mobile1.setText(mb);
        address1.setText(add);
        price1.setText(pr);
        paid1.setText(pd);
        unpaid1.setText(unp);

        paid1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("After", editable.toString());
                try {
                    if (!editable.toString().equals("")) {
                        if (Integer.parseInt(editable.toString()) <= Integer.parseInt(price1.getText().toString())) {
                            if (editable.toString().equals("")) {
                                paid = 0;
                            } else {
                                paid = Integer.parseInt(String.valueOf(editable.toString()));
                                unpaid1.setText(String.valueOf(Integer.parseInt(price1.getText().toString()) - paid));
                                unPaid = Integer.parseInt(unpaid1.getText().toString());
                                Log.d("UNPPAID", String.valueOf(unPaid));
                            }
                        } else {
                            unpaid1.setText("Please enter valid amount");
                        }
                    } else {
                        unpaid1.setText("Please pay amount");
                    }
                } catch (Exception e) {
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

    }

    private void hitUrlForUploadImage() {
        {
            final ProgressDialog dialog= ProgressDialog.show(Update.this,"","Loading.....",false);
            VolleyMultipartRequest request= new VolleyMultipartRequest(Request.Method.POST, Constant.UPDATE, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    dialog.dismiss();
                    String parsed= "";
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        Log.d("Responseforglass",parsed);
                        Intent intent= new Intent(Update.this,MainActivity.class);
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
                    HashMap<String,String>params=new HashMap<>();
                    params.put("key",Constant.KEY);
                    params.put("item_id",id1);
                    params.put("self_name",sale1.getText().toString());
                    params.put("name",name1.getText().toString());
                    params.put("mobile",mobile1.getText().toString());
                    params.put("address",address1.getText().toString());
                    params.put("product_price",price1.getText().toString());
                    params.put("paid_price",paid1.getText().toString());
                    params.put("unpaid_amount",unpaid1.getText().toString());
                    return params;
                }



                @Override
                protected Map<String, DataPart> getByteData() throws AuthFailureError {
                    HashMap<String,DataPart>imagePart= new HashMap<>();
                    String fil = currentDateFormat()+".png";
                    DataPart dataPart = new DataPart(fil,getFileDataFromDrawable(mBitmap));
                    imagePart.put("image",dataPart);
                    Log.d("imagee", String.valueOf(dataPart));
                    return imagePart;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Update.this);
            requestQueue.getCache().clear();
            requestQueue.add(request);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode != RESULT_CANCELED){
            if (requestCode == CAMERA_REQUEST) {
                mBitmap = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(mBitmap);
            }
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private String currentDateFormat(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    private  boolean checkAndRequestPermissions() {
        int permissionRead = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int permissionWrite = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera =  ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Update.this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    1);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                        }
                    }
                    Toast.makeText(Update.this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
        /*private static Bitmap createScaledBitmap(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return scaledBitmap;

    }*/

    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }




}

