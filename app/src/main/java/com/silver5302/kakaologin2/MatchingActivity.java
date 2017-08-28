package com.silver5302.kakaologin2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MatchingActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    String date,region;
    ArrayList<MatchingTeamItem> matchingTeamItems=new ArrayList<>();
    MatchingRecyclerAdapter adapter;
    RecyclerView recyclerView;
    String matchTeamLoadURL="http://silver5302.dothome.co.kr/Team/matchLoad.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        Intent intent=getIntent();
        date=intent.getStringExtra("date");
        region=intent.getStringExtra("region");
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle(date);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new MatchingRecyclerAdapter(this,matchingTeamItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, matchTeamLoadURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("")){
                    return;
                }
                String[] strs=response.split(";");
                for(int i=0;i<strs.length;i++){
                    String[] unitStrs=strs[i].split("&");
                    matchingTeamItems.add(new MatchingTeamItem(unitStrs[0],unitStrs[1],unitStrs[2],
                            unitStrs[3],unitStrs[4],unitStrs[5],unitStrs[6],unitStrs[7]));
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

        smpr.addStringParam("date",date);
        smpr.addStringParam("region",region);
        requestQueue.add(smpr);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_matching,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_matching){
            if(G.isCaptain==null){
                Toast.makeText(this,"주장만 게시할수 있습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(this,MatchingRegistActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }

        }

        return true;
    }
}
