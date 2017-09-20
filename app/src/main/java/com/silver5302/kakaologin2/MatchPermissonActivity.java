package com.silver5302.kakaologin2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MatchPermissonActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    RecyclerView recyclerView;
    ArrayList<MatchListItem> matchListItems=new ArrayList<>();
    MatchingListRecyclerAdapter adapter;
    String MyMatchListURL="http://silver5302.dothome.co.kr/Team/MyMatchList.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_permisson);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("매치 리스트");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new MatchingListRecyclerAdapter(this,matchListItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.GET, MyMatchListURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("")) return;
                String[] strs=response.split(";");
                for(int i=0;i<strs.length;i++){
                    String[] unitStrs=strs[i].split("&");
                    Log.e("eeee",unitStrs[0]);
                    matchListItems.add(new MatchListItem(unitStrs[0],unitStrs[1],unitStrs[2],unitStrs[3]));


                }

                runOnUiThread(new Runnable() {
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
        smpr.addStringParam("teamName",G.captainTeam);
        requestQueue.add(smpr);



    }


}
