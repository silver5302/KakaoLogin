package com.silver5302.kakaologin;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alfo06-19 on 2017-07-26.
 */

public class SearchTeamFragment extends Fragment {

    String loadUrl = "http://silver5302.dothome.co.kr/Team/loadDB.php";
    SearchView searchView;
    RecyclerView recycler;
    ArrayList<TeamList> teamLists=new ArrayList<>();
    ArrayList<TeamList> originalTeamLists = new ArrayList<>();
    RecyclerAdapter adapter;
    LoadThread loadThread;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_search_team,container,false);

        searchView=(SearchView)view.findViewById(R.id.searchview);
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

        recycler=(RecyclerView)view.findViewById(R.id.recycler);

        adapter=new RecyclerAdapter(teamLists,getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        loadThread=new LoadThread();
        loadThread.start();


        return view;
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


                    getActivity().runOnUiThread(new Runnable() {
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
