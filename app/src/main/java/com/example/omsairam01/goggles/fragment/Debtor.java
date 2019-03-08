package com.example.omsairam01.goggles.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.omsairam01.goggles.activity.MainActivity;
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

import static android.app.Activity.RESULT_CANCELED;

public class Debtor extends Fragment
{
    EditText name,price,paid_amount;
    Button submit;
    TextView unpaid;
    Bitmap mBitmap;
    Button button;
    CircleImageView image;
    private static final int CAMERA_REQUEST = 1;
    String Name;
    int paid;
    int unPaid = 0;

    public Debtor() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.debtorfrg,container,false);
        name= view.findViewById(R.id.editText12);
        price=view.findViewById(R.id.editText16);
        paid_amount=view.findViewById(R.id.editText);
        submit=view.findViewById(R.id.button2);
        unpaid=view.findViewById(R.id.textView2);
        image=view.findViewById(R.id.profile_image);
        button=view.findViewById(R.id.button3);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Imageactivity.class);
                intent.putExtra("abc",mBitmap);
                startActivity(intent);



            }
        });


        Name=name.getText().toString();

        paid_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("After",editable.toString());
                try {
                    if (!editable.toString().equals("")) {
                        if (Integer.parseInt(editable.toString())<= Integer.parseInt(price.getText().toString())){
                            if (editable.toString().equals("")) {
                                paid = 0;
                            } else {
                                paid = Integer.parseInt(String.valueOf(editable.toString()));
                                unpaid.setText(String.valueOf(Integer.parseInt(price.getText().toString()) - paid));
                                unPaid = Integer.parseInt(unpaid.getText().toString());
                                Log.d("UNPPAID",String.valueOf(unPaid));
                            }
                        }else {
                            unpaid.setText("Please enter valid amount");
                        }
                    }else {
                        unpaid.setText("Please pay amount");
                    }
                }catch (Exception e){}

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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (name.getText().toString().length()==0 ){
                    name.setError("please enter name");
                }else if (price.getText().toString().length()==0 ){
                    price.setError("please enter price");
                }else if (paid_amount.getText().toString().length()==0 ){
                    paid_amount.setError("please enter paid amount");
                }else if (mBitmap != null){
                    hitUrlForUploadImage();
                }
                else {
                    Toast.makeText(getActivity(), "Please select a file..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;


    }






    private void hitUrlForUploadImage() {
        final ProgressDialog dialog= ProgressDialog.show(getActivity(),"","Loading.....",false);
        VolleyMultipartRequest request= new VolleyMultipartRequest(Request.Method.POST, Constant.ADD_DEBTOR, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                dialog.dismiss();
                String parsed= "";
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    Log.d("Responseforglass",parsed);
                    Intent intent= new Intent(getActivity(),MainActivity.class);
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
                params.put("debetor_name",name.getText().toString());
                params.put("product_price",price.getText().toString());
                params.put("paid_amount",paid_amount.getText().toString());
                params.put("unpay_amount", String.valueOf(unPaid));
                return params;
            }



            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                HashMap<String,DataPart>imagePart= new HashMap<>();
                String fil = currentDateFormat()+".png";
                DataPart dataPart = new DataPart(fil,getFileDataFromDrawable(mBitmap));
                imagePart.put("picture",dataPart);
                Log.d("imagee", String.valueOf(dataPart));
                return imagePart;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(request);
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
        int permissionRead = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int permissionWrite = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera =  ContextCompat.checkSelfPermission(getActivity(),
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
            ActivityCompat.requestPermissions(getActivity(),
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
                    Toast.makeText(getActivity(), "Permission Denied...", Toast.LENGTH_SHORT).show();
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