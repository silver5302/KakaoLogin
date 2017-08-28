package com.silver5302.kakaologin2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class SelectRegionActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("지역 선택");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String[] regions = new String[]{
                "전체지역","서울특별시", "부산광역시", "인천광역시", "대구광역시", "광주광역시", "대전광역시", "울산광역시",
                "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"
        };
        MyAdapter adapter=new MyAdapter(this,regions);
        gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = regions[position];

                Intent intent= getIntent();
                intent.putExtra("region", s);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}

class MyAdapter extends BaseAdapter {

    Context context;
    String[] regions;

    public MyAdapter(Context context, String[] regions) {
        this.context = context;
        this.regions = regions;
    }

    @Override
    public int getCount() {
        return regions.length;
    }

    @Override
    public Object getItem(int position) {
        return regions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.region,parent,false);
            TextView tv=(TextView) convertView.findViewById(R.id.tv_region);
            tv.setText(regions[position]);
        }

        return convertView;
    }
}
