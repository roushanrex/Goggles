package com.example.omsairam01.goggles.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.omsairam01.goggles.Imageactivity;
import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.config.Connectivity;
import com.example.omsairam01.goggles.config.Constant;
import com.example.omsairam01.goggles.model.Debmodel;
import com.example.omsairam01.goggles.model.ViewData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Debrecy extends Fragment {

    ArrayList<Debmodel> al;
    RecyclerView recyclerView;
    Debadpter adapter;

    public Debrecy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deb, container, false);
        recyclerView = view.findViewById(R.id.rv_debtor_detail);
        hitUrlForData();
        return view;
    }

    private void hitUrlForData() {
        al = new ArrayList<>();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Refresh....", false);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.DEBTOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("ResponseData",response);
                dialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONArray dataArr = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject object = dataArr.getJSONObject(i);
                        Debmodel viewData = new Debmodel();
                        viewData.setId(object.getString("id"));
                        viewData.setName(object.getString("debetor_name"));
                        viewData.setPayed(object.getString("paid_amount"));
                        viewData.setPrice(object.getString("product_price"));
                        viewData.setUnpayed(object.getString("unpay_amount"));
                        viewData.setImg(object.getString("picture"));
                        viewData.setDate(object.getString("recipt_date"));
                        al.add(viewData);

                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new Debadpter(al, getActivity());
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("key", Constant.KEY);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(request);

    }

    public class Debadpter extends RecyclerView.Adapter<Debadpter.ViewHolder> {
        ArrayList<Debmodel> al;
        Context context;

        public Debadpter(ArrayList<Debmodel> al, Context context) {
            this.al = al;
            this.context = context;
        }

        @NonNull
        @Override
        public Debadpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.debtor_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final Debmodel data = al.get(i);
            viewHolder.name.setText("Name  : " + data.getName());
            viewHolder.price.setText("Product price  : " + data.getPrice());
            viewHolder.pay.setText(" Pay amount  : " + data.getPayed());
            viewHolder.unpay.setText("Unpay amount  : " + data.getUnpayed());
            viewHolder.datetxt.setText(data.getDate());

            Picasso.with(getActivity()).load(data.getImg()).into(viewHolder.user_img);


        }


        @Override
        public int getItemCount() {
            return al.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, price, pay, unpay,datetxt;
            ImageView user_img;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.self1);
                price = itemView.findViewById(R.id.price);
                pay = itemView.findViewById(R.id.pay);
                unpay = itemView.findViewById(R.id.unpay);
                user_img = itemView.findViewById(R.id.Img_user);
                datetxt = itemView.findViewById(R.id.datetxt);

                //  name.setText(getArguments().getString("name1"));


                user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(context, Imageactivity.class);
                        intent.putExtra("abc", al.get(getAdapterPosition()).getImg());
                        startActivity(intent);
                        Toast.makeText(getActivity(), "clicked imasgten ", Toast.LENGTH_SHORT).show();


                    }
                });


            }
        }
    }

}