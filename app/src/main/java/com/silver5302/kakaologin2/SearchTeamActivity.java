package com.silver5302.kakaologin2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchTeamActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    String loadUrl = "http://silver5302.dothome.co.kr/Team/loadDB.php";
    SearchView searchView;
    RecyclerView recycler;
    ArrayList<TeamList> teamLists=new ArrayList<>();
    ArrayList<TeamList> originalTeamLists = new ArrayList<>();
    SearchTeamRecyclerAdapter adapter;
    LoadThread loadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("팀찾기");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView=(SearchView)findViewById(R.id.searchview);
        searchView.setQueryHint("팀명을 입력하시오.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                teamLists.clear();

                if(newText.equals("")){
                    for(int i=0; i<originalTeamLists.size(); i++){
                        teamLists.add(originalTeamLists.get(i));
                    }
                    adapter.notifyDataSetChanged();

                }else {

                    for(int i=0; i<originalTeamLists.size(); i++){
                        if(originalTeamLists.get(i).name.contains(newText)){
                            teamLists.add(originalTeamLists.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }

                }

                return true;
            }
        });

        recycler=(RecyclerView)findViewById(R.id.recycler);

        adapter=new SearchTeamRecyclerAdapter(teamLists,this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(this,2));

        loadThread=new LoadThread();
        loadThread.start();
    }
    class LoadThread extends Thread{

        public void run() {
            super.run();
            try {
                URL url=new URL(loadUrl);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setUseCaches(false);

                InputStream is=conn.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                StringBuffer stringBuffer=new StringBuffer();
                String line=br.readLine();
                while (line!=null){
                    stringBuffer.append(line);
                    line = br.readLine();
                }

                String[] str=stringBuffer.toString().split(";");
                for (int i=0;i<str.length;i++){
                    String[] unitstr=str[i].split("&");

                    if(unitstr.length==6){
                        teamLists.add(new TeamList(unitstr[0],unitstr[1],
                                unitstr[2],unitstr[3],unitstr[4],unitstr[5],null));
                    }else if(unitstr.length==7){
                        teamLists.add(new TeamList(unitstr[0],unitstr[1],
                                unitstr[2],unitstr[3],unitstr[4],unitstr[5],unitstr[6]));
                    }

                }

                for(int i=0; i<teamLists.size(); i++){
                    originalTeamLists.add(teamLists.get(i));
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
