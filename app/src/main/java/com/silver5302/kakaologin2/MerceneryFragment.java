package com.silver5302.kakaologin2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alfo06-19 on 2017-09-11.
 */

public class MerceneryFragment extends Fragment {

    FloatingActionButton fab;
    TabLayout tabLayout;
    com.silver5302.kakaologin2.PagerAdapter adapter;
    ViewPager pager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mercenery, container, false);
//
//        fab=(FloatingActionButton)view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),RegistSupportActivity.class);
//                getActivity().startActivity(intent);
//            }
//        });

        pager=(ViewPager)view.findViewById(R.id.pager);
        tabLayout=(TabLayout)view.findViewById(R.id.layout_tab);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        adapter=new com.silver5302.kakaologin2.PagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);




        return view;
    }


}
