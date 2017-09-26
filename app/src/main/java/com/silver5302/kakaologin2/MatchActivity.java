package com.silver5302.kakaologin2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class MatchActivity extends AppCompatActivity {

    String matchCalendarURL = "http://silver5302.dothome.co.kr/Team/matchCalendar.php";
    MCalendarView calendarView;
    Button btnRegion;
    String region = "전체지역";
    String year, month, day;

    Toolbar toolbar;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("매칭하기");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegion = (Button) findViewById(R.id.btn_select_region);
        btnRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, SelectRegionActivity.class);
                startActivityForResult(intent, 10);

            }
        });
        calendarView = (MCalendarView) findViewById(R.id.calendar);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, matchCalendarURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals(""))return;
                Log.e("aaa", response);
                String[] strs = response.split(";");
                Log.e("bbb", strs[0]);
                for (int i = 0; i < strs.length; i++) {
                    year = strs[i].substring(0, 4);
                    month = strs[i].substring(5, 7);
                    day = strs[i].substring(8, 10);
                    calendarView.markDate(new DateData(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED)));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        smpr.addStringParam("region", region);

        requestQueue.add(smpr);

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String realDate = date.getYear() + "년" + date.getMonthString() + "월" + date.getDayString() + "일";
                Toast.makeText(MatchActivity.this, realDate, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MatchActivity.this, MatchingActivity.class);
                intent.putExtra("date", realDate);
                intent.putExtra("region", region);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                region = data.getStringExtra("region");
                for(int k=0;k<10;k++){
                    for (int j = 0; j < 12; j++) {
                        for (int i = 0; i < 31; i++) {
                            calendarView.unMarkDate(2017+k, j, i);

                        }
                    }

                }


                ////////////////////////

                int n=0;
                String[] arr= getResources().getStringArray(R.array.datas_location);
                for(int i=0; i<arr.length; i++){
                    if(arr[i].equals(region)){
                        n= i;
                        break;
                    }
                }

                int[] imgs={R.drawable.alllocation,R.drawable.seoul,R.drawable.busan,R.drawable.incheon,R.drawable.daegu,R.drawable.gwangju,R.drawable.dajeon,
                        R.drawable.ulsan,R.drawable.kyungki,R.drawable.kangwon,R.drawable.chungbuk,R.drawable.chungnam,R.drawable.jeonbuk,R.drawable.jeonnam,R.drawable.kyungbuk,
                        R.drawable.kyungnam,R.drawable.jeju};

                btnRegion.setBackgroundResource(imgs[n]);



                ///////////////////////


                RequestQueue requestQueue = Volley.newRequestQueue(MatchActivity.this);
                SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, matchCalendarURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals(""))return;
                        String[] strs = response.split(";");
                        for (int i = 0; i < strs.length; i++) {
                            year = strs[i].substring(0, 4);
                            month = strs[i].substring(5, 7);
                            day = strs[i].substring(8, 10);
                            calendarView.markDate(new DateData(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED)));
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                smpr.addStringParam("region", region);

                requestQueue.add(smpr);

            }

        }
    }
}
