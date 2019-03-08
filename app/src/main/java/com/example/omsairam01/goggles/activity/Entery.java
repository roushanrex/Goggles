package com.example.omsairam01.goggles.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Entery extends AppCompatActivity {

    EditText selfname, mobile, price, paid_amount, customer, address;
    Button submit, prbtn;

    TextView unpaid;
    Bitmap mBitmap;
    Button button;
    TextView txt;
    CircleImageView image;
    private static final int CAMERA_REQUEST = 4;
    String Name;
    int paid;
    int unPaid = 0;
    Button loginbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entery);

        selfname = findViewById(R.id.editText9);
        customer =findViewById(R.id.editText10);
        mobile = findViewById(R.id.editText8);
        address =findViewById(R.id.editText11);
        submit =findViewById(R.id.button7);
        price = findViewById(R.id.price_et);
        paid_amount =findViewById(R.id.editText21);
        unpaid =findViewById(R.id.textView7);
        image =findViewById(R.id.profile_image);
        prbtn =findViewById(R.id.button8);
        txt = findViewById(R.id.textView6);
        loginbtn=findViewById(R.id.button75);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Entery.this,Editpassword.class);
                startActivity(intent);
                finish();
            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput();
            }
        });


        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput2();
            }
        });
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput3();
            }
        });


        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    submit.setVisibility(View.VISIBLE);
                } else {
                    submit.setVisibility(View.GONE);
                }
                //calculate();
            }
        });
        paid_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    submit.setVisibility(View.VISIBLE);
                } else {
                    submit.setVisibility(View.GONE);
                }
                //calculate();
            }
        });



//        Addres form button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selfname.getText().toString().length() == 0) {
                    selfname.setError("please enter name");
                } else if (customer.getText().toString().length() == 0) {
                    customer.setError("Please enter name");
                } else if (mobile.getText().toString().length() < 10) {
                    mobile.setError("invalid Number ");
                } else if (address.getText().toString().length() == 0) {
                    address.setError("please enter name");
                } else if (mBitmap != null) {
                    hitUrlForUploadImage(mBitmap);

                } else {
                    hitUrlForUploadImage(BitmapFactory.decodeResource(getResources(), R.drawable.no));

                }
            }
        });
//        Paid form button
        prbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selfname.getText().toString().length() == 0) {
                    selfname.setError("please enter name");
                } else if (customer.getText().toString().length() == 0) {
                    customer.setError("Please enter name");
                } else if (mobile.getText().toString().length() < 10) {
                    mobile.setError("invalid Number ");
                } else if (address.getText().toString().length() == 0) {
                    address.setError("please enter name");
                } else if (price.getText().toString().length() == 0) {
                    price.setError("please enter price");
                } else if (paid_amount.getText().toString().length() == 0) {
                    paid_amount.setError("please enter paid amount");
                } else if (mBitmap != null) {

                    hitUrlForUploadImage(mBitmap);
                } else {
                    Toast.makeText(getApplicationContext(), "Select a file", Toast.LENGTH_SHORT).show();
                }


            }
        });


        paid_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.GONE);
            }
        });

        button =findViewById(R.id.button6);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Plz Choose image", Toast.LENGTH_SHORT).show();

            }
        });


        Name = selfname.getText().toString();

        paid_amount.addTextChangedListener(new TextWatcher() {
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
                        if (Integer.parseInt(editable.toString()) <= Integer.parseInt(price.getText().toString())) {
                            if (editable.toString().equals("")) {
                                paid = 0;
                            } else {
                                paid = Integer.parseInt(String.valueOf(editable.toString()));
                                unpaid.setText(String.valueOf(Integer.parseInt(price.getText().toString()) - paid));
                                unPaid = Integer.parseInt(unpaid.getText().toString());
                                Log.d("UNPPAID", String.valueOf(unPaid));
                            }
                        } else {
                            unpaid.setText("Please enter valid amount");
                        }
                    } else {
                        unpaid.setText("Please pay amount");
                    }
                } catch (Exception e) {
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


    }


    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, 1);

        } catch (ActivityNotFoundException a) {

        }
    }
    private void askSpeechInput2() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, 2);

        } catch (ActivityNotFoundException a) {

        }
    }


    private void askSpeechInput3() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, 3);

        } catch (ActivityNotFoundException a) {

        }
    }


    public void HitUrl(String number) {
        String msg = "Welcome Mehra Opticals";
        String sendMsg = msg.replaceAll(" ","%20");
        String urlString = Constant.SMS + "&to=" + number + "&message=" + sendMsg + "&priority=1&dnd=1&unicode=0";
        StringRequest request = new StringRequest(Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("SmSResponse", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.getCache().clear();
        queue.add(request);
    }


    private void hitUrlForUploadImage(final Bitmap bitmap) {
        final ProgressDialog dialog = ProgressDialog.show(Entery.this, "", "Loading.....", false);
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constant.REGISTER, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                dialog.dismiss();
                // HitUrl(number);
                HitUrl(mobile.getText().toString());
                String parsed = "";
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Toast.makeText(getApplicationContext(), "Data insert", Toast.LENGTH_LONG).show();
                    Log.d("Responseforglass", parsed);

                  Intent intent = new Intent(getApplicationContext(), Entery.class);
                 intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
                  finish();

                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("key", Constant.KEY);
                params.put("self_name", selfname.getText().toString());
                params.put("name", customer.getText().toString());
                params.put("mobile", mobile.getText().toString());
                params.put("product_price", price.getText().toString());
                params.put("address", address.getText().toString());
                params.put("paid_price", paid_amount.getText().toString());
                params.put("unpaid_amount", String.valueOf(unPaid));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                HashMap<String, DataPart> imagePart = new HashMap<>();
                String fil = currentDateFormat() + ".png";
                DataPart dataPart = new DataPart(fil, getFileDataFromDrawable(bitmap));
                imagePart.put("image", dataPart);
                return imagePart;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
                /* mBitmap = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(mBitmap);*/
            if (resultCode == RESULT_OK) {
                ArrayList<String> result =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // t1.setText(result.get(0));
//                Log.d("Speech",result.get(0));
                String[] arr = result.get(0).split(" ");
                for (int i = 0; i < arr.length; i++) {
                    Log.d("Speech123", arr[0]);
                    selfname.setText(arr[0]);
                    customer.setText(arr[1]);
                    address.setText(arr[2]);


                }
            }


        }
        else if (requestCode == 2)
        {

            if (resultCode == RESULT_OK) {

                ArrayList<String> result =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mobile.setText(result.get(0));


            }

        }
        else if (requestCode == 3) {
                /* mBitmap = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(mBitmap);*/
            if (resultCode == RESULT_OK) {
                ArrayList<String> result =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // t1.setText(result.get(0));
//                Log.d("Speech",result.get(0));
                String[] arr = result.get(0).split(" ");
                for (int i = 0; i < arr.length; i++) {
                    Log.d("Speech123", arr[0]);
                    price.setText(arr[0]);
                    paid_amount.setText(arr[1]);


                }
            }


        }


        else if (requestCode == CAMERA_REQUEST && data != null) {
            mBitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(mBitmap);
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
            ActivityCompat.requestPermissions(Entery.this,
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
                    Toast.makeText(getApplicationContext(), "Permission Denied...", Toast.LENGTH_SHORT).show();
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