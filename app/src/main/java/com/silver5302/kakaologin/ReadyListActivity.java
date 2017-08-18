package com.silver5302.kakaologin;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class ReadyListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ReadyMember> readyMembers=new ArrayList<>();
    TeamlistRecyclerAdapter adapter;
    String readyListUrl="http://silver5302.dothome.co.kr/Team/readyListDB.php";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_list);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new TeamlistRecyclerAdapter(this,readyMembers);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        RequestQueue requestQueue=Volley.newRequestQueue(ReadyListActivity.this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST,readyListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                String[] strs=response.split(";");
                for(int i=0;i<strs.length;i++){
                    String[] unitStrs=strs[i].split("&");
                    if(unitStrs.length==5){
                        readyMembers.add(new ReadyMember(unitStrs[0],unitStrs[1],unitStrs[2],unitStrs[3],unitStrs[4]));
                    }else if(unitStrs.length==4){
                        readyMembers.add(new ReadyMember(unitStrs[0],unitStrs[1],unitStrs[2],unitStrs[3],null));
                    }


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
                Toast.makeText(ReadyListActivity.this, "에러!!", Toast.LENGTH_SHORT).show();
            }
        });

        smpr.addStringParam("teamName",G.captainTeam);
        requestQueue.add(smpr);
    }
}
