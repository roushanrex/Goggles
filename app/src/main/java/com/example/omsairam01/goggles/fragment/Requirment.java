package com.example.omsairam01.goggles.fragment;


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
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
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
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Requirment extends Fragment {

    EditText name, description, price;
    Button submit;





    private static final int CAMERA_REQUEST = 4;


    public Requirment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requirment, container, false);
        name = view.findViewById(R.id.editText12);
        description = view.findViewById(R.id.editText16);
        price = view.findViewById(R.id.editText);
        submit = view.findViewById(R.id.button2);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput();
            }
        });


        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askSpeechInput2();
            }
        });





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().length() == 0) {
                    name.setError("please enter name");
                } else if (description.getText().toString().length() == 0) {
                    description.setError("Please enter name");
                } else if (price.getText().toString().length()==10) {
                    price.setError("invalid Number ");

                }
                else
                {
                    hitUrlForUploadImage();

                }
            }
        });
//


        return view;
    }


        private void hitUrlForUploadImage() {
            final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading.....", false);

            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constant.REQUIRMENT, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {

                    dialog.dismiss();

                    String parsed = "";
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        Toast.makeText(getActivity(), "Data insert", Toast.LENGTH_LONG).show();
                        Log.d("Responseforglass", parsed);
                        Toast.makeText(getActivity(), "Data insert", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();

                        Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_LONG).show();
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
                    params.put("name", name.getText().toString());
                    params.put("description", description.getText().toString());
                    params.put("price", price.getText().toString());


                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.getCache().clear();
            requestQueue.add(request);
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




//    private void hitUrlForUploadImage(final Bitmap bitmap) {
//        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading.....", false);
//        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Constant.REGISTER, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                dialog.dismiss();
//                // HitUrl(number);
//
//                String parsed = "";
//                try {
//                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                    Toast.makeText(getActivity(), "Data insert", Toast.LENGTH_LONG).show();
//                    Log.d("Responseforglass", parsed);
//                    Toast.makeText(getActivity(), "Data insert", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    getActivity().finish();
//
//                    Toast.makeText(getActivity(), "Data Inserted", Toast.LENGTH_LONG).show();
//                } catch (UnsupportedEncodingException e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//
//            }
//        })
//
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("key", Constant.KEY);
//
//                params.put("self_name", name.getText().toString());
//                params.put("name", description.getText().toString());
//                params.put("mobile", price.getText().toString());
//                params.put("product_price", price.getText().toString());
//
//                return params;
//            }
//
////            @Override
////            protected Map<String, DataPart> getByteData() throws AuthFailureError {
////                HashMap<String, DataPart> imagePart = new HashMap<>();
////                String fil = currentDateFormat() + ".png";
////                DataPart dataPart = new DataPart(fil, getFileDataFromDrawable(bitmap));
////                imagePart.put("image", dataPart);
////                return imagePart;
////            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.getCache().clear();
//        requestQueue.add(request);
//    }
//

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                String[] arr = result.get(0).split(" ");
                for (int i = 0; i < arr.length; i++) {
                    Log.d("Speech123", arr[0]);
                    name.setText(arr[0]);
                    description.setText(arr[1]);


                }
            }


        } else if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                ArrayList<String> result =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                price.setText(result.get(0));


            }

        }
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


}
