package com.example.omsairam01.goggles.fragment;


import android.app.Dialog;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.example.omsairam01.goggles.activity.Editdata;
import com.example.omsairam01.goggles.activity.Editpassword;
import com.example.omsairam01.goggles.activity.MainActivity;
import com.example.omsairam01.goggles.activity.Update;
import com.example.omsairam01.goggles.config.Connectivity;
import com.example.omsairam01.goggles.config.Constant;
import com.example.omsairam01.goggles.model.SearchModel;
import com.example.omsairam01.goggles.model.ViewData;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Search extends Fragment {


    ArrayList<SearchModel> searchModels;
    ArrayList<ViewData> al;
    RecyclerView recyclerView;
    String unpaidAmount;
    //    RecyclerView recyclerView1;
    TextView txttotal;
    String mobilee;

    String s,sent;

    DataAdapter adapter;
    MyTotalAdapter myTotalAdapter;


    public Search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.rv_serach_detail);
        txttotal = view.findViewById(R.id.txt23);


        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (new Connectivity().isNetworkAvailable(getContext())) {
                hitUrlForData();
                totalunpaidamount();


            } else {
                Toast.makeText(getActivity(), "No Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        } else {
            //no
        }

    }

    private void totalunpaidamount() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        searchModels = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.UNPAID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("ResponseDa", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String msg = jsonObject.getString("msg");
                    String code = jsonObject.getString("code");
                    if (code.equalsIgnoreCase("200")) {
                        JSONArray dataArr = jsonObject.getJSONArray("data");


                        JSONObject object = dataArr.getJSONObject(0);
                        unpaidAmount = object.getString("total_unpaid");

                    } else {
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("key", Constant.KEY);
                return params;

            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(request);

    }


    private void hitUrlForData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        al = new ArrayList<>();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading....", false);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseData", response);
                dialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONArray dataArr = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONObject object = dataArr.getJSONObject(i);
                        ViewData viewData = new ViewData();
                        viewData.setId(object.getString("id"));
                        viewData.setName(object.getString("name"));
                        viewData.setMobile(object.getString("mobile"));
                        mobilee=object.getString("mobile");
                        viewData.setImg(object.getString("image"));
                        viewData.setSelf(object.getString("self_name"));
                        viewData.setAddress(object.getString("address"));
                        viewData.setPrice(object.getString("product_price"));
                        viewData.setPayed(object.getString("paid_price"));
                        viewData.setUnpayed(object.getString("unpaid_amount"));
                        viewData.setDate(object.getString("recipt_date"));
//                        Log.d("mobile",mobilee);

                        al.add(viewData);
                        txttotal.setText(unpaidAmount);

                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new DataAdapter(al, getActivity());
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
        requestQueue.getCache().clear();
        requestQueue.add(request);

    }

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        ArrayList<ViewData> al;
        Context context;

        public DataAdapter(ArrayList<ViewData> al, Context context) {
            this.al = al;
            this.context = context;
        }

        @NonNull
        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_list, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final ViewData data = al.get(i);

            viewHolder.self.setText("Sales Person : " + data.getSelf());
            viewHolder.name.setText("Customer Name  : " + data.getName());
            viewHolder.mobile.setText("Mobile  : " + data.getMobile());
            viewHolder.price.setText("Product price  : " + data.getPrice());
            viewHolder.pay.setText("Paid amount  : " + data.getPayed());
            viewHolder.unpay.setText("Balance  : " + data.getUnpayed());
            viewHolder.address.setText("Address  : " + data.getAddress());
            viewHolder.date.setText(data.getDate());

             s=data.getMobile();

            Picasso.with(getActivity()).load(data.getImg()).into(viewHolder.user_img);


        }

        @Override
        public int getItemCount() {
            return al.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, price, mobile, pay, unpay, self, address, date;
            ImageView user_img;
            Button readybtn, editbtn, btnupdate;




            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                price = itemView.findViewById(R.id.price);
                mobile = itemView.findViewById(R.id.mobile);
                pay = itemView.findViewById(R.id.pay);
                unpay = itemView.findViewById(R.id.unpay);
                self = itemView.findViewById(R.id.self);
                address = itemView.findViewById(R.id.address);
                user_img = itemView.findViewById(R.id.imageView);
                readybtn = itemView.findViewById(R.id.ready);
                editbtn = itemView.findViewById(R.id.edit);
                btnupdate = itemView.findViewById(R.id.button5);
                date = itemView.findViewById(R.id.datetxt);

                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Update.class);
                        intent.putExtra("id", al.get(getAdapterPosition()).getId());
                        intent.putExtra("sale1", al.get(getAdapterPosition()).getSelf());
                        intent.putExtra("name1", al.get(getAdapterPosition()).getName());
                        intent.putExtra("mobile1", al.get(getAdapterPosition()).getMobile());
                        intent.putExtra("address1", al.get(getAdapterPosition()).getAddress());
                        intent.putExtra("price1", al.get(getAdapterPosition()).getPrice());
                        intent.putExtra("paid1", al.get(getAdapterPosition()).getPayed());
                        intent.putExtra("unpaid1", al.get(getAdapterPosition()).getUnpayed());

                        startActivity(intent);


                    }
                });


                readybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                Toast.makeText(getActivity(),"Product is Ready",Toast.LENGTH_SHORT).show();


                        String msg = "Product is ready googles shope";
                        String sendMsg = msg.replaceAll(" ", "%20");
                        String urlString = Constant.SMS + "&to=" +al.get(getAdapterPosition()).getMobile()+ "&message=" + sendMsg + "&priority=1&dnd=1&unicode=0";
                        StringRequest request = new StringRequest(Request.Method.GET, urlString, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

//                                Log.d("SmSResponsee", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                        };
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.getCache().clear();
                        queue.add(request);
                    }

                });


                editbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(getActivity(), Editdata.class);
                        intent.putExtra("id", al.get(getAdapterPosition()).getId());
                        intent.putExtra("price2", al.get(getAdapterPosition()).getPrice());
                        intent.putExtra("paid2", al.get(getAdapterPosition()).getPayed());
                        startActivity(intent);


                    }
                });


                user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(context, Imageactivity.class);
                        intent.putExtra("abc", al.get(getAdapterPosition()).getImg());
                        startActivity(intent);

                    }
                });
            }
        }

        public void setSearchOption(List<ViewData> newList) {
            al = new ArrayList<>();
            al.addAll(newList);
            notifyDataSetChanged();

        }

    }


    public class MyTotalAdapter extends RecyclerView.Adapter<MyTotalAdapter.ViewHolder> {
        Context context;
        ArrayList<SearchModel> searchModels;

        public MyTotalAdapter(Context context, ArrayList<SearchModel> searchModels) {
            this.context = context;
            this.searchModels = searchModels;
        }


        @NonNull
        @Override
        public MyTotalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.totalunpaid, viewGroup, false);


            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyTotalAdapter.ViewHolder viewHolder, int i) {
            SearchModel unp = searchModels.get(i);

            viewHolder.txtinpuaid2.setText(" " + unp.getTotalunpaid());

        }

        @Override
        public int getItemCount() {
            return searchModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtinpuaid2;

            public ViewHolder(@NonNull View itemView) {

                super(itemView);
                txtinpuaid2 = itemView.findViewById(R.id.txtunpaid);

            }
        }
    }
}