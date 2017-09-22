package com.silver5302.kakaologin2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class SelectedSupportActivity extends AppCompatActivity {

    String nickname, type, date, region, introduce, phone;
    TextView tv_nickname,tv_type,tv_date,tv_region,tv_introduce;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_support);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("용병지원");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_date=(TextView)findViewById(R.id.date);
        tv_introduce=(TextView)findViewById(R.id.introduce);
        tv_nickname=(TextView)findViewById(R.id.nickname);
        tv_region=(TextView)findViewById(R.id.region);
        tv_type=(TextView)findViewById(R.id.type);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        type = intent.getStringExtra("type");
        date = intent.getStringExtra("date");
        region = intent.getStringExtra("region");
        introduce = intent.getStringExtra("introduce");
        phone = intent.getStringExtra("phone");

        tv_nickname.setText(nickname);
        tv_type.setText(type);
        tv_date.setText(date);
        tv_region.setText(region);
        tv_introduce.setText(introduce);

    }

    public void clickSMS(View v){
        //문자보내기
        Intent intent= new Intent(Intent.ACTION_SENDTO);
        Uri uri = Uri.parse("smsto:"+phone);
        intent.setData(uri);
        startActivity(intent);

    }
    public void clickCall(View v){
        //전화걸기
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);

        Uri uri = Uri.parse("tel:"+phone);

        intent.setData(uri);
        startActivity(intent);

    }
}
