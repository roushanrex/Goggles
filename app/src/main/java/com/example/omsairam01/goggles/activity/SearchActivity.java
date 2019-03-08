package com.example.omsairam01.goggles.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.omsairam01.goggles.R;
import com.example.omsairam01.goggles.config.Connectivity;
import com.example.omsairam01.goggles.config.Constant;
import com.example.omsairam01.goggles.model.SearchModel;
import com.example.omsairam01.goggles.model.ViewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText search;
    SearchAdapter adapter;
    LinearLayout noRecord;
    ArrayList<SearchModel>arrayList;
   // String regexStr = "^[0-9]*$";
    String regexStr = "\\d+";
    String isName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        arrayList = new ArrayList<>();
        init();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() >= 2) {
                    if (new Connectivity().isNetworkAvailable(SearchActivity.this)) {
                            if (arrayList.size() > 0) {
                                arrayList.clear();
                            }
                            hitUrlForSearchList(String.valueOf(editable));
                    } else {
                        Toast.makeText(SearchActivity.this, "No internet connection...", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    noRecord.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void hitUrlForSearchList(final String key) {

        final ProgressDialog dialog= ProgressDialog.show(this,"","Loading...",false);
        StringRequest request= new StringRequest(Request.Method.POST, Constant.SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponserSearch",response);
               dialog.dismiss();
              // arrayList.clear();
               try {
                   JSONArray jsonArray = new JSONArray(response);
                   JSONObject jsonObject=  jsonArray.getJSONObject(0);
                   isName = jsonObject.getString("nam");
                   if (jsonObject.getString("code").equalsIgnoreCase("200")) {
                       noRecord.setVisibility(View.GONE);
                       recyclerView.setVisibility(View.VISIBLE);
                       JSONArray dataArr = jsonObject.getJSONArray("data");
                       for (int i = 0; i < dataArr.length(); i++) {
                           JSONObject object = dataArr.getJSONObject(i);
                           final SearchModel model = new SearchModel();
                           model.setId(object.getString("id"));
                           model.setSelf(object.getString("self_name"));
                           model.setName(object.getString("name"));
                           model.setMobile(object.getString("mobile"));
                           model.setAddress(object.getString("address"));
                           model.setPrice(object.getString("product_price"));
                           model.setPaidPrice(object.getString("paid_price"));
                           model.setUnpaidAmount(object.getString("unpaid_amount"));
                           model.setImages(object.getString("image"));
                           arrayList.add(model);
                       }
                       adapter = new SearchAdapter(arrayList, getApplicationContext());
                       recyclerView.setAdapter(adapter);
                   }else {
                       arrayList.clear();
                       noRecord.setVisibility(View.VISIBLE);
                       recyclerView.setVisibility(View.GONE);
                   }

               } catch (JSONException e) {
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
                HashMap<String,String>params= new HashMap<>();
                params.put("key",Constant.KEY);
                if (key.trim().matches(regexStr)){
                    params.put("mobile",key);
//                    Toast.makeText(SearchActivity.this, "true", Toast.LENGTH_SHORT).show();
                }else {
                    params.put("name",key);
                }
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    private void init() {
        recyclerView=findViewById(R.id.rv_search);
        search=findViewById(R.id.edt_search);
        noRecord = findViewById(R.id.no_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
        ArrayList<SearchModel>list;
        Context context;

        public SearchAdapter(ArrayList<SearchModel> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(context).inflate(R.layout.search_listview,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder viewHolder, int i) {
            final SearchModel model= list.get(i);
            Log.d("ListSize", String.valueOf(list.size()));
            //viewHolder.textView.setText(model.getName());

            if (isName.matches("true")){
                viewHolder.textView.setText(model.getName());
            }else {
                viewHolder.textView.setText(model.getMobile());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.txt1);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i= new Intent(context,SearchDeatail.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("searchData",arrayList.get(getAdapterPosition()));
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });
            }
        }
    }
}
