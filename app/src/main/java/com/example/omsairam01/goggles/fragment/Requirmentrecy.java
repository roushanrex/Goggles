package com.example.omsairam01.goggles.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.config.Constant;
import com.example.omsairam01.goggles.model.ReqModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Requirmentrecy extends Fragment {

    ArrayList<ReqModel> al;
    RecyclerView recyclerView;
    Debadpter adapter;

    public Requirmentrecy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requirmentrecy, container, false);
        recyclerView = view.findViewById(R.id.rv_request_detail);
        hitUrlForData();
        return view;
    }

    private void hitUrlForData() {
        al = new ArrayList<>();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Refresh....", false);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_REQUIRMENT, new Response.Listener<String>() {
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
                        ReqModel viewData = new ReqModel();
                        viewData.setId(object.getString("id"));
                        viewData.setName(object.getString("name"));
                        viewData.setDescription(object.getString("description"));
                        viewData.setPrice(object.getString("price"));
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
        ArrayList<ReqModel> al;
        Context context;

        public Debadpter(ArrayList<ReqModel> al, Context context) {
            this.al = al;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.requirment_list, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final ReqModel data = al.get(i);
            viewHolder.name.setText("Name  : " + data.getName());
            viewHolder.description.setText("Description  : " + data.getDescription());
            viewHolder.price.setText("Price  : " + data.getPrice());
            viewHolder.date.setText(data.getDate());


        }


        @Override
        public int getItemCount() {
            return al.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, description, price,date;
            Button readybtn;


            public ViewHolder(@NonNull final View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.self1);
                description = itemView.findViewById(R.id.description);
                price = itemView.findViewById(R.id.pay);
                date = itemView.findViewById(R.id.datetxt);

                readybtn = itemView.findViewById(R.id.btnyess);
            String s=readybtn.getText().toString();

                readybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readybtn.setText("COMPLETE");


                    }
                });

            }
        }

    }


}