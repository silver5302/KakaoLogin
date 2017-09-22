package com.silver5302.kakaologin2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedRecruitActivity extends AppCompatActivity {
    CircleImageView imageView;
    Toolbar toolbar;
    TextView tvTeamName, tvType,tvTime, tvIntroduce, tvRegion,tvDate;
    ActionBar actionBar;
    String teamName, type, time, introduce, region, phone,date;
    String loadTeamImgURL = "http://silver5302.dothome.co.kr/Team/loadTeamImg.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_recruit);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("용병모집");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        teamName=intent.getStringExtra("teamname");
        type=intent.getStringExtra("type");
        time=intent.getStringExtra("time");
        date=intent.getStringExtra("date");
        introduce=intent.getStringExtra("introduce");
        phone=intent.getStringExtra("phone");
        region=intent.getStringExtra("region");

        imageView=(CircleImageView)findViewById(R.id.img);
        tvTeamName=(TextView)findViewById(R.id.teamname);
        tvRegion=(TextView)findViewById(R.id.region);
        tvType=(TextView)findViewById(R.id.type);
        tvDate=(TextView)findViewById(R.id.date);
        tvTime=(TextView)findViewById(R.id.time);
        tvIntroduce=(TextView)findViewById(R.id.introduce);

        tvTeamName.setText(teamName);
        tvIntroduce.setText(introduce);
        tvTime.setText(time);
        tvDate.setText(date);
        tvType.setText(type);
        tvRegion.setText(region);

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, loadTeamImgURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("")) return;
                String imgurl = "http://silver5302.dothome.co.kr/Team/" + response;
                Glide.with(SelectedRecruitActivity.this).load(imgurl).into(imageView);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        smpr.addStringParam("teamName",teamName);
        requestQueue.add(smpr);

    }
    public void clickSMS(View v) {
        //문자보내기
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri uri = Uri.parse("smsto:" + phone);
        intent.setData(uri);
        startActivity(intent);


    }

    public void clickCall(View v) {
        //전화걸기
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);

        Uri uri = Uri.parse("tel:" + phone);

        intent.setData(uri);
        startActivity(intent);

    }
}
