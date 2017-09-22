package com.silver5302.kakaologin2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by alfo06-19 on 2017-09-13.
 */

public class SupportFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    SupportRecyclerAdapter adapter;
    TextView tv_nocontent;
    ArrayList<SupportItem> supportItems=new ArrayList<>();

    String supportLoadURL="http://silver5302.dothome.co.kr/Team/supportLoad.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page=inflater.inflate(R.layout.fragment_support,container,false);

        recyclerView=(RecyclerView)page.findViewById(R.id.recycler);
        swipeRefreshLayout=(SwipeRefreshLayout)page.findViewById(R.id.swipelayout);
        tv_nocontent=(TextView)page.findViewById(R.id.tv_nocontent);

        adapter=new SupportRecyclerAdapter(supportItems,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, supportLoadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("support",response);
                if(response.equals("")){
                    tv_nocontent.setVisibility(View.VISIBLE);
                    return;
                }
                tv_nocontent.setVisibility(View.GONE);
                String[] strs=response.split(";");
                for(int i=0;i<strs.length;i++){
                    String[] unitStrs=strs[i].split("&");
                    supportItems.add(new SupportItem(unitStrs[0],unitStrs[1],unitStrs[2],unitStrs[3],unitStrs[4],unitStrs[5]));
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(smpr);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                supportItems.clear();
                RequestQueue requestQueue=Volley.newRequestQueue(getActivity());
                SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, supportLoadURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("support",response);
                        if(response.equals("")){
                            tv_nocontent.setVisibility(View.VISIBLE);
                            return;
                        }
                        tv_nocontent.setVisibility(View.GONE);
                        String[] strs=response.split(";");
                        for(int i=0;i<strs.length;i++){
                            String[] unitStrs=strs[i].split("&");
                            supportItems.add(new SupportItem(unitStrs[0],unitStrs[1],unitStrs[2],unitStrs[3],unitStrs[4],unitStrs[5]));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(smpr);



                swipeRefreshLayout.setRefreshing(false);
            }
        });





        return page;
    }
}
