package com.silver5302.kakaologin2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchingInformActivity extends AppCompatActivity {

    CircleImageView imageView;
    Toolbar toolbar;
    TextView tvTeamName,tvType,tvDate,tvTime,tvInform,tvRegion;
    ActionBar actionBar;
    String teamName,type,date,time,inform,region,phone;
    String matchInformImgURL="http://silver5302.dothome.co.kr/Team/loadTeamImg.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_inform);
        imageView=(CircleImageView)findViewById(R.id.img);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        tvType=(TextView)findViewById(R.id.type);
        tvDate=(TextView)findViewById(R.id.date);
        tvTime=(TextView)findViewById(R.id.time);
        tvInform=(TextView)findViewById(R.id.inform);
        tvTeamName=(TextView)findViewById(R.id.tv_teamname);
        tvRegion=(TextView)findViewById(R.id.region);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("매치 찾기");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        teamName=intent.getStringExtra("teamName");
        type=intent.getStringExtra("type");
        date=intent.getStringExtra("date");
        time=intent.getStringExtra("time");
        inform=intent.getStringExtra("inform");
        region=intent.getStringExtra("region");
        phone=intent.getStringExtra("phone");

        tvType.setText(type); tvDate.setText(date); tvTime.setText(time); tvInform.setText(inform);
        tvTeamName.setText(teamName); tvRegion.setText(region);

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr=new SimpleMultiPartRequest(Request.Method.POST, matchInformImgURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals(""))return;
                String imgurl="http://silver5302.dothome.co.kr/Team/"+response;
                Glide.with(MatchingInformActivity.this).load(imgurl).into(imageView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MatchingInformActivity.this, "에러", Toast.LENGTH_SHORT).show();
            }
        });
        smpr.addStringParam("teamName",teamName);
        requestQueue.add(smpr);


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
