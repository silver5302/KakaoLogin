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

public class RecruitFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView tv_nocontent;
    RecruitRecyclerAdapter adapter;
    ArrayList<RecruitItem> recruitItems=new ArrayList<>();

    String recruitLoadURL="http://silver5302.dothome.co.kr/Team/recruitLoad.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View page=inflater.inflate(R.layout.fragment_recruit,container,false);

        swipeRefreshLayout=(SwipeRefreshLayout) page.findViewById(R.id.swipelayout);
        recyclerView=(RecyclerView)page.findViewById(R.id.recycler);
        tv_nocontent=(TextView)page.findViewById(R.id.tv_nocontent);

        adapter=new RecruitRecyclerAdapter(recruitItems,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, recruitLoadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("recruit",response);
                if(response.equals("")){
                    tv_nocontent.setVisibility(View.VISIBLE);
                    return;
                }
                tv_nocontent.setVisibility(View.GONE);
                String[] strs=response.split(";");
                for(int i=0;i<strs.length;i++){
                    String[] unitStrs=strs[i].split("&");
                    recruitItems.add(new RecruitItem(unitStrs[0],unitStrs[1],unitStrs[2],
                            unitStrs[3],unitStrs[4],unitStrs[5],unitStrs[6]));
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
                recruitItems.clear();
                RequestQueue requestQueue=Volley.newRequestQueue(getActivity());
                SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, recruitLoadURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("")){
                            tv_nocontent.setVisibility(View.VISIBLE);
                            return;
                        }
                        tv_nocontent.setVisibility(View.GONE);
                        String[] strs=response.split(";");
                        for(int i=0;i<strs.length;i++){
                            String[] unitStrs=strs[i].split("&");
                            recruitItems.add(new RecruitItem(unitStrs[0],unitStrs[1],unitStrs[2],
                                    unitStrs[3],unitStrs[4],unitStrs[5],unitStrs[6]));
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
