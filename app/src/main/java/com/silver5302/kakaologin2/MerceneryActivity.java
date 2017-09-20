package com.silver5302.kakaologin2;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class MerceneryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;
    FloatingActionButton fab;
    ActionBar actionBar;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercenery);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("용병게시판");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab=(FloatingActionButton)findViewById(R.id.fab);
        tabLayout=(TabLayout)findViewById(R.id.layout_tab);
        pager=(ViewPager)findViewById(R.id.pager);

        adapter=new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pager.getCurrentItem()){
                    case 0:
                        if(G.Team.size()>=1){

                            Intent intent=new Intent(MerceneryActivity.this,RegistRecruitActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MerceneryActivity.this, "팀이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:

                        Intent intent2=new Intent(MerceneryActivity.this,RegistSupportActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });


    }
}
