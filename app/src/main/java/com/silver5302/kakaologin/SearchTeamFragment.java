package com.silver5302.kakaologin;

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
    RecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_search_team,container,false);

//        searchView=(SearchView)view.findViewById(R.id.searchview);
        recycler=(RecyclerView)view.findViewById(R.id.recycler);

        adapter=new RecyclerAdapter(teamLists,getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        new Thread(){
            @Override
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

                        teamLists.add(new TeamList(unitstr[0],unitstr[1],
                                unitstr[2],unitstr[3],unitstr[4],unitstr[5]));
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
        }.start();

        return view;
    }
}
